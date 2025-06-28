package id.ac.binainsani.bookinghotel.dao;

import database.DatabaseConnection;
import id.ac.binainsani.bookinghotel.model.HotelRoom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelRoomDAOImpl implements HotelRoomDAO {

    private final DatabaseConnection dbConnection;

    public HotelRoomDAOImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void addHotelRoom(HotelRoom room) throws SQLException {
        String sql = "INSERT INTO hotel_rooms (room_number, type, price, available) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setInt(3, room.getPrice());
            pstmt.setBoolean(4, room.isAvailable());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                dbConnection.closeConnection(conn);
            }
        }
    }

    @Override
    public ArrayList<HotelRoom> getAllHotelRooms() throws SQLException {
        ArrayList<HotelRoom> rooms = new ArrayList<>();
        String sql = "SELECT room_number, type, price, available FROM hotel_rooms";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String roomNumber = rs.getString("room_number");
                String type = rs.getString("type");
                // Price is set in the HotelRoom constructor based on type, so we don't need to pass it directly
                HotelRoom room = new HotelRoom(roomNumber, type);
                room.setAvailable(rs.getBoolean("available"));
                rooms.add(room);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                dbConnection.closeConnection(conn);
            }
        }
        return rooms;
    }

    @Override
    public HotelRoom getHotelRoomByNumber(String roomNumber) throws SQLException {
        String sql = "SELECT room_number, type, price, available FROM hotel_rooms WHERE room_number = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        HotelRoom room = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type");
                room = new HotelRoom(roomNumber, type);
                room.setAvailable(rs.getBoolean("available"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                dbConnection.closeConnection(conn);
            }
        }
        return room;
    }

    @Override
    public void updateHotelRoomAvailability(String roomNumber, boolean available) throws SQLException {
        String sql = "UPDATE hotel_rooms SET available = ? WHERE room_number = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, available);
            pstmt.setString(2, roomNumber);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                dbConnection.closeConnection(conn);
            }
        }
    }

    @Override
    public void deleteHotelRoom(String roomNumber) throws SQLException {
        String sql = "DELETE FROM hotel_rooms WHERE room_number = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            pstmt.executeUpdate();
        }
    }

}
