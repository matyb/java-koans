package intermediate;

import com.sandwich.koan.Koan;

import java.util.ArrayList;
import java.util.List;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutAutoboxing {

    @Koan
    public void addPrimitivesToCollection() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(0, new Integer(42));
        assertEquals(list.get(0), 42);
    }

    @Koan
    public void addPrimitivesToCollectionWithAutoBoxing() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(0, 42);
        assertEquals(list.get(0), 42);
    }

    @Koan
    public void migrateYourExistingCodeToAutoBoxingWithoutFear() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(0, new Integer(42));
        assertEquals(list.get(0), 42);

        list.add(1, 84);
        assertEquals(list.get(1), 84);
    }

    @Koan
    public void allPrimitivesCanBeAutoboxed() {
        List<Double> doubleList = new ArrayList<Double>();
        doubleList.add(0, new Double(42));
        assertEquals(doubleList.get(0), 42.0);

        List<Long> longList = new ArrayList<Long>();
        longList.add(0, new Long(42));
        assertEquals(longList.get(0), (long)42);

        List<Character> characterList = new ArrayList<Character>();
        characterList.add(0, new Character('z'));
        assertEquals(characterList.get(0), 'z');
    }

}
