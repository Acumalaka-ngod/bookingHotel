package id.ac.binainsani.bookinghotel.service;

import id.ac.binainsani.bookinghotel.model.HotelRoom;
import java.util.ArrayList;

public interface HotelRoomService {

    void addRoom(HotelRoom room);

    ArrayList<HotelRoom> getAllRooms();

    int getRoomPrice(String roomNumber, int days);

    boolean bookRoom(String roomNumber);

    void deleteRoom(String roomNumber);
}
