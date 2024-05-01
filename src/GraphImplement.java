import Graph.Graph;
import Graph.GraphNode;
import classes.Business;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GraphImplement {



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

                for (Business businessNeighbor: b1.getClosestNeighbors()) {
                        g1.addEdge(new GraphNode(b1), new GraphNode(businessNeighbor), GraphNode.haversine(b1, businessNeighbor));
                }
                fileIn.close();
                in.close();
            }
        }


//        g1.convertGraphToSerFile();

        g1.displayVertexDegrees();
//        System.out.println();
//        g1.getDisjointSets();

        //"tkootvLq3Be6vDg2oMif6g" - American Leak , "t4G4ugGCp1YbkhLOJhS9Ng" = creamy, 6o7lbtHHMBeE5xCOl3SWlA = classy sweet
        //JMczMvfpoCnhyjL523dONg =  Boise Thai Noodle House, -oRyTR9br2I3xMbTvGZDXw - Babby Farms Puppy Boutique,
        //5kr0Xf61tEs5X50PlVLk_g - Summit Family Health


        //rxs7GHCeRh9M5iqJ3VKgNA - Pepper Palace -> Ld7K9CU1bgovY0kM_VpUXw - Eat Well Market/ First Tennessee Bank  - pkc3G9rUGLlNSRrxskGwtA
        //Falafel N Cafe AglkyqtcY_NHlaZHYGJRRQ -> Burger King       - TkIlHiDNcoMt2SL5sA9cCw
        //eeh3p2qf0unkx6AGmBV4EA - Hertz Car Sales Rivergate -> 509S3lxFEPa_qBeokJg47A - Quiznos
        //Classic Car Spa ntg2mWpN-kTsUczgI5ae3g -> Goodwill Thrift Store and Donation Center- eNM4YpOYxGqiQFn_pxFQRA

        //Binford Cafe TMEnErP6Y6mMgfmX2wFcLw -> Outback Steakhouse - 06w-nCn3voKD69dxGxYn-A
        //JMczMvfpoCnhyjL523dONg =  Boise Thai Noodle House -> Canyon Electric - tJa7JuaRV7kWuCllHchxDw
        //Ahi Sushi Bar wMQMMxaGq0HPG0mApezXMw -> Shoe Carnival - wknUAmMjzjAyNFmwUWd4fQ
        //Dadz Bar & Grill 6TwYcKp_47VueK7EwNFDxQ -> Hook & Reel Cajun Seafood & Bar - lGwmPQcKEGPIEWKGSoM1kw
        //*****Balayage Blonde Salon zQJYqfF9S7cXTNkVa9QdPw -> Academic Alliance in Dermatology - YAeRe3GpFPTdDZtSNsr8bQ
        //Showing no path if neighbor and node
        //*****Classic Car Spa   - ntg2mWpN-kTsUczgI5ae3g -> Goodwill Thrift Store and Donation Center  - eNM4YpOYxGqiQFn_pxFQRA
        //rxs7GHCeRh9M5iqJ3VKgNA - Pepper Palace -> Ld7K9CU1bgovY0kM_VpUXw - Eat Well Market/ First Tennessee Bank  - pkc3G9rUGLlNSRrxskGwtA
        GraphNode gn1 = g1.getGraphNodeFromBusinessId("rxs7GHCeRh9M5iqJ3VKgNA");
        GraphNode gn2 = g1.getGraphNodeFromBusinessId("pkc3G9rUGLlNSRrxskGwtA");


        List<GraphNode> list = g1.getShortestPathWithTFIDF(gn1, gn2);
        if (list != null) {
            for (GraphNode i : list) {
                System.out.println(i.getBusiness().getName());
            }
            System.out.println(list.size());
        } else {
            System.out.println("No path found between the specified businesses.");
        }


    }


}
