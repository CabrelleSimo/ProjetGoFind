package com.example.projetgofind.model;

public class ReservationLocation {
    private int id;
    private int locationId;
    private int locataireId;

    private static int compteur = 1;

    public ReservationLocation(int locationId, int locataireId) {
        this.id = compteur++;
        this.locationId = locationId;
        this.locataireId = locataireId;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getLocataireId() {
        return locataireId;
    }

    public void setLocataireId(int locataireId) {
        this.locataireId = locataireId;
    }
}

