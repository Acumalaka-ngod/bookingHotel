package id.ac.binainsani.bookinghotel.view;

import id.ac.binainsani.bookinghotel.model.HotelRoom;
import id.ac.binainsani.bookinghotel.model.Transaction;
import id.ac.binainsani.bookinghotel.service.BookingService;
import id.ac.binainsani.bookinghotel.service.HotelRoomService;
import java.util.ArrayList;
import java.util.Scanner;

public class UserView {

    private final BookingService bookingService;
    private final String username;
    private final Scanner scanner;
    private final HotelRoomService hotelRoomService;

    public UserView(BookingService bookingService,
            HotelRoomService hotelRoomService,
            String username) {
        this.bookingService = bookingService;
        this.hotelRoomService = hotelRoomService;
        this.username = username;
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        while (true) {
            System.out.println("\n=== MENU USER ===");
            System.out.println("1. Booking Kamar");
            System.out.println("2. Lihat Transaksi Saya");
            System.out.println("0. Logout");
            System.out.print("Pilih menu: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    bookingHotelRoom();
                    break;
                case "2":
                    SeeMyTransaction();
                    break;
                case "0":
                    System.out.println("Logout berhasil.");
                    return;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void bookingHotelRoom() {
        System.out.println("\n--- Kamar Hotel ---");
        ArrayList<HotelRoom> hotelRooms = hotelRoomService.getAllRooms();
        if (hotelRooms.isEmpty()) {
            System.out.println("Belum ada kamar hotel yang terdaftar.");
            return;
        } else {
            for (HotelRoom room : hotelRooms) {
                System.out.printf("No: %s | Tipe: %s | Harga: Rp%d/hari | Status: %s%n",
                        room.getRoomNumber(),
                        room.getType(),
                        room.getPrice(),
                        room.isAvailable() ? "Tersedia" : "Dipesan");
            }
        }

        System.out.println("\n--- Booking Kamar ---");
        System.out.print("Nomor Kamar: ");
        String roomNumber = scanner.nextLine();

        System.out.print("Lama Menginap (hari): ");
        int days = Integer.parseInt(scanner.nextLine());

        int total = hotelRoomService.getRoomPrice(roomNumber, days);
        System.out.println("Total Harga: Rp." + total);

        System.out.print("Jumlah Pembayaran: ");
        int payment = scanner.nextInt();
        scanner.nextLine();

        if (payment < total) {
            System.out.println("Pembayaran tidak cukup! Silakan masukkan jumlah yang benar.");
            return;
        }

        if (bookingService.bookHotelRoom(roomNumber, days, payment, username)) {
            System.out.println("Booking kamar berhasil!");
        } else {
            System.out.println("Booking kamar gagal!");
        }
    }

    private void SeeMyTransaction() {

        System.out.println("\n--- Riwayat Transaksi Saya ---");
        ArrayList<Transaction> transactions = bookingService.getUserTransactions(username);

        if (transactions.isEmpty()) {
            System.out.println("Belum ada transaksi yang tercatat.");
        } else {
            for (Transaction t : transactions) {
                System.out.println("----------------------------------");
                System.out.println("Jenis: " + t.getType());
                System.out.println("Item: " + t.getItemName());
                System.out.println("Durasi: " + t.getDuration() + (t.getType().contains("HotelRoom") ? " hari" : " jam"));
                System.out.println("Total: Rp" + t.getTotalPrice());
                System.out.println("Dibayar: Rp" + t.getPayment());
                System.out.println("Kembalian: Rp" + t.getChange());
            }
        }
    }
}
