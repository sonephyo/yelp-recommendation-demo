package Graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph {

    private Map<GraphNode, List<GraphNode>> map = new HashMap<>();

    public void addVertex(GraphNode gn) {
        map.put(gn, new LinkedList<GraphNode>());
    }

    public void addEdge(GraphNode source, GraphNode dest) {
        if (!map.containsKey(source))
            addVertex(source);

        if (!map.containsKey(dest))
            addVertex(dest);

        map.get(source).add(dest);
    }

    public Map<GraphNode, List<GraphNode>> getMap() {
        return map;
    }

}
