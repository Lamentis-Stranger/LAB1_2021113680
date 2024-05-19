package preprocess;

import graph.Graph;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextFileProcessor {
    public static Graph ProcessTextToGraph(String filePath) {
        Graph graph = new Graph();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath))).toLowerCase();
            content = content.replaceAll("[^a-z\\s]", " ").replaceAll("\\s+", " ");
            String[] words = content.split("\\s+");

            for (int i = 0; i < words.length - 1; i++) {
                graph.addEdge(words[i], words[i + 1]);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return graph;
    }
}
