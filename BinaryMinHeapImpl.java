

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @param <V>   {@inheritDoc}
 * @param <Key> {@inheritDoc}
 */
public class BinaryMinHeapImpl<Key extends Comparable<Key>, V> implements BinaryMinHeap<Key, V> {
    /**
     * {@inheritDoc}
     */
    
    private ArrayList<Key> heap;
    private HashMap<Integer, V> map;
    private HashMap<V, Key> vMap;
    
    public BinaryMinHeapImpl() {
        heap = new ArrayList<Key>();
        map = new HashMap<Integer, V>();
        vMap = new HashMap<V, Key>();
    }
    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public boolean isEmpty() {
        return heap.size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(V value) {
        return vMap.containsKey(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Key key, V value) {
        if (key == null || vMap.containsKey(value)) {
            throw new IllegalArgumentException();
        }
        heap.add(key);
        map.put(heap.size() - 1, value);
        vMap.put(value, key);
        minHeapify(heap.size() - 1);
    }
    
    private void minHeapify(int index) {
        if (index == 0) {
            return;
        }
        int parent = (index + 1) / 2 - 1;
        if (heap.get(index).compareTo(heap.get(parent)) < 0) {
            Key temp = heap.get(index);
            heap.remove(index);
            heap.add(index, heap.get(parent));
            heap.remove(parent);
            heap.add(parent, temp);
            V tempV = map.get(index);
            map.put(index, map.get(parent));
            map.put(parent, tempV);
            minHeapify(parent);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseKey(V value, Key newKey) {
        if (!vMap.containsKey(value)) {
            throw new IllegalArgumentException();
        }
        Key k = vMap.get(value);
        int i = heap.indexOf(k);
        if (map.get(i) != value) {
            i = heap.lastIndexOf(k);
        }
        if (newKey.compareTo(k) > 0) {
            throw new IllegalArgumentException();
        } else {
            vMap.put(value, newKey);
            heap.remove(i);
            heap.add(i, newKey);
            minHeapify(i);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<Key, V> peek() {
        if (heap.size() == 0) {
            throw new NoSuchElementException();
        }
        return new Entry<Key, V>(heap.get(0), map.get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<Key, V> extractMin() {
        if (heap.size() == 0) {
            throw new NoSuchElementException();
        }
        if (heap.size() == 1) {
            Entry<Key, V> output = new Entry<>(heap.get(0), map.get(0));
    	    heap.remove(0);
    	    vMap.remove(map.get(0));
    	    map.remove(0);
    	    return output;
    	} else {
    	    Entry<Key, V> output = new Entry<>(heap.get(0), map.get(0));
    	    minHeapifyTop(0);
    	    return output;
    	}
    	
    }
    
    private void minHeapifyTop(int index) {
        int left = 2 * (index + 1) - 1;
        int right = 2 * (index + 1);
        if (heap.size() == right) {
    	    heap.remove(index);
    	    vMap.remove(map.get(index));
    	    map.remove(index);
    	    heap.add(index, heap.get(left - 1));
    	    map.put(index, map.get(left));
    	    heap.remove(left);
    	    map.remove(left);
    	    return;
        }
        if (heap.get(left).compareTo(heap.get(right)) < 0) {
    	    heap.remove(index);
    	    vMap.remove(map.get(index));
    	    heap.add(index, heap.get(left - 1));
            map.put(index, map.get(left));
    	    if (heap.size() <= 2 * (left + 1) - 1) {
                heap.remove(left);
                map.remove(left);
                heap.add(left, heap.get(heap.size() - 1));
                map.put(left, map.get(heap.size() - 1));
                vMap.remove(map.get(heap.size() - 1));
                map.remove(heap.size() - 1);
                heap.remove(heap.size() - 1);
            } else {
                minHeapifyTop(left);
            }
    	} else {
    	    heap.remove(index);
    	    heap.add(index, heap.get(right - 1));
    	    map.put(index, map.get(right));
    	    if (heap.size() <= 2 * (right + 1) - 1) {
                heap.remove(right);
                map.remove(right);
                heap.add(right, heap.get(heap.size() - 1));
                map.put(right, map.get(heap.size() - 1));
                vMap.remove(map.get(heap.size() - 1));
                map.remove(heap.size() - 1);
                heap.remove(heap.size() - 1);
    	    } else {
                minHeapifyTop(right);
    	    }
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> values() {
        return (HashSet<V>) map.values();
    }
}