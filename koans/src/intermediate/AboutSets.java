package intermediate;

import com.sandwich.koan.Koan;

import java.util.*;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutSets {

    @Koan
    public void addElementsOneByOne() {
        Set<String> animals = new HashSet<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");
        animals.add("Cat");
        assertEquals(animals.contains("Cat"), __);
        assertEquals(animals.size(), __);
    }

    @Koan
    public void createASet() {
        Set<String> animals = new HashSet<>();
        animals.addAll(Arrays.asList("Dog", "Cat", "Monkey", "Dog", "Cat"));
        assertEquals(animals.contains("Dog"), __);
        assertEquals(animals.contains("Snake"), __);
        assertEquals(animals.size(), __);
    }

    @Koan
    public void isEmpty() {
        Set<String> animals = new HashSet<>();
        assertEquals(animals.isEmpty(), __);
        animals.add("Monkey");
    }

    @Koan
    public void sizeAndClear() {
        Set<String> animals = new HashSet<>();
        animals.addAll(Arrays.asList("Dog", "Cat"));
        assertEquals(animals.size(), __);
        animals.add("Monkey");
        assertEquals(animals.size(), __);
        animals.add("Dog");
        assertEquals(animals.size(), __);
        assertEquals(animals.remove("Dog"), __);
        assertEquals(animals.size(), __);
        animals.clear();
        assertEquals(animals.size(), __);
    }
}
