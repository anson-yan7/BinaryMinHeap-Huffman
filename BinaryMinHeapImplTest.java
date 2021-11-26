import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BinaryMinHeapImplTest {
    private BinaryMinHeap<Integer, Character> bin;

    @Before
    public void setup() {
        bin = new BinaryMinHeapImpl();
    }

    @Test
    public void binTest2() {
        bin.add(3, 'a');
        bin.add(3, 'b');
        bin.add(6, 'c');
        assertTrue(bin.containsValue('c'));
        assertTrue(bin.containsValue('b'));
        assertTrue(bin.containsValue('a'));
    }

    @Test
    public void binTest1() {
        bin.add(3, 'a');
        bin.add(3, 'b');
        bin.add(6, 'c');
        bin.decreaseKey('c', 1);
        BinaryMinHeap.Entry<Integer, Character> en = bin.extractMin();
        assertEquals(en.value.charValue(), 'c');
        assertEquals(en.key.intValue(), 1);
        bin.extractMin();
        bin.extractMin();
        assertTrue(bin.isEmpty());
    }

    @Test
    public void binTest3() {
        bin.add(3, 'a');
        bin.add(4, 'b');
        bin.add(6, 'c');
        bin.add(7, 'd');
        bin.add(2, 'e');
        bin.add(8, 'f');
        bin.extractMin();
        bin.extractMin();
        bin.extractMin();
        bin.extractMin();
        bin.extractMin();
        BinaryMinHeap.Entry<Integer, Character> en = bin.peek();
        assertEquals(en.value.charValue(), 'f');
        assertEquals(en.key.intValue(), 8);
        assertEquals(bin.size(),1);
    }
}
