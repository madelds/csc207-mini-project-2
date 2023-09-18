public class QuickCalculator {
    public static void main(String[] args) {
        BFCalculator calculator = new BFCalculator();

        for (String arg : args) {
            try {
                if (arg.startsWith("STORE")) {
                    calculator.store(arg);
                } else {
                    BigFraction result = calculator.evaluate(arg);
                    if (result != null) {
                        System.out.println(arg + " = " + result);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
