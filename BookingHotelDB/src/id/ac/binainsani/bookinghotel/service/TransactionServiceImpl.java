package id.ac.binainsani.bookinghotel.service;

import id.ac.binainsani.bookinghotel.dao.TransactionDAO;
import id.ac.binainsani.bookinghotel.model.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionServiceImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override
    public void add(Transaction transaction) {
        try {
            transactionDAO.addTransaction(transaction);
        } catch (SQLException e) {
            System.err.println("Error adding transaction: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Transaction> getAll() {
        try {
            return transactionDAO.getAllTransactions();
        } catch (SQLException e) {
            System.err.println("Error getting all transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<Transaction> getAllHotelTransactions() {
        try {
            return transactionDAO.getAllHotelTransactions();
        } catch (SQLException e) {
            System.err.println("Error getting all hotel transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<Transaction> getAllRestaurantTransactions() {
        // This method would typically interact with a RestaurantTransactionDAO
        // For now, it will return an empty list as there's no specific DAO for it.
        System.out.println("Restaurant transactions not yet implemented with DAO.");
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Transaction> getAllMeetingRoomTransactions() {
        // This method would typically interact with a MeetingRoomTransactionDAO
        // For now, it will return an empty list as there's no specific DAO for it.
        System.out.println("Meeting room transactions not yet implemented with DAO.");
        return new ArrayList<>();
    }
}
