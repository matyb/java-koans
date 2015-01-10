package java8;

import com.sandwich.koan.Koan;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutStreams {

    String str = "";

    List<String> places = Arrays.asList("Belgrade", "Zagreb", "Sarajevo", "Skopje", "Ljubljana", "Podgorica");

    @Koan
    public void simpleCount() {
        long count = places.stream().count();
        assertEquals(count, __);
    }

    @Koan
    public void filteredCount() {
        long count = places.stream()
                .filter(s -> s.startsWith("S"))
                .count();
        assertEquals(count, __);
    }

    @Koan
    public void lazyEvaluation() {
        Stream stream = places.stream()
                .filter(s -> {
                    str = "hello";
                    return s.startsWith("S");
                });
        assertEquals(str, __);
    }

    @Koan
    public void sumRange(){
        int sum = IntStream.range(1, 4).sum();
        assertEquals(sum, __);
    }

    @Koan
    public void rangeToList(){
        List<Integer> range = IntStream.range(1,4)
                .boxed()
                .collect(Collectors.toList());
        assertEquals(range, __);
    }
}
