package com.example.projetgofind.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private ImageView carpoolingImage;

    @FXML
    private ImageView stolenItemsImage;

    @FXML
    private ImageView rentalImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            carpoolingImage.setImage(new Image(getClass().getResourceAsStream("/com/example/projetgofind/images/covv.png")));
            stolenItemsImage.setImage(new Image(getClass().getResourceAsStream("/com/example/projetgofind/images/objv.png")));
            rentalImage.setImage(new Image(getClass().getResourceAsStream("/com/example/projetgofind/images/colv.png")));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger certaines images.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleStolenItems(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/StolenItemsView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("GoFind - Objets volés");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page des objets volés", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCarpooling(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/CarpoolingView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("GoFind - Covoiturage");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page de covoiturage", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRental(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/RentalView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("GoFind - Colocation");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page de colocation", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Stage stage = com.example.projetgofind.SceneUtils.getCurrentStage(event);
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/LoginView.fxml"));
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("GoFind - Connexion");
            com.example.projetgofind.SessionManager.logout();
        } catch (IOException | IllegalArgumentException e) {
            showAlert("Erreur", "Problème de navigation: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleManageProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/UserProfileView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("GoFind - Mon Profil");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de profil", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
