package alphag.com.receipts.models;

import java.util.ArrayList;

public class User {
    private String userId;
    private String address;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private ArrayList<Receipt> receiptsArrayList;

    public User(String firstName, String lastName,String email) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User(String firstName, String lastName, String email, ArrayList<Receipt> receiptsArrayList) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.receiptsArrayList = receiptsArrayList;
    }
    public User(String userId, String email, String firstName, String lastName, String phoneNumber, ArrayList<Receipt> receiptsArrayList) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = setPhoneNumber(phoneNumber);
        this.receiptsArrayList = receiptsArrayList;
    }

    public User(ArrayList<Receipt> receiptsArrayList) {
        this.receiptsArrayList = receiptsArrayList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Receipt> getReceiptsArrayList() {
        return receiptsArrayList;
    }

    public void setReceiptsArrayList(ArrayList<Receipt> receiptsArrayList) {
        this.receiptsArrayList = receiptsArrayList;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String setPhoneNumber(String phoneNumber) {
        if(phoneNumber.length() == 0 || phoneNumber.length() > 10){
            return this.phoneNumber = null;
        }
        String prefix = "1-";
        String formattedPhoneNumber = prefix
                + phoneNumber.substring( 0,3 )
                + "-"
                + phoneNumber.substring( 3,6 )
                + "-"
                + phoneNumber.substring( 6,10 );
        return this.phoneNumber = formattedPhoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", receiptsArrayList=" + receiptsArrayList +
                '}';
    }
}
