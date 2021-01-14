import java.io.*;  // Import the File class
import java.util.*;

public class p9 {

    private static int preamble = 25;

    private static boolean valuesAddUp(int val, List<Integer> buffer) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Integer i : buffer) {
            if (!map.containsKey(i)) {
                map.put(i, 1);
            } else {
                map.put(i, 2);
            }
        }

        for (Integer i : map.keySet()) {
            if (i*2 == val && map.get(i) == 2) {
                return true;
            } else if (map.containsKey(val - i)) {
                return true;
            }
        }
        return false;
    }

    private static int findContiguousSum(int sum, List<String> in, int idx) {
        int c = 0;
        for (int i = 0; i < idx - 1; i++) {
            for (int j = i + 1; j < idx; j++) {
                Long temp = 0L;
                List<Integer> range = new ArrayList<>();
                for (int k = i; k <= j; k++) {
                    temp += Integer.parseInt(in.get(k));
                    range.add(Integer.parseInt(in.get(k)));
                }
                if (temp == sum) {
                    return Collections.min(range) + Collections.max(range);
                }
            }
        }
        return -1;
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

        List<Integer> buffer = new ArrayList<>();
        for (int i = 0; i < preamble; i++) {
            buffer.add(Integer.parseInt(in.get(i)));
        }

        for (int i = preamble; i < in.size(); i++) {
            int curr = Integer.parseInt(in.get(i));
            if (!valuesAddUp(curr, buffer)) {
                System.out.println(curr);
                System.out.println(findContiguousSum(curr, in, i));
                return;
            }
            buffer.remove(0);
            buffer.add(curr);
        }

        return;
	}
}
