package id.ac.binainsani.bookinghotel.dao;

import database.DatabaseConnection;
import id.ac.binainsani.bookinghotel.model.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDAOImpl implements TransactionDAO {

    private final DatabaseConnection dbConnection;

    public TransactionDAOImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (type, service_type, item_name, duration, total_price, payment, change_amount, customer_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, transaction.getType());
            pstmt.setString(2, transaction.getServiceType());
            pstmt.setString(3, transaction.getItemName());
            pstmt.setInt(4, transaction.getDuration());
            pstmt.setInt(5, transaction.getTotalPrice());
            pstmt.setInt(6, transaction.getPayment());
            pstmt.setInt(7, transaction.getChange());
            pstmt.setString(8, transaction.getCustomerName());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) dbConnection.closeConnection(conn);
        }
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT type, service_type, item_name, duration, total_price, payment, change_amount, customer_name FROM transactions";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getString("type"),
                        rs.getString("service_type"),
                        rs.getString("item_name"),
                        rs.getInt("duration"),
                        rs.getInt("total_price"),
                        rs.getInt("payment"),
                        rs.getInt("change_amount")
                );
                transaction.setCustomerName(rs.getString("customer_name"));
                transactions.add(transaction);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbConnection.closeConnection(conn);
        }
        return transactions;
    }

    @Override
    public ArrayList<Transaction> getTransactionsByCustomerName(String customerName) throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT type, service_type, item_name, duration, total_price, payment, change_amount, customer_name FROM transactions WHERE customer_name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customerName);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getString("type"),
                        rs.getString("service_type"),
                        rs.getString("item_name"),
                        rs.getInt("duration"),
                        rs.getInt("total_price"),
                        rs.getInt("payment"),
                        rs.getInt("change_amount")
                );
                transaction.setCustomerName(rs.getString("customer_name"));
                transactions.add(transaction);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbConnection.closeConnection(conn);
        }
        return transactions;
    }

    @Override
    public ArrayList<Transaction> getAllHotelTransactions() throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT type, service_type, item_name, duration, total_price, payment, change_amount, customer_name FROM transactions WHERE service_type = 'HOTEL'";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getString("type"),
                        rs.getString("service_type"),
                        rs.getString("item_name"),
                        rs.getInt("duration"),
                        rs.getInt("total_price"),
                        rs.getInt("payment"),
                        rs.getInt("change_amount")
                );
                transaction.setCustomerName(rs.getString("customer_name"));
                transactions.add(transaction);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbConnection.closeConnection(conn);
        }
        return transactions;
    }
}
