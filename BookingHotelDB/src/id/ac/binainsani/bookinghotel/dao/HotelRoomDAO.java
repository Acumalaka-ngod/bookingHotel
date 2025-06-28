package id.ac.binainsani.bookinghotel.dao;

import id.ac.binainsani.bookinghotel.model.HotelRoom;
import java.sql.SQLException;
import java.util.ArrayList;

public interface HotelRoomDAO {

    void addHotelRoom(HotelRoom room) throws SQLException;

    ArrayList<HotelRoom> getAllHotelRooms() throws SQLException;

    HotelRoom getHotelRoomByNumber(String roomNumber) throws SQLException;

    void updateHotelRoomAvailability(String roomNumber, boolean available) throws SQLException;

    void deleteHotelRoom(String roomNumber) throws SQLException;
}
