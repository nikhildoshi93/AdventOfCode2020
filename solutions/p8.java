import java.io.*;  // Import the File classes and exceptions
import java.util.*;
import java.util.stream.*;
import javafx.util.Pair;

public class p8 {
    
    // Get acc value before we loop back to the same point.
    private static int getAccBeforeLoop(int acc, int idx, List<Pair<String, Integer>> instructions, int[] visited) {
        if (idx == instructions.size()) {
            return acc;
        }
        if (visited[idx] == 1) {
            return acc;
        }

        visited[idx] = 1;
        if (instructions.get(idx).getKey().equals("nop")) {
            return getAccBeforeLoop(acc, idx + 1, instructions, visited);
        } else if (instructions.get(idx).getKey().equals("acc")) {
            return getAccBeforeLoop(acc + instructions.get(idx).getValue(), idx + 1, instructions, visited);
        } else if (instructions.get(idx).getKey().equals("jmp")) {
            return getAccBeforeLoop(acc, idx + instructions.get(idx).getValue(), instructions, visited);
        }
        return -1;
    }

    // For each nop/jmp, switch it and check if the program executes.
    private static int fixInfiniteLoop(List<Pair<String, Integer>> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            if (!(instructions.get(i).getKey().equals("acc"))) {
                instructions.set(i, new Pair(
                    (instructions.get(i).getKey().equals("jmp")) ? "nop" : "jmp", 
                    instructions.get(i).getValue()));
                int[] visited = new int[instructions.size()];
                int acc = getAccBeforeLoop(0, 0, instructions, visited);
                if (visited[instructions.size() - 1] == 1) {
                    return acc;
                }
                instructions.set(i, new Pair(
                    (instructions.get(i).getKey().equals("jmp")) ? "nop" : "jmp", 
                    instructions.get(i).getValue()));
            }
        }
        return -1;
    }

	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p8_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }
	    
        List<Pair<String, Integer>> instructions = new ArrayList<>();
        for (String row : in) {
            String[] chunks = row.split(" ");
            instructions.add(new Pair(chunks[0], Integer.parseInt(chunks[1])));
        }

        int[] visited = new int[instructions.size()];
        System.out.println(getAccBeforeLoop(0, 0, instructions, visited));

        System.out.println(fixInfiniteLoop(instructions));
	}
}
