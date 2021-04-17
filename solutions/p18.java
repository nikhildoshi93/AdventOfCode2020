import java.io.*;  // Import the File class
import java.util.*;

public class p18 {

    static List<String> operatorPrecedence = Arrays.asList("+", "*");

    public static long processExpr(List<String> expr) {
        long left = Long.parseLong(expr.get(0));
        String sign = " ";
        for (String c : expr) {
            if (c.equals("+") || c.equals("*")) {
                sign = c;
            } else {
                if (sign.equals("+")) {
                    left += Long.parseLong(c);
                } else if (sign.equals("*")) {
                    left *= Long.parseLong(c);
                }

            }
        }
        return left;
    }

    public static long processExprWithPrecedence(List<String> expr) throws Exception {
        // List<String> stack = new ArrayList<>();
        String prevVal = "";
        for (String operator : operatorPrecedence) {
            int i = 0;
            while (i < expr.size()) {
                String c = expr.get(i);
                if (c.equals(operator)) {
                    String left = expr.get(i - 1);
                    String right = expr.get(i + 1);
                    expr.remove(i); // sign
                    expr.remove(i); // i+1 is left-shifted
                    Long res = 0l;
                    if (operator.equals("+")) {
                        res = Long.parseLong(left) + Long.parseLong(right);
                    } else {
                        res = Long.parseLong(left) * Long.parseLong(right);
                    }
                    expr.set(i - 1, Long.toString(res));

                } else {
                    i += 1;
                }
            }
        }

        if (expr.size() != 1) {
            throw new Exception("expr pending");
        }
        return Long.parseLong(expr.get(0));
    }

    public static long evaluateExpression(String row) throws Exception {
        List<String> stack = new ArrayList<>();

        for (Character c_raw : row.toCharArray()) {
            String c = String.valueOf(c_raw);
            if (c.equals(" ")) {
                continue;
            }

            if (c.equals(")")) {
                List<String> subStack = new ArrayList<>();
                while (true) {
                    String sc = stack.get(stack.size() - 1);
                    stack.remove(stack.size() - 1);
                    if (sc.equals("(")) {
                        stack.add(Long.toString(processExprWithPrecedence(subStack)));
                        break;
                    }
                    subStack.add(0, sc);
                }
            }
            else {
                stack.add(c);
            }
        }
        return processExprWithPrecedence(stack);
    }

	public static void main(String[] args) throws Exception{
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p18_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }

        long sum = 0;
        for (String row : in) {
            try {
                sum += evaluateExpression(row);
            } catch(Exception e) {
                throw e;
            }
        }

        System.out.println(sum);
	}
}
