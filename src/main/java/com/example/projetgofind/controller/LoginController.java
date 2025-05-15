package com.example.projetgofind.controller;

import com.example.projetgofind.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private ImageView logoImage;
    @FXML private TextField loginEmailField;
    @FXML private PasswordField loginPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            logoImage.setImage(new Image(getClass().getResourceAsStream("/com/example/projetgofind/images/G.png")));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'image.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }

        // On vérifie d'abord si l'email existe
        String checkEmailSql = "SELECT id FROM utilisateur WHERE email = ?";
        // On vérifie ensuite l'email et le mot de passe
        String loginSql = "SELECT id, nom FROM utilisateur WHERE email = ? AND mot_de_passe = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Vérifier si l'email existe
            boolean emailExists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql)) {
                checkStmt.setString(1, email);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    emailExists = rs.next();
                }
            }

            if (!emailExists) {
                showAlert("Échec", "Aucun compte trouvé avec cet email", Alert.AlertType.ERROR);
                return;
            }

            // 2. Si l'email existe, vérifier le mot de passe
            try (PreparedStatement loginStmt = conn.prepareStatement(loginSql)) {
                loginStmt.setString(1, email);
                loginStmt.setString(2, password);

                try (ResultSet rs = loginStmt.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("id");
                        String nomComplet = rs.getString("nom");

                        // Stockage des infos de l'utilisateur connecté
                        com.example.projetgofind.SessionManager.setCurrentUserId(userId);
                        com.example.projetgofind.SessionManager.setCurrentUserNom(nomComplet);
                        showAlert("Succès", "Connexion réussie!", Alert.AlertType.INFORMATION);
                        redirectToHomePage(loginEmailField.getScene().getWindow());
                    } else {
                        showAlert("Échec", "Mot de passe incorrect", Alert.AlertType.ERROR);
                    }
                }
            }
        } catch (SQLException e) {
            showAlert("Erreur BD", "Erreur de connexion: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    protected static void redirectToHomePage(javafx.stage.Window currentWindow) {
        try {
            Parent root = FXMLLoader.load(LoginController.class.getResource("/com/example/projetgofind/HomeView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("GoFind - Accueil");
            stage.show();

            if (currentWindow != null) {
                ((Stage) currentWindow).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToRegisterView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/RegisterView.fxml"));
            Stage stage = (Stage) loginEmailField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}