import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public class p1
{
    static Integer sum = 2020;
    
    /**
     * Store elements in a hashMap and search for findSum-x.
     */
    public static Integer productOfTwoFindSum(List<Integer> in) {
        HashMap<Integer, Integer> inMap = new HashMap<Integer, Integer>();
        in.forEach((n) -> {
           inMap.put(n, 1);
        });
        
        for (int n: in) {
           if (inMap.containsKey(sum - n)) {
               return (n * (sum-n));
           } 
        };
        
        return -1;
    }
    
    public static Integer productOfThreeFindSum(List<Integer> in) {
        HashMap<Integer, Integer> inMap = new HashMap<Integer, Integer>();
        in.forEach((n) -> {
           inMap.put(n, 1);
        });
        
        int i = 0, j = 0;
        while (i < in.size()) {
            j = i + 1;
            while (j < in.size()) {
                if (inMap.containsKey(sum - in.get(i) - in.get(j))) {
                    return (in.get(i) * in.get(j) * (sum - in.get(i) - in.get(j)));
                };
                j++;
            };
            i++;
        }
        
        return -1;
    }
    
	public static void main(String[] args) {
	    List<Integer> in = new ArrayList<>();
	    try {
    		File myObj = new File("p1_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while(scanner.hasNextInt()) {
    		    in.add(scanner.nextInt());
    		}
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }
	    
	    System.out.println(productOfThreeFindSum(in));
	}
}
