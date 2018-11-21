package alphag.com.receipts.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import alphag.com.receipts.models.Receipt;

public class DateUtils {
    /* INSIDE MAIN (online ide)
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        Date currentDay = parseDate(dateFormat.format(date));
        Date rDate = parseDate("11/16/2018");
        Date minDate = subtractDays(currentDay, 7);

        System.out.println("R Date:\t" + rDate.toString());

        System.out.println("TODAY'S Date:\t" + currentDay.toString());
        System.out.println("Min Date:\t" + minDate.toString());

        System.out.println("RESULT:\t" + checkRange(rDate, minDate, currentDay));
     */

    public static boolean filter(Receipt receipt, int days){

        //ArrayList<Receipt> filteredReceipts = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        if(days == 1){
            Date currentDay = parseDate(dateFormat.format(date));
            Date minDate = subtractDays(currentDay, days);
            Date dateFromReceipt = parseDate(receipt.getDate());
            if (checkRange(dateFromReceipt, minDate, currentDay)){
                return true;
            }
        }
//        else if(days == 7){
//            Date currentDay = parseDate(dateFormat.format(date));
//            Date minDate = subtractDays(currentDay, days);
//            for(Receipt receipt : receipts){
//                Date dateFromReceipt = parseDate(receipt.getDate());
//                if (checkRange(dateFromReceipt, minDate, currentDay)){
//                    filteredReceipts.add(receipt);
//                }
//            }
//        }
//        else if(days == 14){
//            Date currentDay = parseDate(dateFormat.format(date));
//            Date minDate = subtractDays(currentDay, days);
//            for(Receipt receipt : receipts){
//                Date dateFromReceipt = parseDate(receipt.getDate());
//                if (checkRange(dateFromReceipt, minDate, currentDay)){
//                    filteredReceipts.add(receipt);
//                }
//            }
//        }
//        else if(days == 30){
//            Date currentDay = parseDate(dateFormat.format(date));
//            Date minDate = subtractDays(currentDay, days);
//            for(Receipt receipt : receipts){
//                Date dateFromReceipt = parseDate(receipt.getDate());
//                if (checkRange(dateFromReceipt, minDate, currentDay)){
//                    filteredReceipts.add(receipt);
//                }
//            }
//        }
        return false;
    }












//    public static ArrayList<Receipt> filter(ArrayList<Receipt> receipts, int days){
//
//        ArrayList<Receipt> filteredReceipts = null;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        Date date = new Date();
//
//        if(days == 7){
//            for(Receipt receipt : receipts){
////                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
////                Date date = new Date();
//                Date currentDay = parseDate(dateFormat.format(date));
//                Date dateFromReceipt = parseDate(receipt.getDate());
//                Date minDate = subtractDays(currentDay, 7);
//
//                if (checkRange(dateFromReceipt, minDate, currentDay)){
//                    filteredReceipts.add(receipt);
//                }
//            }
//        }
//        return filteredReceipts;
//    }



    /*
        These methods help store data when creating Data Object.
        These methods also help convert String dates to Dates for calculations with dates.
     */
    public static Date parseDate(String stringDate) {
        String pattern = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
            System.out.println("inside try: " + date);
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

    public static boolean checkRange(Date receiptDate, Date minDay, Date currDate){
        Date rdate = subtractDays(receiptDate, -1);
        Date currentDate = subtractDays(currDate, -1);

        System.out.println("\nrange: Receipt Date:\t " + rdate);
        System.out.println("range: Current Date:\t" + currentDate);

        return minDay.before(rdate) && !rdate.after(currentDate);
    }
}

