import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import javafx.util.Pair;

public class p15 {

    // Stackoverflow for larger recursive loops.
    public static int findnthRecursive(int n, int limit, int ex, Map<Integer, Integer> p1, Map<Integer, Integer> p2) {
        if (n == limit) {
            return ex;
        }

        int v1 = p1.getOrDefault(ex, -1);
        int v2 = p2.getOrDefault(ex, -1);

        int res = -1;
        if (v1 != -1 && v2 != -1) {
            res = v2 - v1;
            p1.put(ex, v2);
            p2.put(ex, n);
        } else {
            res = 0;
            // p1.put(ex, n);
        }

        int v11 = p1.getOrDefault(res, -1);
        int v12 = p1.getOrDefault(res, -1);
        if (v11 != -1 && v12 != -1) {
            p1.put(res, v12);
            p2.put(res, n);
        } else if (v11 != -1) {
            p2.put(res, n);
        } else {
            p1.put(res, n);
        }

        return findnth(n + 1, limit, res, p1, p2);
    }

    public static int findnth(int n, int limit, int ex, Map<Integer, Integer> p1, Map<Integer, Integer> p2) {

        int v1 = p1.getOrDefault(ex, -1);
        int v2 = p2.getOrDefault(ex, -1);

        int res = -1;
        if (v1 != -1 && v2 != -1) {
            res = v2 - v1;
            p1.put(ex, v2);
            p2.put(ex, n);
        } else {
            res = 0;
            // p1.put(ex, n);
        }

        int v11 = p1.getOrDefault(res, -1);
        int v12 = p1.getOrDefault(res, -1);
        if (v11 != -1 && v12 != -1) {
            p1.put(res, v12);
            p2.put(res, n);
        } else if (v11 != -1) {
            p2.put(res, n);
        } else {
            p1.put(res, n);
        }

        return res;
    }
    
	public static void main(String[] args) {
        int limit = 30000000;
	    List<Integer> in = Arrays.asList(13,16,0,12,15,1);
	    
        Map<Integer, Integer> p1 = new HashMap<>();
        Map<Integer, Integer> p2 = new HashMap<>();

        int idx = 0;
        for (int i : in) {
            p1.put(i, idx);
            idx += 1;
        }

        int curr = in.size();
        int prev = in.get(in.size() - 1);
        while (curr != limit) {
            prev = findnth(curr, limit, prev, p1, p2);
            curr += 1;
        }
        System.out.println(prev);

        // Recursive attempt
        // System.out.println(findnth(in.size(), limit, in.get(in.size() - 1), p1, p2));
	}
}
