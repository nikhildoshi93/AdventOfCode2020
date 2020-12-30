import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;

public class p3 {

    private static Pair<Integer, Integer> pattern = new Pair<>(3, 1);
    private static List<Pair<Integer, Integer>> patterns = new ArrayList<>();
    
    // Since the pattern repeats, get the modulus of the row to search for the existence of the tree.
    private static boolean checkForTreeAtPos(Pair<Integer, Integer> pos, List<List<Integer>> forest) {
        int idx = pos.getKey() % forest.get(0).size();
        return (forest.get(pos.getValue()).get(idx) == 1);
    }

    // Count the trees after iterating through the given pattern.
    private static int countTreesForPattern(Pair<Integer, Integer> pos, Pair<Integer, Integer> pattern, List<List<Integer>> forest) {
        // If reached the bottom.
        if (pos.getValue() >= forest.size()) {
            return 0;
        }

        Pair<Integer, Integer> updatedPos = new Pair<>(pos.getKey() + pattern.getKey(), pos.getValue() + pattern.getValue());
        int iteratePattern = countTreesForPattern(updatedPos, pattern, forest);
        if (checkForTreeAtPos(pos, forest)) {
            return 1 + iteratePattern;
        } else {
            return iteratePattern;
        }
    }

	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p3_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }
	    
        List<List<Integer>> forest = new ArrayList<>();
        for (String row : in) {
            List<Integer> treeRow = new ArrayList<>();
            for (Character c : row.toCharArray()) {
                if (c == '.') {
                    treeRow.add(0);
                } else {
                    treeRow.add(1);
                }
            }
            forest.add(treeRow);
        }

        System.out.println(countTreesForPattern(new Pair<>(0, 0), pattern, forest));

        patterns.add(new Pair<>(1, 1));
        patterns.add(new Pair<>(3, 1));
        patterns.add(new Pair<>(5, 1));
        patterns.add(new Pair<>(7, 1));
        patterns.add(new Pair<>(1, 2));
        Long productOfTrees = 1L;
        for (Pair<Integer, Integer> newPattern : patterns) {
            productOfTrees *= countTreesForPattern(new Pair<>(0, 0), newPattern, forest);
        }
        System.out.println(productOfTrees);
	}
}
