package id.ac.binainsani.bookinghotel.service;

import id.ac.binainsani.bookinghotel.dao.HotelRoomDAO;
import id.ac.binainsani.bookinghotel.dao.TransactionDAO;
import id.ac.binainsani.bookinghotel.model.HotelRoom;
import id.ac.binainsani.bookinghotel.model.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingServiceImpl implements BookingService {

    private final HotelRoomDAO hotelRoomDAO;
    private final TransactionDAO transactionDAO;

    public BookingServiceImpl(HotelRoomDAO hotelRoomDAO, TransactionDAO transactionDAO) {
        this.hotelRoomDAO = hotelRoomDAO;
        this.transactionDAO = transactionDAO;
    }

    @Override
    public boolean bookHotelRoom(String roomNumber, int days, int payment, String username) {
        try {
            HotelRoom room = hotelRoomDAO.getHotelRoomByNumber(roomNumber);
            if (room == null || !room.isAvailable()) {
                System.out.println("Kamar tidak ditemukan atau tidak tersedia.");
                return false;
            }

            int total = room.getPrice() * days;
            if (payment < total) {
                System.out.println("Pembayaran tidak cukup.");
                return false;
            }

            hotelRoomDAO.updateHotelRoomAvailability(roomNumber, false); // Set to unavailable
            Transaction transaction = new Transaction(
                    "HotelRoom", "HOTEL", roomNumber, days, total, payment, payment - total
            );
            transaction.setCustomerName(username);
            transactionDAO.addTransaction(transaction);
            return true;
        } catch (SQLException e) {
            System.err.println("Error booking room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Transaction> getUserTransactions(String username) {
        try {
            return transactionDAO.getTransactionsByCustomerName(username);
        } catch (SQLException e) {
            System.err.println("Error getting user transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        try {
            return transactionDAO.getAllTransactions();
        } catch (SQLException e) {
            System.err.println("Error getting all transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void addHotelRoom(String roomNumber, String type) {
        try {
            hotelRoomDAO.addHotelRoom(new HotelRoom(roomNumber, type));
        } catch (SQLException e) {
            System.err.println("Error adding hotel room: " + e.getMessage());
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
    public ArrayList<Transaction> getUserHotelTransactions(String username) {
        ArrayList<Transaction> userTransactions = getUserTransactions(username);
        ArrayList<Transaction> hotelTransactions = new ArrayList<>();
        for (Transaction t : userTransactions) {
            if ("HOTEL".equalsIgnoreCase(t.getServiceType())) {
                hotelTransactions.add(t);
            }
        }
        return hotelTransactions;
    }
}
