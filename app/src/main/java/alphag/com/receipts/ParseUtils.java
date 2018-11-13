package alphag.com.receipts;

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

//    public static String getAddressFromReceipt(String address){
//        // Assume that a non perfect address string can still pass for google places does a good job
//        // going through the real address
//        String streetAbbreviations[] = {"st", "street", "ave", "avenue", "blvd", "boulevard", "dr", "drive"};
//        String reduceStringToAddress[] = address.toLowerCase().split("[^\\d]+[^A-Za-z0-9\\s,.]+");
//        // OR TRY
//        // String reduceStringToAddress[] = x.split("[^\\d]+[^A-Za-z0-9\\s,.]+");
//        Log.d("ADDRESS", "getAddressFromReceipt: " + Arrays.toString(reduceStringToAddress));
//        String finalString = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
//            finalString = String.join(" ", reduceStringToAddress);
//
//        for (int i = 0; i < streetAbbreviations.length; i++) {
//            String street = streetAbbreviations[i];
//            if (address.contains(street)) {
//                if (reduceStringToAddress[i].matches("[^\\d]+[^A-Za-z0-9\\s,.]+")) {
//                    Log.d("ADDRESS", "MATCH: " + reduceStringToAddress[i]);
//                    return reduceStringToAddress[i];
//                }
//            }
//        }
//        return null;
//    }

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

                //Log.d("TEST", "MATCH: " + filteredString);
                return filteredString;
        }
        return null;
    }
}
