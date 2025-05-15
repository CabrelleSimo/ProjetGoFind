package com.example.projetgofind.service;

import com.example.projetgofind.model.Location;
import com.example.projetgofind.model.ReservationLocation;
import com.example.projetgofind.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

public class RentalService {

    public boolean publierLocation(Location location) {
        String sql = "INSERT INTO location (proprietaire_id, adresse, type_location, description, prix, date_publication, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, location.getProprietaireId());
            stmt.setString(2, location.getAdresse());
            stmt.setString(3, location.getTypeLocation());
            stmt.setString(4, location.getDescription());
            stmt.setDouble(5, location.getPrix());
            stmt.setDate(6, Date.valueOf(location.getDatePublication()));
            stmt.setString(7, location.getStatut());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        location.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la publication de la location: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Location> rechercherLocationsDisponibles(String adresse, String type) {
        ObservableList<Location> locations = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM location WHERE statut = 'inoccupée'");

        if (adresse != null && !adresse.isEmpty()) {
            sql.append(" AND adresse LIKE ?");
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type_location LIKE ?");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (adresse != null && !adresse.isEmpty()) {
                stmt.setString(paramIndex++, "%" + adresse + "%");
            }
            if (type != null && !type.isEmpty()) {
                stmt.setString(paramIndex, "%" + type + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Location location = new Location();
                location.setId(rs.getInt("id"));
                location.setProprietaireId(rs.getInt("proprietaire_id"));
                location.setAdresse(rs.getString("adresse"));
                location.setTypeLocation(rs.getString("type_location"));
                location.setDescription(rs.getString("description"));
                location.setPrix(rs.getDouble("prix"));
                location.setDatePublication(rs.getDate("date_publication").toLocalDate());
                location.setStatut(rs.getString("statut"));

                locations.add(location);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de locations: " + e.getMessage());
        }
        return locations;
    }

    public boolean reserverLocation(int locationId, int locataireId) {
        String checkSql = "SELECT statut FROM location WHERE id = ?";
        String updateSql = "UPDATE location SET statut = 'occupée' WHERE id = ? AND statut = 'inoccupée'";
        String insertSql = "INSERT INTO reservation_location (location_id, locataire_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Étape 1 : Vérification du statut
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, locationId);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    throw new SQLException("Location introuvable");
                }

                String currentStatut = rs.getString("statut");
                if ("occupée".equalsIgnoreCase(currentStatut)) {
                    throw new SQLException("Location déjà occupée");
                }
            }

            // Étape 2 : Mise à jour du statut
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, locationId);
                int updated = updateStmt.executeUpdate();

                if (updated == 0) {
                    throw new SQLException("La location n'est plus disponible");
                }
            }

            // Étape 3 : Enregistrement de la réservation
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, locationId);
                insertStmt.setInt(2, locataireId);
                insertStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Échec de la réservation: " + e.getMessage());
        }
    }


    public ObservableList<Location> getLocationsParProprietaire(int userId) {
        ObservableList<Location> locations = FXCollections.observableArrayList();
        String sql = "SELECT * FROM location WHERE proprietaire_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Location location = new Location();
                location.setId(rs.getInt("id"));
                location.setProprietaireId(rs.getInt("proprietaire_id"));
                location.setAdresse(rs.getString("adresse"));
                location.setTypeLocation(rs.getString("type_location"));
                location.setDescription(rs.getString("description"));
                location.setPrix(rs.getDouble("prix"));
                location.setDatePublication(rs.getDate("date_publication").toLocalDate());
                location.setStatut(rs.getString("statut"));

                locations.add(location);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des locations publiées: " + e.getMessage());
        }
        return locations;
    }

    public ObservableList<Location> getLocationsReserveesParUtilisateur(int userId) {
        ObservableList<Location> locations = FXCollections.observableArrayList();
        String sql = "SELECT l.* FROM location l JOIN reservation_location r ON l.id = r.location_id WHERE r.locataire_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Location location = new Location();
                location.setId(rs.getInt("id"));
                location.setProprietaireId(rs.getInt("proprietaire_id"));
                location.setAdresse(rs.getString("adresse"));
                location.setTypeLocation(rs.getString("type_location"));
                location.setDescription(rs.getString("description"));
                location.setPrix(rs.getDouble("prix"));
                location.setDatePublication(rs.getDate("date_publication").toLocalDate());
                location.setStatut(rs.getString("statut"));

                locations.add(location);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations: " + e.getMessage());
        }
        return locations;
    }
}
