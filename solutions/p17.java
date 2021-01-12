import java.io.*;  // Import the File class and exceptions
import java.util.*;
public class p17 {

	private static List<List<Integer>> get_neighbours(int x, int y, int z) {
		List<List<Integer>> neighbours = new ArrayList<>();
		List<Integer> permutations = Arrays.asList(-1, 0, 1);
		for (int c : Arrays.asList(-1, 0, 1)) {
			for (int b : Arrays.asList(-1, 0, 1)) {
				for (int a : Arrays.asList(-1, 0, 1)) {
					if (!(c == 0 && b == 0 && a == 0)){
						neighbours.add(Arrays.asList(z-c, y-b, x-a));
					}
				}
			}
		}
		return neighbours;
	}

	private static List<List<Integer>> get_neighboursLarge(int x, int y, int z, int o) {
		List<List<Integer>> neighbours = new ArrayList<>();
		List<Integer> permutations = Arrays.asList(-1, 0, 1);
		for (int d : Arrays.asList(-1, 0, 1)) {
			for (int c : Arrays.asList(-1, 0, 1)) {
				for (int b : Arrays.asList(-1, 0, 1)) {
					for (int a : Arrays.asList(-1, 0, 1)) {
						if (!(d == 0 && c == 0 && b == 0 && a == 0)){
							neighbours.add(Arrays.asList(z-d, y-c, x-b, o-a));
						}
					}
				}
			}
		}
		return neighbours;
	}

	private static int calculateState(List<List<List<Integer>>> world, int x, int y, int z, int x_lim, int y_lim, int z_lim) {
		List<List<Integer>> neighbours = get_neighbours(x, y, z);
		int activeCount = 0;
		for (List<Integer> neighbour : neighbours) {
			int c = neighbour.get(0);
			int b = neighbour.get(1);
			int a = neighbour.get(2);
			// Check if neighbour is valid
			if (!( (c <= 0 || c >= z_lim-1) || (b <= 0 || b >= y_lim-1) || (a <= 0 || a >= x_lim-1) )){
				if (world.get(c-1).get(b-1).get(a-1) == 1) {
					activeCount += 1;
				}
			}
		}

		if (!( (z <= 0 || z >= z_lim-1) || (y <= 0 || y >= y_lim-1) || (x <= 0 || x >= x_lim-1) )){
			if (world.get(z-1).get(y-1).get(x-1) == 1) {
				if (activeCount == 2 || activeCount == 3) {
					return 1;
				}
			} else {
				if (activeCount == 3) {
					return 1;
				}	
			}
		} else {
			if (activeCount == 3) {
				return 1;
			}
		}
		return 0;
	}

	private static int calculateStateLarge(List<List<List<List<Integer>>>> world, int x, int y, int z, int o, int x_lim, int y_lim, int z_lim, int o_lim) {
		List<List<Integer>> neighbours = get_neighboursLarge(x, y, z, o);
		int activeCount = 0;
		for (List<Integer> neighbour : neighbours) {
			int c = neighbour.get(0);
			int b = neighbour.get(1);
			int a = neighbour.get(2);
			int p = neighbour.get(3);
			// Check if neighbour is valid
			if (!( (c <= 0 || c >= z_lim-1) || (b <= 0 || b >= y_lim-1) || (a <= 0 || a >= x_lim-1) || (p <= 0 || p >= o_lim-1) )){
				if (world.get(c-1).get(b-1).get(a-1).get(p-1) == 1) {
					activeCount += 1;
				}
			}
		}

		if (!( (z <= 0 || z >= z_lim-1) || (y <= 0 || y >= y_lim-1) || (x <= 0 || x >= x_lim-1) || (o <= 0 || o >= o_lim-1) )){
			if (world.get(z-1).get(y-1).get(x-1).get(o-1) == 1) {
				if (activeCount == 2 || activeCount == 3) {
					return 1;
				}
			} else {
				if (activeCount == 3) {
					return 1;
				}	
			}
		} else {
			if (activeCount == 3) {
				return 1;
			}
		}
		return 0;
	}
    
