package com.example.projetgofind.model;

import java.time.LocalDate;

public class Location {
    private int id;
    private int proprietaireId;
    private String adresse;
    private String typeLocation; // "maison entière", "pièce privée", "pièce partagée"
    private double prix;
    private String description;
    private LocalDate datePublication;
    private String statut; // "inoccupée" ou "occupée"

    // Constructeur par défaut
    public Location() {
    }

    // Constructeur avec tous les champs
    public Location(int id, int proprietaireId, String adresse, String typeLocation,
                    double prix, String description, LocalDate datePublication, String statut) {
        this.id = id;
        this.proprietaireId = proprietaireId;
        this.adresse = adresse;
        this.typeLocation = typeLocation;
        this.prix = prix;
        this.description = description;
        this.datePublication = datePublication;
        this.statut = statut;

    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(int proprietaireId) {
        this.proprietaireId = proprietaireId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTypeLocation() {
        return typeLocation;
    }

    public void setTypeLocation(String typeLocation) {
        this.typeLocation = typeLocation;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", adresse='" + adresse + '\'' +
                ", typeLocation='" + typeLocation + '\'' +
                ", prix=" + prix +
                ", statut='" + statut + '\'' +
                '}';
    }
    public Location(int proprietaireId, String adresse, String typeLocation, String description,
                    double prix, LocalDate datePublication, String statut) {
        this.proprietaireId = proprietaireId;
        this.adresse = adresse;
        this.typeLocation = typeLocation;
        this.description = description;
        this.prix = prix;
        this.datePublication = datePublication;
        this.statut = statut;
    }

}
