package Graph;

import classes.Business;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Graph implements Serializable {

    private final Map<GraphNode, List<GraphNode>> map = new HashMap<>();


    private static HashMap<String, Business> businessHashMap = new HashMap<>();

    //Map business IDs to review texts
    private static HashMap<String, String> businessReviewMap = new HashMap<>();

    public Graph() throws IOException, ClassNotFoundException {
        File path = new File("src/file_with_neighbours");
        File[] files = path.listFiles();

        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            if (files[i].isFile()) {
                FileInputStream fileIn = new FileInputStream(files[i]);
                ObjectInputStream in =  new ObjectInputStream(fileIn);
                Business b1  = (Business) in.readObject();

                businessHashMap.put(b1.getBusiness_id(), b1);
                businessReviewMap.put(b1.getBusiness_id(), b1.getRv_text());

                fileIn.close();
                in.close();
            }
        }
    }

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

//    public List<GraphNode> getShortestPath(GraphNode start, GraphNode finish) throws IOException, ClassNotFoundException {
//        final Map<GraphNode, Double> distances = new HashMap<>();
//        final Map<GraphNode, GraphNode> previous = new HashMap<>();
//        PriorityQueue<GraphNode> nodes = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
//
//        for (GraphNode vertex : map.keySet()) {
//            distances.put(vertex, vertex.equals(start) ? 0.0 : Double.MAX_VALUE);
//            previous.put(vertex, null);
//            nodes.add(vertex);
//        }
//
//        while (!nodes.isEmpty()) {
//            GraphNode smallest = nodes.poll();
//            if (smallest.equals(finish)) {
//                List<GraphNode> path = new ArrayList<>();
//                while (previous.get(smallest) != null) {
//                    path.add(smallest);
//                    smallest = previous.get(smallest);
//                }
//                Collections.reverse(path);
//                return path;
//            }
//
//            if (distances.get(smallest) == Double.MAX_VALUE) {
//                break; // No path found
//            }
//
//            for (GraphNode neighbor : map.get(smallest)) {
//                System.out.println("lala");
//                System.out.println(neighbor.getBusiness().getName());
//                System.out.println("--> " + smallest.getBusiness().getName());
//                System.out.println("lala");
//                double tfidfScore = calculateTFIDF(smallest, neighbor);
//                double alt = distances.get(smallest) + tfidfScore;
//                if (alt < distances.get(neighbor)) {
//                    distances.put(neighbor, alt);
//                    previous.put(neighbor, smallest);
//                    nodes.remove(neighbor);
//                    nodes.add(neighbor);
//                }
//            }
//            System.out.println("-----");
//        }
//        return new ArrayList<>(distances.keySet());
//    }

    //changes with tf-idf as distance
    public List<GraphNode> getShortestPathWithTFIDF(GraphNode start, GraphNode finish) throws IOException, ClassNotFoundException {
        final Map<GraphNode, Double> tfidfScores = new HashMap<>();
        final Map<GraphNode, GraphNode> previous = new HashMap<>();
        PriorityQueue<GraphNode> nodes = new PriorityQueue<>(Comparator.comparingDouble(tfidfScores::get));

        for (GraphNode vertex : map.keySet()) {
            tfidfScores.put(vertex, vertex.equals(start) ? 0.0 : Double.POSITIVE_INFINITY);
            previous.put(vertex, null);
            nodes.add(vertex);
        }

        while (!nodes.isEmpty()) {
            GraphNode highestTfidfNode = nodes.poll();
            if (highestTfidfNode.equals(finish)) {
                List<GraphNode> path = new ArrayList<>();
                while (previous.get(highestTfidfNode) != null) {
                    path.add(highestTfidfNode);
                    highestTfidfNode = previous.get(highestTfidfNode);
                }
                path.add(start);
                Collections.reverse(path);
                return path;
            }

            if (Double.isInfinite(tfidfScores.get(highestTfidfNode))) {
                break; // No path found
            }

            for (GraphNode neighbor : map.get(highestTfidfNode)) {
                System.out.println("lala");
                System.out.println(neighbor.getBusiness().getName());
                System.out.println("--> " + highestTfidfNode.getBusiness().getName());
                System.out.println("lala");
                double tfidfReciprocal = 1/calculateTFIDF(highestTfidfNode, neighbor);
                //double tfidfScore = calculateTFIDF(highestTfidfNode, neighbor);
                double alt = tfidfScores.get(highestTfidfNode) + tfidfReciprocal;
                if (alt < tfidfScores.get(neighbor)) {
                    tfidfScores.put(neighbor, alt);
                    previous.put(neighbor, highestTfidfNode);
                    nodes.remove(neighbor);
                    nodes.add(neighbor);
                }
            }
            System.out.println("-----");
        }
        return null;
    }



