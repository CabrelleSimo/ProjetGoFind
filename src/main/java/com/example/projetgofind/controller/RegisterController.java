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

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ImageView logoImage;

    @FXML
    private TextField registerNomField;
    @FXML
    private TextField registerEmailField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private TextField registerPhoneField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            logoImage.setImage(new Image(getClass().getResourceAsStream("/com/example/projetgofind/images/G.png")));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger l'image.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String nomComplet = registerNomField.getText().trim();
        String email = registerEmailField.getText().trim();
        String password = registerPasswordField.getText().trim();
        String phone = registerPhoneField.getText().trim();

        if (nomComplet.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.ERROR);
            return;
        }

        String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe, telephone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nomComplet);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, phone);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        com.example.projetgofind.SessionManager.setCurrentUserId(userId);
                        com.example.projetgofind.SessionManager.setCurrentUserNom(nomComplet);
                    }
                }
                showAlert("Succès", "Inscription réussie!", Alert.AlertType.INFORMATION);
                redirectToHomePage(registerNomField.getScene().getWindow());
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // Cas spécifique : email déjà utilisé (clé unique)
            showAlert("Erreur", "Cet email est déjà utilisé", Alert.AlertType.ERROR);
            // Pas besoin d'afficher la stacktrace ici
        } catch (SQLException e) {
            // Autres erreurs SQL
            showAlert("Erreur BD", "Erreur lors de l'inscription: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void redirectToHomePage(javafx.stage.Window currentWindow) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/HomeView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("GoFind - Accueil");
            stage.show();

            if (currentWindow != null) {
                ((Stage) currentWindow).close();
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger la page d'accueil", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToLoginView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/LoginView.fxml"));
            Stage stage = (Stage) registerNomField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger la page de connexion", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
