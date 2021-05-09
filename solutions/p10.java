import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import javafx.util.Pair;
import java.util.Collections;

public class p10 {

    public static Long countPermutations(List<Integer> in, Integer idx, Map<Integer, Long> memory) {
        if (memory.containsKey(idx)) {
            return memory.get(idx);
        }
        if (idx >= in.size() - 1) {
            return 1l;
        }

        Map<Integer, Integer> counter = new HashMap<>();
        int prev = in.get(idx);
        int i = idx + 1;
        int curr = in.get(i);
        int nxt = (i >= in.size() - 2) ? curr + 100 : in.get(i+1);

        Long res;
        if (nxt - prev > 3) {
            res = countPermutations(in, i, memory);
        } else {
            int post = (i >= in.size() - 3) ? curr + 100 : in.get(i+2);
            if (post - prev > 3) {
                res = countPermutations(in, i, memory) + countPermutations(in, i + 1, memory);
            } else {
                res = countPermutations(in, i, memory) + countPermutations(in, i + 1, memory) + countPermutations(in, i + 2, memory);
            }
        }
        memory.put(idx, res);
        return res;

    }
    
    public static void main(String[] args) {
        List<Integer> in = new ArrayList<>();
        try {
            File myObj = new File("p10_in.txt");
            Scanner scanner = new Scanner(myObj);
            while (scanner.hasNextLine()) {
                in.add(Integer.parseInt(scanner.nextLine()));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        
        Collections.sort(in);

        Map<Integer, Integer> counter = new HashMap<>();
        int prev = 0;
        for (int nxt : in) {
            int diff = nxt - prev;
            prev = nxt;
            
            if (counter.containsKey(diff)) {
                counter.put(diff, counter.get(diff) + 1);
            } else {
                counter.put(diff, 1);
            }
            
            if (diff > 3) {
                throw new RuntimeException("ERR");
            }
        }

        System.out.println(counter.get(1) * (counter.get(3) + 1));

        in.add(0);
        in.add(Collections.max(in) + 3);
        Collections.sort(in);

        System.out.println(countPermutations(in, 0, new HashMap<Integer, Long>()));
    }
}
