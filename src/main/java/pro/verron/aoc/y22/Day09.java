package pro.verron.aoc.y22;

import pro.verron.aoc.AdventOfCode;
import pro.verron.aoc.utils.board.Direction;
import pro.verron.aoc.utils.board.Vector;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.IntStream.range;
import static pro.verron.aoc.utils.assertions.Assertions.assertEquals;
import static pro.verron.aoc.utils.assertions.Assertions.assertThat;

public class Day09 {
    public static void main(String[] args) throws IOException {
        AdventOfCode aoc = new AdventOfCode(22, 9);
        assertEquals(ropeBridge(aoc.testStream(), 2),13);
        assertEquals(ropeBridge(aoc.trueStream(), 2),6098);
        assertEquals(ropeBridge(aoc.testStream(2), 10),36);
        assertEquals(ropeBridge(aoc.trueStream(), 10),2597);
    }

    private static int ropeBridge(Stream<String> input, int nbKnots) {
        assertThat(nbKnots >= 2, "The rope cannot have less than two ends and zero intermediate knots");
        var pattern = Pattern.compile("([RULD]) (\\d+)");
        var startPosition = new Vector(0, 0);
        var vectors = range(0, nbKnots)
                .mapToObj(i -> startPosition)
                .collect(toCollection(LinkedList::new));
        var places = new HashSet<>(Set.of(startPosition));
        input.map(pattern::matcher)
                .filter(Matcher::matches)
                .flatMap(Day09::parseInputLines)
                .map(Day09::parseDirection)
                .forEachOrdered(d -> {
                    var head = vectors.getFirst();
                    vectors.set(0, d.from(head));
                    for (int i = 1; i < nbKnots; i++) {
                        var prev = vectors.get(i - 1);
                        var curr = vectors.get(i);
                        var delta = prev.minus(curr);
                        if(!delta.isNormalized()) {
                            vectors.set(i, curr.add(delta.normalized()));
                        }
                    }
                    var tail = vectors.getLast();
                    places.add(tail);
                });
        return places.size();
    }

    private static Stream<String> parseInputLines(Matcher m) {
        var directionCharacter = m.group(1);
        var directionValue = parseInt(m.group(2));
        return directionCharacter
                .repeat(directionValue)
                .chars()
                .mapToObj(Character::toString);
    }

    private static Direction parseDirection(String s) {
        return switch (s) {
            case "R" -> Direction.RIGHT;
            case "U" -> Direction.UP;
            case "L" -> Direction.LEFT;
            case "D" -> Direction.DOWN;
            default -> throw new RuntimeException();
        };
    }
}