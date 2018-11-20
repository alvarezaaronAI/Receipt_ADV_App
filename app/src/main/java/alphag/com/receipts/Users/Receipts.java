package alphag.com.receipts.Users;

public class Receipts {

    private int id;
    private String location;
    private String date;
    private String price;
    private int image;

    public Receipts(int id, String location, String date, String price, int image) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

