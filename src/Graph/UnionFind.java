package Graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UnionFind {

        private final Map<GraphNode, GraphNode> parent;
        private final Map<GraphNode, Integer> rank;

        public UnionFind(Set<GraphNode> elements) {
            parent = new HashMap<>();
            rank = new HashMap<>();
            for (GraphNode element : elements) {
                parent.put(element, element);
                rank.put(element, 0);
            }
        }

        public GraphNode find(GraphNode node) {
            if (parent.get(node) != node) {
                parent.put(node, find(parent.get(node)));  // Path compression
            }
            return parent.get(node);
        }

        public void union(GraphNode node1, GraphNode node2) {
            GraphNode root1 = find(node1);
            GraphNode root2 = find(node2);

            if (root1 != root2) {
                // Union by rank
                if (rank.get(root1) > rank.get(root2)) {
                    parent.put(root2, root1);
                } else if (rank.get(root1) < rank.get(root2)) {
                    parent.put(root1, root2);
                } else {
                    parent.put(root2, root1);
                    rank.put(root1, rank.get(root1) + 1);
                }
            }
        }
}
