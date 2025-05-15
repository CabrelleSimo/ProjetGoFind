package com.example.projetgofind.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/gofind";
    private static final String USER = "root";
    private static final String PASSWORD = "caby*27";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                createTablesIfNotExist();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Erreur lors de la connexion à la base de données");
            }
        }
        return connection;
    }

    private static void createTablesIfNotExist() throws SQLException {
        // Création des tables si elles n'existent pas
        String createUtilisateursTable = "CREATE TABLE IF NOT EXISTS utilisateurs (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nom VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "mot_de_passe VARCHAR(100) NOT NULL, " +
                "telephone VARCHAR(20))";

        String createTrajetsTable = "CREATE TABLE IF NOT EXISTS trajets (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "conducteur_id INT NOT NULL, " +
                "depart VARCHAR(100) NOT NULL, " +
                "arrivee VARCHAR(100) NOT NULL, " +
                "heure_depart DATETIME NOT NULL, " +
                "prix DECIMAL(10,2) NOT NULL, " +
                "places_disponibles INT NOT NULL, " +
                "description_voiture TEXT, " +
                "FOREIGN KEY (conducteur_id) REFERENCES utilisateurs(id))";

        String createReservationsTrajetTable = "CREATE TABLE IF NOT EXISTS reservations_trajet (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "trajet_id INT NOT NULL, " +
                "passager_id INT NOT NULL, " +
                "FOREIGN KEY (trajet_id) REFERENCES trajets(id), " +
                "FOREIGN KEY (passager_id) REFERENCES utilisateurs(id))";

        String createLocationsTable = "CREATE TABLE IF NOT EXISTS locations (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "proprietaire_id INT NOT NULL, " +
                "localisation VARCHAR(100) NOT NULL, " +
                "type VARCHAR(50) NOT NULL, " +
                "prix DECIMAL(10,2) NOT NULL, " +
                "description TEXT, " +
                "disponible BOOLEAN DEFAULT true, " +
                "FOREIGN KEY (proprietaire_id) REFERENCES utilisateurs(id))";

        String createReservationsLocationTable = "CREATE TABLE IF NOT EXISTS reservations_location (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "location_id INT NOT NULL, " +
                "locataire_id INT NOT NULL, " +
                "FOREIGN KEY (location_id) REFERENCES locations(id), " +
                "FOREIGN KEY (locataire_id) REFERENCES utilisateurs(id))";

        String createObjetsVolesTable = "CREATE TABLE IF NOT EXISTS objets_voles (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "declareur_id INT NOT NULL, " +
                "numero_serie VARCHAR(100) NOT NULL, " +
                "type VARCHAR(50) NOT NULL, " +
                "description TEXT, " +
                "trouve BOOLEAN DEFAULT false, " +
                "FOREIGN KEY (declareur_id) REFERENCES utilisateurs(id))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createUtilisateursTable);
            statement.execute(createTrajetsTable);
            statement.execute(createReservationsTrajetTable);
            statement.execute(createLocationsTable);
            statement.execute(createReservationsLocationTable);
            statement.execute(createObjetsVolesTable);
        }
    }
}
