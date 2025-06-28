package id.ac.binainsani.bookinghotel.dao;

import id.ac.binainsani.bookinghotel.model.Admin;
import java.sql.SQLException;

public interface AdminDAO {

    Admin getAdminByUsername(String username) throws SQLException;
}
