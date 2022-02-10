import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import java.util.Collections;

public class p12 {

    public static Map<Integer, Long> processMovementsWithWaypoint(List<String> in) {
        int curr = 0;
        Map<Integer, Long> movement = new HashMap<Integer, Long>() {{
            put(0, 0l);
            put(1, 0l);
            put(2, 0l);
            put(3, 0l);
        }};
        Map<Integer, Integer> waypoint = new HashMap<Integer, Integer>() {{
            put(0, 10);
            put(1, 0);
            put(2, 0);
            put(3, 1);
        }};
        List<Character> dirChars = Arrays.asList('E','S','W','N');
        List<Character> turnChars = Arrays.asList('L', 'R');


        for (String sugg : in) {
            Character dir = sugg.charAt(0);
            int target = Integer.parseInt(sugg.substring(1));
            if (dir == 'F') {
                for (int i : waypoint.keySet()) {
                    movement.put(i, movement.get(i) + target * waypoint.get(i));
                }
            } else if (dirChars.contains(dir)) {
                int idx = dirChars.indexOf(dir);
                waypoint.put(idx, waypoint.get(idx) + target);
            } else if (turnChars.contains(dir)) {
                int turns = target / 90;
                if (dir == 'L') {
                    turns = -1 * turns + 4;
                }
                Map<Integer, Integer> newWaypoint = new HashMap<Integer, Integer>() {{
                    put(0, 0);
                    put(1, 0);
                    put(2, 0);
                    put(3, 0);
                }};
                for (int i : waypoint.keySet()) {
                    int newIdx = (i + turns) % 4;
                    newWaypoint.put(newIdx, waypoint.get(i));
                }
                waypoint = newWaypoint;
            }

        }
        return movement;
    }

    public static Map<Integer, Integer> processMovements(List<String> in) {
        int curr = 0;
        Map<Integer, Integer> movement = new HashMap<Integer, Integer>() {{
            put(0, 0);
            put(1, 0);
            put(2, 0);
            put(3, 0);
        }};
        List<Character> dirChars = Arrays.asList('E','S','W','N');
        List<Character> turnChars = Arrays.asList('L', 'R');


        for (String sugg : in) {
            Character dir = sugg.charAt(0);
            int target = Integer.parseInt(sugg.substring(1));
            if (dir == 'F') {
                movement.put(curr, movement.get(curr) + target);
            } else if (dirChars.contains(dir)) {
                int idx = dirChars.indexOf(dir);
                movement.put(idx, movement.get(idx) + target);
            } else if (turnChars.contains(dir)) {
                int turns = target / 90;
                if (dir == 'L') {
                    turns = -1 * turns + 4;
                }
                curr = (curr + turns) % 4;
            }

        }
        return movement;
    }
    
    public static void main(String[] args) {
        List<String> in = new ArrayList<>();
        try {
            File myObj = new File("p12_in.txt");
            Scanner scanner = new Scanner(myObj);
            while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }

        Map<Integer, Integer> totalMovement = processMovements(in);
        Map<Integer, Long> totalMovementWithWaypoint = processMovementsWithWaypoint(in);

        System.out.println(totalMovement);
        System.out.println(totalMovementWithWaypoint);
    }
}
