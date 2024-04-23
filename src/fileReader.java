
import classes.Business;
import classes.Review;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.*;
import java.io.FileReader;
import java.io.IOException;


public class fileReader {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        BufferedReader br = new BufferedReader(new FileReader("src/database/businesses.json"));

        // Building Gson
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.create();
        String line;
        int count = 0;
        int busCount = 0;


        // Making the hashtable for the businesses
        Hashtable<String, Business> businessHashtable = new Hashtable<>();

        while ((line = br.readLine()) != null && busCount < 1000) {
            if (count % 100 == 0) {
                Business b1 = gson.fromJson(line, Business.class);
                businessHashtable.put(b1.getBusiness_id(), b1);
                busCount++;
            }
            count++;
        }
        br.close();
        BufferedReader brReview = new BufferedReader(new FileReader("src/database/reviews.json"));

        GsonBuilder gbReview = new GsonBuilder();
        Gson gsonReview = gbReview.create();

        String lineReview;
        int reviewcount = 0;
        int reviewLengthToParse = 10000;

        Review[] reviewList = new Review[reviewLengthToParse];
        Set<String> uniqueBusinessNames = new HashSet<>(); // to keep track of unique business names

        while ((lineReview = brReview.readLine()) != null && reviewcount < reviewLengthToParse) {
                Review r1 = gsonReview.fromJson(lineReview, Review.class);
                Business business = businessHashtable.get(r1.getBusiness_id());
                if (business != null && !uniqueBusinessNames.contains(business.getName())) {
                    business.setRv_text(r1.getReview_text());
                    uniqueBusinessNames.add(business.getName());
                    reviewList[reviewcount] = r1;
                    reviewcount++;
                }
        }
        brReview.close();
        for (Business business : businessHashtable.values()){
            System.out.println(business);
        }
    }

}
