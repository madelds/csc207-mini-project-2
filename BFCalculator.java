import java.util.*;
import java.math.BigInteger;

/**
 * A calculator that performs operations with fractions and stores 
 * results in registers.
 */
public class BFCalculator {
    private BigFraction lastResult; // Stores the last calculated result
    private Map<Character, BigFraction> registers; // Stores fractions in registers
    private static final BigFraction ZERO = new BigFraction(BigInteger.ZERO, BigInteger.ONE);
    
    /**
     * Constructs a new BFCalculator with initial values.
     */
    public BFCalculator() {
        this.lastResult = null;
        this.registers = new HashMap<>();
    }

    /**
     * Evaluates a given expression or retrieves a value from a register.
     *
     * @param exp The expression or variable to evaluate.
     * @return The result of the evaluation.
     * @throws IllegalArgumentException If the expression or variable is invalid.
     */
    public BigFraction evaluate(String exp) {
        try {
            if (exp.matches("[a-zA-Z]+")) {
              // If the expression is a single variable, look it up in registers
                char variable = exp.charAt(0);
                BigFraction result = registers.get(variable);
                if (result == null) {
                    throw new IllegalArgumentException("Variable not found: " + variable);
                }
                lastResult = result;
                return result;
            } else {
              // Parse and evaluate the expression using ExpressionParser
                ExpressionParser parser = new ExpressionParser(exp);
                BigFraction result = parser.parse();
                lastResult = result;
                return result;
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid expression: " + exp);
        }
    }

    /**
     * Stores the last calculated result in a register.
     *
     * @param arg A string containing the register name (e.g., "A").
     * @throws IllegalStateException If there is no result to store.
     */
    public void store(String arg) {
        if (lastResult == null) {
            throw new IllegalStateException("No result to store.");
        }
        char variable = arg.charAt(arg.length() - 1);
        registers.put(variable, lastResult);
    }

    /**
     * A private class for parsing and evaluating mathematical expressions.
     */
    private class ExpressionParser {
        private String[] tokens; // Tokens obtained by splitting the expression
        private int currentTokenIndex; // Index of the current token being processed

        /**
         * Constructs an ExpressionParser with the given expression.
         *
         * @param exp The mathematical expression to parse.
         */
        public ExpressionParser(String exp) {
            tokens = tokenize(exp); // Tokenize the input expression
            currentTokenIndex = 0; // Initialize the current token index
        }

        /**
         * Parses and evaluates the expression.
         *
         * @return The result of the expression evaluation.
         */
        public BigFraction parse() {
            return parseAdditionSubtraction();
        }


        /**
         * Tokenizes the given expression string by splitting it into an array of strings
         * based on whitespace characters.
         *
         * @param exp The mathematical expression to tokenize.
         * @return An array of strings representing individual tokens from the expression.
         */
        private String[] tokenize(String exp) {
            return exp.split("\\s+"); // Split the expression
        }

        /**
         * Parses addition and subtraction operations within the expression.
         *
         * @return The result of addition and subtraction operations.
         */
        private BigFraction parseAdditionSubtraction() {
            BigFraction left = parseMultiplicationDivision();
            while (currentTokenIndex < tokens.length) {
                String operator = tokens[currentTokenIndex];
                if (operator.equals("+") || operator.equals("-")) {
                    currentTokenIndex++;
                    BigFraction right = parseMultiplicationDivision();
                    if (operator.equals("+")) {
                        left = left.add(right);
                    } else {
                        left = left.subtract(right);
                    }
                } else {
                    break;
                }
            }
            return left;
        }

        /**
         * Parses multiplication and division operations within the expression.
         *
         * @return The result of multiplication and division operations.
         * @throws ArithmeticException If division by zero is encountered.
         */
        private BigFraction parseMultiplicationDivision() {
            BigFraction left = parseOperand();
            while (currentTokenIndex < tokens.length) {
                String operator = tokens[currentTokenIndex];
                if (operator.equals("*") || operator.equals("/")) {
                    currentTokenIndex++;
                    BigFraction right = parseOperand();
                    if (operator.equals("*")) {
                        left = left.multiply(right);
                    } else {
                        if (right.equals(ZERO)) {
                            throw new ArithmeticException("Division by zero is not allowed.");
                        }
                        left = left.divide(right);
                    }
                } else {
                    break;
                }
            }
            return left;
        }
        /**
         * Parses an operand within the expression.
         *
         * @return The parsed operand as a BigFraction.
         * @throws IllegalArgumentException If an invalid operand is encountered.
         */
        private BigFraction parseOperand() {
            String token = tokens[currentTokenIndex];
            if (token.matches("[+\\-*/]")) {
                throw new IllegalArgumentException("Invalid operand: " + token);
            }
            currentTokenIndex++;
            if (token.length() == 1 && Character.isLetter(token.charAt(0))) {
                char variable = token.charAt(0);
                BigFraction value = registers.get(variable);
                if (value != null) {
                    return value;
                } else {
                    throw new IllegalArgumentException("Variable not found: " + variable);
                }
            } else {
                try {
                    return new BigFraction(token);
                } catch (IllegalArgumentException e) {
                    try {
                        BigInteger wholeNumber = new BigInteger(token);
                        return new BigFraction(wholeNumber);
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid operand: " + token);
                    }
                }
            }
        }
    }

}
