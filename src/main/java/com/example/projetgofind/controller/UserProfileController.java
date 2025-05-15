package com.example.projetgofind.controller;

import com.example.projetgofind.SessionManager;
import com.example.projetgofind.model.Utilisateur;
import com.example.projetgofind.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class UserProfileController implements Initializable {
    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField telephoneField;
    @FXML private TextField dateInscriptionField;

    private final UserService userService = new UserService();
    private Utilisateur currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserData();
    }

    private void loadUserData() {
        currentUser = userService.getUserById(SessionManager.getCurrentUserId());
        if (currentUser != null) {
            nomField.setText(currentUser.getNom());
            emailField.setText(currentUser.getEmail());
            telephoneField.setText(currentUser.getTelephone());

            // Affichage de la date (lecture seule)
            if (currentUser.getDateInscription() != null) {
                dateInscriptionField.setText(currentUser.getDateInscription().toString());
            }

            // Désactivation des champs non modifiables
            dateInscriptionField.setDisable(true);
            nomField.setDisable(true);
        }
    }

    @FXML
    private void handleUpdateProfile() {
        try {
            // Seuls email, mot de passe et téléphone peuvent être modifiés
            String newEmail = emailField.getText();
            String newPassword = passwordField.getText();
            String newTelephone = telephoneField.getText();

            // Validation des champs obligatoires
            if (newEmail.isEmpty() || newTelephone.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.ERROR);
                return;
            }

            // Préparation des modifications
            currentUser.setEmail(newEmail);
            currentUser.setTelephone(newTelephone);

            // On ne met à jour le mot de passe que si le champ n'est pas vide
            if (!newPassword.isEmpty()) {
                currentUser.setMotDePasse(newPassword);
            }

            if (userService.updateUser(currentUser)) {
                showAlert("Succès", "Profil mis à jour avec succès", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Échec de la mise à jour", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer votre compte? Cette action est irréversible et supprimera toutes vos données.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (userService.deleteUser(SessionManager.getCurrentUserId())) {
                showAlert("Information", "Compte supprimé avec succès", Alert.AlertType.INFORMATION);
                SessionManager.logout();
                navigateToLogin();
            } else {
                showAlert("Erreur", "Échec de la suppression du compte", Alert.AlertType.ERROR);
            }
        }
    }

    private void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/LoginView.fxml"));
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Connexion");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page de connexion", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void navigateToHome() {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/com/example/projetgofind/HomeView.fxml"));
            javafx.scene.Scene scene = nomField.getScene();
            scene.setRoot(root);
        } catch (java.io.IOException e) {
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
    @FXML
    private void handleReturnToHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/HomeView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600)); // Dimensions fixes
            stage.setTitle("GoFind - Accueil");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'accueil", Alert.AlertType.ERROR);
        }
    }
}
