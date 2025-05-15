package com.example.projetgofind.service;

import com.example.projetgofind.model.Utilisateur;
import com.example.projetgofind.DatabaseConnection;
import java.sql.*;

public class UserService {
    public Utilisateur getUserById(int userId) {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Utilisation du constructeur privé
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("telephone"),
                        rs.getDate("date_inscription") != null ?
                                rs.getDate("date_inscription").toLocalDate() : null
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUser(Utilisateur user) {
        String sql;
        if (user.getMotDePasse() != null && !user.getMotDePasse().isEmpty()) {
            sql = "UPDATE utilisateur SET email = ?, mot_de_passe = ?, telephone = ? WHERE id = ?";
        } else {
            sql = "UPDATE utilisateur SET email = ?, telephone = ? WHERE id = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            stmt.setString(paramIndex++, user.getEmail());

            if (user.getMotDePasse() != null && !user.getMotDePasse().isEmpty()) {
                stmt.setString(paramIndex++, user.getMotDePasse());
            }

            stmt.setString(paramIndex++, user.getTelephone());
            stmt.setInt(paramIndex, user.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Désactive l'autocommit

            // 1. Supprimer les objets volés déclarés
            String deleteStolenItemsSql = "DELETE FROM objet_vole WHERE utilisateur_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteStolenItemsSql)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // 2. Supprimer les réservations de trajet (en tant que passager)
            String deleteTripReservationsSql = "DELETE FROM reservation_trajet WHERE passager_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTripReservationsSql)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // 3. Supprimer les réservations de location (en tant que locataire)
            String deleteRentalReservationsSql = "DELETE FROM reservation_location WHERE locataire_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteRentalReservationsSql)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // 4. Supprimer les trajets (en tant que conducteur)
            String deleteTripsSql = "DELETE FROM trajet WHERE conducteur_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTripsSql)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // 5. Supprimer les locations (en tant que propriétaire)
            String deleteLocationsSql = "DELETE FROM location WHERE proprietaire_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteLocationsSql)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // 6. Enfin supprimer l'utilisateur
            String deleteUserSql = "DELETE FROM utilisateur WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserSql)) {
                stmt.setInt(1, userId);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit(); // Valide la transaction
                    return true;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Annule en cas d'erreur
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback: " + ex.getMessage());
            }
            System.err.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Réactive l'autocommit
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
        return false;
    }
}
