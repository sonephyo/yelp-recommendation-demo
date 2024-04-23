
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
        Set<String> uniqueBusinessIds = new HashSet<>(); // to keep track of unique business names

        while ((lineReview = brReview.readLine()) != null) {
            Review r1 = gsonReview.fromJson(lineReview, Review.class);
            Business business = businessHashtable.get(r1.getBusiness_id());
            if (business != null && !uniqueBusinessIds.contains(business.getBusiness_id()) && r1.getReview_text() != null) {
                business.setRv_text(r1.getReview_text());
                uniqueBusinessIds.add(business.getBusiness_id());
                reviewcount++;
            }
        }
        brReview.close();
//        for (Business business : businessHashtable.values()) {
//            System.out.println(business);
//        }
//        for (String id: businessHashtable.keySet()) {
//            if (businessHashtable.get(id).getRv_text() == null) {
//                System.out.println(businessHashtable.get(id));
//            }
//        }
        for (Business business : businessHashtable.values()){
            findNeighbors(business, businessHashtable);
        }
        for (Business business : businessHashtable.values()){
            System.out.println("Business: " + business.getBusiness_id());
            List<Business> neighbors = business.getClosestNeighbors();
            if(neighbors!= null) {
                for (Business neighbor : neighbors) {
                    double distance = Haversine(business, neighbor);
                    System.out.println(" Neighbor id: " + neighbor.getBusiness_id() + " Name: " + neighbor.getName() +
                            ", Distance: " + distance + " km");
                }
            }
            System.out.println();
        }
    }
    public static double Haversine(Business b1, Business b2 ) {
        double eRadius = 6371;
        double dLat = Math.toRadians(b2.getLatitude()-b1.getLatitude());
        double dLon = Math.toRadians(b2.getLongitude()-b1.getLongitude());
        double lat1 = Math.toRadians(b1.getLatitude());
        double lat2 = Math.toRadians(b2.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2* Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return eRadius * c;
    }

    public static void findNeighbors (Business business, Hashtable<String, Business> businessHashtable){
        PriorityQueue<Business> closestNeighbors = new PriorityQueue<>(Comparator.comparingDouble(b->Haversine(business, b)));
        for (Business otherB : businessHashtable.values()){
            if(!business.equals(otherB)){
                closestNeighbors.offer(otherB);
                if(closestNeighbors.size()>4){
                    closestNeighbors.poll();
                }
            }
        }
        List<Business> neighbors = new ArrayList<>(closestNeighbors);
        business.setClosestNeighbors(neighbors);
    }
}
