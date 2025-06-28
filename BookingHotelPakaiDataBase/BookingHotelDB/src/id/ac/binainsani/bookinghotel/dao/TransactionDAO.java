package id.ac.binainsani.bookinghotel.dao;

import id.ac.binainsani.bookinghotel.model.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TransactionDAO {

    void addTransaction(Transaction transaction) throws SQLException;

    ArrayList<Transaction> getAllTransactions() throws SQLException;

    ArrayList<Transaction> getTransactionsByCustomerName(String customerName) throws SQLException;

    ArrayList<Transaction> getAllHotelTransactions() throws SQLException;
}
