package com.example.projetgofind.controller;

import com.example.projetgofind.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML private Label welcomeLabel;

    @FXML
    private void initialize() {
        Utilisateur currentUser = AuthController.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Bienvenue, " + currentUser.getNom() + "!");
        }
    }

    @FXML
    private void handleTrajetButton() {

    }

    @FXML
    private void handleLocationButton() {

    }

    @FXML
    private void handleObjetButton() {

    }

   /* @FXML
    private void handleProfileButton() {
    }*/

    @FXML
    private void handleLogoutButton() {
        AuthController.setCurrentUser(null);
        loadView("com/example/projetgofind/LoginView.fxml", "GoFind - Connexion");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}