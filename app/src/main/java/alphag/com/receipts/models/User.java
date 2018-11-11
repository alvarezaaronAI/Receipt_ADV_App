package alphag.com.receipts.models;

import java.util.ArrayList;

public class User {
    private String userId;
    private ArrayList<Receipt> receiptsArrayList;

    public User(String userId, ArrayList<Receipt> receiptsArrayList) {
        this.userId = userId;
        this.receiptsArrayList = receiptsArrayList;
    }

    public ArrayList<Receipt> getReceiptsArrayList() {
        return receiptsArrayList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setReceiptsArrayList(ArrayList<Receipt> receiptsArrayList) {
        this.receiptsArrayList = receiptsArrayList;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", receiptsArrayList=" + receiptsArrayList +
                '}';
    }
}
