import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MathematicalReader {
    public static void evaluateExpression(File f) {
        try (var bfr = new BufferedReader(new FileReader(f))) {

            String line = bfr.readLine();
            String[] lineArray = new String[2];
            Expressions expression = Expressions.DEFAULT;
            while (line != null) {
                line = line.replaceAll("\\s", "");
                for (int i = 0; i < line.length(); i++) {
                    if (!Character.isDigit(line.charAt(i))) {
                        char currentChar = line.charAt(i);
                        if (currentChar == '+')
                            expression = Expressions.ADD;
                        if (currentChar == '-')
                            expression = Expressions.SUBTRACT;
                        if (currentChar == 'x' || currentChar == 'X' || currentChar == '*')
                            expression = Expressions.MULTIPLY;
                        if (currentChar == '/')
                            expression = Expressions.DIVIDE;
                        if (currentChar == '^')
                            expression = Expressions.ROOT;
                        if (currentChar == 's') {
                            try {
                                if (line.charAt(i + 1) == 'q' &&
                                        line.charAt(i + 2) == 'r' &&
                                        line.charAt(i + 3) == 't') {
                                    expression = Expressions.SQUARE_ROOT;
                                }
                            } catch (StringIndexOutOfBoundsException e) {
                                expression = Expressions.DEFAULT;
                            }
                        }

                        if (expression != Expressions.SQUARE_ROOT) {
                            line = line.replace(line.charAt(i), '&');
                        }
                        break;

                    }
                }

                int numOne = 0;
                int numTwo = 0;
                if (expression != Expressions.SQUARE_ROOT) {
                    lineArray = line.split("&");
                    numOne = Integer.parseInt(lineArray[0]);
                    numTwo = Integer.parseInt(lineArray[1]);
                } else {
                    // Square root, only one number
                    StringBuilder numbers = new StringBuilder();
                    for (int i = 0; i < line.length(); i++) {
                        if (Character.isDigit(line.charAt(i))) {
                            numbers.append(line.charAt(i));
                        }
                    }
                    numOne = Integer.parseInt(numbers.toString());
                }

                switch (expression) {
                    case ADD:
                        System.out.println(numOne + numTwo);
                        break;
                    case SUBTRACT:
                        System.out.println(numOne - numTwo);
                        break;
                    case MULTIPLY:
                        System.out.println(numOne * numTwo);
                        break;
                    case DIVIDE:
                        System.out.println(numOne / numTwo);
                        break;
                    case ROOT:
                        System.out.println(Math.pow(numOne, numTwo));
                        break;
                    case SQUARE_ROOT:
                        System.out.println(Math.sqrt(numOne));
                        break;
                    case DEFAULT:
                        default:
                            System.out.println("Invalid line!");
                }

                line = bfr.readLine();
            }

        } catch (IOException e) {
            // File should certainly exist by now
        }
    }

    public static File getFile() {
        var scan = new Scanner(System.in);
        System.out.println("Enter the input file name: ");
        boolean invalidInputLoop = false;
        boolean invalidInput = false;
        String input;
        do {
            System.out.print(invalidInputLoop ? "Incorrect input! Try again...\n" : "");
            invalidInput = false;
            try {
                input = scan.nextLine();
            } catch (InputMismatchException e) {
                input = "";
                invalidInput = true;
            }
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == '.') {
                    if (i == 0) {
                        // First digit
                        invalidInput = true;
                    }
                    if (i != (input.length() - 1)) {
                        // Ensures that there is a character following the dot
                        if (input.charAt(i + 1) == '.') {
                            // Two dots follow each other
                            invalidInput = true;
                        }
                    } else {
                        // This is the final character, meaning there is no file extension
                        invalidInput = true;
                    }
                }
            }
            invalidInputLoop = true;
        } while (invalidInput);
        // The file is valid
        return new File(input);
    }

    public static void main(String[] args) {
        evaluateExpression(getFile());
    }

}
