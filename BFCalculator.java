import java.util.*;
import java.math.BigInteger;

/**
 * A calculator that performs operations with fractions and stores 
 * results in registers.
 * @author Madel Sibal
 * September 17, 2023
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
     */
    public BigFraction evaluate(String exp) {
        try {
            if (exp.matches("[a-zA-Z]+")) {
                char variable = exp.charAt(0);
                BigFraction result = registers.get(variable);
                if (result == null) {
                    throw new IllegalArgumentException("Variable not found: " + variable);
                }
                lastResult = result;
                return result;
            } else {
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
        private String[] tokens;
        private int currentTokenIndex;

        /**
         * Constructs an ExpressionParser with the given expression.
         */
        public ExpressionParser(String exp) {
            tokens = tokenize(exp); 
            currentTokenIndex = 0; 
        }

        /**
         * Parses and evaluates the expression.
         */
        public BigFraction parse() {
            return parseAdditionSubtraction();
        }

        /**
         * Tokenizes the given expression string by splitting it into an array of strings
         * based on whitespace characters.
         */
        private String[] tokenize(String exp) {
            return exp.split("\\s+"); // Split the expression
        }

        /**
         * Parses addition and subtraction operations within the expression.
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
