package Graph;

import java.util.List;
import java.util.Map;

public class GraphNode {

    private String name;

    private int id;

    public GraphNode(String name) {
        this.name = name;
        this.id = name.hashCode();
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "name='" + name + '\'' +
                '}';
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

    public static void main(String[] args) {
        Graph g1 = new Graph();

        g1.addEdge(new GraphNode("test1"), new GraphNode("test2"));
        g1.addEdge(new GraphNode("test1"), new GraphNode("test3"));
        g1.addEdge(new GraphNode("test1"), new GraphNode("test4"));
        g1.addEdge(new GraphNode("test2"), new GraphNode("test3"));
        g1.addEdge(new GraphNode("test2"), new GraphNode("test5"));

        Map<GraphNode, List<GraphNode>> output = g1.getMap();

        System.out.println(output.keySet());




    }
}
