package graph;

import org.junit.jupiter.api.Test;
import preprocess.TextFileProcessor;

import static org.junit.jupiter.api.Assertions.*;

class TestGraphBlack {

    @Test
    public void testcase1() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        assertEquals("The bridge words from to to strange are: explore.",
                graph.queryBridgeWords("to","strange"));
    }

    @Test
    public void testcase2() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        assertEquals("No to or + in the graph!",
                graph.queryBridgeWords("to","+"));
    }

    @Test
    public void testcase3() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        assertEquals("No seek or happy in the graph!",
                graph.queryBridgeWords("seek","happy"));
    }

    @Test
    public void testcase4() {
        Graph graph = TextFileProcessor.ProcessTextToGraph("text");
        assertEquals("No to or  in the graph!",
                graph.queryBridgeWords("to",""));
    }
}