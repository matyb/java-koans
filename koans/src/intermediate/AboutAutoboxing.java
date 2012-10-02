package intermediate;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.sandwich.koan.Koan;

public class AboutAutoboxing {

	@Koan
	public void addPrimitivesToCollection() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(0, new Integer(42));
		assertEquals(list.get(0), __);
	}
	
	@Koan
	public void addPrimitivesToCollectionWithAutoBoxing() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(0, 42);
		assertEquals(list.get(0), __);
	}
	
	@Koan
	public void migrateYourExistingCodeToAutoBoxingWithoutFear() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(0, new Integer(42));
		assertEquals(list.get(0), __);

		list.add(1, 84);
		assertEquals(list.get(1), __);
	}
	
	@Koan
	public void allPrimitivesCanBeAutoboxed() {
		List<Double> doubleList = new ArrayList<Double>();
		doubleList.add(0, new Double(42));
		assertEquals(doubleList.get(0), __);

		List<Long> longList = new ArrayList<Long>();
		longList.add(0, new Long(42));
		assertEquals(longList.get(0), __);

		List<Character> characterList = new ArrayList<Character>();
		characterList.add(0, new Character('z'));
		assertEquals(characterList.get(0), __);
	}
	
}
