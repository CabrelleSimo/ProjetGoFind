package com.example.projetgofind;

public class SessionManager {
    private static int currentUserId;
    private static String currentUserNom;
    private static String currentUserPrenom;
    private static String currentUserRole;

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserNom(String nom) {
        currentUserNom = nom;
    }

    public static String getCurrentUserNom() {
        return currentUserNom;
    }

    public static void setCurrentUserPrenom(String prenom) {
        currentUserPrenom = prenom;
    }

    public static String getCurrentUserPrenom() {
        return currentUserPrenom;
    }

    public static void clearSession() {
        currentUserId = 0;
        currentUserNom = null;
        currentUserPrenom = null;
    }
    public static void logout() {
        currentUserId = 0; // ou -1 selon votre logique
        currentUserRole = null;
        // Ajoutez ici toute autre réinitialisation nécessaire

    }}
