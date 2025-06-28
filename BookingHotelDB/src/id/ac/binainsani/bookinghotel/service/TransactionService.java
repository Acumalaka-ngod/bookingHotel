package id.ac.binainsani.bookinghotel.service;

import id.ac.binainsani.bookinghotel.model.Transaction;
import java.util.ArrayList;

public interface TransactionService {

    void add(Transaction transaction);

    public ArrayList<Transaction> getAllHotelTransactions();

    public ArrayList<Transaction> getAllRestaurantTransactions();

    public ArrayList<Transaction> getAllMeetingRoomTransactions();

    ArrayList<Transaction> getAll();
}
