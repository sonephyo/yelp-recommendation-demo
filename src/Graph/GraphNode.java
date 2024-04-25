package Graph;

import bTree.BTree;
import classes.Business;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphNode implements Serializable {

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
        return this.id;
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Graph g1 = new Graph();


        File path = new File("src/file_with_neighbours");
        File[] files = path.listFiles();

        // Deserializing and importing in BTree
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            if (files[i].isFile()) {
                FileInputStream fileIn = new FileInputStream(files[i]);
                ObjectInputStream in =  new ObjectInputStream(fileIn);
                Business b1  = (Business) in.readObject();
                System.out.println("----------");
                for (Business businessNeighbor: b1.getClosestNeighbors()) {
                    g1.addEdge(new GraphNode(b1), new GraphNode(businessNeighbor), haversine(b1, businessNeighbor));
                    System.out.println(businessNeighbor.getName());
                    System.out.println(haversine(b1, businessNeighbor));
                }
                fileIn.close();
                in.close();
            }
        }

        g1.displayVertexDegrees();
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        g1.getDisjointSets();
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
}
