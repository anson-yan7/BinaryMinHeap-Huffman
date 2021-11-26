
import java.util.HashMap;
import java.util.Map;

/**
 * Implements construction, encoding, and decoding logic of the Huffman coding algorithm. Characters
 * not in the given seed or alphabet should not be compressible, and attempts to use those
 * characters should result in the throwing of an {@link IllegalArgumentException} if used in {@link
 * #compress(String)}.
 */
public class Huffman {

    /**
     * Constructs a {@code Huffman} instance from a seed string, from which to deduce the alphabet
     * and corresponding frequencies.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param seed the String from which to build the encoding
     * @throws IllegalArgumentException seed is null, seed is empty, or resulting alphabet only has
     *                                  1 character
     */
    public class Node<V> {
        private Node<V> left;
        private Node<V> right;
        private V value;

        public Node(V value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
        
        public Node(Node<V> left, Node<V> right) {
            this.left = left;
            this.right = right;
            this.value = null;
        }
        public Boolean isLeaf() {
            return this.value != null;
        }
        public Node<V> getLeft() {
            return this.left;
        }
        public Node<V> getRight() {
            return this.right;
        }
    }
    
    private Node<Character> root;
    private HashMap<Character, String> str = new HashMap<>();
    private HashMap<Character, Integer> alpha;
    private double inputBits = 0;
    private double outputBits = 0;

    public Huffman(String seed) {
        if (seed == null || seed.isEmpty()) {
            throw new IllegalArgumentException();
        }
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < seed.length(); i++) {
            if (map.containsKey(seed.charAt(i))) {
                map.put(seed.charAt(i), map.get(seed.charAt(i)) + 1);
            } else {
                map.put(seed.charAt(i), 1);
            }
        }
        if (map.size() == 1) {
            throw new IllegalArgumentException();
        }
        alpha = map;
        huffmanConstructor(makeHeap(map));
        makeMap(root, "");
    }

    /**
     * Constructs a {@code Huffman} instance from a frequency map of the input alphabet.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param alphabet a frequency map for characters in the alphabet
     * @throws IllegalArgumentException if the alphabet is null, empty, has fewer than 2 characters,
     *                                  or has any non-positive frequencies
     */
    public Huffman(Map<Character, Integer> alphabet) {
        if (alphabet == null || alphabet.isEmpty() || alphabet.size() < 2) {
            throw new IllegalArgumentException();
        }
        for (Character c : alphabet.keySet()) {
            if (alphabet.get(c).intValue() < 0) {
                throw new IllegalArgumentException();
            }
        }
        alpha = (HashMap<Character, Integer>) alphabet;
        huffmanConstructor(makeHeap(alphabet));
        makeMap(root, "");
    }
    
    private void makeMap(Node<Character> top, String v) {
        if (top.isLeaf()) {
            str.put(top.value, v);
        } else {
            makeMap(top.right, v + '1');
            makeMap(top.left, v + '0');
        }
    }
    private BinaryMinHeap<Integer, Node<Character>> makeHeap(Map<Character, Integer> alphabet) {
        BinaryMinHeap<Integer, Node<Character>> output = new BinaryMinHeapImpl<>();
        alphabet.forEach((k,v) -> output.add(v, new Node<>(k)));
        return output;
    }
    private void huffmanConstructor(BinaryMinHeap<Integer, Node<Character>> heap) {
        if (heap.size() != 1) {
            BinaryMinHeap.Entry<Integer, Node<Character>> first = heap.extractMin();
            BinaryMinHeap.Entry<Integer, Node<Character>> second = heap.extractMin();
            heap.add(first.key + second.key, new Node<>(first.value, second.value));
            huffmanConstructor(heap);
        } else {
            BinaryMinHeap.Entry<Integer, Node<Character>> first = heap.extractMin();
            this.root = first.value;
        }
    }

    /**
     * Compresses the input string.
     *
     * @param input the string to compress, can be the empty string
     * @return a string of ones and zeroes, representing the binary encoding of the inputted String.
     * @throws IllegalArgumentException if the input is null or if the input contains characters
     *                                  that are not compressible
     */
    public String compress(String input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        inputBits = inputBits + (double) input.length();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i ++) {
            if (str.get(input.charAt(i)) == null) {
                throw new IllegalArgumentException();
            }
            output.append(str.get(input.charAt(i)));
            outputBits = outputBits + (double) str.get(input.charAt(i)).length();
        }
        return output.toString();
    }

    /**
     * Decompresses the input string.
     *
     * @param input the String of binary digits to decompress, given that it was generated by a
     *              matching instance of the same compression strategy
     * @return the decoded version of the compressed input string
     * @throws IllegalArgumentException if the input is null, or if the input contains characters
     *                                  that are NOT 0 or 1, or input contains a sequence of bits
     *                                  that is not decodable
     */
    public String decompress(String input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        StringBuilder output = new StringBuilder();
        Node<Character> curr = root;
        int i = 0;
        while (i < input.length()) {
            if (curr.value == null) {
                if (input.charAt(i) == '1') {
                    curr = curr.right;
                } else if (input.charAt(i) == '0') {
                    curr = curr.left;
                } else {
                    throw new IllegalArgumentException();
                }
                i++;
            }
            if (curr.value != null) {
                output.append(curr.value);
                curr = root;
            }
        }
        return output.toString();
    }

    /**
     * Computes the compression ratio so far. This is the length of all output strings from {@link
     * #compress(String)} divided by the length of all input strings to {@link #compress(String)}.
     * Assume that each char in the input string is a 16 bit int.
     *
     * @return the ratio of the total output length to the total input length in bits
     * @throws IllegalStateException if no calls have been made to {@link #compress(String)} before
     *                               calling this method
     */
    public double compressionRatio() {
        if (inputBits == 0 || outputBits == 0) {
            throw new IllegalArgumentException();
        } else {
            return outputBits / (inputBits * 16.0);
        }
    }

    /**
     * Computes the expected encoding length of an arbitrary character in the alphabet based on the
     * objective function of the compression.
     * <p>
     * The expected encoding length is simply the sum of the length of the encoding of each
     * character multiplied by the probability that character occurs.
     *
     * @return the expected encoding length of an arbitrary character in the alphabet
     */
    public double expectedEncodingLength() {
        double output = 0;
        double sum = 0;
        for (Character c : alpha.keySet()) {
            sum = sum + (double) alpha.get(c);
        }
        for (Character c : alpha.keySet()) {
            output = output + ((double) str.get(c).length() * (double) alpha.get(c) / sum);
        }
        return output;
    }
    
}
