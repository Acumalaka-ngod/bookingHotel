package id.ac.binainsani.bookinghotel.model;

public class HotelRoom {

    private final String roomNumber;
    private final String type;
    private final int price;
    private boolean available;

    public HotelRoom(String roomNumber, String type) {
        this.roomNumber = roomNumber;

        this.type = type.toLowerCase();
        if (this.type.equals("double")) {
            this.price = 220000;
        } else {
            this.price = 120000;
        }

        this.available = true;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
