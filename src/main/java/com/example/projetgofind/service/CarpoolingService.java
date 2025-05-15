package com.example.projetgofind.service;

import com.example.projetgofind.model.Trajet;
import com.example.projetgofind.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class CarpoolingService {
    public boolean publierTrajet(Trajet trajet) {
        String sql = "INSERT INTO trajet (conducteur_id, lieu_depart, lieu_arrivee, date_depart, heure_depart, places_disponibles, prix, description_vehicule) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, trajet.getConducteurId());
            stmt.setString(2, trajet.getDepart());
            stmt.setString(3, trajet.getDestination());
            stmt.setDate(4, Date.valueOf(trajet.getDateDepart()));
            stmt.setTime(5, Time.valueOf(trajet.getHeureDepart()));
            stmt.setInt(6, trajet.getPlacesDisponibles());
            stmt.setDouble(7, trajet.getPrix());
            stmt.setString(8, trajet.getDescriptionVoiture());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        trajet.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la publication du trajet: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Trajet> rechercherTrajets(String depart, String destination, LocalDate date) {
        ObservableList<Trajet> trajets = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM trajet WHERE places_disponibles > 0");

        if (depart != null && !depart.isEmpty()) {
            sql.append(" AND lieu_depart LIKE ?");
        }
        if (destination != null && !destination.isEmpty()) {
            sql.append("" +
                    "");
        }
        if (date != null) {
            sql.append(" AND date_depart = ?");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (depart != null && !depart.isEmpty()) {
                stmt.setString(paramIndex++, "%" + depart + "%");
            }
            if (destination != null && !destination.isEmpty()) {
                stmt.setString(paramIndex++, "%" + destination + "%");
            }
            if (date != null) {
                stmt.setDate(paramIndex, Date.valueOf(date));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Trajet trajet = new Trajet();
                trajet.setId(rs.getInt("id"));
                trajet.setConducteurId(rs.getInt("conducteur_id"));
                trajet.setDepart(rs.getString("lieu_depart"));
                trajet.setDestination(rs.getString("lieu_arrivee"));
                trajet.setDateDepart(rs.getDate("date_depart").toLocalDate());
                trajet.setHeureDepart(rs.getTime("heure_depart").toLocalTime());
                trajet.setPlacesDisponibles(rs.getInt("places_disponibles"));
                trajet.setPrix(rs.getDouble("prix"));
                trajet.setDescriptionVoiture(rs.getString("description_vehicule"));

                trajets.add(trajet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de trajets: " + e.getMessage());
        }
        return trajets;
    }

    public ObservableList<Trajet> getTrajetsPubliesParUtilisateur(int userId) {
        ObservableList<Trajet> trajets = FXCollections.observableArrayList();
        String sql = "SELECT * FROM trajet WHERE conducteur_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Trajet trajet = new Trajet();
                trajet.setId(rs.getInt("id"));
                trajet.setConducteurId(rs.getInt("conducteur_id"));
                trajet.setDepart(rs.getString("lieu_depart"));
                trajet.setDestination(rs.getString("lieu_arrivee"));
                trajet.setDateDepart(rs.getDate("date_depart").toLocalDate());
                trajet.setHeureDepart(rs.getTime("heure_depart").toLocalTime());
                trajet.setPlacesDisponibles(rs.getInt("places_disponibles"));
                trajet.setPrix(rs.getDouble("prix"));
                trajet.setDescriptionVoiture(rs.getString("description_vehicule"));

                trajets.add(trajet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des trajets publiés: " + e.getMessage());
        }
        return trajets;
    }

    public ObservableList<Trajet> getTrajetsReservesParUtilisateur(int userId) {
        ObservableList<Trajet> trajets = FXCollections.observableArrayList();
        String sql = "SELECT t.* FROM trajet t JOIN reservation_trajet r ON t.id = r.trajet_id WHERE r.passager_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Trajet trajet = new Trajet();
                trajet.setId(rs.getInt("id"));
                trajet.setConducteurId(rs.getInt("conducteur_id"));
                trajet.setDepart(rs.getString("lieu_depart"));
                trajet.setDestination(rs.getString("lieu_arrivee"));
                trajet.setDateDepart(rs.getDate("date_depart").toLocalDate());
                trajet.setHeureDepart(rs.getTime("heure_depart").toLocalTime());
                trajet.setPlacesDisponibles(rs.getInt("places_disponibles"));
                trajet.setPrix(rs.getDouble("prix"));
                trajet.setDescriptionVoiture(rs.getString("description_vehicule"));

                trajets.add(trajet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations: " + e.getMessage());
        }
        return trajets;
    }
    public String reserverTrajet(int trajetId, int userId) {
        String checkSql = "SELECT places_disponibles FROM trajet WHERE id = ?";
        String updateSql = "UPDATE trajet SET places_disponibles = places_disponibles - 1 WHERE id = ? AND places_disponibles > 0";
        String insertSql = "INSERT INTO reservation_trajet (trajet_id, passager_id) VALUES (?, ?)";

        Connection conn = null; // Déclaration déplacée en dehors du try
        try {
            conn = DatabaseConnection.getConnection();
            // Vérification des places disponibles
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, trajetId);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    return "Trajet introuvable";
                }

                int placesDisponibles = rs.getInt("places_disponibles");
                if (placesDisponibles <= 0) {
                    return "Ce trajet est complet. Veuillez choisir un autre trajet.";
                }
            }

            // Début de la transaction
            conn.setAutoCommit(false);

            // Mise à jour des places
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, trajetId);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    conn.rollback();
                    return "Ce trajet est complet. Veuillez choisir un autre trajet.";
                }
            }

            // Enregistrement de la réservation
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, trajetId);
                insertStmt.setInt(2, userId);
                insertStmt.executeUpdate();
            }

            conn.commit();
            return "Réservation confirmée avec succès";

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Erreur technique lors de la réservation";
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close(); // N'oubliez pas de fermer la connexion
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
}
}
}
