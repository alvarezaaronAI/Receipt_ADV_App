package alphag.com.receipts;

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
}
