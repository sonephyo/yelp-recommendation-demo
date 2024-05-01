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
                    GraphNode gn1 = new GraphNode(b1);
                    GraphNode gn2 = new GraphNode(businessNeighbor);
                    g1.addEdge(new GraphNode(b1), new GraphNode(businessNeighbor), Graph.calculateTFIDF(gn1, gn2));
                }
                fileIn.close();
                in.close();
            }
        }


//        g1.convertGraphToSerFile();

        g1.displayVertexDegrees();
//        System.out.println();
//        g1.getDisjointSets();
//
//        System.out.println();
//
//
//        GraphNode gn1 = g1.getGraphNodeFromBusinessId("qKcEyi0idj7cTQYzbhuKkg");
//        GraphNode gn2 = g1.getGraphNodeFromBusinessId("8t6mbhBNAT027aeDQ4mR3A");
//
//
//        List<GraphNode> list = g1.getShortestPath(gn1, gn2);
//        for (GraphNode i : list) {
//            System.out.println(i.getBusiness().getName());
//        }
//        System.out.println(list.size());


//        for (Set<GraphNode> gnSet: g1.getDisjointSetResults().values()) {
//            GraphNode gn3 = g1.getGraphNodeFromBusinessId("qKcEyi0idj7cTQYzbhuKkg");
//            GraphNode gn4 = g1.getGraphNodeFromBusinessId("8t6mbhBNAT027aeDQ4mR3A");
//            System.out.println(gnSet.contains(gn3) && gnSet.contains(gn4));
//        }

//        Map<GraphNode, Double> output = g1.getShortestPath(gn1,gn2);
//        for (GraphNode gn: output.keySet()) {
//            System.out.println(gn.getBusiness().getName() + ", " + gn.getBusiness().getBusiness_id() + ": " + output.get(gn));
//        }

    }


}
