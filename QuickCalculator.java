/**
 * A simple command-line calculator program for evaluating mathematical expressions
 * and storing variables using the BFCalculator.
 */
public class QuickCalculator {
    public static void main(String[] args) {
        // Create a new instance of the BFCalculator for performing calculations
        BFCalculator calculator = new BFCalculator();

        // Iterate through the command-line arguments provided
        for (String arg : args) {
            try {
              // Check if the argument starts with "STORE"
                if (arg.startsWith("STORE")) {
                    calculator.store(arg); // Store the variable using the BFCalculator
                } else {
                    // Evaluate the expression using the BFCalculator
                    BigFraction result = calculator.evaluate(arg);
                    // Check if a valid result was obtained
                    if (result != null) {
                        // Print the expression and its result
                        System.out.println(arg + " = " + result);
                    }
                }
            } catch (Exception e) {
                // Handle exceptions
                e.printStackTrace();
            }
        }
    }
}
