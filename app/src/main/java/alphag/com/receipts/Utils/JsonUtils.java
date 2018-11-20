package alphag.com.receipts.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alphag.com.receipts.models.Receipt;


public class JsonUtils {
    private static final String TAG="JsonUtils";
    public static ArrayList<Receipt> parseReceipts(String jsonString){
        ArrayList<Receipt> readyReceipts = new ArrayList<>();
        Log.d(TAG, "parseReceipts: " + jsonString);
        try {
            JSONObject mainJSONObject = new JSONObject(jsonString);
            JSONArray items = mainJSONObject.getJSONArray("receipts");
            Log.d(TAG, "parseReceipts: " + mainJSONObject);
            Log.d(TAG, "parseReceipts: " + items);
            for(int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);
                readyReceipts.add(new Receipt(item.getString("longitude"),
                        item.getString("latitude"),
                        item.getString("address"),
                        item.getString("date"),
                        item.getDouble("total")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return readyReceipts;
    }


    // To be used when method above is used
    // ex: ArrayList<Receipt> receipts = parseReceipts(json);

    public static ArrayList<String> getAddresses(ArrayList<Receipt> receipts){
        ArrayList<String> addresses = new ArrayList<>();

        for(Receipt receipt : receipts){
            addresses.add(receipt.getAddress());
        }

        return addresses;
    }
}
