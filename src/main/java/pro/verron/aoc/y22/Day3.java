package pro.verron.aoc.y22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;

public class Day3 {
    private static final List<String> items = str2list("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    public static void main(String[] args) throws IOException {
        Path source = Path.of("input","y22", "day03-input.txt");
        out.println(ex1(Files.lines(source)));
        out.println(ex2(Files.lines(source)));
    }

    private static String ex1(Stream<String> content) {
        return content
                .map(Day3::str2list)
                .map(bag -> Stream
                        .of(bag.subList(0, bag.size() / 2), bag.subList(bag.size() / 2, bag.size()))
                        .reduce(items, Day3::intersect))
                .map(list -> list.stream().findAny().orElseThrow())
                .map(Day3::getPriority)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No elfs found");
    }

    private static String ex2(Stream<String> content) {
        AtomicInteger i = new AtomicInteger();
        return content
                .map(Day3::str2list)
                .collect(groupingBy(line -> i.getAndIncrement() / 3))
                .values()
                .stream()
                .map(strings -> strings.stream().reduce(items, Day3::intersect))
                .map(l -> l.stream().findAny().orElseThrow())
                .map(Day3::getPriority)
                .reduce(Integer::sum)
                .map(String::valueOf)
                .orElse("No elfs found");
    }

    private static <T> List<T> intersect(List<T> accumulator, List<T> current) {
        return accumulator.stream().filter(current::contains).toList();
    }

    private static int getPriority(String item) {
        return items.indexOf(item) + 1;
    }

    private static List<String> str2list(String str) {
        return stream(str.split("")).toList();
    }
}