    private static List<List<List<Integer>>> simulateCycles(List<List<List<Integer>>> world, int cycles) {
    	if (cycles == 0) {
    		return world;
    	}

    	List<List<List<Integer>>> next = new ArrayList<>();
    	int z_lim = world.size() + 2;
    	int y_lim = world.get(0).size() + 2;
    	int x_lim = world.get(0).get(0).size() + 2;

    	for (int z = 0; z < z_lim; z++) {
    		List<List<Integer>> grid = new ArrayList<>();
    		for (int y = 0; y < y_lim; y++) {
    			List<Integer> row = new ArrayList<>();
    			for (int x = 0; x < x_lim; x++) {
    				row.add(0);
    			}
    			grid.add(row);
    		}
    		next.add(grid);
    	}

    	for (int z = 0; z < z_lim; z++) {
    		for (int y = 0; y < y_lim; y++) {
    			for (int x = 0; x < x_lim; x++) {
    				next.get(z).get(y).set(x, calculateState(world, x, y, z, x_lim, y_lim, z_lim));
    			}
    		}
    	}

    	return simulateCycles(next, cycles - 1);
    }

    private static List<List<List<List<Integer>>>> simulateCyclesLarge(List<List<List<List<Integer>>>> world, int cycles) {
    	if (cycles == 0) {
    		return world;
    	}

    	List<List<List<List<Integer>>>> next = new ArrayList<>();
    	int z_lim = world.size() + 2;
    	int y_lim = world.get(0).size() + 2;
    	int x_lim = world.get(0).get(0).size() + 2;
    	int o_lim = world.get(0).get(0).get(0).size() + 2;

    	for (int z = 0; z < z_lim; z++) {
    		List<List<List<Integer>>> box = new ArrayList<>();
    		for (int y = 0; y < y_lim; y++) {
    			List<List<Integer>> grid = new ArrayList<>();	
    			for (int x = 0; x < x_lim; x++) {
    				List<Integer> row = new ArrayList<>();
    				for (int o = 0; o < o_lim; o++) {
    					row.add(0);
    				}
    				grid.add(row);
    			}
    			box.add(grid);
    		}
    		next.add(box);
    	}

    	for (int z = 0; z < z_lim; z++) {
    		for (int y = 0; y < y_lim; y++) {
    			for (int x = 0; x < x_lim; x++) {
    				for (int o = 0; o < o_lim; o++) {
    					next.get(z).get(y).get(x).set(o, calculateStateLarge(world, x, y, z, o, x_lim, y_lim, z_lim, o_lim));
    				}
    			}
    		}
    	}

    	return simulateCyclesLarge(next, cycles - 1);
    }
    
	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p17_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }

	    List<List<List<Integer>>> init = new ArrayList<>();
	    List<List<Integer>> grid = new ArrayList<>();
	    for (String buffer : in) {
	    	List<Integer> row = new ArrayList<>();
	    	for (Character c : buffer.toCharArray()) {
	    		if (c.equals('.')) {
	    			row.add(0);
	    		} else if (c.equals('#')) {
	    			row.add(1);
	    		}
	    	}
	    	grid.add(row);
	    }
	    init.add(grid);

	    int totalActive = 0;
	    List<List<List<Integer>>> world = simulateCycles(init, 1);
	    for (List<List<Integer>> _grid : world) {
	    	for (List<Integer> _row : _grid) {
	    		for (Integer i : _row) {
	    			totalActive += i;
	    		}
	    	}
	    }
	    System.out.println(totalActive);


	    List<List<List<List<Integer>>>> initLarge = new ArrayList<>();
	    initLarge.add(init);
	    int totalActiveLarge = 0;
	    List<List<List<List<Integer>>>> worldLarge = simulateCyclesLarge(initLarge, 6);
	    for (List<List<List<Integer>>> worldMini : worldLarge) {
	    	for (List<List<Integer>> gridMini : worldMini) {
	    		for (List<Integer> rowMini : gridMini) {
	    			for (Integer i : rowMini) {
	    				totalActiveLarge += i;
	    			}
	    		}
	    	}
	    }
	    System.out.println(totalActiveLarge);
    }
}