//    public List<GraphNode> getShortestPathWithTFIDF(GraphNode start, GraphNode finish) throws IOException, ClassNotFoundException {
//        final Map<GraphNode, Double> tfidfScores = new HashMap<>();
//        final Map<GraphNode, GraphNode> previous = new HashMap<>();
//        PriorityQueue<GraphNode> nodes = new PriorityQueue<>(Comparator.comparingDouble(tfidfScores::get));
//
//        for (GraphNode vertex : map.keySet()) {
//            tfidfScores.put(vertex, vertex.equals(start) ? 0.0 : Double.MAX_VALUE);
//            previous.put(vertex, null);
//            nodes.add(vertex);
//        }
//
//        while (!nodes.isEmpty()) {
//            GraphNode highestTfidfNode = nodes.poll();
//            if (highestTfidfNode.equals(finish)) {
//                List<GraphNode> path = new ArrayList<>();
//                while (previous.get(highestTfidfNode) != null) {
//                    path.add(highestTfidfNode);
//                    highestTfidfNode = previous.get(highestTfidfNode);
//                }
//                path.add(start);
//                Collections.reverse(path);
//                return path; // Return the constructed path
//            }
//
//            if (Double.isInfinite(tfidfScores.get(highestTfidfNode))) {
//                break; // No reachable path found
//            }
//
//            for (GraphNode neighbor : map.get(highestTfidfNode)) {
//                System.out.println("lala");
//                System.out.println(neighbor.getBusiness().getName());
//                System.out.println("--> " + highestTfidfNode.getBusiness().getName());
//                System.out.println("lala");
//                double tfidfScore = calculateTFIDF(highestTfidfNode, neighbor);
//                double alt = tfidfScores.get(highestTfidfNode) + tfidfScore;
//                if (alt < tfidfScores.get(neighbor)) {
//                    tfidfScores.put(neighbor, alt);
//                    previous.put(neighbor, highestTfidfNode);
//                    nodes.remove(neighbor);
//                    nodes.add(neighbor);
//                }
//            }
//            System.out.println("-----");
//        }
//        return null; // No path found, return null instead of empty list
//    }

    public Map<GraphNode, List<GraphNode>> getMap() {
        return map;
    }

    public static Double calculateTFIDF(GraphNode gn1, GraphNode gn2) throws IOException, ClassNotFoundException {

        //String medoidFilename

        //HashMap objects for count of each word in the review text for each filename
        //check if the words contain in the review text
        //stores the total weighted value for each file name
        HashMap<String, int[]> countOfEachWordMap = new HashMap<>();
        HashMap<String, boolean[]> containsWordMap = new HashMap<>();
        HashMap<String, Double> totalWeightMap = new HashMap<>();


        // function for calculating weight depending on medoid and one review
        String medoidReview = businessHashMap.get(gn1.getBusiness().getBusiness_id()).getRv_text();

        String[] medoidSplit = cleanString(medoidReview);

        // Note: DF means Total Number of reviews that contain that word
        int[] dfCount = new int[medoidSplit.length];

        //traverse b tree
        //for each file name in b tree, count occurance of each word,
        // track if exist in the review
        for (String i: businessHashMap.keySet()) {
            int[] countOfEachWord = new int[medoidSplit.length];
            Arrays.fill(countOfEachWord, 0);
            countOfEachWordMap.put(i, countOfEachWord);


            boolean[] containsWord = new boolean[medoidSplit.length];
            Arrays.fill(containsWord, false);
            containsWordMap.put(i, containsWord);
        }


        int count = 0;

        //for each file names, preprocess the review text
        //do count of word
        // check if the review has the word
        for(String filename: businessHashMap.keySet()) {
            if (!filename.equalsIgnoreCase(gn1.getBusiness().getBusiness_id())) {

                String[] cleanReviewData = cleanString(businessHashMap.get(filename).getRv_text());

                for (String i: cleanReviewData) {
                    for (int j = 0; j < medoidSplit.length; j++) {
                        if (medoidSplit[j].equalsIgnoreCase(i)) {
                            countOfEachWordMap.get(filename)[j]++;
                            containsWordMap.get(filename)[j] = true;
                        }
                    }
                }

                // Calculating the df values from true or false values from frequency table
                for (int i = 0; i < containsWordMap.get(filename).length; i++) {
                    if(containsWordMap.get(filename)[i]) {
                        dfCount[i]++;
                    }
                }

//                count++;
//                if (count % 5000 == 0) {
//                    System.out.println(count);
//                }


            }
        }

        HashMap<String, Double> weightValues = new HashMap<>();
        HashMap<String, String> weightValuesNames = new HashMap<>();
        //for each file name in b tree
        for (String filename: businessHashMap.keySet()) {

            //calculate the weighted value using calculateWeight method using countOfEachWordMap, dfCount, the total number of review
            //add the result value to tatalWeightMap
            totalWeightMap.put(filename, calculateWeight(countOfEachWordMap.get(filename), dfCount, businessHashMap.size()));
//            System.out.println(businessHashMap.get(filename).getName() + calculateWeight(countOfEachWordMap.get(filename), dfCount, businessHashMap.size()));

            //add the value to weightValues Map
            //add the business name and its associated value to weightValuesNames map
            weightValues.put(
                    businessHashMap.get(filename).getBusiness_id() + ".ser",
                    calculateWeight(countOfEachWordMap.get(filename), dfCount, businessHashMap.size()));
            weightValuesNames.put(
                    businessHashMap.get(filename).getBusiness_id() + ".ser",
                    businessHashMap.get(filename).getName()
            );

        }
        //return weightValues map containing filenames and associated weighted values
        return weightValues.get(gn2.getBusiness().getBusiness_id()+ ".ser");
    }


    private static double calculateWeight(int[] tfData, int[] dfData, int totalReview) {
        double total = 0;
        for (int i = 0; i < tfData.length; i++) {
            total += Math.log10(1+tfData[i])*((double) totalReview /(dfData[i]+1));
        }
        return total;
    }

    public static Business findFile(String filename) throws IOException, ClassNotFoundException {
        //file path
        FileInputStream fileIn = new FileInputStream("src/files_with_neighbours/" + filename);
        ObjectInputStream in =  new ObjectInputStream(fileIn);
        Object findOutput = in.readObject();
        fileIn.close();
        in.close();

        return (Business) findOutput;

    }
    // Cleaning the user input string and outputting a String array
    //reusing cleanString from project 1
    private static String[] cleanString(String rawString) throws IOException {
        rawString = rawString.replaceAll("[^a-zA-Z']", " ");
        rawString = rawString.toLowerCase();

        String wordTxt = Files.readString(Paths.get("Library/eng.txt"), Charset.defaultCharset());
        String[] words = wordTxt.split("\\s");
        for (String word : words){
            word = word.toLowerCase();
            rawString = rawString.replaceAll("\\b" + word +"\\b", "");
        }
        return Arrays.stream(rawString.split("\\s+")).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }


}
