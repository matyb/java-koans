package intermediate;

import com.sandwich.koan.Koan;

import java.util.*;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutMaps {

    @Koan
    public void createAMap() {
        Map<String, Integer> animalNumbers = new HashMap<>();
        animalNumbers.put("Dogs", 12);
        animalNumbers.put("Monkeys", 7);
        assertEquals(animalNumbers.get("Dogs"), __);
        assertEquals(animalNumbers.get("Monkeys"), __);
    }

    @Koan
    public void containsKeyValue() {
        Map<String, Integer> animalNumbers = new HashMap<>();
        animalNumbers.put("Dogs", 12);
        animalNumbers.put("Monkeys", 7);
        assertEquals(animalNumbers.containsKey("Dogs"), __);
        assertEquals(animalNumbers.containsKey("Birds"), __);
        assertEquals(animalNumbers.containsValue(583), __);
        assertEquals(animalNumbers.containsValue(12), __);
    }

    @Koan
    public void putAgain() {
        Map<String, Integer> animalNumbers = new HashMap<>();
        animalNumbers.put("Dogs", 12);
        assertEquals(animalNumbers.get("Dogs"), __);
        animalNumbers.put("Dogs", 25);
        assertEquals(animalNumbers.get("Dogs"), __);
    }

    @Koan
    public void putIfAbsent() {
        Map<String, Integer> animalNumbers = new HashMap<>();
        animalNumbers.putIfAbsent("Dogs", 12);
        assertEquals(animalNumbers.get("Dogs"), __);
        animalNumbers.putIfAbsent("Dogs", 25);
        assertEquals(animalNumbers.get("Dogs"), __);
    }

    @Koan
    public void replace() {
        Map<String, Integer> animalNumbers = new HashMap<>();
        animalNumbers.put("Dogs", 12);
        assertEquals(animalNumbers.get("Dogs"), __);
        animalNumbers.replace("Dogs", 25);
        assertEquals(animalNumbers.get("Dogs"), __);
        animalNumbers.replace("Monkeys", 2);
        assertEquals(animalNumbers.containsKey("Monkeys"), __);
    }

    @Koan
    public void keySet() {
        Map<String, Integer> animalNumbers = new HashMap<>();
        animalNumbers.put("Dogs", 12);
        animalNumbers.put("Monkeys", 7);
        Set<String> animals = animalNumbers.keySet();
        assertEquals(animals.contains("Dogs"), __);
        assertEquals(animals.contains("Monkeys"), __);
        assertEquals(animals.contains("Spiders"), __);
    }

    @Koan
    public void values() {
        Map<String, Integer> animalNumbers = new HashMap<>();
        animalNumbers.put("Dogs", 12);
        animalNumbers.put("Monkeys", 7);
        Collection<Integer> values = animalNumbers.values();
        assertEquals(values.contains("Dogs"), __);
        assertEquals(values.contains(12), __);
        assertEquals(values.contains(2121), __);
    }
}
