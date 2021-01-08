import java.io.*;  // Import the File class
import java.util.*;

public class p9 {

    private static int preamble = 25;

    private static boolean valuesAddUp(int val, HashMap<Integer, Integer> buffer) {
        for (Integer i : buffer.keySet()) {
            if (buffer.containsKey(val - i)) {
                return true;
            }
        }
        return false;
    }

	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p9_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }

        HashMap<Integer, Integer> buffer = new HashMap<Integer, Integer>();
        for (int i = 0; i < preamble; i++) {
            buffer.put(Integer.parseInt(in.get(i)), 1);
        }

        for (int i = preamble; i < in.size(); i++) {
            buffer.remove(Integer.parseInt(in.get(i - preamble)));
            int curr = Integer.parseInt(in.get(i));
            if (!valuesAddUp(curr, buffer)) {
                System.out.println(curr);
                return;
            }
            buffer.put(curr, 1);
        }

        return;
	}
}
