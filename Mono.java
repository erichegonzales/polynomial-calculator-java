
/**
 * Subclass to represent a mononomial, e.g. 3.5x^4
 *
 * @author Eriche Gonzales
 * @version CMPU-102 Polynomial Calculator
 * @date May 3, 2022
 */
public class Mono
{
    // instance fields
    /**
     * represents the coefficient of a polynomial
     */
    double coef;

    /**
     * represents the exponent of a polynomial
     */
    int exp;

    /**
     * Creates a new monomial
     * 
     * @param coef the single term's coefficient.
     * @param exp the single term's exponent.
     * @return the monomial created.
     */
    public Mono(double coef, int exp){
        this.coef = coef;
        this.exp = exp;
    }
}
