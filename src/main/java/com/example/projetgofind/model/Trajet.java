package com.example.projetgofind.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Trajet {
    private int id;
    private int conducteurId;
    private String depart;
    private String destination;
    private LocalDate dateDepart;
    private LocalTime heureDepart;
    private int placesDisponibles;
    private double prix;
    private String descriptionVoiture;

    // Constructeurs
    public Trajet() {}

    public Trajet(int conducteurId, String depart, String destination,
                  LocalDate dateDepart, LocalTime heureDepart,
                  int placesDisponibles, double prix, String descriptionVoiture) {
        this.conducteurId = conducteurId;
        this.depart = depart;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.placesDisponibles = placesDisponibles;
        this.prix = prix;
        this.descriptionVoiture = descriptionVoiture;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getConducteurId() { return conducteurId; }
    public void setConducteurId(int conducteurId) { this.conducteurId = conducteurId; }

    public String getDepart() { return depart; }
    public void setDepart(String depart) { this.depart = depart; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getDateDepart() { return dateDepart; }
    public void setDateDepart(LocalDate dateDepart) { this.dateDepart = dateDepart; }

    public LocalTime getHeureDepart() { return heureDepart; }
    public void setHeureDepart(LocalTime heureDepart) { this.heureDepart = heureDepart; }

    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getDescriptionVoiture() { return descriptionVoiture; }
    public void setDescriptionVoiture(String descriptionVoiture) {
        this.descriptionVoiture = descriptionVoiture;
    }

    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", conducteurId=" + conducteurId +
                ", depart='" + depart + '\'' +
                ", destination='" + destination + '\'' +
                ", dateDepart=" + dateDepart +
                ", heureDepart=" + heureDepart +
                ", placesDisponibles=" + placesDisponibles +
                ", prix=" + prix +
                ", descriptionVoiture='" + descriptionVoiture + '\'' +
                '}';
    }
}