import java.util.*;

/**
 * Class to sort monomials in ascending order exponent.
 *
 * @author Eriche Gonzales
 * @version CMPU-102 Polynomial Calculator
 * @date May 3, 2022
 */
class SortByExp implements Comparator<Mono>
{
    /**
     * Sorts monomials in ascending order exponent.
     * 
     * @param a monomial.
     * @param a monomial.
     * @return the difference of the monomial exponents.
     */
    public int compare(Mono a, Mono b)
    {
        return b.exp - a.exp;
    }
}