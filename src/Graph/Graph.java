package Graph;

import java.io.Serializable;
import java.util.*;

public class Graph implements Serializable {

    private final Map<GraphNode, List<GraphNode>> map = new HashMap<>();

    public void addVertex(GraphNode gn) {
        map.put(gn, new LinkedList<GraphNode>());
    }

    public void addEdge(GraphNode source, GraphNode dest, double distance) {
        if (!map.containsKey(source))
            addVertex(source);

        if (!map.containsKey(dest))
            addVertex(dest);

        GraphNode gn = new GraphNode(dest);
        gn.setDistanceFromSource(distance);

        map.get(source).add(gn);
    }

    public void getDisjointSets() {
        UnionFind uf = new UnionFind(new HashSet<>(map.keySet()));

        // Apply union operation for all connected nodes
        for (GraphNode node : map.keySet()) {
            for (GraphNode neighbor : map.get(node)) {
                uf.union(node, neighbor);
            }
        }

        // Group nodes by their root to form the disjoint sets
        Map<GraphNode, Set<GraphNode>> components = new HashMap<>();
        for (GraphNode node : map.keySet()) {
            GraphNode root = uf.find(node);
            components.putIfAbsent(root, new HashSet<>());
            components.get(root).add(node);
        }

        // Print or return the components
        for (Set<GraphNode> component : components.values()) {
            System.out.println(component);
        }

        }


    public Map<GraphNode, List<GraphNode>> getMap() {
        return map;
    }

}
