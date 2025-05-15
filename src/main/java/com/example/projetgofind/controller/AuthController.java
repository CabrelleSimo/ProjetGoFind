package com.example.projetgofind.controller;

import com.example.projetgofind.model.Utilisateur;
import com.example.projetgofind.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthController {
    @FXML private TextField loginEmailField;
    @FXML private PasswordField loginPasswordField;
    @FXML private TextField registerNameField;
    @FXML private TextField registerEmailField;
    @FXML private PasswordField registerPasswordField;
    @FXML private TextField registerPhoneField;

    private AuthService authService = new AuthService();
    private static Utilisateur currentUser;

    public static Utilisateur getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Utilisateur utilisateur) {
        currentUser = utilisateur;
    }

    @FXML
    private void handleLogin() {
        String email = loginEmailField.getText();
        String password = loginPasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        Utilisateur utilisateur = authService.login(email, password);

        if (utilisateur != null) {
            currentUser = utilisateur;
            loadMainView();
        } else {
            showAlert("Erreur", "Email ou mot de passe incorrect");
        }
    }

    @FXML
    private void handleRegister() {
        String nom = registerNameField.getText();
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();
        String telephone = registerPhoneField.getText();

        if (nom.isEmpty() || email.isEmpty() || password.isEmpty() || telephone.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        if (authService.emailExists(email)) {
            showAlert("Erreur", "Cet email est déjà utilisé");
            return;
        }

        Utilisateur newUser = new Utilisateur(nom, email, password, telephone);

        if (authService.register(newUser)) {
            showAlert("Succès", "Inscription réussie! Veuillez vous connecter.");
            switchToLoginView();
        } else {
            showAlert("Erreur", "Échec de l'inscription");
        }
    }

    @FXML
    private void switchToLoginView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gofind/view/auth/LoginView.fxml"));
            Stage stage = (Stage) loginEmailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GoFind - Connexion");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToRegisterView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/RegisterView.fxml"));
            Stage stage = (Stage) registerNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GoFind - Inscription");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMainView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gofind/view/MainView.fxml"));
            Stage stage = (Stage) loginEmailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GoFind - Accueil");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}