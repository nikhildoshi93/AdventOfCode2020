import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public class p2 {

    /**
      * Create a frequency map of characters and check if the key occurs within the range.
      */
    private static boolean isPasswordRangeValid(int leftRange, int rightRange, Character key, String password) {
        HashMap<Character, Integer> inMap = new HashMap<Character, Integer>();
        for (Character n: password.toCharArray()) {
            if (inMap.containsKey(n)) {
                inMap.put(n, inMap.get(n) + 1);
            }
            else {
                inMap.put(n, 1);
            }
        };

        // Handle the case where the password does not contain the key and the leftRange is not 0.
        if (!inMap.containsKey(key) && leftRange > 0) {
            return false;
        }

        return (inMap.get(key) >= leftRange && inMap.get(key) <= rightRange);
    }

    /**
      * Create a frequency map of characters and check if the key occurs within the range.
      */
    private static boolean isPasswordOccurenceValid(int leftRange, int rightRange, Character key, String password) {
        boolean isLeft = password.charAt(leftRange - 1) == key;
        boolean isRight = password.charAt(rightRange - 1) == key;
        return isLeft ^ isRight;
    }

	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p2_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }
	    
	    int valid = 0;
        for (String password: in) {
            String[] components = password.split(" ");
            String[] rangeChar = components[0].split("-");
            String[] keyChar = components[1].split(":");
            if (isPasswordOccurenceValid(Integer.parseInt(rangeChar[0]), Integer.parseInt(rangeChar[1]), keyChar[0].toCharArray()[0], components[2])) {
                valid += 1;
            }
        };
        System.out.println(valid);
	}
}
