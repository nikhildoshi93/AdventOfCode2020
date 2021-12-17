import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import java.util.Collections;

public class p13 {

    public static int findNearestBus(int s, List<Integer> buses) {
        int lowest = s;
        int res = 0;
        for (int bus : buses) {
            int rem = s % bus;
            int delta = bus - rem;
            if (delta < lowest) {
                lowest = delta;
                res = bus * delta;
            }
        }
        return res;
    }

    public static long chineseRemainderTheorem(int s, List<Integer> buses, List<Integer> rawIdxList) {
        List<Integer> idxList = new ArrayList<>();
        for (int idx: rawIdxList) {
            idxList.add(rawIdxList.get(rawIdxList.size() - 1) - idx);
        }

        long prod = 1;
        for (int bus : buses) {
            prod *= bus;
        }
        List<Long> pp = new ArrayList<>();
        for (int bus : buses) {
            pp.add(prod/(long) bus);
        }
        List<Long> inv = new ArrayList<>();
        int j = 0;
        for (int bus : buses) {
            int i = 1;
            while (i > 0) {
                if (((pp.get(j) * i)) % bus - 1 == 0) {
                    inv.add((long) i);
                    break;
                }
                i += 1;
            }
            j += 1;
        }
        long sum = 0;
        for (int i = 0; i < buses.size(); i++) {
            sum += idxList.get(i)*pp.get(i)*inv.get(i);
        }
        return sum % prod - rawIdxList.get(rawIdxList.size() - 1);
    }

    public static long findPatternedTimeBruteForce(int s, List<Integer> buses, List<Integer> idxList) {
        long init = buses.get(0);
        int factor = 1;
        while (init > 0) {
            int num = buses.get(0) * factor - idxList.get(0);
            boolean done = true;
            for (int i = 1; i < buses.size(); i++) {
                int rem = (num + idxList.get(i)) % buses.get(i); 
                if (rem != 0) {
                    done = false;
                    break;
                }
            }
            if (done) {
                return num;
            }
            factor += 1;
        }
        return 0;
    }
    
    public static void main(String[] args) {
        int start = 0;
        List<Integer> in = new ArrayList<>();
        List<Integer> idxList = new ArrayList<>();
        int idx = 0;
        try {
            File myObj = new File("p13_in.txt");
            Scanner scanner = new Scanner(myObj);
            start = Integer.parseInt(scanner.nextLine());
            String[] vals = scanner.nextLine().split(",");
            for(int i=0; i<vals.length; i++) {
                if (!vals[i].equals("x")) {
                    in.add(Integer.parseInt(vals[i]));
                    idxList.add(idx);
                }
                idx += 1;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        
        System.out.println(start);
        System.out.println(in);
        System.out.println(idxList);

        System.out.println(findNearestBus(start, in));

        System.out.println(chineseRemainderTheorem(start, in, idxList));
    }
}
