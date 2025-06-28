package id.ac.binainsani.bookinghotel.dao;

import database.DatabaseConnection;
import id.ac.binainsani.bookinghotel.model.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAOImpl implements AdminDAO {

    private final DatabaseConnection dbConnection;

    public AdminDAOImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Admin getAdminByUsername(String username) throws SQLException {
        String sql = "SELECT username, password FROM admins WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Admin admin = null;
        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                admin = new Admin(rs.getString("username"), rs.getString("password"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                dbConnection.closeConnection(conn);
            }
        }
        return admin;
    }
}
