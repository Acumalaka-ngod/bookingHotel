package id.ac.binainsani.bookinghotel.service;

public interface AuthService {

    boolean LoginAdmin(String username, String password);

    boolean LoginUser(String username);
    
    boolean IsAdminLoggedIn();
}
