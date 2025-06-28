package id.ac.binainsani.bookinghotel.view;



import id.ac.binainsani.bookinghotel.service.AuthService;
import id.ac.binainsani.bookinghotel.service.BookingService;
import id.ac.binainsani.bookinghotel.service.HotelRoomService;
import id.ac.binainsani.bookinghotel.service.TransactionService;
import java.util.Scanner;

public class MainView {

    private AuthService authService;
    private BookingService bookingService;
    private HotelRoomService hotelRoomService;
    private Scanner scanner;

    public MainView(AuthService authService,
            BookingService bookingService,
            HotelRoomService hotelRoomService,
            TransactionService txService) {
        this.authService = authService;
        this.bookingService = bookingService;
        this.hotelRoomService = hotelRoomService;
        this.scanner = new Scanner(System.in);
        
    }

    public void show() {
        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("0. Exit");
            System.out.print("Pilih: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    GoToAdminLogin();
                    break;
                case "2":
                    GoToUserLogin();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice");
                    show();
            }
        }
    }

    private void GoToAdminLogin() {
        AdminLoginView adminLoginView = new AdminLoginView(bookingService, hotelRoomService,authService);
        adminLoginView.show();
    }

    private void GoToUserLogin() {
        UserLoginView userLoginView = new UserLoginView(authService, bookingService, hotelRoomService);
        userLoginView.show();
    }
}
