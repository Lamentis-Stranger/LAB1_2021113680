package graph;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class GraphVisualizer {
    public static void ShowGraphDot(Graph graph, String outputFile) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(outputFile)) {
            out.println("digraph G {");
            out.println("    rankdir=LR;");  // 指定图的方向为从左到右
            out.println("    node [shape=circle];");  // 设置节点形状为圆形

            Set<String> vertices = graph.getVertices(); // 获取所有顶点
            for (String vertex : vertices) {
                List<Edge> edges = graph.getEdges(vertex); // 获取每个顶点的所有边
                for (Edge edge : edges) {
                    out.println("    " + vertex + " -> " + edge.target + " [label=" + edge.weight + "];");
                }
            }

            out.println("}");
        }
    }

    public static void renderGraph(String dotFile, String outputFile) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", dotFile, "-o", outputFile);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Graphviz did not process correctly.");
        }
    }
}
