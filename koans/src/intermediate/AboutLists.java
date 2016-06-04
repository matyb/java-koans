package intermediate;

import com.sandwich.koan.Koan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutLists {

    @Koan
    public void createAList() {
        List<String> animals = new ArrayList<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");
        assertEquals(animals.get(0), __);
        assertEquals(animals.get(1), __);
        assertEquals(animals.get(2), __);
    }

    @Koan
    public void addElementAtIndex() {
        List<String> animals = new ArrayList<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");
        assertEquals(animals.get(1), __);
        animals.add(1, "Elephant");
        assertEquals(animals.get(1), __);
        assertEquals(animals.get(2), __);
    }

    @Koan
    public void setElementAtIndex() {
        List<String> animals = new ArrayList<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");
        assertEquals(animals.get(1), __);
        animals.set(1, "Elephant");
        assertEquals(animals.get(1), __);
    }

    @Koan
    public void addAll() {
        List<String> animals = new ArrayList<>();
        animals.add("Snake");
        animals.addAll(Arrays.asList("Bird", "Cat"));
        assertEquals(animals.get(0), __);
        assertEquals(animals.get(1), __);
        assertEquals(animals.get(2), __);
    }

    @Koan
    public void contains() {
        List<String> animals = new ArrayList<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");
        assertEquals(animals.contains("Cat"), __);
        assertEquals(animals.contains("Elephant"), __);
        animals.add("Elephant");
        assertEquals(animals.contains("Elephant"), __);
    }

    @Koan
    public void isEmpty() {
        List<String> animals = new ArrayList<>();
        assertEquals(animals.isEmpty(), __);
        animals.add("Monkey");
        assertEquals(animals.isEmpty(), __);
    }

    @Koan
    public void clear() {
        List<String> animals = new ArrayList<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");
        assertEquals(animals.isEmpty(), __);
        animals.clear();
        assertEquals(animals.isEmpty(), __);
    }

    @Koan
    public void size() {
        List<String> animals = new ArrayList<>();
        assertEquals(animals.size(), __);
        animals.add("Snake");
        assertEquals(animals.size(), __);
        animals.addAll(Arrays.asList("Bird", "Cat"));
        assertEquals(animals.size(), __);
    }

    @Koan
    public void remove() {
        List<String> animals = new ArrayList<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");
        assertEquals(animals.remove("Dog"), __);
        assertEquals(animals.get(0), __);
        assertEquals(animals.get(1), __);
    }

    @Koan
    public void removeAtIndex() {
        List<String> animals = new ArrayList<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");
        animals.remove(1);
        assertEquals(animals.get(1), __);
        animals.remove(0);
        assertEquals(animals.get(0), __);
    }
}
