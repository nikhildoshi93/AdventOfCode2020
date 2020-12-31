import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import javafx.util.Pair;

public class p4 {

    public static List<String> mandatoryTags = Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid");
    public static List<String> optionalTags = Arrays.asList("cid");

    // Check if the passport contains the mandatory tags apart from the optional ones.
    private static boolean isPassportValid(Map<String,String> passport) {
        for (String tag : mandatoryTags) {
            if (!optionalTags.contains(tag)) {
                if (!passport.containsKey(tag)) {
                    return false;
                }
                if (tag == "byr" && !isBirthYearValid(passport.get(tag))) {
                    return false;
                } else if (tag == "iyr" && !isIssueYearValid(passport.get(tag))) {
                    return false;
                } else if (tag == "eyr" && !isExpiryYearValid(passport.get(tag))) {
                    return false;
                } else if (tag == "hgt" && !isHeightValid(passport.get(tag))) {
                    return false;
                } else if (tag == "hcl" && !isHairColorValid(passport.get(tag))) {
                    return false;
                } else if (tag == "ecl" && !isEyeColorValid(passport.get(tag))) {
                    return false;
                } else if (tag == "pid" && !isPassportIdValid(passport.get(tag))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isBirthYearValid(String value) {
        try {
            int year = Integer.parseInt(value);
            if (year < 1920 || year > 2002) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isIssueYearValid(String value) {
        try {
            int year = Integer.parseInt(value);
            if (year < 2010 || year > 2020) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isExpiryYearValid(String value) {
        try {
            int year = Integer.parseInt(value);
            if (year < 2020 || year > 2030) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isHeightValid(String value) {
        try {
            if (value.contains("cm")) {
                String[] heightStr = value.split("cm");
                if (heightStr.length > 1) {
                    return false;
                }
                int height = Integer.parseInt(heightStr[0]);
                if (height >= 150 && height <= 193) {
                    return true;
                }
            } else if (value.contains("in")) {
                String[] heightStr = value.split("in");
                if (heightStr.length > 1) {
                    return false;
                }
                int height = Integer.parseInt(heightStr[0]);
                if (height >= 59 && height <= 76) {
                   return true;
               }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static boolean isHairColorValid(String value) {
        try {
            if (value.contains("#")) {
                String[] colorStr = value.split("#");
                if (colorStr.length > 2) {
                    return false;
                }
                for (Character c : colorStr[1].toCharArray()) {
                    if (c >= '0' && c <= '9') {
                        return true;
                    } else if (c >= 'a' && c <= 'f') {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static boolean isEyeColorValid(String value) {
        List<String> validEyeColors = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
        return validEyeColors.contains(value);
    }

    private static boolean isPassportIdValid(String value) {
        try {
            if (value.length() == 9) {
                Integer.parseInt(value);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    // Check for valid passports amongst all.
    private static int countValidPassports(List<Map<String,String>> passports) {
        int valid = 0;
        for (Map<String,String> passport : passports) {
            if (isPassportValid(passport)) {
                valid += 1;
            }
        }
        return valid;
    }
    

	public static void main(String[] args) {
	    List<String> in = new ArrayList<>();
	    try {
    		File myObj = new File("p4_in.txt");
    		Scanner scanner = new Scanner(myObj);
    		while (scanner.hasNextLine()) {
                in.add(scanner.nextLine());
            }
            scanner.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("File not found.");
            e.printStackTrace();
	    }
	    
        List<Map<String,String>> passports = new ArrayList<>();
        Map<String, String> passport = new HashMap<>();
        for (String row : in) {
            if (row.length() == 0) {
                passports.add(passport);
                passport = new HashMap<>();
            } else {
                for (String chunk : row.split(" ")) {
                    String[] info = chunk.split(":");
                    passport.put(info[0], info[1]);
                }
            }
        }

        // Last passport might not be separated by a new-line
        if (passport.size() != 0) {
            passports.add(passport);
        }

        System.out.println(countValidPassports(passports));
	}
}
