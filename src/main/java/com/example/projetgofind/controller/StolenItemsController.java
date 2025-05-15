package com.example.projetgofind.controller;

import javafx.event.ActionEvent;
import com.example.projetgofind.model.StolenItem;
import com.example.projetgofind.service.StolenItemsService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;

public class StolenItemsController implements Initializable {
    @FXML private TextField typeObjetField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField numeroSerieField;
    @FXML private TextField lieuVolField;
    @FXML private DatePicker dateVolPicker;

    @FXML private TextField searchTypeField;
    @FXML private TextField searchNumeroSerieField;

    @FXML private TableView<StolenItem> stolenItemsTable;
    @FXML private TableColumn<StolenItem, String> colTypeObjet;
    @FXML private TableColumn<StolenItem, String> colDescription;
    @FXML private TableColumn<StolenItem, String> colNumeroSerie;
    @FXML private TableColumn<StolenItem, String> colLieuVol;
    @FXML private TableColumn<StolenItem, LocalDate> colDateVol;
    @FXML private TableColumn<StolenItem, String> colStatut;

    private final StolenItemsService stolenItemsService = new StolenItemsService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des colonnes du tableau
        colTypeObjet.setCellValueFactory(new PropertyValueFactory<>("typeObjet"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colNumeroSerie.setCellValueFactory(new PropertyValueFactory<>("numeroSerie"));
        colLieuVol.setCellValueFactory(new PropertyValueFactory<>("lieuVol"));
        colDateVol.setCellValueFactory(new PropertyValueFactory<>("dateVol"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    @FXML
    private void handleDeclarerObjetVole() {
        try {
            if (typeObjetField.getText().isEmpty() || numeroSerieField.getText().isEmpty()) {
                showAlert("Erreur", "Type d'objet et numéro de série sont obligatoires", Alert.AlertType.ERROR);
                return;
            }

            StolenItem item = new StolenItem(
                    com.example.projetgofind.SessionManager.getCurrentUserId(),
                    typeObjetField.getText(),
                    descriptionArea.getText(),
                    numeroSerieField.getText(),
                    lieuVolField.getText(),
                    dateVolPicker.getValue() != null ? dateVolPicker.getValue() : LocalDate.now()
            );

            if (stolenItemsService.declarerObjetVole(item)) {
                showAlert("Succès", "Objet volé déclaré avec succès", Alert.AlertType.INFORMATION);
                clearDeclarerFields();
            } else {
                showAlert("Erreur", "Échec de la déclaration", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleRechercherObjetVole() {
        try {
            ObservableList<StolenItem> items = stolenItemsService.rechercherObjetsVoles(
                    searchTypeField.getText(),
                    searchNumeroSerieField.getText()
            );
            stolenItemsTable.setItems(items);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearDeclarerFields() {
        typeObjetField.clear();
        descriptionArea.clear();
        numeroSerieField.clear();
        lieuVolField.clear();
        dateVolPicker.setValue(null);
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
            stage.setScene(new Scene(root, 800, 600)); // Mêmes dimensions que l'accueil
            stage.setTitle("GoFind - Accueil");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'accueil", Alert.AlertType.ERROR);
        }
    }
}