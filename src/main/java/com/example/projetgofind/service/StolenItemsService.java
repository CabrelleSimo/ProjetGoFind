package com.example.projetgofind.service;

import com.example.projetgofind.model.StolenItem;
import com.example.projetgofind.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

public class StolenItemsService {

    public boolean declarerObjetVole(StolenItem item) {
        String sql = "INSERT INTO objet_vole (utilisateur_id, type_objet, description, numero_serie, lieu_vol, date_vol, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getUtilisateurId());
            stmt.setString(2, item.getTypeObjet());
            stmt.setString(3, item.getDescription());
            stmt.setString(4, item.getNumeroSerie());
            stmt.setString(5, item.getLieuVol());
            stmt.setDate(6, Date.valueOf(item.getDateVol()));
            stmt.setString(7, item.getStatut());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la déclaration d'objet volé: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<StolenItem> rechercherObjetsVoles(String typeObjet, String numeroSerie) {
        ObservableList<StolenItem> items = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM objet_vole WHERE 1=1");

        if (typeObjet != null && !typeObjet.isEmpty()) {
            sql.append(" AND type_objet LIKE ?");
        }
        if (numeroSerie != null && !numeroSerie.isEmpty()) {
            sql.append(" AND numero_serie LIKE ?");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (typeObjet != null && !typeObjet.isEmpty()) {
                stmt.setString(paramIndex++, "%" + typeObjet + "%");
            }
            if (numeroSerie != null && !numeroSerie.isEmpty()) {
                stmt.setString(paramIndex, "%" + numeroSerie + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StolenItem item = new StolenItem();
                item.setId(rs.getInt("id"));
                item.setUtilisateurId(rs.getInt("utilisateur_id"));
                item.setTypeObjet(rs.getString("type_objet"));
                item.setDescription(rs.getString("description"));
                item.setNumeroSerie(rs.getString("numero_serie"));
                item.setLieuVol(rs.getString("lieu_vol"));
                item.setDateVol(rs.getDate("date_vol").toLocalDate());
                item.setDateDeclaration(rs.getDate("date_declaration").toLocalDate());
                item.setStatut(rs.getString("statut"));

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche d'objets volés: " + e.getMessage());
        }
        return items;
    }
}