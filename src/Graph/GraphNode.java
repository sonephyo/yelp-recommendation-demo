package Graph;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GraphNode implements Serializable {

    private String name;

    private final int id;

    private double distanceFromSource;

    public GraphNode(String name) {
        this.name = name;
        this.id = name.hashCode();
    }

    // Cloning constructor
    public GraphNode(GraphNode gn) {
        this.name = gn.getName();
        this.id = gn.hashCode();
        this.distanceFromSource = 0;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass() ) return false;
        GraphNode graphNode =  (GraphNode) obj;
        return this.name.equalsIgnoreCase(graphNode.name);
    }

    @Override
    public int hashCode() {
        return this.id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", id=" + id +
                ", distanceFromSource=" + distanceFromSource +
                '}';
    }

    public static void main(String[] args) {
        Graph g1 = new Graph();

        g1.addEdge(new GraphNode("test1"), new GraphNode("test2"), 10);
        g1.addEdge(new GraphNode("test1"), new GraphNode("test3"), 11);
        g1.addEdge(new GraphNode("test3"), new GraphNode("test1"), 1110);

        g1.addEdge(new GraphNode("test1"), new GraphNode("test4"), 12);
        g1.addEdge(new GraphNode("test2"), new GraphNode("test3"), 13);
        g1.addEdge(new GraphNode("test2"), new GraphNode("test5"), 14);
        g1.addEdge(new GraphNode("test10"), new GraphNode("test11"), 14);
        g1.addEdge(new GraphNode("test12"), new GraphNode("test15"), 4);
        g1.addEdge(new GraphNode("test10"), new GraphNode("test11"), 14);



        g1.addEdge(new GraphNode("test20"), new GraphNode("test21"), 14);
        g1.addEdge(new GraphNode("test20"), new GraphNode("test21"), 20);



        g1.getDisjointSets();


    }
}
