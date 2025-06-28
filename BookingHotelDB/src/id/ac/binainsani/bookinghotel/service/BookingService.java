package id.ac.binainsani.bookinghotel.service;

import id.ac.binainsani.bookinghotel.model.Transaction;
import java.util.ArrayList;

public interface BookingService {

    ArrayList<Transaction> getAllTransactions();

    public boolean bookHotelRoom(String roomNumber, int days, int payment, String username);

    public ArrayList<Transaction> getUserTransactions(String username);

    public void addHotelRoom(String RoomNumber, String Type);

    public ArrayList<Transaction> getAllHotelTransactions();
    
    public ArrayList<Transaction> getUserHotelTransactions(String username);
}
