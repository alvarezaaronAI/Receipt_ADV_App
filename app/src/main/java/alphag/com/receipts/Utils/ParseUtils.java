package alphag.com.receipts.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
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
        String dateRegex = "([1-9]|0[1-9]|1[0-2])/([1-9]|0[1-9]|1\\d|2\\d|3[01])/(\\d{4}|\\d{2})";
        String finalDate = null;
        if (date.contains("/")) {
            String reduceStringToDate[] = date.split(" ");
            for (String group : reduceStringToDate) {
                if (group.matches(dateRegex)) {
                    finalDate = group;
                    break; // Breaks loop as soon as first date is seen to avoid "expiration date"
                }
            }
            return finalDate;
        }
        return null;
    }

    public static Date parseDate(String stringDate) {
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }


    public static String formatDate(Date aDate) {
        return getMonth(aDate) + "/" + getDay(aDate) + "/" + getYear(aDate);
    }


    public static boolean checkRange(Date receiptDate, Date minDay){
        return minDay.before(receiptDate) || receiptDate.after(minDay);
    }


    /*
        Helper Methods
     */
    // +1 since the months start from 0..11
    public static int getMonth(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getYear(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.YEAR);
    }
}
