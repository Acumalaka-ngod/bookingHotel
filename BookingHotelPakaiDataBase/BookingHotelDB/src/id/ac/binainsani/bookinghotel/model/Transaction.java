package id.ac.binainsani.bookinghotel.model;

public class Transaction {

    private final String type;
    private final String serviceType;
    private final String itemName;
    private final int duration;
    private final int totalPrice;
    private final int payment;
    private final int change;
    private String customerName;

    public Transaction(String type,
            String serviceType,
            String itemName,
            int duration,
            int totalPrice,
            int payment,
            int change) {
        this.type = type;
        this.serviceType = serviceType;
        this.itemName = itemName;
        this.duration = duration;
        this.totalPrice = totalPrice;
        this.payment = payment;
        this.change = payment - totalPrice;
    }

    public String getReportLine() {
        String durationUnit = "jam";
        if ("HotelRoom".equalsIgnoreCase(type)) {
            durationUnit = "hari";
        }
        return String.format("[%s] %s: %s | Durasi: %d %s | Total: Rp%d | Bayar: Rp%d | Kembalian: Rp%d",
                type, type, itemName, duration, durationUnit, totalPrice, payment, change);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getType() {
        return type;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getItemName() {
        return itemName;
    }

    public int getDuration() {
        return duration;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPayment() {
        return payment;
    }

    public int getChange() {
        return change;
    }
}
