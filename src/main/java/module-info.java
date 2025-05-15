module com.example.projetgofind {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.projetgofind to javafx.fxml;
    exports com.example.projetgofind.controller;
    exports com.example.projetgofind.service;
    exports com.example.projetgofind.model;
    opens com.example.projetgofind.controller to javafx.fxml;
    exports com.example.projetgofind;

}