package alphag.com.receipts.models;

import android.graphics.Bitmap;

public class Receipt {

    public String receiptUId;
    private String name;
    private String longitude;
    private String latitude;
    private String address;
    private String date;
    private String snapshotUri;
    private double total;

    /**
     * Full Detail Receipt Stored.
     * @param receiptUId Unique identifier of receipt
     * @param name Name of the receipt
     * @param longitude Longitude of the receipt
     * @param latitude Latitude of the receipt
     * @param address Address of the Receipt
     * @param date Date of the receipt
     * @param snapshotUri Picture content of the receipt
     * @param total Total Val of the receipt
     */
    public Receipt(String receiptUId, String name, String longitude, String latitude, String address, String date, String snapshotUri, double total) {
        this.receiptUId = receiptUId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.date = date;
        this.snapshotUri = snapshotUri;
        this.total = total;
    }

    /**
     * Constructor Without Image Uri
     * @param longitude Longitude of the IMAGE
     * @param latitude Latitiude of the IAMGE
     * @param address Address of the IMAGE
     * @param date Date of the IMAGE
     * @param total Total of IMAGE
     */
    public Receipt(String longitude, String latitude, String address, String date, double total) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.date = date;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getReceiptUId() {
        return receiptUId;
    }

    public void setReceiptUId(String receiptUId) {
        this.receiptUId = receiptUId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSnapshotUri() {
        return snapshotUri;
    }

    public void setSnapshotUri(String snapshotUri) {
        this.snapshotUri = snapshotUri;
    }

    /**
     * @return Returns information about the receipt
     */
    @Override
    public String toString() {
        return "Receipt{" +
                "receiptUId='" + receiptUId + '\'' +
                ", name='" + name + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", snapshotUri='" + snapshotUri + '\'' +
                ", total=" + total +
                '}';
    }
}
