package Graph;

import classes.Business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    public GraphNode getGraphNodeFromBusinessId(String business_id) {
        for (GraphNode gn: map.keySet()) {
            if (gn.getBusiness().getBusiness_id().equalsIgnoreCase(business_id)) {
                return gn;
            }
        }
        return null;
    }

    public void displayVertexDegrees() {

        System.out.println("Vertex Degrees:");
        int count = 0;
        for (GraphNode entry : map.keySet()) {
            System.out.println("-------");
//            System.out.println(map.get(entry.getKey()).size() + " -> " + entry.getValue().size());
            System.out.println(entry.getBusiness().getName() + " " + entry.getBusiness().getBusiness_id());
            for (GraphNode node: map.get(entry)) {
                System.out.println(node.getBusiness().getName() + "       - " + node.getBusiness().getBusiness_id());
            }
            System.out.println("-------");
            count++;
        }
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

        /**
         * Print out setsize
         */
        for (Set<GraphNode> component : components.values()) {
            System.out.println(component.size());
        }


    }

    public void convertGraphToSerFile() throws IOException { //serialize B-tree objects to a file
        FileOutputStream fileOut = new FileOutputStream("src/graphOutput/graphOutput.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
    }

//    public List<GraphNode> getShortestPath(GraphNode start, GraphNode finish) {
//        final Map<GraphNode, Double> distances = new HashMap<>();
//        final Map<GraphNode, GraphNode> previous = new HashMap<>();
//        PriorityQueue<GraphNode> nodes = new PriorityQueue<GraphNode>();
//
//        for (GraphNode vertex: map.keySet()) {
//            if (vertex.equals(start)) {
//                distances.put(vertex,0.0);
//                nodes.add(new GraphNode(vertex));
//            } else {
//                distances.put(vertex, Double.MAX_VALUE);
//                nodes.add(new GraphNode(vertex));
//            }
//            previous.put(vertex, null);
//        }
//
//        while (!nodes.isEmpty()) {
//            GraphNode smallest = nodes.poll();
//            if (smallest.getId() == finish.getId()) {
//                final List<GraphNode> path = new ArrayList<>();
//                while (previous.get(smallest) != null) {
//                    path.add(smallest);
//                    smallest = previous.get(smallest);
//                }
//                return path;
//            }
//
//            if (distances.get(smallest) == Double.MAX_VALUE) {
//                break;
//            }
//
//            for (GraphNode neighbor: map.get(smallest)) {
//                double alt = distances.get(smallest) + neighbor.getDistanceFromSource();
//                if (alt < distances.get(neighbor)) {
//                    distances.put(neighbor, alt);
//                    previous.put(neighbor, smallest);
//
//                    for (GraphNode n: nodes) {
//                        if (n.equals(neighbor)) {
//                            nodes.remove(n);
//                            n.setDistanceFromSource(alt);
//                            nodes.add(n);
//                        }
//                    }
//                }
//            }
//        }
//        return  new ArrayList<>(distances.keySet());
//    }
//

    public List<GraphNode> getShortestPath(GraphNode start, GraphNode finish) {
        final Map<GraphNode, Double> distances = new HashMap<>();
        final Map<GraphNode, GraphNode> previous = new HashMap<>();
        PriorityQueue<GraphNode> nodes = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (GraphNode vertex : map.keySet()) {
            distances.put(vertex, vertex.equals(start) ? 0.0 : Double.MAX_VALUE);
            previous.put(vertex, null);
            nodes.add(vertex);
        }

        while (!nodes.isEmpty()) {
            GraphNode smallest = nodes.poll();
            if (smallest.equals(finish)) {
                List<GraphNode> path = new ArrayList<>();
                while (previous.get(smallest) != null) {
                    path.add(smallest);
                    smallest = previous.get(smallest);
                }
                Collections.reverse(path);
                return path;
            }

            if (distances.get(smallest) == Double.MAX_VALUE) {
                break; // No path found
            }

            for (GraphNode neighbor : map.get(smallest)) {
                double alt = distances.get(smallest) + neighbor.getDistanceFromSource();
                if (alt < distances.get(neighbor)) {
                    distances.put(neighbor, alt);
                    previous.put(neighbor, smallest);
                    nodes.remove(neighbor);
                    nodes.add(neighbor);
                }
            }
        }
        return new ArrayList<>(distances.keySet());
    }

    public Map<GraphNode, List<GraphNode>> getMap() {
        return map;
    }



}
