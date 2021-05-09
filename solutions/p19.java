import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import javafx.util.Pair;

public class p19 {

    // Method 1: Compute all combinations.
    public static List<String> calculateRuleCandidates(Integer idx, Map<Integer, String> ruleMap, Map<Integer, List<String>> memory) {
        if (memory.containsKey(idx)) {
            return memory.get(idx);
        }

        List<String> candidates = new ArrayList<>();

        if (ruleMap.get(idx).contains("\"")) {
            candidates.add(ruleMap.get(idx).substring(1,2));
        } else {
            String[] segments = ruleMap.get(idx).split(" \\| ");
            for (int i = 0; i < segments.length; i++) {
                String[] indices = segments[i].split(" ");
                
                List<String> subCandidates = new ArrayList<>();
                for (int j = 0; j < indices.length; j++) {
                    List<String> partials = calculateRuleCandidates(Integer.parseInt(indices[j]), ruleMap, memory);
                    List<String> appends = new ArrayList<>();
                    if (subCandidates.size() > 0) {
                        subCandidates.forEach((sub) -> {
                            partials.forEach((partial) -> {
                                appends.add(sub.concat(partial));
                            });
                        });
                        subCandidates = appends;
                    } else {
                        subCandidates.addAll(partials);
                    }
                }
                
                candidates.addAll(subCandidates);
            }
        }

        memory.put(idx, candidates);
        return candidates;
    }

    // Method 2: For each query, check if it fulfils the rules.
    public static boolean doesSatisfy(String str, Integer idx, Map<Integer, String> ruleMap, Map<String, Boolean> memory) {
        if (str.length() == 0) {
            return false;
        }

        String memKey = str.concat(idx.toString());
        if (memory.containsKey(memKey)) {
            return memory.get(memKey);
        }
        String rule = ruleMap.get(idx);

        if (rule.contains("\"")) {
            rule = rule.replaceAll("\"", "");
            if (str.equals(rule)) {
                // System.out.println(memKey);
                memory.put(memKey, true);
                return true;
            }
        } else {
            String[] segments = rule.split(" \\| ");
            for (int i = 0; i < segments.length; i++) {
                String[] indices = segments[i].split(" ");
                if (indices.length == 1) {
                    System.out.println(indices[0]);
                    if (doesSatisfy(str, Integer.parseInt(indices[0]), ruleMap, memory)) {
                        memory.put(memKey, true);
                        return true;
                    }
                } else {
                    String leftIdx = indices[0];
                    String rightIdx = indices[1];
                    for (int x = 0; x < str.length() + 1 - indices.length; x++) {
                        String leftSub = str.substring(0, x + 1);
                        if (doesSatisfy(leftSub, Integer.parseInt(leftIdx), ruleMap, memory)) {
                            if (indices.length == 2) {
                                String rightSub = str.substring(x+1);
                                if (doesSatisfy(rightSub, Integer.parseInt(rightIdx), ruleMap, memory)) {
                                    // System.out.println(memKey);
                                    memory.put(memKey, true);
                                    return true;
                                }
                            } else { // Case of 3
                                for (int y = x+1; y < str.length() + 2 - indices.length; y++) {
                                    String rightSub = str.substring(x+1, y+1);
                                    if (doesSatisfy(rightSub, Integer.parseInt(rightIdx), ruleMap, memory)) {
                                        String thirdIdx = indices[2];
                                        String thirdSub = str.substring(y+1);
                                        if (doesSatisfy(thirdSub, Integer.parseInt(thirdIdx), ruleMap, memory)) {
                                            memory.put(memKey, true);
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        memory.put(memKey, false);
        return false;
    }
    
    public static void main(String[] args) {
        List<String> rules = new ArrayList<>();
        List<String> in = new ArrayList<>();
        boolean isRule = true;
        try {
            File myObj = new File("p19_in.txt");
            Scanner scanner = new Scanner(myObj);
            while (scanner.hasNextLine()) {
                String nxt = scanner.nextLine();
                if ("".equals(nxt)) {
                    isRule = false;
                    continue;
                }
                if (isRule) {
                    rules.add(nxt);
                } else {
                    in.add(nxt);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        
        Map<Integer, String> ruleMap = new HashMap<>();
        for (String rule : rules) {
            String[] segments = rule.split(": ");
            ruleMap.put(Integer.parseInt(segments[0]), segments[1]);
        }

        // Calculate Candidates, doesn't work for loops. Method-1.
      
        // List<String> candidates = calculateRuleCandidates(0, ruleMap, new HashMap<Integer, List<String>>());
        // int pass = 0;
        // for (String query : in) {
        //     pass += (candidates.contains(query)) ? 1 : 0;
        // }
        // System.out.println(pass);

        int pass = 0;
        Map<String, Boolean> memory = new HashMap<String, Boolean>();
        for (String query : in) {

            pass += (doesSatisfy(query, 0, ruleMap, memory) ? 1 : 0);
        }
        System.out.println(pass);
    }
}
