import Graph.Graph;
import Graph.GraphNode;
import classes.Business;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Objects;

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

//        g1.displayVertexDegrees();
//        System.out.println();
//        g1.getDisjointSets();


        GraphNode gn1 = g1.getGraphNodeFromBusinessId("n2Va0mKydUWM-Y4fv1pOtA");
        GraphNode gn2 = g1.getGraphNodeFromBusinessId("Pns2l4eNsfO8kk83dixA6A");


        List<GraphNode> list = g1.getShortestPath(gn1, gn2);
        for (GraphNode i : list) {
            System.out.println(i.getBusiness().getName());
        }
        System.out.println(list.size());
    }

}
