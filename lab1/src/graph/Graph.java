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

    public List<String> dijkstraShortestPath(String start, String end) {
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return new ArrayList<>(); // 如果起点或终点不存在于图中，则直接返回空列表
        }

        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<Vertex> pq = new PriorityQueue<>(Comparator.comparingInt(v -> v.distance));
        Map<String, String> predecessors = new HashMap<>();
        Set<String> visited = new HashSet<>();

        // 初始化距离和前驱节点
        adjacencyList.keySet().forEach(vertex -> {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, null);
        });

        // 初始化起点
        distances.put(start, 0);
        pq.add(new Vertex(start, 0));

        while (!pq.isEmpty()) {
            Vertex current = pq.poll();
            if (visited.contains(current.name)) continue; // 如果当前顶点已访问，跳过
            visited.add(current.name);

            if (current.name.equals(end)) {
                break; // 如果当前顶点是终点，结束循环
            }

            // 遍历所有邻接顶点
            for (Edge edge : adjacencyList.getOrDefault(current.name, Collections.emptyList())) {
                if (visited.contains(edge.target)) continue;
                int currentDistance = distances.getOrDefault(current.name, Integer.MAX_VALUE);
                int newDist = (currentDistance == Integer.MAX_VALUE) ? Integer.MAX_VALUE : currentDistance + edge.weight;
                if (newDist < distances.getOrDefault(edge.target, Integer.MAX_VALUE)) {
                    distances.put(edge.target, newDist);
                    predecessors.put(edge.target, current.name);
                    pq.add(new Vertex(edge.target, newDist)); // 更新优先队列
                }
            }
        }

        // 从终点回溯至起点以重建路径
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = predecessors.get(at)) {
            path.add(at);
            if (at.equals(start)) break;
        }
        if (!path.isEmpty() && path.get(path.size() - 1).equals(start)) {
            Collections.reverse(path);
            return path; // 返回有效路径
        }

        return new ArrayList<>(); // 如果终点不可达或没有有效路径
    }

    public Map<String, List<String>> dijkstraAllPaths(String start) {
        if (!adjacencyList.containsKey(start)) {
            return new HashMap<>(); // 如果起点不存在于图中，则直接返回空映射
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        PriorityQueue<Vertex> pq = new PriorityQueue<>(Comparator.comparingInt(v -> v.distance));
        Set<String> visited = new HashSet<>();
        Map<String, List<String>> allPaths = new HashMap<>();

        // 初始化距离和前驱节点
        adjacencyList.keySet().forEach(vertex -> {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, null);
            pq.add(new Vertex(vertex, Integer.MAX_VALUE)); // 将所有顶点以无限大距离加入队列
        });

        // 设置起点的初始距离
        distances.put(start, 0);
        pq.add(new Vertex(start, 0)); // 更新起点在优先队列中的距离

        while (!pq.isEmpty()) {
            Vertex current = pq.poll();
            if (visited.contains(current.name)) continue; // 如果当前顶点已访问，跳过
            visited.add(current.name);

            for (Edge edge : adjacencyList.getOrDefault(current.name, Collections.emptyList())) {
                if (visited.contains(edge.target)) continue;
                int newDist = distances.get(current.name) + edge.weight;
                if (newDist < distances.getOrDefault(edge.target, Integer.MAX_VALUE)) {
                    distances.put(edge.target, newDist);
                    predecessors.put(edge.target, current.name);
                    pq.add(new Vertex(edge.target, newDist)); // 用新的距离更新优先队列
                }
            }
        }

        // 重建从起点到所有其他节点的路径
        for (String target : adjacencyList.keySet()) {
            LinkedList<String> path = new LinkedList<>();
            for (String at = target; at != null; at = predecessors.get(at)) {
                path.addFirst(at);
            }
            if (!path.isEmpty() && path.getFirst().equals(start)) {
                allPaths.put(target, path);
            }
        }

        return allPaths;
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




