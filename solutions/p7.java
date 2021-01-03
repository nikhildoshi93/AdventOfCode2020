import java.io.*;  // Import the File class and exceptions
import java.util.*;
public class p7 {
    
    // Count potential bags which can contain this destination bag
    private static void countPotentialSources(String key, Map<String, List<String>> reverseMap, List<String> res) {
        if (!reverseMap.containsKey(key)) {
            return;
        }
        
        List<String> sources = reverseMap.get(key);
        for (String src : sources) {
            if (!res.contains(src)) {
                res.add(src);
            }
            countPotentialSources(src, reverseMap, res);
        }
    }
    
    // Count the total number of bags one source bag can contain.
    private static int countChildrenCombinations(String key, Map<String, Map<String, Integer>> freqMap) {
        if (!freqMap.containsKey(key)) {
            return 0;
        }
        
        int count = 0;
        for (Map.Entry<String, Integer> entry : freqMap.get(key).entrySet()) {
            count += entry.getValue();
            count += entry.getValue() * countChildrenCombinations(entry.getKey(), freqMap);
        }
        return count;
    }
    
	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p7_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }
	    
	    // Parse the rule set to frequency map and reverse map (for walking backwards)
	    Map<String, Map<String, Integer>> freqMap = new HashMap<>();
	    Map<String, List<String>> reverseMap = new HashMap<>();
	    for (String rule : in) {
	        String[] params = rule.split(" contain ");
	        String src = params[0].split(" bags")[0];
	        String[] bags = params[1].split(",");
	        for (String bag : bags) {
	            bag = bag.trim();
	            
	            if (bag.contains("no other")) {
	                continue;
	            }
	            String freqStr = bag.substring(0, bag.indexOf(" "));
	            String bagStr = bag.substring(bag.indexOf(" ") + 1);
	            if (bagStr.contains(".")) {
	                bagStr = bagStr.substring(0, bagStr.length() - 1);
	            }
	            if (bagStr.contains("bags")) {
	                bagStr = bagStr.substring(0, bagStr.length() - 5);
	            } else {
	                bagStr = bagStr.substring(0, bagStr.length() - 4);
	            }
	            
	            if (reverseMap.containsKey(bagStr)) {
	                reverseMap.get(bagStr).add(src);
	            } else {
	                List<String> sources = new ArrayList<>();
	                sources.add(src);
	                reverseMap.put(bagStr, sources);
	            }
	            
	            if (freqMap.containsKey(src)) {
	                freqMap.get(src).put(bagStr, Integer.parseInt(freqStr));
	            } else {
	                Map<String, Integer> children = new HashMap<>();
	                children.put(bagStr, Integer.parseInt(freqStr));
	                freqMap.put(src, children);
	            }
	        }
	    }
	    
	    List<String> res = new ArrayList<>();
	    countPotentialSources("shiny gold", reverseMap, res);
        System.out.println(res.size());
        
        System.out.println(countChildrenCombinations("shiny gold", freqMap));
	}
}
