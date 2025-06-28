package id.ac.binainsani.bookinghotel.view;

import id.ac.binainsani.bookinghotel.service.AuthService;
import id.ac.binainsani.bookinghotel.service.BookingService;
import id.ac.binainsani.bookinghotel.service.HotelRoomService;
import java.util.Scanner;

public class AdminLoginView {

    private final BookingService bookingService;
    private final HotelRoomService hotelRoomService;
    private final AuthService authService;
    private final Scanner scanner;

    public AdminLoginView(BookingService bookingService,
            HotelRoomService hotelRoomService, 
            AuthService authService) {

        this.bookingService = bookingService;
        this.hotelRoomService = hotelRoomService;
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        System.out.print("Username admin: ");
        String username = scanner.nextLine();
        System.out.print("Password admin: ");
        String password = scanner.nextLine();

        if (!authService.LoginAdmin(username, password)) {
            System.out.println("Login gagal.");
            return;
        }
        AdminView adminView = new AdminView(bookingService, hotelRoomService);
        adminView.show();
    }
}
