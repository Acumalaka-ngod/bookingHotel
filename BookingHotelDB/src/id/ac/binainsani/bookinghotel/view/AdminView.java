package id.ac.binainsani.bookinghotel.view;

import id.ac.binainsani.bookinghotel.model.HotelRoom;

import id.ac.binainsani.bookinghotel.model.Transaction;
import id.ac.binainsani.bookinghotel.service.BookingService;
import id.ac.binainsani.bookinghotel.service.HotelRoomService;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminView {

    private final BookingService bookingService;
    private final HotelRoomService hotelRoomService;

    private final Scanner scanner;

    public AdminView(BookingService bookingService,
            HotelRoomService hotelRoomService) {
        this.bookingService = bookingService;
        this.hotelRoomService = hotelRoomService;
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Tambah Kamar Hotel");
            System.out.println("2. Lihat Semua Transaksi");
            System.out.println("3. Lihat Semua Ruangan");
            System.out.println("4. Atur Ketersediaan Kamar/Ruangan");
            System.out.println("5. Hapus Kamar");
            System.out.println("0. Logout");
            System.out.print("Pilih menu: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    AddHotelRoom();
                    break;
                case "2":
                    SeeAllTransactions();
                    break;
                case "3":
                    SeeAllRooms();
                    break;
                case "4":
                    SetAvailables();
                    break;
                case "0":
                    System.out.println("Good Bye");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void AddHotelRoom() {
        System.out.println("\n--- Tambah Kamar Hotel ---");
        System.out.print("Nomor Kamar: ");
        String roomNumber = scanner.nextLine();

        System.out.print("Tipe Kamar (single/double): ");
        String type = scanner.nextLine();

        bookingService.addHotelRoom(roomNumber, type);
        System.out.println("Kamar berhasil ditambahkan.");
    }

    private void SeeAllTransactions() {
        System.out.println("\n--- Daftar Semua Transaksi ---");
        ArrayList<Transaction> transactions = bookingService.getAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println("Belum ada transaksi yang tercatat.");
        } else {
            for (Transaction t : transactions) {
                System.out.println("----------------------------------");
                System.out.println("Customer: " + t.getCustomerName());
                System.out.println("Jenis: " + t.getType());
                System.out.println("Item: " + t.getItemName());
                String durationUnit;
                if (t.getType().equalsIgnoreCase("HotelRoom")) {
                    durationUnit = "hari";
                } else {
                    durationUnit = "jam";
                }
                System.out.println("Durasi: " + t.getDuration() + " " + durationUnit);
                System.out.println("Total: Rp" + t.getTotalPrice());
                System.out.println("Dibayar: Rp" + t.getPayment());
                System.out.println("Kembalian: Rp" + t.getChange());
            }
            System.out.println("----------------------------------");
            System.out.println("Total transaksi: " + transactions.size());
        }
    }

    private void SeeAllRooms() {
        System.out.println("\n=== DAFTAR SEMUA RUANGAN ===");
        System.out.println("\n--- Kamar Hotel ---");
        ArrayList<HotelRoom> hotelRooms = hotelRoomService.getAllRooms();
        if (hotelRooms.isEmpty()) {
            System.out.println("Belum ada kamar hotel yang terdaftar.");
        } else {
            for (HotelRoom room : hotelRooms) {
                System.out.printf("No: %s | Tipe: %s | Harga: Rp%d/hari | Status: %s%n",
                        room.getRoomNumber(),
                        room.getType(),
                        room.getPrice(),
                        room.isAvailable() ? "Tersedia" : "Dipesan");
            }
        }
    }

    private void SetAvailables() {
        System.out.println("\n=== ATUR KETERSEDIAAN RUANGAN ===");
        System.out.println("1. Kamar Hotel");
        System.out.println("0. Kembali");
        System.out.print("Pilih jenis ruangan: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                HotelRoom();
            case "0":
                return;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }

    private void HotelRoom() {
        System.out.println("\n--- KAMAR HOTEL ---");
        ArrayList<HotelRoom> rooms = hotelRoomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("Belum ada kamar hotel yang terdaftar.");
            return;
        }
        for (HotelRoom room : rooms) {
            System.out.printf("No: %s | Tipe: %s | Status: %s%n",
                    room.getRoomNumber(),
                    room.getType(),
                    room.isAvailable() ? "Tersedia" : "Dipesan");
        }

        System.out.print("\nMasukkan nomor kamar yang ingin diubah statusnya: ");
        String roomNumber = scanner.nextLine();
        HotelRoom selectedRoom = null;
        for (HotelRoom room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                selectedRoom = room;
                break;
            }
        }
        if (selectedRoom == null) {
            System.out.println("Kamar tidak ditemukan!");
            return;
        }
        System.out.print("Ubah status ketersediaan (1. Tersedia / 2. Dipesan): ");
        int statusChoice = scanner.nextInt();
        scanner.nextLine();

        if (statusChoice == 1) {
            selectedRoom.setAvailable(true);
            System.out.println("Status kamar berhasil diubah menjadi Tersedia");
        } else if (statusChoice == 2) {
            selectedRoom.setAvailable(false);
            System.out.println("Status kamar berhasil diubah menjadi Dipesan");
        } else {
            System.out.println("Pilihan tidak valid!");
        }
    }
}
