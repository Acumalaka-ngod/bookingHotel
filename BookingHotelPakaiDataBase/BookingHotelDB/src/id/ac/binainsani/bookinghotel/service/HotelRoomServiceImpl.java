package id.ac.binainsani.bookinghotel.service;

import id.ac.binainsani.bookinghotel.dao.HotelRoomDAO;
import id.ac.binainsani.bookinghotel.model.HotelRoom;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelRoomServiceImpl implements HotelRoomService {

    private final HotelRoomDAO hotelRoomDAO;

    public HotelRoomServiceImpl(HotelRoomDAO hotelRoomDAO) {
        this.hotelRoomDAO = hotelRoomDAO;
    }

    @Override
    public void addRoom(HotelRoom room) {
        try {
            hotelRoomDAO.addHotelRoom(room);
        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<HotelRoom> getAllRooms() {
        try {
            return hotelRoomDAO.getAllHotelRooms();
        } catch (SQLException e) {
            System.err.println("Error getting all rooms: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public int getRoomPrice(String roomNumber, int days) {
        try {
            HotelRoom room = hotelRoomDAO.getHotelRoomByNumber(roomNumber);
            if (room != null && room.isAvailable()) {
                return room.getPrice() * days;
            }
        } catch (SQLException e) {
            System.err.println("Error getting room price: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean bookRoom(String roomNumber) {
        try {
            HotelRoom room = hotelRoomDAO.getHotelRoomByNumber(roomNumber);
            if (room != null && room.isAvailable()) {
                hotelRoomDAO.updateHotelRoomAvailability(roomNumber, false);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error booking room: " + e.getMessage());
        }
        return false;
    } 
    
    @Override
    public void deleteRoom(String roomNumber) {
        try {
            hotelRoomDAO.deleteHotelRoom(roomNumber);
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
        }
    }
}
