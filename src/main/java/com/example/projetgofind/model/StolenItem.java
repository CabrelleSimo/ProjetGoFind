package com.example.projetgofind.model;

import java.time.LocalDate;

public class StolenItem {
    private int id;
    private int utilisateurId;
    private String typeObjet;
    private String description;
    private String numeroSerie;
    private String lieuVol;
    private LocalDate dateVol;
    private LocalDate dateDeclaration;
    private String statut; // "non retrouvé" ou "retrouvé"

    // Constructeurs
    public StolenItem() {}

    public StolenItem(int utilisateurId, String typeObjet, String description,
                      String numeroSerie, String lieuVol, LocalDate dateVol) {
        this.utilisateurId = utilisateurId;
        this.typeObjet = typeObjet;
        this.description = description;
        this.numeroSerie = numeroSerie;
        this.lieuVol = lieuVol;
        this.dateVol = dateVol;
        this.statut = "non retrouvé";
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public String getTypeObjet() { return typeObjet; }
    public void setTypeObjet(String typeObjet) { this.typeObjet = typeObjet; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    public String getLieuVol() { return lieuVol; }
    public void setLieuVol(String lieuVol) { this.lieuVol = lieuVol; }
    public LocalDate getDateVol() { return dateVol; }
    public void setDateVol(LocalDate dateVol) { this.dateVol = dateVol; }
    public LocalDate getDateDeclaration() { return dateDeclaration; }
    public void setDateDeclaration(LocalDate dateDeclaration) { this.dateDeclaration = dateDeclaration; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}