import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import javafx.util.Pair;
import java.util.Collections;
import java.lang.Math;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class p20 {

    public static List<String> rotateEdges(List<String> grid, Integer times) {
        for (int i = 0; i < times; i++) {
            String temp = grid.get(grid.size() - 1);
            for (int j = 0; j < grid.size() - 1; j++) {
                grid.set(grid.size() - 1 - j, grid.get(grid.size() - 1 - j - 1));
            }            
            grid.set(0, temp);
        }
        return grid;
    }

    public static List<String> rotateGrid(List<String> grid, Integer times) {
        for (int i = 0; i < times; i++) {
            grid = flipDiagonal(grid);
            grid = flipGrid(grid);
        }
        return grid;
    }

    public static Pair<List<String>, List<String>> alignCorner(String key, List<String> edges, Map<String, List<String>> neighbourMap, List<String> grid,
            Map<String, List<String>> grids, Map<String, List<String>> in) {
        List<Integer> cornerEdges = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String edge = edges.get(i);
            if (neighbourMap.get(edge).size() == 1) {
                cornerEdges.add(i);
            }
        }

        int smallerEdge = Collections.min(cornerEdges);
        int largerEdge = Collections.max(cornerEdges);
        if (!(smallerEdge == 0) || !(largerEdge == 3)) {
            grid = rotateGrid(grid, 3-smallerEdge);
            edges = rotateEdges(edges, 3-smallerEdge);
        }
        
        return new Pair(grid, edges);
    }

    public static List<String> flipDiagonal(List<String> grid) {
        List<String> diagonalFlip = new ArrayList<>();
        for (String row : grid) {
            diagonalFlip.add("");
        }
        
        for (String row : grid) {
            int i = 0;
            for (Character c : row.toCharArray()) {
                diagonalFlip.set(i, diagonalFlip.get(i).concat(c.toString()));
                i += 1;
            }
        }
        return diagonalFlip;
    }

    public static List<String> flipGrid(List<String> grid) {

        List<String> flip = new ArrayList<>();
        for (String row : grid) {
            flip.add(new StringBuilder(row).reverse().toString());
        }
        return flip;
    }

    public static Pair<List<String>, List<String>> findOrientation(String find, List<String> grid, List<String> edges, Integer orientation) {
        int findIndex = -1;
        Boolean isFlip = false;
        for (int i = 0; i < 4; i++) {
            String edge = edges.get(i);
            String reverseEdge = new StringBuilder(edge).reverse().toString();

            if (orientation == 3) {
                String temp = edge;
                edge = reverseEdge;
                reverseEdge = temp;
            }

            if (edge.equals(find)) {
                findIndex = i;
                isFlip = false;
                break;
            }
            if (reverseEdge.equals(find)) {
                findIndex = i;
                isFlip = true;
                break;
            }
        }

        if (findIndex == -1) {
            System.out.println("find failure");
        }

        if (isFlip) {
            grid = flipGrid(grid);
            edges = getEdges(grid);
            if (findIndex == 3) {
                findIndex = 1;
            } else if (findIndex == 1) {
                findIndex = 3;
            }
        }

        int diff = 0;
        if (findIndex == orientation) {
            diff = 0;
        } else if (findIndex < orientation) {
            // if (orientation == 3) {
                diff = orientation - findIndex;
            // } else {
            //     diff = 
            // }
        } else {
            if (orientation == 3) {
                diff = 4 - (orientation - findIndex);
            } else {
                diff = 4 - findIndex;
            }
        }

        return new Pair(rotateGrid(grid, diff), rotateEdges(edges, diff));
    }

    public static Pair<String, Pair<List<String>, List<String>>> findMatchingGrid(String key, String edge, Map<String, List<String>> neighbourMap, 
            Map<String, List<String>> grids, Map<String, List<String>> in, Integer orientation) {
        List<String> neighbours = neighbourMap.get(edge);

        if (neighbours.size() == 2) {
            String neigh = neighbours.get(0);
            if (key.equals(neighbours.get(0))) {
                neigh = neighbours.get(1);
            }
            Pair<List<String>, List<String>> neighGrid = findOrientation(edge, grids.get(neigh), in.get(neigh), orientation);

            return new Pair<>(neigh, neighGrid);
        } else {
            System.out.println(key);
            System.out.println(edge);
            System.out.println(neighbours);
            System.out.println("ERROR");
            return null;
        }
    }

    public static List<String> truncateRows(List<String> in) {
        List<String> trunc = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            if (!(i == 0 || i == in.size() - 1)) {
                trunc.add(in.get(i).substring(1, in.size() - 1));
            }
        }
        return trunc;
    }

    public static List<List<List<String>>> fitGridsIntoImage(Map<String, List<String>> grids, 
                                                       Map<String, List<String>> in,
                                                       Map<String, List<String>> corners) {
        Map<String, List<String>> neighbourMap = calculateNeighbourMap(in);

        String topCorner = corners.keySet().iterator().next();
        int squareSize = (int) Math.sqrt(grids.size());

        List<List<List<String>>> square = new ArrayList<>();
        String currentId = topCorner;
        Pair<List<String>, List<String>> alignedGrid = alignCorner(topCorner, in.get(topCorner), neighbourMap, grids.get(topCorner), grids, in);
        List<String> currGrid = alignedGrid.getKey();
        List<String> currEdges = alignedGrid.getValue();

            List<String> firstGrid = currGrid;
            List<String> firstEdges = currEdges;
            String firstId = currentId;
        // System.out.println(currEdges);
        for (int i = 0; i < squareSize; i++) {
            List<List<String>> row = new ArrayList<>();
            row.add(truncateRows(currGrid));
            firstGrid = currGrid;
            firstEdges = currEdges;
            firstId = currentId;
            // System.out.println(currentId);
            for (int j = 0; j < squareSize - 1; j++) {
                String rightEdge = currEdges.get(1);
                Pair<String, Pair<List<String>, List<String>>> matchSquare = findMatchingGrid(currentId, rightEdge, neighbourMap, grids, in, 3);
                row.add(truncateRows(matchSquare.getValue().getKey()));
                currEdges = matchSquare.getValue().getValue();
                currentId = matchSquare.getKey();
                // System.out.println(currentId);
                // System.out.println(currEdges);
                // System.out.println(matchSquare.getValue().getKey());
            }
            square.add(row);
            if (i != squareSize - 1) {
                String downEdge = firstEdges.get(2);
                downEdge = new StringBuilder(downEdge).reverse().toString();
                Pair<String, Pair<List<String>, List<String>>> matchSquare = findMatchingGrid(firstId, downEdge, neighbourMap, grids, in, 0);
                currGrid = matchSquare.getValue().getKey();
                currentId = matchSquare.getKey();
                currEdges = matchSquare.getValue().getValue();
            }
        }
        return square;
    }

    public static Map<String, List<String>> calculateNeighbourMap(Map<String, List<String>> in) {
        Map<String, List<String>> neighbourMap = new HashMap<>();
        for (String key : in.keySet()) {
            List<String> edges = in.get(key);
            for (String edge : edges) {
                String reverseEdge = new StringBuilder(edge).reverse().toString();
                List<String> n1 = neighbourMap.getOrDefault(edge, new ArrayList<String>());
                n1.add(key);
                neighbourMap.put(edge, n1);
                List<String> n2 = neighbourMap.getOrDefault(reverseEdge, new ArrayList<String>());
                n2.add(key);
                neighbourMap.put(reverseEdge, n2);
            }
        }
        return neighbourMap;
    }

    public static Map<String, List<String>> findCorners(Map<String, List<String>> in) {
        Map<String, Integer> edgeMap = new HashMap<>();
        for (List<String> edges : in.values()) {
            for (String edge : edges) {
                String reverseEdge = new StringBuilder(edge).reverse().toString();
                edgeMap.put(edge, edgeMap.getOrDefault(edge, 0) + 1);
                edgeMap.put(reverseEdge, edgeMap.getOrDefault(reverseEdge, 0) + 1);
            }
        }

        Map<String, List<String>> corners = new HashMap<>();
        for (String key : in.keySet()) {
            List<String> edges = in.get(key);
            int count = 0;
            List<String> noMatches = new ArrayList<>(); 
            for (String edge : edges) {
                String reverseEdge = new StringBuilder(edge).reverse().toString();
                int edgeMatches = edgeMap.get(edge);
                int reverseEdgeMatches = edgeMap.get(reverseEdge);
                if (edgeMatches <= 1 && reverseEdgeMatches <= 1) {
                    count += 1;
                    noMatches.add(edge);
                }
            }
            if (count == 2) {
                corners.put(key, noMatches);
            }
        }

        return corners;
    }

    private static List<String> getEdges(List<String> array) {
        int len = array.size();
        List<String> edges = Arrays.asList("", "", "", "");
        for (int i = 0; i < len; i++) {
            Character top = array.get(0).charAt(i);
            Character right = array.get(i).charAt(len-1);
            Character bottom = array.get(len-1).charAt(i);
            Character left = array.get(i).charAt(0);
            edges.set(0, edges.get(0).concat(top.toString()));
            edges.set(1, edges.get(1).concat(right.toString()));
            edges.set(2, bottom.toString().concat(edges.get(2)));
            edges.set(3, left.toString().concat(edges.get(3)));
        }
        return edges;
    }

    public static List<String> combineSquares(List<List<List<String>>> squares) {
        int rows = squares.size();
        int stringSize = squares.get(0).get(0).size();
        List<String> megaSquare = new ArrayList<>();
        for (int i = 0; i < rows * stringSize; i++) {
            megaSquare.add("");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < stringSize; k++) {
                    int z = i*stringSize + k;
                    megaSquare.set(z, megaSquare.get(z).concat(squares.get(i).get(j).get(k)));
                }
            }
        }
        return megaSquare;
    }

    public static List<String> makeMonster() {
        List<String> monster = new ArrayList<>();
        monster.add("..................#.");
        monster.add("#....##....##....###");
        monster.add(".#..#..#..#..#..#...");
        return monster;
    }

    public static List<Pair<Integer, Integer>> allMatches(String text, String regex)
    {
        List<Pair<Integer, Integer>> out = new ArrayList<>();
      Matcher m = Pattern.compile(regex).matcher(text);
      int end = text.length();
      for (int i = 0; i < end; ++i)
      {
        for (int j = i + 1; j <= end; ++j) 
        {
          m.region(i, j);

          if (m.find()) 
          {   
            if (j - i == 20) {
                out.add(new Pair(i, j));
            }
          }   
        }   
      }   
      return out;
    }

    public static List<String> countMonsters(List<String> monster, List<String> square) {
        
        int count = 0;
        Pattern pattern1 = Pattern.compile(monster.get(1));
        Pattern pattern2 = Pattern.compile(monster.get(2));
        int t = 0;
        for (int i = 1; i < square.size() - 1; i++) {
            int block = 0;
            Matcher midMatcher = pattern1.matcher(square.get(i));

            List<Pair<Integer, Integer>> midMatches = allMatches(square.get(i), monster.get(1));
            for (Pair<Integer, Integer> pr0 : midMatches) {
                int start = pr0.getKey();
                int end = pr0.getValue();
            // while(midMatcher.find()) {
            //     int start = midMatcher.start();
            //     int end = midMatcher.end();
                t+=1;
                
                // if (start <= block) {
                //     continue; // block
                // }

                // Matcher endMatcher = pattern2.matcher(square.get(i+1));
                boolean done = false;
                List<Pair<Integer, Integer>> endMatches = allMatches(square.get(i+1), monster.get(2));
                for (Pair<Integer, Integer> pr : endMatches) {
                // while(endMatcher.find()) {
                    // int start2 = endMatcher.start();
                    // int end2 = endMatcher.end();

                    int start2 = pr.getKey();
                    int end2 = pr.getValue();

                    // if (end2 > end) {
                    //     break;
                    // }

                    if (end2 == end) {
                        if (square.get(i-1).charAt(end - 2) == '#') {
                            done = true;
                            count += 1;
                            block = end-1;

                            for (int x = 0; x < 3; x++) {
                                for (int y = start; y < end; y++) {
                                    StringBuilder st = new StringBuilder(square.get(i - 1 + x));
                                    if (st.charAt(y) == '#') {
                                        if (monster.get(x).charAt(y - start) == '.') {
                                            st.setCharAt(y, '1');
                                        } else {
                                            st.setCharAt(y, '0');
                                        }
                                    } else {
                                        st.setCharAt(y, '0');
                                    }
                                    square.set(i - 1 + x, st.toString());
                                }
                            }
                            
                        }
                    }
                }
            }
        }
        System.out.println(count);
        return square;
    }
    
    public static void main(String[] args) {
        Map<String, List<String>> in = new HashMap<>();
        Map<String, List<String>> grids = new HashMap<>();
        try {
            File myObj = new File("p20_in.txt");
            Scanner scanner = new Scanner(myObj);
            String id = "";
            List<String> array = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("")) {
                    in.put(id, getEdges(array));
                    grids.put(id, array);
                } else {
                    if (line.contains("Tile")) {
                        String[] chunks = line.split("Tile ");
                        id = chunks[1].substring(0, chunks[1].length() - 1);
                        array = new ArrayList<>();
                    } else {
                        array.add(line);
                    }
                }
            }
            in.put(id, getEdges(array));
            grids.put(id, array);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }

        Map<String, List<String>> corners = findCorners(in);

        // First problem: Product of each corner id.
        // Long product = new Long(corners.get(0)) + new Long(corners.get(1)) + new Long(corners.get(2)) + new Long(corners.get(3));
        // System.out.println(product); // 14986175499719

        List<List<List<String>>> square = fitGridsIntoImage(grids, in, corners);

        List<String> megaSquare = combineSquares(square);
        // for (int i = 0; i < 3*8; i++) {
        //     System.out.println(megaSquare.get(i));
        // }

        megaSquare = flipGrid(megaSquare);
        megaSquare = rotateGrid(megaSquare, 1);

        List<String> monster = makeMonster();
        megaSquare = countMonsters(monster, megaSquare);

        int count1 = 0;
        int count2 = 0;
        for (int i = 0; i < megaSquare.size(); i++) {
            count1 += megaSquare.get(i).chars().filter(ch -> ch == '1').count();
            count2 += megaSquare.get(i).chars().filter(ch -> ch == '#').count();
            // System.out.println(megaSquare.get(i));
        }
        System.out.println(count1+count2); // 2161
    }
}
