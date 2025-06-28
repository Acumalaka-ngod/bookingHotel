package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String URL = "jdbc:mysql://localhost:3306/booking_hotel"; // Ganti dengan jdbc:mariadb://localhost:3306/booking_hotel jika menggunakan MariaDB
    private final String USER = "root"; // Ganti dengan username database Anda
    private final String PASSWORD = ""; // Ganti dengan password database Anda

    // Method untuk mendapatkan koneksi
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method untuk menutup koneksi
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
