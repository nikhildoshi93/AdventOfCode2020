import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.*;

public class p5 {
    
    private static List<Character> leftSectors = Arrays.asList('F', 'L');
    private static List<Character> rightSectors = Arrays.asList('B', 'R');
    
    private static Integer binarySearchIdx(int curr, String seatInfo, int start, int end) {
        // Base condition: After parsing through all directions.
        if (curr == seatInfo.length() || start >= end) {
            return start;
        }
        
        int mid = (start + end) / 2;
        if (leftSectors.contains(seatInfo.charAt(curr))) {
            return binarySearchIdx(curr + 1, seatInfo, start, mid);
        } else if (rightSectors.contains(seatInfo.charAt(curr))) {
            return binarySearchIdx(curr + 1, seatInfo, mid + 1, end);
        }
        return null;
    }
    
    private static List<Integer> parseSeats(List<String> seatDir) {
        List<Integer> seats = new ArrayList<>();
        for (String seatInfo : seatDir) {
            int row = binarySearchIdx(0, seatInfo.substring(0, 7), 0, 127);
            int column = binarySearchIdx(0, seatInfo.substring(7), 0, 7);
            seats.add(row * 8 + column);
        }
        return seats;
    }

	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p5_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }
	    
	    List<Integer> seats = parseSeats(in);
	    System.out.println(Collections.max(seats));
	    
	    Collections.sort(seats);
	    int i = 0;
	    while (i+1 < seats.size()) {
	        if (seats.get(i) + 2 == seats.get(i+1)) {
	            System.out.println(seats.get(i) + 1);
	        }
	        i += 1;
	    }
	}
}

