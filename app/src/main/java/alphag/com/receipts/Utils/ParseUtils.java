package alphag.com.receipts.Utils;

import android.util.Log;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class ParseUtils {

    public static void cleanUpStringPriceToDoublePrice(String price, Set<Double> pricesHashSet){
        // This line splits the line by words or numbers and
        // avoids special characters except the '.'
        String rawStringArray[] = price.split("[^.\\w]");
        String lastElementInLine = rawStringArray[rawStringArray.length-1];

        // Since most receipts have their values on the right side and since we are reading line by
        // line, we take the last element from the current line and parse the element to our HashSet
        if (lastElementInLine.contains(".")){
            pricesHashSet.add(Double.parseDouble(lastElementInLine));
        }
    }


    public static double getMaxPrice(Collection<Double> prices){
        double maxPrice = 0;
        for(double price: prices){
            if (price > maxPrice){
                maxPrice = price;
            }
        }
        return maxPrice;
    }


    public static String getAddressFromReceipt(String address){
        String addressRegex = "[^\\d]+[^A-Za-z0-9\\s,.]+";
        String reduceStringToAddress[] = address.toLowerCase().split(addressRegex);
        String streetAbbreviations[] = {"st", "street", "ave", "avenue", "blvd", "boulevard", "dr", "drive"};

        // Before added this to if statement:
        // element.matches("[\\d]+[A-Za-z0-9\\s,.]+") && element...
        for (String filteredString : reduceStringToAddress) {
            if (filteredString.contains("st") || filteredString.contains("street")
                    || filteredString.contains("ave") || filteredString.contains("avenue")
                    || filteredString.contains("blvd") || filteredString.contains("boulevard")
                    || filteredString.contains("dr") || filteredString.contains("drive"))
                return filteredString;
        }
        return null;
    }

    public static String getDateFromReceipt(String date){
        String dateRegex = "^(0[1-9]|1[0-2])/^(0[1-9]|1\\d|2\\d|3[01])/^(\\d{4}|\\d{2})";
        if (date.contains("/")) {
            String reduceStringToDate[] = date.toLowerCase().split(dateRegex);
            date = reduceStringToDate[0];
            Log.d("DATE", "getDateFromReceipt: " + Arrays.toString(reduceStringToDate));
            return date;
        }
        return null;
    }
}
