package com.example.projetgofind.controller;

import com.example.projetgofind.model.Location;
import com.example.projetgofind.service.RentalService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RentalController implements Initializable {

    @FXML private ComboBox<String> publierTypeComboBox;

    @FXML private TextField publierAdresseField;
    @FXML private TextArea publierDescriptionArea;
    @FXML private TextField publierPrixField;
    @FXML private DatePicker publierDatePicker;

    @FXML private TextField rechercherAdresseField;
    @FXML private TextField rechercherTypeField;

    @FXML private TableView<Location> locationsTable;
    @FXML private TableColumn<Location, String> colAdresse;
    @FXML private TableColumn<Location, String> colType;
    @FXML private TableColumn<Location, String> colDescription;
    @FXML private TableColumn<Location, Double> colPrix;
    @FXML private TableColumn<Location, LocalDate> colDatePublication;

    private final RentalService rentalService = new RentalService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeLocation"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colDatePublication.setCellValueFactory(new PropertyValueFactory<>("datePublication"));

        publierTypeComboBox.getItems().addAll("maison entière", "pièce partagée");
        publierTypeComboBox.getSelectionModel().selectFirst(); // Optionnel
    }

    @FXML
    private void handleRetourAccueil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/HomeView.fxml"));
            Stage stage = (Stage) publierAdresseField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("GoFind - Accueil");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'accueil", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handlePublierLocation() {
        try {
            String typeLocation = publierTypeComboBox.getValue();

            if (publierAdresseField.getText().isEmpty() ||
                    typeLocation == null ||
                    publierPrixField.getText().isEmpty()) {

                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.ERROR);
                return;
            }

            Location location = new Location(
                    com.example.projetgofind.SessionManager.getCurrentUserId(),
                    publierAdresseField.getText(),
                    typeLocation,
                    publierDescriptionArea.getText(),
                    Double.parseDouble(publierPrixField.getText()),
                    publierDatePicker.getValue() != null ? publierDatePicker.getValue() : LocalDate.now(),
                    "inoccupée"
            );

            if (rentalService.publierLocation(location)) {
                showAlert("Succès", "Location publiée avec succès", Alert.AlertType.INFORMATION);
                clearPublierFields();
            } else {
                showAlert("Erreur", "Échec de la publication", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRechercherLocation() {
        try {
            ObservableList<Location> locations = rentalService.rechercherLocationsDisponibles(
                    rechercherAdresseField.getText(),
                    rechercherTypeField.getText()
            );
            locationsTable.setItems(locations);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReserverLocation() {
        Location selected = locationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Information", "Veuillez sélectionner une location", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            if (rentalService.reserverLocation(
                    selected.getId(),
                    com.example.projetgofind.SessionManager.getCurrentUserId())) {

                showAlert("Succès", "Réservation confirmée", Alert.AlertType.INFORMATION);
                handleRechercherLocation();
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            Alert.AlertType alertType = Alert.AlertType.ERROR;

            if (errorMessage.contains("déjà occupée")) {
                errorMessage = "Cette location est déjà réservée. Veuillez en choisir une autre.";
                alertType = Alert.AlertType.WARNING;
            } else if (errorMessage.contains("plus disponible")) {
                errorMessage = "La location n'est plus disponible. Veuillez actualiser la liste.";
                alertType = Alert.AlertType.WARNING;
            }

            showAlert("Information", errorMessage, alertType);
        }
    }

    @FXML
    private void handleVoirMesLocationsPubliees() {
        try {
            ObservableList<Location> locations = rentalService.getLocationsParProprietaire(
                    com.example.projetgofind.SessionManager.getCurrentUserId());
            locationsTable.setItems(locations);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement de vos locations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleVoirMesReservations() {
        try {
            ObservableList<Location> locations = rentalService.getLocationsReserveesParUtilisateur(
                    com.example.projetgofind.SessionManager.getCurrentUserId());
            locationsTable.setItems(locations);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement de vos réservations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clearPublierFields() {
        publierAdresseField.clear();
        publierDescriptionArea.clear();
        publierPrixField.clear();
        publierDatePicker.setValue(null);
        publierTypeComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
