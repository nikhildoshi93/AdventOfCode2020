import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import java.util.Collections;

public class p21 {

    public static int findNonAllergens(List<List<String>> words, List<List<String>> allergens) {
		Map<String, List<Integer>> allergenIdx = new HashMap<>();
		int idx = 0;
		for (List<String> currAllergy : allergens) {
			for (String name : currAllergy) {
				if (allergenIdx.containsKey(name)) {
					allergenIdx.get(name).add(idx);
				} else {
					List<Integer> arr = new ArrayList<>();
					arr.add(idx);
					allergenIdx.put(name, arr);
				}
			}
			idx += 1;
		}
		
		List<Set<String>> foodSets = new ArrayList<>();
		for (List<String> foods : words) {
			Set<String> foodSet = new HashSet<String>(foods);
			foodSets.add(foodSet);
		}
        
		Set<String> potentialOffenders = new HashSet<>();
		Map<String, Set<String>> allergyMap = new HashMap<>();
		for (Map.Entry<String, List<Integer>> entry : allergenIdx.entrySet()) {
			List<Integer> foodIdxs = entry.getValue();
			Set<String> bufferSet = new HashSet<String>(foodSets.get(foodIdxs.get(0)));
			for (int i = 1; i < foodIdxs.size(); i++) {
				bufferSet.retainAll(foodSets.get(foodIdxs.get(i)));
				//System.out.println(foodIdxs);
				//System.out.println(bufferSet);
				
			}
			allergyMap.put(entry.getKey(), bufferSet);
			potentialOffenders.addAll(bufferSet);
		}
		
		int totals = 0;
		for (Set<String> foodSet : foodSets) {
			foodSet.removeAll(potentialOffenders);
			totals += foodSet.size();
		}
		
		Map<String, String> culprits = new TreeMap<>();
		while (potentialOffenders.size() > 0) {
			for (Map.Entry<String, Set<String>> entry : allergyMap.entrySet()) {
				Set<String> currOffenders = entry.getValue();
				currOffenders.retainAll(potentialOffenders);
				if (currOffenders.size() == 1) {
					culprits.put(entry.getKey(), currOffenders.iterator().next());
					potentialOffenders.removeAll(currOffenders);
					break;
				}
			}
		}
		
		String res = "";
		for (Map.Entry<String, String> entry : culprits.entrySet()) {
			res = res + "," + entry.getValue();
		}
		res = res.substring(1);
		
		System.out.println(totals);
		System.out.println(culprits);
		System.out.println(res);
		
		return 0;
		
    }
    
    public static void main(String[] args) {
        List<List<String>> words = new ArrayList<>();
		List<List<String>> allergens = new ArrayList<>();
        try {
            File myObj = new File("p21_in.txt");
            Scanner scanner = new Scanner(myObj);
			while(scanner.hasNextLine()) {
				String[] segments = scanner.nextLine().split("contains");
				String[] foods = segments[0].substring(0,segments[0].length() - 1).split(" ");
				words.add(Arrays.asList(foods));
				String[] currAllergy = segments[1].substring(1, segments[1].length() -1).split(", ");
				allergens.add(Arrays.asList(currAllergy));
			}
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        
        //System.out.println(words);
        //System.out.println(allergens);

		System.out.println(findNonAllergens(words, allergens));
    }
}
