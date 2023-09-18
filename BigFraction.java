import java.math.BigInteger;

/**
 * A simple implementation of Fractions.
 * 
 * @author Samuel A. Rebelsky
 * @author Madel Sibal
 * @version 1.2 of August 2023
 */
public class BigFraction {
  // +------------------+---------------------------------------------
  // | Design Decisions |
  // +------------------+
  /*
   * (1) Denominators are always positive. Therefore, negative fractions are represented 
   * with a negative numerator. Similarly, if a fraction has a negative numerator, it 
   * is negative.
   * 
   * (2) Fractions are not necessarily stored in simplified form. To obtain a fraction 
   * in simplified form, one must call the `simplify` method.
   */

  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  /** The numerator of the fraction. Can be positive, zero or negative. */
  BigInteger num;

  /** The denominator of the fraction. Must be non-negative. */
  BigInteger denom;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

    /**
     * Build a new fraction with (BigInteger) numerator num and 
     * (Big Integer) denominator denom.
     * The fraction is simplified.
     */
    public BigFraction(BigInteger num, BigInteger denom) {
      BigInteger gcd = num.gcd(denom); // Calculate the greatest common divisor
      this.num = num.divide(gcd);      // Simplify the numerator
      this.denom = denom.divide(gcd);  // Simplify the denominator
  }

  /**
   * Build a new fraction with (int) numerator num and (int) denominator denom.
   * The fraction is simplified.
   */
  public BigFraction(int num, int denom) {
      this.num = BigInteger.valueOf(num);
      this.denom = BigInteger.valueOf(denom);
      BigInteger gcd = this.num.gcd(this.denom); // Calculate the greatest common divisor
      this.num = this.num.divide(gcd);           // Simplify the numerator
      this.denom = this.denom.divide(gcd);       // Simplify the denominator
  }

  /**
   * Build a new fraction by parsing a string.
   * The fraction is simplified.
   */
  public BigFraction(String str) {
    int slashIndex = str.indexOf('/');
    if (slashIndex == -1) {
        throw new IllegalArgumentException("Invalid fraction format: " + str);
    }

    String numeratorStr = str.substring(0, slashIndex);
    String denominatorStr = str.substring(slashIndex + 1);

    this.num = new BigInteger(numeratorStr);
    this.denom = new BigInteger(denominatorStr);

    BigInteger gcd = this.num.gcd(this.denom); // Calculate the greatest common divisor
    this.num = this.num.divide(gcd);           // Simplify the numerator
    this.denom = this.denom.divide(gcd);       // Simplify the denominator

    if (this.denom.compareTo(BigInteger.ZERO) <= 0) {
        throw new IllegalArgumentException("Denominator must be positive: " + str);
    }
}

  /**
   * Build a new fraction with a (BigInteger) whole number num.
   * The fraction is simplified.
   */
  public BigFraction(BigInteger num) {
    this.num = num;
    this.denom = BigInteger.ONE;
  }

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Express this fraction as a double.
   */
  public double doubleValue() {
    return this.num.doubleValue() / this.denom.doubleValue();
  } // doubleValue()

  /**
   * Add the fraction `addMe` to this fraction.
   */
  public BigFraction add(BigFraction addMe) {
    BigInteger resultNumerator;
    BigInteger resultDenominator;

    // The denominator of the result is the
    // product of this object's denominator
    // and addMe's denominator
    resultDenominator = this.denom.multiply(addMe.denom);
    // The numerator is more complicated
    resultNumerator = (this.num.multiply(addMe.denom)).add(addMe.num.multiply(this.denom));

    // Return the computed value
    return new BigFraction(resultNumerator, resultDenominator);
  }// add(Fraction)

  /**
   * Get the denominator of this fraction.
   */
  public BigInteger denominator() {
    return this.denom;
  } // denominator()
  
  /**
   * Get the numerator of this fraction.
   */
  public BigInteger numerator() {
    return this.num;
  } // numerator()
  
  /**
   * Convert this fraction to a string for ease of printing.
   */
  public String toString() {
    // Special case: It's zero
    if (this.num.equals(BigInteger.ZERO)) {
        return "0";
    } // If it's zero:
    // Check if the denominator is 1, then return the numerator as a whole number
    if (this.denom.equals(BigInteger.ONE)) {
        return this.num.toString();
    }
    
    // Put together the string representation of the numerator,
    // a slash, and the string representation of the denominator
    return this.num + "/" + this.denom;
  } // toString()


  /**
   * Multiply this fraction by another fraction.
   */
  public BigFraction multiply(BigFraction other) {
    BigInteger resultNumerator = this.num.multiply(other.num);
    BigInteger resultDenominator = this.denom.multiply(other.denom);

    // Return the computed value
    return new BigFraction(resultNumerator, resultDenominator);
  }

  /**
   * Divide this fraction by another fraction.
   */
  public BigFraction divide(BigFraction other) {
    if (other.num.equals(BigInteger.ZERO)) {
        throw new IllegalArgumentException("Division by zero is not allowed.");
    }
    BigInteger resultNumerator = this.num.multiply(other.denom);
    BigInteger resultDenominator = this.denom.multiply(other.num);
    // Return the computed value
    return new BigFraction(resultNumerator, resultDenominator);
}

  /**
   * Subtract another fraction from this fraction.
   */
  public BigFraction subtract(BigFraction subtrahend) {
    BigInteger resultNumerator = (this.num.multiply(subtrahend.denom)).subtract(subtrahend.num.multiply(this.denom));
    BigInteger resultDenominator = this.denom.multiply(subtrahend.denom);

    // Return the computed value
    return new BigFraction(resultNumerator, resultDenominator);
  }

} // class Fraction