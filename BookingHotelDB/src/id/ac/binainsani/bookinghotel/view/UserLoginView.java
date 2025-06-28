package id.ac.binainsani.bookinghotel.view;

import id.ac.binainsani.bookinghotel.service.AuthService;
import id.ac.binainsani.bookinghotel.service.BookingService;
import id.ac.binainsani.bookinghotel.service.HotelRoomService;
import java.util.Scanner;

public class UserLoginView {

    private final AuthService authService;
    private final BookingService bookingService;
    private final HotelRoomService hotelRoomService;
    private final Scanner scanner;

    public UserLoginView(AuthService authService,
            BookingService bookingService,
            HotelRoomService hotelRoomService) {
        this.authService = authService;
        this.bookingService = bookingService;
        this.hotelRoomService = hotelRoomService;
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        System.out.print("Username user: ");
        String username = scanner.nextLine();

        if (!authService.LoginUser(username)) {
            System.out.println("Login gagal. Kembali ke menu utama.");
            return;
        }
        System.out.println("Login berhasil.");
        new UserView(bookingService, hotelRoomService, username).show();
    }
}
