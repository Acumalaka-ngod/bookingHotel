package id.ac.binainsani.bookinghotel;
import database.DatabaseConnection;
import id.ac.binainsani.bookinghotel.dao.*;
import id.ac.binainsani.bookinghotel.service.*;
import id.ac.binainsani.bookinghotel.view.swing.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize database connection
        DatabaseConnection dbConnection = new DatabaseConnection();
        
        // Initialize DAOs
        AdminDAO adminDAO = new AdminDAOImpl(dbConnection);
        HotelRoomDAO hotelRoomDAO = new HotelRoomDAOImpl(dbConnection);
        TransactionDAO transactionDAO = new TransactionDAOImpl(dbConnection);
        // Initialize Services
        AuthService authService = new AuthServiceImpl(adminDAO);
        HotelRoomService roomService = new HotelRoomServiceImpl(hotelRoomDAO);
        TransactionService txService = new TransactionServiceImpl(transactionDAO);
        BookingService bookingService = new BookingServiceImpl(hotelRoomDAO, transactionDAO);
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(authService, bookingService, roomService, txService);
            mainFrame.setVisible(true);
        });
    }
}