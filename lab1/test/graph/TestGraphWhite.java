package graph;

import org.junit.jupiter.api.Test;
import preprocess.TextFileProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestGraphWhite {

    @Test
    public void testcase1() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        List<String> actual = graph.dijkstraShortestPath("new", "happy");
        List<String> expected = new ArrayList<String>();
        assertEquals(expected, actual);
    }

    @Test
    public void testcase2() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        List<String> actual = graph.dijkstraShortestPath("new", "worlds");
        List<String> expected = Arrays.asList("new", "worlds");
        assertEquals(expected, actual);
    }

    @Test
    public void testcase3() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        List<String> actual = graph.dijkstraShortestPath("to", "new");
        List<String> expected = Arrays.asList("to", "strange", "new");
        assertEquals(expected, actual);
    }

    @Test
    public void testcase4() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        List<String> actual = graph.dijkstraShortestPath("to", "to");
        List<String> expected = Arrays.asList("to");
        assertEquals(expected, actual);
    }

    @Test
    public void testcase5() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        List<String> actual = graph.dijkstraShortestPath("new", "and");
        List<String> expected = Arrays.asList("new", "life", "and");
        assertEquals(expected, actual);
    }

    @Test
    public void testcase6() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        List<String> actual = graph.dijkstraShortestPath("to", "strange");
        List<String> expected = Arrays.asList("to","strange");
        assertEquals(expected, actual);
    }

    @Test
    public void testcase7() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        List<String> expected = graph.dijkstraShortestPath("and", "strange");
        List<String> actual = Arrays.asList("and", "new", "worlds", "to", "strange");
        assertEquals(expected, actual);
    }



}