package java8;

import com.sandwich.koan.Koan;

import java.util.*;
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
    public void max() {
        String longest = places.stream()
                .max(Comparator.comparing(cityName -> cityName.length()))
                .get();
        assertEquals(longest, __);
    }

    @Koan
    public void min() {
        String shortest = places.stream()
                .min(Comparator.comparing(cityName -> cityName.length()))
                .get();
        assertEquals(shortest, __);
    }

    @Koan
    public void reduce() {
        String join = places.stream()
                .reduce("", String::concat);
        assertEquals(join, __);
    }

    @Koan
    public void reduceWithoutStarterReturnsOptional() {
        Optional<String> join = places.stream()
                .reduce(String::concat);
        assertEquals(join.get(), __);
    }

    @Koan
    public void join() {
        String join = places.stream()
                .reduce((accumulated, cityName) -> accumulated + "\", \"" + cityName)
                .get();
        assertEquals(join, __);
    }

    @Koan
    public void stringJoin() {
        String join = places.stream()
                .collect(Collectors.joining("\", \""));
        assertEquals(join, __);
    }

    @Koan
    public void mapReduce() {
        OptionalDouble averageLengthOptional = places.stream()
                .mapToInt(String::length)
                .average();
        double averageLength = Math.round(averageLengthOptional.getAsDouble());
        assertEquals(averageLength, __);
    }

    @Koan
    public void parallelMapReduce() {
        int lengthSum = places.parallelStream()
                .mapToInt(String::length)
                .sum();
        assertEquals(lengthSum, __);
    }

    @Koan
    public void streamFromAnArray() {
        int[] ints = {4,5,6};
        long numberOfElements = Arrays.stream(ints).count();
        assertEquals(numberOfElements, __);
    }

    @Koan
    public void allMatch() {
        boolean allNonEmpty = places.stream().allMatch(cityName -> ! cityName.isEmpty());
        assertEquals(allNonEmpty, __);
    }

    @Koan
    public void anyMatch() {
        boolean anyStartsWithX = places.stream().anyMatch(cityName -> cityName.startsWith("X"));
        assertEquals(anyStartsWithX, __);
    }

    @Koan
    public void streamOf() {
        long count = Stream.of("foo", "bar").count();
        assertEquals(count, __);
    }

    @Koan
    public void distinct() {
        long count = Stream.of("foo", "bar", "bar").distinct().count();
        assertEquals(count, __);
    }

    @Koan
    public void findFirst() {
        String first = places.stream().findFirst().get();
        assertEquals(first, __);
    }

    @Koan
    public void flatMap() {
        List<List<String>> listOfLists = Arrays.asList(Arrays.asList("foo", "bar"), Arrays.asList("one", "two"));
        assertEquals(listOfLists.stream().findFirst().get(), __);
        Stream streamOfStrings = listOfLists.stream().flatMap(list -> list.stream());
        assertEquals(streamOfStrings.findFirst().get(), __);
    }

    List<String> listOfStrings = Arrays.asList("This is the first string.",
            "This line follows the first line.");

    @Koan
    public void wordCountUsingFlatMap() {
        long wordCount = listOfStrings.stream()
                // use flatMap method here to count words in listOfStrings
                .count();
        assertEquals(wordCount, 11L);
    }

    @Koan
    public void limitSkip() {
        int lengthSum_Limit_3_Skip_1 = places.stream()
                .mapToInt(String::length)
                .limit(3)
                .skip(1)
                .sum();
        assertEquals(lengthSum_Limit_3_Skip_1, __);
    }

    @Koan
    public void sorted() {
        String second = places.stream().sorted().skip(1).findFirst().get();
        assertEquals(second, __);
    }

    @Koan
    public void sortedReversed() {
        String first = places.stream().sorted(Comparator.reverseOrder()).findFirst().get();
        assertEquals(first, __);
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
    public void sumRange() {
        int sum = IntStream.range(1, 4).sum();
        assertEquals(sum, __);
    }

    @Koan
    public void rangeToList() {
        List<Integer> range = IntStream.range(1, 4)
                .boxed()
                .collect(Collectors.toList());
        assertEquals(range, __);
    }
}
