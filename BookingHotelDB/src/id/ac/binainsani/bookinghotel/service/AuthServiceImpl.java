package id.ac.binainsani.bookinghotel.service;

import id.ac.binainsani.bookinghotel.dao.AdminDAO;
import id.ac.binainsani.bookinghotel.model.Admin;
import java.sql.SQLException;

public class AuthServiceImpl implements AuthService {

    private final AdminDAO adminDAO;
    public boolean adminLoggedIn = false;
    private String loggedInAdmin = "";

    public AuthServiceImpl(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @Override 
    public boolean LoginAdmin(String username, String password) {
        try {
            Admin admin = adminDAO.getAdminByUsername(username);
            if (admin != null && admin.getPassword().equals(password)) {
                adminLoggedIn = true;
                loggedInAdmin = username;
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error during admin login: " + e.getMessage());
            return false;
        }
    }
    

    @Override
    public boolean LoginUser(String username) {
        return username != null && !username.isEmpty();
    }

    @Override
    public boolean IsAdminLoggedIn() {
       return adminLoggedIn;
    }
}
