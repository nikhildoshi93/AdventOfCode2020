import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;

public class p6 {
    
    // Count all subscriptions amongst each group
    private static List<Integer> countSubscriptions(List<List<String>> groups) {
        List<Integer> subscriptions = new ArrayList<>();
        for (List<String> group : groups) {
            int[] counter = new int[26];
            for (String row : group) {
                for (Character c : row.toCharArray()) {
                    counter[((int) c - (int) 'a')] = 1;    
                }
            }
            subscriptions.add(Arrays.stream(counter).sum());
        }
        return subscriptions;
    }
    
    // Count common subscriptions in the group
    private static List<Integer> countCommonListings(List<List<String>> groups) {
        List<Integer> subscriptions = new ArrayList<>();
        for (List<String> group : groups) {
            int[] counter = new int[26];
            for (String row : group) {
                for (Character c : row.toCharArray()) {
                    counter[((int) c - (int) 'a')] += 1;
                }
            }
            int res = 0;
            for (int i = 0; i < counter.length; i++) {
                if (counter[i] == group.size()) {
                    res += 1;
                }
            }
            subscriptions.add(res);
        }
        return subscriptions;
    }
    
	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p6_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }
	    
	    List<List<String>> groups = new ArrayList<>();
	    List<String> buffer = new ArrayList<>();
	    for (String row : in) {
	        if (row.length() == 0) {
	            groups.add(buffer);
	            buffer = new ArrayList<>();
	        } else {
	            buffer.add(row);
	        }
	    }
	    if (buffer.size() != 0) {
	        groups.add(buffer);
	    }

        List<Integer> subscriptions = countSubscriptions(groups);
        System.out.println(subscriptions.stream().mapToInt(Integer::intValue).sum());
        
        List<Integer> commonListings = countCommonListings(groups);
        System.out.println(commonListings.stream().mapToInt(Integer::intValue).sum());
	}
}
