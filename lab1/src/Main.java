
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import graph.Graph;
import graph.GraphVisualizer;
import preprocess.TextFileProcessor;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String filePath = getFilePath(args, reader);

        Graph graph = TextFileProcessor.ProcessTextToGraph(filePath);
        boolean flag = true;
        try {
            while (flag) {
                showMenu();
                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        showDirectedGraph(graph);
                        break;
                    case "2":
                        BridgeWordsQuery(reader, graph);
                        break;
                    case "3":
                        generateNewText(reader, graph);
                        break;
                    case "4":
                        //calculateShortestPath(reader, graph);
                        break;
                    case "5":
                        performRandomWalk(reader, graph);
                        break;
                    case "6":
                        flag = false;
                        System.out.println("Exit Successful");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        System.out.println("hello world!");

    }


    private static String getFilePath(String[] args, BufferedReader reader) throws IOException {
        if (args.length < 1) {
            System.out.println("please input the File path");
            return reader.readLine(); // 从标准输入获取文件路径
        } else {
            return args[0]; // 使用命令行参数提供的文件路径
        }
    }

    private static void showMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Show graph");
        System.out.println("2. Find bridge words");
        System.out.println("3. Generate new text");
        System.out.println("4. Calculate shortest path");
        System.out.println("5. Perform random walk");
        System.out.println("6. Exit");
    }

    private static void showDirectedGraph(Graph graph) throws Exception {
        GraphVisualizer.ShowGraphDot(graph, "outputGraph.dot");
        System.out.println("Graph has saved in outputGraph.dot");
        GraphVisualizer.renderGraph("outputGraph.dot", "outputGraph.png");
        System.out.println("Graph visualized and saved as outputGraph.png");
    }

    private static void BridgeWordsQuery(BufferedReader reader, Graph graph) throws IOException {
        System.out.println("请输入两个单词，以查找桥接词（word1 word2）:");
        String[] words = reader.readLine().toLowerCase().split("\\s+");
        if (words.length >= 2) {
            String word1 = words[0];
            String word2 = words[1];
            String result = graph.queryBridgeWords(word1, word2);
            System.out.println(result);
        }
        else{
            System.out.println("请输入两个有效的单词！");
        }
    }

    private static void generateNewText(BufferedReader reader, Graph graph) throws IOException {
        System.out.println("请输入一行文本，程序将根据桥接词生成新文本:");
        //String inputText = reader.readLine().toLowerCase();
        String inputText = reader.readLine();
        String[] words = inputText.split("\\s+");
        StringBuilder newText = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < words.length-1; i++) {
            newText.append(words[i]).append(" ");
            List<String> bridges = graph.BridgeWordsResult(words[i], words[i+1]);
            if (!bridges.isEmpty()) {
                // 如果存在多个桥接词，随机选择一个插入
                String bridgeWord = bridges.get(random.nextInt(bridges.size()));
                newText.append(bridgeWord).append(" ");
            }
        }
        if (words.length > 0) {
            newText.append(words[words.length - 1]);  // 添加最后一个单词
        }
        System.out.println("生成的新文本:");
        System.out.println(newText.toString());
    }

    private static void performRandomWalk(BufferedReader reader, Graph graph){
        String randomPath = graph.randomWalk();
        //System.out.println(randomPath);test
        try {
            FileWriter writer = new FileWriter("path");
            writer.write(randomPath);
            writer.close();
            System.out.println("\nPath have been saved");
        } catch (IOException e) {
            System.err.println("Error writing path to file: " + e.getMessage());
        }
    }


}
