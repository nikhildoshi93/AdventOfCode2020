import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import javafx.util.Pair;

public class p16 {

    private static int invalidFigure(List<Integer> ticket, List<Pair<String, Pair<List<Integer>, List<Integer>>>> rules) {
        for (Integer val : ticket) {
            boolean matchFlag = false;
            for (Pair<String, Pair<List<Integer>, List<Integer>>> rule : rules) {
                if (matchFlag) {
                    break;
                }
                List<Integer> leftRange = rule.getValue().getKey();
                if (val >= leftRange.get(0) && val <= leftRange.get(1)) {
                    matchFlag = true;
                }
                List<Integer> rightRange = rule.getValue().getValue();
                if (val >= rightRange.get(0) && val <= rightRange.get(1)) {

                    matchFlag = true;
                }
            }
            if (!matchFlag) {
                return val;
            }
        }
        return 0;
    }

    // Find invalid tickets
    private static int findInvalidTicketTotalCount(List<List<Integer>> tickets, List<Pair<String, Pair<List<Integer>, List<Integer>>>> rules) {
        int totalOdds = 0;
        for (List<Integer> ticket : tickets) {
            totalOdds += invalidFigure(ticket, rules);
        }
        return totalOdds;
    }

    private static List<List<Integer>> findValidTickets(List<List<Integer>> tickets, List<Pair<String, Pair<List<Integer>, List<Integer>>>> rules) {
        List<List<Integer>> validTickets = new ArrayList<>();
        int totalOdds = 0;
        for (List<Integer> ticket : tickets) {
            int fig = invalidFigure(ticket, rules);
            if (fig == 0) {
                validTickets.add(ticket);
            }
        }
        return validTickets;
    }

    private static Map<String, List<Integer>> calculateFieldMapping(List<List<Integer>> tickets, List<Pair<String, Pair<List<Integer>, List<Integer>>>> rules) {
        Map<String, List<Integer>> fieldMap = new HashMap<>();
        int j = 0;
        while (j < rules.size()) {
            for (int i = 0; i < rules.size(); i++) {
                // if (fieldMap.containsKey(rules.get(i).getKey())) {
                //     continue;
                // }
                boolean matchFlag = true;
                for (List<Integer> ticket : tickets) {
                    int val = ticket.get(j);
                    List<Integer> leftRange = rules.get(i).getValue().getKey();
                    List<Integer> rightRange = rules.get(i).getValue().getValue();
                    if (val > leftRange.get(1) || val < leftRange.get(0)) {
                        if (val > rightRange.get(1) || val < rightRange.get(0)) {
                            matchFlag = false;
                            break;
                        }
                    }
                }
                if (matchFlag) {
                    if (fieldMap.containsKey(rules.get(i).getKey())) {
                        List<Integer> matches = fieldMap.get(rules.get(i).getKey());
                        matches.add(j);
                    } else {
                        List<Integer> matches = new ArrayList<>();
                        matches.add(j);
                        fieldMap.put(rules.get(i).getKey(), matches);
                    } 
                }
            }
            j += 1;
        }
        return fieldMap;
    }

    // Can use topological sort.
    public static Map<String, Integer> assignFieldMapping(Map<String, List<Integer>> fieldMap) {
        List<Pair<String, List<Integer>>> sortedMapping = new ArrayList<>();

        int i = 1, j = 0;
        while (j < fieldMap.size()) {
            for (String field : fieldMap.keySet()) {
                if (fieldMap.get(field).size() == i) {
                    j += 1;
                    sortedMapping.add(new Pair(field, fieldMap.get(field)));
                }
            }
            i += 1;
        }

        Map<String, Integer> assignedMapping = new HashMap<>();
        List<Integer> visitedRows = new ArrayList<>();
        for (Pair<String, List<Integer>> fieldPair : sortedMapping) {
            for (Integer row : fieldPair.getValue()) {
                if (!visitedRows.contains(row)) {
                    assignedMapping.put(fieldPair.getKey(), row);
                    visitedRows.add(row);
                }
            }
        }
        return assignedMapping;
    }
    
	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p16_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }

        List<Pair<String, Pair<List<Integer>, List<Integer>>>> rules = new ArrayList<>();
        int currIdx = 0;
        while (!in.get(currIdx).equals("")) {
            String[] chunks = in.get(currIdx).split(": ");
            String[] ranges = chunks[1].split(" or ");
            String[] leftRangeBits = ranges[0].split("-");
            List<Integer> leftRange = Arrays.asList(Integer.parseInt(leftRangeBits[0]), Integer.parseInt(leftRangeBits[1]));
            String[] rightRangeBits = ranges[1].split("-");
            List<Integer> rightRange = Arrays.asList(Integer.parseInt(rightRangeBits[0]), Integer.parseInt(rightRangeBits[1]));
            rules.add(new Pair(chunks[0], new Pair(leftRange, rightRange)));
            currIdx += 1;
        }
        
        currIdx += 2;
        List<Integer> myTicket = new ArrayList<>();
        for (String chunk : in.get(currIdx).split(",")) {
            myTicket.add(Integer.parseInt(chunk));
        }

        currIdx += 3;
        List<List<Integer>> otherTickets = new ArrayList<>();
        while (currIdx < in.size()) {
            List<Integer> ticket = new ArrayList<>();
            for (String chunk : in.get(currIdx).split(",")) {
                ticket.add(Integer.parseInt(chunk));
            }
            otherTickets.add(ticket);
            currIdx += 1;
        }

        int odds = findInvalidTicketTotalCount(otherTickets, rules);
        System.out.println(odds);

        List<List<Integer>> validTickets = findValidTickets(otherTickets, rules);
        Map<String, List<Integer>> fieldMap = calculateFieldMapping(validTickets, rules);
        Map<String, Integer> assignedMapping = assignFieldMapping(fieldMap);

        Long depProduct = 1l;
        for (String field : assignedMapping.keySet()) {
            if (field.startsWith("departure")) {
                depProduct *= myTicket.get(assignedMapping.get(field));
            }
        }
        System.out.println(depProduct);
	}
}
