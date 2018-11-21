package alphag.com.receipts.models;

import java.util.ArrayList;

public class User {
    private String address;
    private String email;
    private String first_Name;
    private String last_Name;
    private String phone_Number;
    private ArrayList<Receipt> receipts;

    public User(){

    }
    public User(String first_Name, String last_Name,String email) {
        this.email = email;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
    }
    public User(String first_Name, String last_Name,String email, ArrayList<Receipt> receipts) {
        this.email = email;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.receipts = receipts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name() {
        return last_Name;
    }

    public void setLast_Name(String last_Name) {
        this.last_Name = last_Name;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        if(phone_Number.length() == 0 || phone_Number.length() > 10){
            this.phone_Number = null;
        }
        String prefix = "1-";
        String formattedPhoneNumber = prefix
                + phone_Number.substring( 0,3 )
                + "-"
                + phone_Number.substring( 3,6 )
                + "-"
                + phone_Number.substring( 6,10 );
        this.phone_Number = phone_Number;
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(ArrayList<Receipt> receipts) {
        this.receipts = receipts;
    }

    @Override
    public String toString() {
        return "User{" +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", first_Name='" + first_Name + '\'' +
                ", last_Name='" + last_Name + '\'' +
                ", phone_Number='" + phone_Number + '\'' +
                ", receipts=" + receipts +
                '}';
    }
}
