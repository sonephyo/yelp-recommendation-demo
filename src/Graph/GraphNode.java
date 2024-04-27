package Graph;


import classes.Business;

import java.io.*;

public class GraphNode implements Serializable, Comparable<GraphNode> {

    private Business business;

    private final int id;

    private double distanceFromSource;

    public GraphNode(Business business) {
        this.business = business;
        this.id = business.hashCode();
    }

    // Cloning constructor
    public GraphNode(GraphNode gn) {
        this.business = gn.getBusiness();
        this.id = gn.hashCode();
        this.distanceFromSource = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass() ) return false;
        GraphNode graphNode =  (GraphNode) obj;
        return this.business.getBusiness_id().equalsIgnoreCase(graphNode.business.getBusiness_id());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37*result + this.business.getLongitude().hashCode();
        result = 37*result + this.business.getLatitude().hashCode();
        result = 37*result + this.business.getBusiness_id().hashCode();
        return result;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public int getId() {
        return id;
    }

    public double getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(double distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }


    @Override
    public String toString() {
        return "GraphNode{" +
                "business=" + business +
                ", id=" + id +
                ", distanceFromSource=" + distanceFromSource +
                '}';
    }

    /**
     * - Haversine formula default
     * @param b1 - business 1
     * @param b2 - business 2
     * @return the haversine distance
     */
    public static double haversine(Business b1, Business b2 ) {
        double eRadius = 6371;
        double dLat = Math.toRadians(b2.getLatitude()-b1.getLatitude());
        double dLon = Math.toRadians(b2.getLongitude()-b1.getLongitude());
        double lat1 = Math.toRadians(b1.getLatitude());
        double lat2 = Math.toRadians(b2.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return eRadius * c;
    }

    @Override
    public int compareTo(GraphNode o) {
        double   result = this.getDistanceFromSource() - o.getDistanceFromSource();
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
