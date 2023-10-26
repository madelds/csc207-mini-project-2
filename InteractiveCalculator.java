import java.util.Scanner;

/**
 * A simple interactive calculator program that allows users to perform
 * fractional arithmetic operations and store results in registers.
 * @author Madel Sibal
 * September 17, 2023
 */
public class InteractiveCalculator {
    public static void main(String[] args) {
        BFCalculator calculator = new BFCalculator();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Interactive Fraction Calculator");
        System.out.println("Enter expressions or commands (e.g., 'store A', 'quit'):");

        boolean isRunning = true;
        while (isRunning) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                isRunning = false;
                System.out.println("Calculator terminated.");
            } else {
                try {
                    if (input.toLowerCase().startsWith("store ")) {
                        String[] parts = input.split("\\s+");
                        if (parts.length == 2 && parts[1].length() == 1) {
                            char register = parts[1].charAt(0);
                            calculator.store(String.valueOf(register));
                            System.out.println("Value " + String.valueOf(register) + " stored.");
                        } else {
                            throw new IllegalArgumentException("Invalid store command format");
                        }
                    } else {
                        if (!isValidExpression(input)) {
                            throw new IllegalArgumentException("Invalid expression format");
                        }
                        
                        BigFraction result = calculator.evaluate(input);
                        System.out.println("Result: " + result);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + e.getMessage());
                } catch (IllegalStateException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        scanner.close();
    }
    
    /**
     * Helper method to check if an expression is valid, ensuring there are no
     * consecutive numbers or operators.
     */
    private static boolean isValidExpression(String input) {
        // Split the expression into tokens
        String[] tokens = input.split("\\s+");
        for (int i = 0; i < tokens.length - 1; i++) {
            String currentToken = tokens[i];
            String nextToken = tokens[i + 1];
            
            if ((isNumber(currentToken) && isNumber(nextToken)) || 
                (isOperator(currentToken) && isOperator(nextToken))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Helper method to check if a string represents a number.
     */
    private static boolean isNumber(String str) {
        return str.matches("-?\\d+(/\\d+)?");
    }
    
    /**
     * Helper method to check if a string represents an operator (+, -, *, /).
     */
    private static boolean isOperator(String str) {
        return str.matches("[+\\-*/]");
    }
}
