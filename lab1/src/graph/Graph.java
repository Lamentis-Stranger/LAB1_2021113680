package graph;
import java.io.IOException;
import java.util.*;

public class Graph {
    private Map<String, List<Edge>> adjacencyList;

    private static volatile boolean exit = false; // 使用volatile关键字确保多线程之间的可见性

    public Graph(){
        this.adjacencyList = new HashMap<>();
    }
    public void addEdge(String source,String target){
        this.adjacencyList.putIfAbsent(source, new ArrayList<>());
        List<Edge> edges = this.adjacencyList.get(source);
        for(Edge edge:edges){
            if(edge.target.equals(target)){
                edge.weight++;
                return;
            }
        }
        edges.add(new Edge(target, 1));
    }

    public List<Edge> getEdges(String node) {//获得一个顶点的所有边
        return this.adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public Set<String> getVertices() {//获得图的所有顶点
        return this.adjacencyList.keySet();
    }

    // 查询桥接词
    public String queryBridgeWords(String word1, String word2) {
        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
            return "No " + word1 + " or " + word2 + " in the graph!";
        }

        List<String> bridgeWords = new ArrayList<>();
        List<Edge> firstWordEdges = getEdges(word1);
        for (Edge edge : firstWordEdges) {
            String possibleBridge = edge.target;
            List<Edge> secondWordEdges = getEdges(possibleBridge);
            for (Edge secondEdge : secondWordEdges) {
                if (secondEdge.target.equals(word2)) {
                    bridgeWords.add(possibleBridge);
                }
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else {
            return "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords) + ".";
        }
    }

    // 查询桥接词
    public List<String> BridgeWordsResult(String word1, String word2) {
        List<String> bridgeWords = new ArrayList<>();
        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
            return bridgeWords;  // 如果任一单词不在图中，返回空列表
        }

        List<Edge> firstWordEdges = getEdges(word1);
        for (Edge edge : firstWordEdges) {
            String possibleBridge = edge.target;  // word1 -> possibleBridge
            List<Edge> secondWordEdges = getEdges(possibleBridge);
            for (Edge secondEdge : secondWordEdges) {
                if (secondEdge.target.equals(word2)) {
                    bridgeWords.add(possibleBridge);  // possibleBridge -> word2
                }
            }
        }

        return bridgeWords;
    }

    public String randomWalk(){
        Random random = new Random();
        int index = random.nextInt(adjacencyList.size());
        List<String> keys = new ArrayList<>(adjacencyList.keySet());
        String startNode = keys.get(index);   //随机获取初始节点
        String currentNode = startNode;
        Set<String> visitedNode = new HashSet<>();
        Set<String> visiteEdge = new HashSet<>();
        StringBuilder randomPath = new StringBuilder();

        while (!visitedNode.contains(currentNode)) {
            randomPath.append(currentNode).append(" ");
            System.out.print(currentNode + "->");
            visitedNode.add(currentNode);
            List<Edge> targetNode = getEdges(currentNode);
            if (targetNode.isEmpty()) {
                break;   //没有下一个节点
            }

            List<Edge> unVisitedTargets = new ArrayList<>();
            for (Edge target : targetNode) {
                String edge = currentNode + "->" + target.target;
                if (!visiteEdge.contains(edge)) {
                    unVisitedTargets.add(target);
                }
            }

            if (unVisitedTargets.isEmpty()) {
                break;   //当前节点没有剩余邻边，终止
            }

            int randomIndex = new Random().nextInt(targetNode.size());
            Edge nextNode = unVisitedTargets.get(randomIndex);
            String edge = currentNode + "->" + nextNode.target;
            visiteEdge.add(edge);
            currentNode = nextNode.target;
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("End!!!");
        return randomPath.toString();
    }

}




