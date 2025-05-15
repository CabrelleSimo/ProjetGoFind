package com.example.projetgofind.model;
import java.time.LocalDate;


public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String telephone;
    private LocalDate dateInscription;

    public Utilisateur() {}

    public Utilisateur(String nom, String email, String motDePasse, String telephone) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
    }
    public Utilisateur(int id, String nom, String email, String motDePasse,
                       String telephone, LocalDate dateInscription) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.dateInscription = dateInscription;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public LocalDate getDateInscription() { return dateInscription; }
}
