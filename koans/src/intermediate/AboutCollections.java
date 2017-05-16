package intermediate;

import com.sandwich.koan.Koan;

import java.util.*;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;


public class AboutCollections {

    @Koan
    public void usingAnArrayList() {
        // List = interface
        // The generic syntax and special generic cases will be handled in
        // AboutGenerics. We just use <String> collections here to keep it
        // simple.
        List<String> list = new ArrayList<String>();
        // ArrayList: simple List implementation
        list.add("Chicken");
        list.add("Dog");
        list.add("Chicken");
        assertEquals(list.get(0), "Chicken");
        assertEquals(list.get(1), "Dog");
        assertEquals(list.get(2), "Chicken");
    }

    @Koan
    public void usingAQueue() {
        // Queue = interface
        Queue<String> queue = new PriorityQueue<String>();
        // PriorityQueue: simple queue implementation
        queue.add("Cat");
        queue.add("Dog");
        assertEquals(queue.peek(), "Cat");
        assertEquals(queue.size(), 2);
        assertEquals(queue.poll(), "Cat");
        assertEquals(queue.size(), 1);
        assertEquals(queue.poll(), "Dog");
        assertEquals(queue.isEmpty(), true);
    }

    @Koan
    public void usingABasicSet() {
        Set<String> set = new HashSet<String>();
        set.add("Dog");
        set.add("Cat");
        set.add("Dog");
        assertEquals(set.size(), 2);
        assertEquals(set.contains("Dog"), true);
        assertEquals(set.contains("Cat"), true);
        assertEquals(set.contains("Chicken"), false);
    }

    @Koan
    public void usingABasicMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("first key", "first value");
        map.put("second key", "second value");
        map.put("first key", "other value");
        assertEquals(map.size(), 2);
        assertEquals(map.containsKey("first key"), true);
        assertEquals(map.containsKey("second key"), true);
        assertEquals(map.containsValue("first value"), false);
        assertEquals(map.get("first key"), "other value");
    }

    @Koan
    public void usingBackedArrayList() {
        String[] array = {"a", "b", "c"};
        List<String> list = Arrays.asList(array);
        list.set(0, "x");
        assertEquals(array[0], "x");
        array[0] = "a";
        assertEquals(list.get(0), "a");
        // Just think of it as quantum state teleportation...
    }

    @Koan
    public void usingBackedSubMap() {
        TreeMap<String, String> map = new TreeMap<String, String>();
        map.put("a", "Aha");
        map.put("b", "Boo");
        map.put("c", "Coon");
        map.put("e", "Emu");
        map.put("f", "Fox");
        SortedMap<String, String> backedMap = map.subMap("c", "f");
        assertEquals(backedMap.size(), 2);
        assertEquals(map.size(), 5);
        backedMap.put("d", "Dog");
        assertEquals(backedMap.size(), 3);
        assertEquals(map.size(), 6);
        assertEquals(map.containsKey("d"), true);
        // Again: backed maps are just like those little quantum states
        // that are connected forever...
    }

    @Koan
    public void differenceBetweenOrderedAndSorted() {
        TreeSet<String> sorted = new TreeSet<String>();
        sorted.add("c");
        sorted.add("z");
        sorted.add("a");
        assertEquals(sorted.first(), "a");
        assertEquals(sorted.last(), "z");
        // Look at the different constructors for a TreeSet (or TreeMap)
        // Ponder how you might influence the sort order. Hold that thought
        // until you approach AboutComparison

        LinkedHashSet<String> ordered = new LinkedHashSet<String>();
        ordered.add("c");
        ordered.add("z");
        ordered.add("a");
        StringBuffer sb = new StringBuffer();
        for (String s : ordered) {
            sb.append(s);
        }
        assertEquals(sb.toString(), "cza");
    }
}
