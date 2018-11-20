package alphag.com.receipts.models;

import android.graphics.Bitmap;

public class Receipt {

    private String longitude;
    private String latitude;
    private String address;
    private String date;
    private Bitmap snapshot;
    private double total;

    // Receipt ID's will start from 0
    public static int counter = 0;
    public final int receiptId;

    public Receipt(String longitude, String latitude, String address, String date, double total) {
        this.receiptId = counter++;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.date = date;
        this.snapshot = snapshot;
        this.total = total;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Bitmap snapshot) {
        this.snapshot = snapshot;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId=" + receiptId +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", snapshot=" + snapshot +
                ", total=" + total +
                '}';
    }
}
