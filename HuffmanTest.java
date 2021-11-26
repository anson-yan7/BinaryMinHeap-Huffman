import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HuffmanTest {
    private Huffman huff;
    private Huffman huffMap;

    @Before
    public void setup() {
        huff = new Huffman("abbccccccdda");
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        map.put('a', 4);
        map.put('b', 2);
        huffMap = new Huffman(map);
    }

    @Test
    public void huffmanTestCompress() {
        assertEquals(huff.compress("ccd"), "0010");
        assertEquals(huffMap.compress("ab"), "10");
        assertTrue(huff.compressionRatio() < 0.1);
    }
    @Test
    public void huffmanTestDecompress() {
        assertEquals(huff.decompress("0010"), "ccd");
    }

    @Test
    public void huffmanTestEEL() {
        assertTrue(huff.expectedEncodingLength() > 1.8);
    }


}
