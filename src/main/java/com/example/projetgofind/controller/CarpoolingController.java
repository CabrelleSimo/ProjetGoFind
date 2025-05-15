package com.example.projetgofind.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import com.example.projetgofind.model.Trajet;
import com.example.projetgofind.service.CarpoolingService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;


public class CarpoolingController implements Initializable {

    @FXML private TextField publierDepartField;
    @FXML private TextField publierDestinationField;
    @FXML private DatePicker publierDatePicker;
    @FXML private TextField publierHeureField;
    @FXML private Spinner<Integer> publierPlacesSpinner;
    @FXML private TextField publierPrixField;
    @FXML private TextArea publierDescriptionArea;
    @FXML private TextField rechercherDepartField;
    @FXML private TextField rechercherDestinationField;
    @FXML private DatePicker rechercherDatePicker;
    @FXML private TableView<Trajet> trajetsTable;
    @FXML private TableColumn<Trajet, String> colDescriptionVoiture;

    private final CarpoolingService carpoolingService = new CarpoolingService();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory.IntegerSpinnerValueFactory placesFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1); // min=1, max=50, initial=1
        publierPlacesSpinner.setValueFactory(placesFactory);
        publierPlacesSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                publierPlacesSpinner.getEditor().setText(oldValue);
            } else if (!newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(newValue);
                    if (value < placesFactory.getMin()) {
                        value = placesFactory.getMin();
                    } else if (value > placesFactory.getMax()) {
                        value = placesFactory.getMax();
                    }
                    publierPlacesSpinner.getValueFactory().setValue(value);
                } catch (NumberFormatException e) {
                    publierPlacesSpinner.getEditor().setText(oldValue);
                }
            }
        });
        // publierPlacesSpinner.setValueFactory(valueFactory);
        publierHeureField.setPromptText("HH:mm (ex: 08:30)");

        // Initialisation de la colonne description véhicule
        if (colDescriptionVoiture != null) {
            colDescriptionVoiture.setCellValueFactory(new PropertyValueFactory<>("descriptionVoiture"));
        }
    }
    @FXML
    private void handleRetourAccueil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetgofind/HomeView.fxml"));
            //Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage stage = (Stage) publierDepartField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600)); // Mêmes dimensions que l'accueil
            stage.setTitle("GoFind - Accueil");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'accueil", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handlePublierTrajet() {
        try {
            if (publierDepartField.getText().isEmpty() ||
                    publierDestinationField.getText().isEmpty() ||
                    publierDatePicker.getValue() == null ||
                    publierHeureField.getText().isEmpty() ||
                    publierPrixField.getText().isEmpty()) {

                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.ERROR);
                return;
            }

            LocalTime heureDepart = LocalTime.parse(publierHeureField.getText(), timeFormatter);

            Trajet trajet = new Trajet(
                    com.example.projetgofind.SessionManager.getCurrentUserId(),
                    publierDepartField.getText(),
                    publierDestinationField.getText(),
                    publierDatePicker.getValue(),
                    heureDepart,
                    publierPlacesSpinner.getValue(),
                    Double.parseDouble(publierPrixField.getText()),
                    publierDescriptionArea.getText()
            );

            if (carpoolingService.publierTrajet(trajet)) {
                showAlert("Succès", "Trajet publié avec succès", Alert.AlertType.INFORMATION);
                clearPublierFields();
            } else {
                showAlert("Erreur", "Échec de la publication", Alert.AlertType.ERROR);
            }
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format d'heure invalide. Utilisez HH:mm (ex: 14:30)", Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRechercherTrajet() {
        try {
            ObservableList<Trajet> trajets = carpoolingService.rechercherTrajets(
                    rechercherDepartField.getText(),
                    rechercherDestinationField.getText(),
                    rechercherDatePicker.getValue()
            );
            trajetsTable.setItems(trajets);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReserverTrajet() {
        Trajet selected = trajetsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Information", "Veuillez sélectionner un trajet", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            String result = carpoolingService.reserverTrajet(
                    selected.getId(),
                    com.example.projetgofind.SessionManager.getCurrentUserId());

            if (result.equals("Réservation confirmée avec succès")) {
                showAlert("Succès", result, Alert.AlertType.INFORMATION);
                handleRechercherTrajet();
            } else {
                showAlert("Information", result, Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue est survenue", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoirMesTrajetsPublies() {
        try {
            ObservableList<Trajet> trajets = carpoolingService.getTrajetsPubliesParUtilisateur(
                    com.example.projetgofind.SessionManager.getCurrentUserId());
            trajetsTable.setItems(trajets);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement de vos trajets: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleVoirMesReservations() {
        try {
            ObservableList<Trajet> trajets = carpoolingService.getTrajetsReservesParUtilisateur(
                    com.example.projetgofind.SessionManager.getCurrentUserId());
            trajetsTable.setItems(trajets);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement de vos réservations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearPublierFields() {
        publierDepartField.clear();
        publierDestinationField.clear();
        publierDatePicker.setValue(null);
        publierHeureField.clear();
        publierPlacesSpinner.getValueFactory().setValue(1);
        publierPrixField.clear();
        publierDescriptionArea.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
