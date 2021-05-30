import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import javafx.util.Pair;
import java.util.Collections;
import java.lang.Math;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class p11 {

    private static List<Integer> xdim = Arrays.asList(-1, -1, -1,  0, 1, 0, 1, 1);
    private static List<Integer> ydim = Arrays.asList(-1,  0,  1, -1, -1, 1, 0, 1);

    public static boolean isValid(int xdim, int ydim, int row, int col, List<List<Character>> seats) {
        int x = row + xdim;
        int y = col + ydim;
        
        if (x < 0 || y < 0) {
            return false;
        }
        if (x > seats.size() - 1 || y > seats.get(x).size() - 1) {
            return false;
        }

        return true;
    }

    public static Character applyRule(int row, int col, List<List<Character>> seats) {
        Character seat = seats.get(row).get(col);
        int covidNeighbours = 0;
        for (int n = 0; n < xdim.size(); n++) {
            if (isValid(xdim.get(n), ydim.get(n), row, col, seats)) {
                int x = row + xdim.get(n);
                int y = col + ydim.get(n);
                if (seats.get(x).get(y) == '#') {
                    covidNeighbours += 1;
                }
            }
        }

        if (seat == 'L' && covidNeighbours == 0) {
            return '#';
        } else if (seat == '#' && covidNeighbours >= 4) {
            return 'L';
        }
        return seat; // No change
    }

    public static Character applySpecialRule(int row, int col, List<List<Character>> seats, 
            Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> adjList) {
        List<Pair<Integer, Integer>> neighbours = adjList.get(new Pair(row, col));
        Character seat = seats.get(row).get(col);
        int covidNeighbours = 0;
        for (int n = 0; n < xdim.size(); n++) {
            Pair<Integer, Integer> neigh = neighbours.get(n);
            if (neigh != null) {
                if (seats.get(neigh.getKey()).get(neigh.getValue()) == '#') {
                    covidNeighbours += 1;
                }
            }
        }

        if (seat == 'L' && covidNeighbours == 0) {
            return '#';
        } else if (seat == '#' && covidNeighbours >= 5) {
            return 'L';
        }
        return seat; // No change
    }

    public static int stabiliseSeating(List<List<Character>> seats, Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> adjList) {
        int rounds = 0;
        while (true) {
            int changes = 0;
            List<List<Character>> updatedSeats = new ArrayList<>();
            for (int row = 0; row < seats.size(); row++) {
                List<Character> rowList = new ArrayList<>();
                for (int col = 0; col < seats.get(0).size(); col++) {
                    Character change;
                    if (adjList == null) {
                        change = applyRule(row, col, seats);
                    } else {
                        change = applySpecialRule(row, col, seats, adjList);
                    }
                    if (change != seats.get(row).get(col)) {
                        changes += 1;
                    }
                    rowList.add(change);
                }
                updatedSeats.add(rowList);
            }

            for (int row = 0; row < seats.size(); row++) {
                seats.set(row, updatedSeats.get(row));
            }

            rounds += 1;
            if (changes == 0) {
                break;
            }
        }

        return rounds - 1;
    }

    public static Pair<Integer, Integer> getNeighbourCoordsInDirection(int xdim, int ydim, int row, int col, List<List<Character>> seats) {
        int x = row + xdim;
        int y = col;
        while (isValid(xdim, ydim, row, col, seats)) {
            row += xdim;
            col += ydim;
            if (seats.get(row).get(col) != '.') {
                return new Pair(row, col);
            }
        }

        return null;
    }

    public static Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> computeAdjList(List<List<Character>> seats) {
        Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> adjList = new HashMap<>();

        for (int row = 0; row < seats.size(); row++) {
            for (int col = 0; col < seats.get(0).size(); col++) {
                List<Pair<Integer, Integer>> neighbours = new ArrayList<>();
                for (int idx = 0; idx < xdim.size(); idx++) {
                    int x = xdim.get(idx);
                    int y = ydim.get(idx);
                    Pair<Integer, Integer> coords = getNeighbourCoordsInDirection(x, y, row, col, seats);
                    neighbours.add(coords);
                }
                adjList.put(new Pair(row, col), neighbours);
            }
        }
        return adjList;
    }
    
    public static void main(String[] args) {
        List<List<Character>> in = new ArrayList<>();
        try {
            File myObj = new File("p11_in.txt");
            Scanner scanner = new Scanner(myObj);
            while (scanner.hasNextLine()) {
                in.add(scanner.nextLine().chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }

        boolean isFirstSolution = false;
        if (isFirstSolution) {
            System.out.println(stabiliseSeating(in, null));
            
        } else {
            Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> adjList = computeAdjList(in);
            System.out.println(stabiliseSeating(in, adjList));
        }

        int occupied = 0;
        for (int row = 0; row < in.size(); row++) {
            for (int col = 0; col < in.get(0).size(); col++) {
                if (in.get(row).get(col) == '#') {
                    occupied += 1;
                }
            }
        }
        System.out.println(occupied);
    }
}
