import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * A class with unit tests for the Poly polynomial class.
 * 
 * We recommend you tackle them (i.e. try to make them pass) in the 
 * order they appear in this file.
 *
 * @author Anna Gommerstadt & Rui Meireles & Eriche Gonzales
 * @version CMPU-102 Polynomial Calculator
 * @date May 3, 2022
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PolyTest{

    /**
     * Tests creation and equality testing of single-term polynomials.
     */
    @Test
    public void test1ConsAndEquals(){

        Poly p1 = new Poly(2.5, 2);
        Poly p2 = new Poly(2.5, 2);
        assertEquals("2.5x^2 != 2.5x^2", p1, p2);

        p2 = new Poly(2.5, 1);
        assertNotEquals("2.5x^2 == 2.5x", p1, p2);

        p2 = new Poly(3, 2);
        assertNotEquals("2.5x^2 == 3x^2", p1, p2);

        p2 = new Poly(4, 0);
        assertNotEquals("2.5x^2 == 4", p1, p2);

        p1 = new Poly(0, 4);
        p2 = new Poly(0, 0);
        assertEquals("0x^4 != 0", p1, p2);

        new Poly(2147483647, 2147483647); // designed to break space-inefficient array-based implementations!
    }

    /**
     * Tests polynomial addition and subtraction.
     * Assumes the following methods are correct: constructor, equals().
     */
    @Test
    public void test2AddAndSub(){

        // does add even do anything?
        Poly p1 = new Poly(2, 2); // p1 = 2x^2
        Poly p2 = new Poly(3, 0); // p2 = 3
        Poly p3 = p1.add(p2);     // p3 = 2x^2 + 3
        assertNotEquals("2x^2 + 3 == 2x^2", p3, p1);

        // testing monomials with same exponent
        p1 = new Poly(1, 1); // p1 = x
        p2 = new Poly(3, 1); // p2 = 3x
        p3 = p1.add(p2);     // p3 = x + 3x
        Poly p4 = new Poly(4, 1); // p4 = 4x
        assertEquals("3x + x != 4x", p4, p3);

        p4 = p3.subtract(p2); // p4 = 4x - 3x
        assertEquals("4x - 3x != x", p1, p4);

        // testing monomials with different exponents
        p1 = new Poly(2, 1); // p1 = 2x
        p2 = new Poly(3, 0); // p2 = 3
        p3 = p1.add(p2); // p3 = 2x + 3
        p4 = p3.subtract(p2); // p3 = 2x
        assertEquals("2x + 3 - 2x != 2x", p1, p4);

        // subtract poly from itself should yield zero result
        p4 = p3.subtract(p3); // p3 = 2x + 3
        p1 = new Poly(0, 0);  // p1 = 0
        assertEquals("2x + 3 - 2x + 3 != 0", p1, p4);

        p1 = new Poly(1, 2); // p1 = x^2
        p2 = new Poly(2, 1); // p2 = 2x
        p1 = p1.add(p2);     // p1 = x^2 + 2x

        p2 = new Poly(3, 1); // p2 = 3x
        p3 = new Poly(4, 0); // p3 = 4
        p2 = p2.add(p3);     // p2 = 3x + 4

        p3 = new Poly(1, 2);         // p3 = x^2
        p3 = p3.add(new Poly(5, 1)); // p3 = x^2 + 5x
        p3 = p3.add(new Poly(4, 0)); // p3 = x^2 + 5x + 4

        p4 = p1.add(p2); // p4 = x^2 + 5x + 4
        assertEquals("(x^2 + 2x) + (3x + 4) != x^2 + 5x + 4", p3, p4);
    }

    /**
     * Tests polynomial multiplication.
     * Assumes the following methods are correct: constructor, equals(), add(). 
     */
    @Test
    public void test3Mul() {

        // multiply by zero should yield zero result
        Poly z0 = new Poly(0,0); // z0 = 0
        Poly z1 = new Poly(2, 2); // z1 = 2x^2
        Poly z2 = z1.multiply(z0); // z2 = 0

        // single term by 0
        assertEquals("2x^2 * 0 != 0", z0, z2);
        // zero by single term
        z2 = z0.multiply(z1); // z2 = 0
        assertEquals("0 * 2x^2 != 0", z0, z2);

        // multiple terms by zero
        z1 = z1.add(new Poly(1, 1)); // z1 = 2x^2 + x
        z2 = z1.multiply(z0); // z2 = 0
        assertEquals("2x^2 + x * 0 != 0", z0, z2);
        // zero by multiple terms
        z2 = z0.multiply(z1); // z2 = 0
        assertEquals("0 * 2x^2 + x != 0", z0, z2);

        // multiply multi-term poly by single term poly
        Poly p1 = new Poly(1,2); // p1 = x^2
        Poly p2 = new Poly(5,0); // p2 = 5
        Poly p3 = p1.add(p2); // p3 = x^2 + 5

        Poly p4 = new Poly(2,1); // p4 = 2x
        Poly p5 = p3.multiply(p4); // p5 = 2x^3 + 10x

        Poly p6 = new Poly(2,3); // p6 = 2x^3
        Poly p7 = new Poly(10,1); // p7 = 10x
        Poly res = p6.add(p7); // res = 2x^3 + 10x
        assertEquals("(x^2 + 5) * 2x != 2x^3 + 10x", res, p5); 

        Poly p8 = p1.add(p7);  // p8 = x^2 + 10x 
        assertNotEquals("(x^2 + 5) * 2x) == x^2 + 10x", p8, p5); 

        // multiply multi-term poly by another multi-term poly
        Poly p11 = new Poly(1,2); // p1 = x^2
        Poly p12 = new Poly(5,1); // p2 = 5x
        Poly p13 = new Poly(3,0); // p3 = 3
        Poly p14 = p11.add(p12).add(p13); // p4 = x^2 + 5x + 3
        Poly p15 = new Poly(7,1); // p5 = 7x
        Poly p16 = p15.add(p13); // p6 = 7x + 3
        Poly p17 = new Poly(7,3); // p7 = 7x^3
        Poly p18 = new Poly(38,2); // p8 = 38x^2
        Poly p19 = new Poly(36,1); // p9 = 36x
        Poly p20 = new Poly(9,0); // p10 = 9
        Poly mult = p16.multiply(p14);
        Poly res2 = p17.add(p18).add(p19).add(p20); // res = 7x^3 + 38x^2 + 36x + 9
        assertEquals("(x^2 + 5x + 3) * (7x + 3) != 7x^3 + 38x^2 + 36x + 9", res2, mult);

        // multiply by -1
        Poly s1 = new Poly(3, 2); // s1 = 3x^2
        s1 = s1.add(new Poly(-4, 0)); // s1 = 3x^2 - 4
        Poly s2 = s1.multiply(new Poly(-1, 0)); // s1 = -3x^2 - 4
        s1 = s1.add(s2); // s1 = 0
        assertEquals("(3x^2 - 4 * -1) + 3x^2 - 4 != 0", z0, s1);

        // tests merging of same-exponent terms
        Poly t1 = new Poly(3, 3); // t1 = 3x^3
        t1.add(new Poly(3,1)); // t1 = 3x^3 + 3x
        t1.add(new Poly(3,0)); // t1 = 3x^3 + 3x + 3
        Poly t2 = new Poly(3, 3); // t2 = 3x^3
        t2.add(new Poly(3, 2)); // t2 = 3x^3 + 3x^2
        t2.add(new Poly(1,1)); // t2 = 3x^3 + 3x^2 + x
        Poly t3 = t1.multiply(t2); // t3 = 9x^6 + 9x^5 + 12x^4 + 18x^3 + 12x^2 + 3x

        // build result to test against
        Poly t4 = new Poly(9, 6); // t4 = 9x^6
        t4.add(new Poly(9, 5)); // t4 = 9x^6 + 9x^5
        t4.add(new Poly(12, 4)); // t4 = 9x^6 + 9x^5 + 12x^4
        t4.add(new Poly(18, 3)); // t4 = 9x^6 + 9x^5 + 12x^4 + 18x^3
        t4.add(new Poly(12, 2)); // t4 = 9x^6 + 9x^5 + 12x^4 + 18x^3 + 12x^2
        t4.add(new Poly(3, 1)); // t4 = 9x^6 + 9x^5 + 12x^4 + 18x^3 + 12 x^2 + 3x

        // compare outcome against expected value
        assertEquals("(3x^3 + 3x + 3) * (3x^3 + 3x^2 + x) != 9x^6 + 9x^5 + 12x^4 + 18x^3 + 12x^2 + 3x", t4, t3);
    }

    /**
     * Tests polynomial division.
     * Assumes the following methods are correct: constructor, equals(), add(), multiply().
     */
    @Test
    public void test5MulDiv() {
        // zero dividend divided by anything
        Poly z1 = new Poly(0,0); // z1 = 0
        Poly z2 = z1.divide(new Poly(1,1)); // z2 = 0 / x
        assertEquals("0 / x != 0", z1, z2);

        // divide by zero
        z1 = new Poly(1,2); // z1 = x^2
        z2 = z1.divide(new Poly(0,0)); // z2 = x^2 / 0
        assertEquals("x^2 / 0 != null", null, z2);

        // divide by one
        Poly o1 = new Poly(3,2); // o1 = 3x^2
        o1 = o1.add(new Poly(4, 0)); // o1 = 3x^2 + 4
        Poly o2 = o1.divide(new Poly(1,0)); // o2 = (3x^2 + 4) / 1
        assertEquals("(3x^2 + 4) / 1 != 3x^2 + 4", o1, o2);

        // simple indivisible test
        o1 = new Poly(3,2); // o1 = 3x^2
        o2 = o1.divide(new Poly(9,5)); // o2 = 9x^5 / 3x^2
        assertEquals("9x^5 / 3x^2 != null", null, o2);

        Poly p1 = new Poly(1,2); // p1 = x^2
        Poly p2 = new Poly(-9,0); // p2 = -9
        Poly p3 = p1.add(p2); // p3 = x^2 - 9
        Poly p4 = new Poly(1,1); // p4 = x
        Poly p5 = new Poly(-3,0); // p5 = -3
        Poly p6 = p4.add(p5); // p6 = x - 3
        Poly q = p3.divide(p6); // q = x + 3
        Poly m = q.multiply(p6); // m = x^2 - 9
        Poly u = p1.divide(q);  // u = null
        assertEquals("(x^2 - 9) / (x - 3) != x + 3", p3, m);
        assertEquals("(x^2 - 9) / x != null", null, u);

        Poly p11 = new Poly(9,0);  // p1 = 9
        Poly p12 = new Poly(6,1);  // p2 = 6x
        Poly p13 = new Poly(1,2);  // p3 = x^2
        Poly p14 = p11.add(p12).add(p13); // p4 = 9 + 6x + x^2
        Poly p15 = new Poly(3,0); // p5 = 3
        Poly p16 = new Poly(1,1); // p6 = x
        Poly p17 = p15.add(p16); // p7 = x + 3
        Poly q2 = p14.divide(p17); // q = x - 3
        Poly m2 = p17.multiply(q2); // m = x^2 + 6x + 9
        assertEquals("(9 + 6x + x^2) / (x + 3) != x - 3", p14, m2);
        Poly u2 = p6.divide(p17); // u = null
        assertEquals("x/3 != null", null, u2);
    }

    /**
     * A helper method to check wheter a polynomial's toString() returns a string
     * in the correct format.
     * 
     * @param polyStr the string that should be returned by polynomial toString().
     * @param p the polynomial to be tested.
     * @return an error message.
     */
    private void testToStringHelper(String polyStr, Poly p){
        String emsg = String.format("%s toString() incorrect", polyStr);
        assertEquals(emsg, polyStr, p.toString());
    }

    /**
     * Tests polynomial toString().
     * Assumes the following methods are correct: constructor, add().
     */
    @Test
    public void test5ToString() {

        // printing zero polynomial
        Poly p1 = new Poly(0,0); // p1 = 0
        String s = "0.0";
        this.testToStringHelper(s, p1);

        // printing polynomial 1
        p1 = new Poly(1,0); // p1 = 0
        s = "1.0";
        this.testToStringHelper(s, p1);

        // printing polynomial -1
        p1 = new Poly(-1,0); // p1 = 0
        s = "-1.0";
        this.testToStringHelper(s, p1);

        // printing polynomials with different kinds of terms
        p1 = new Poly(2.0, 4); // p1 = 2.0x^4
        s = "2.0x^4";
        this.testToStringHelper(s, p1);

        p1 = p1.add(new Poly(3.4, 3)); // p1 = 2.0x^4 + 3.4x^3
        s += " + 3.4x^3";
        this.testToStringHelper(s, p1);

        p1 = p1.add(new Poly(1.0, 2)); // p1 = 2.0x^4 + 3.4x^3 + x^2
        s += " + x^2";
        this.testToStringHelper(s, p1);

        p1 = p1.add(new Poly(1.0, 1)); // p1 = 2.0x^4 + 3.4x^3 + x^2 + x
        s += " + x";
        this.testToStringHelper(s, p1);

        p1 = p1.add(new Poly(4, 0)); // p1 = 2.0x^4 + 3.4x^3 + x^2 + x + 4.0
        s += " + 4.0";
        this.testToStringHelper(s, p1);

        // adding out of order to test sorting
        p1 = p1.add(new Poly(50, 40)); // p1 = 50.0x^40 + 2.0x^4 + 3.4x^3 + x^2 + x + 4.0
        s = "50.0x^40 + " + s; 
        this.testToStringHelper(s, p1);

        // adding zero coefficient terms shouldn't affect printing
        p1 = p1.add(new Poly(0, 40)); // p1 = 50.0x^40 + 2.0x^4 + 3.4x^3 + x^2 + x + 4
        this.testToStringHelper(s, p1);

        // printing polynomial with negative coefficients 
        p1 = new Poly(-1, 3); // p1 = -x^3
        s = "-x^3";
        this.testToStringHelper(s, p1);

        p1 = p1.add(new Poly(-2.0, 2)); // p1 = -x^3 - 2.0x^2
        s += " - 2.0x^2";
        this.testToStringHelper(s, p1);

        p1 = p1.add(new Poly(1, 1)); // p1 = -x^3 - 2.0x^2 + x
        String olds = s;
        s += " + x";
        this.testToStringHelper(s, p1);

        // killing a term should make it disappear
        p1 = p1.add(new Poly(-1, 1)); // p1 = -x^3 - 2.0x^2 
        this.testToStringHelper(olds, p1);
    }

    /**
     * Tests addiction and subtraction operations of 0.0's and polynomials.
     * Assumes the following methods are correct: constructor, equals().
     */
    @Test
    public void test6AddSubZeros(){

        // testing the addition of 0.0 to a polynomial
        Poly p1 = new Poly(3, 2); // p1 = 3.0x^2
        Poly p2 = p1.add(new Poly(0, 0)); // p2 = 3.0x^2, actual
        Poly p3 = new Poly(3, 2); // p3 = 3.0x^2, expected
        assertEquals("3.0x^2 == 3.0x^2", p3, p2);

        // testing the addition of a polynomial to 0.0
        p1 = new Poly(0, 0); // p1 = 0.0
        p2 = p1.add(new Poly (5, 3)); // p2 = 5.0x^3, actual
        p3 = new Poly(5, 3); // p3 = 5.0x^3, expected
        assertEquals("5.0x^3 == 5.0x^3", p3, p2);

        // testing the addition of 0.0 to 0.0
        p1 = new Poly(0, 0); // p1 = 0.0, expected
        p2 = p1.add(p1); // p2 = 0.0, actual
        assertEquals("0.0 == 0.0", p1, p2);

        // testing the subtraction of 0.0 from a polynomial
        p1 = new Poly(9, 5); // p1 = 9.0x^5
        p2 = p1.subtract(new Poly(0, 0)); // p2 = 9.0x^5, actual
        p3 = new Poly(9, 5); // p3 = 9.0x^5, expected
        assertEquals("9.0x^5 == 9.0x^5", p3, p2);

        // testing the subtraction of a polynomial from 0.0
        p1 = new Poly(0, 0); // p1 = 0.0
        p2 = p1.subtract(new Poly(4, 8)); // p2 = -4.0x^8, actual
        p3 = new Poly (4, 8); // p3 = 4.0x^8, expected
        assertNotEquals("4.0x^8 == -4.0x^8", p3, p2);

        // testing the subtraction of 0.0 from 0.0
        p1 = new Poly(0, 0); // p1 = 0.0, expected
        p2 = p1.subtract(p1); // p2 = 0.0, actual
        assertEquals("0.0 - 0.0 == 0.0", p1, p2);
    }

    /**
     * Tests polynomial multiplication by operating with multiple-term polynomials.
     * Assumes the following methods are correct: constructor, equals(), add().
     */
    @Test
    public void test7MulLongPoly(){
        // creating single-term polynomials
        Poly p0 = new Poly(0, 1); // p0 = 0.0
        Poly p1 = new Poly(1, 2); // p1 = x^2
        Poly p2 = new Poly(2, 3); // p2 = 2.0x^3
        Poly p3 = new Poly(3, 8); // p3 = 3.0x^8
        Poly p4 = new Poly(4, 8); // p4 = 4.0x^8
        Poly p5 = new Poly(5, 3); // p5 = 5.0x^3
        Poly p6 = new Poly(1, 2); // p6 = x^2
        Poly p7 = new Poly(10, 7); // p7 = 10.0x^7
        Poly p8 = new Poly(4, 1); // p8 = 4.0x
        Poly p9 = new Poly(5, 7); // p9 = 5.0x^7
        Poly p10 = new Poly(1, 1); // p10 = x

        // creating multiple-termed polynomials
        Poly p11 = new Poly(0, 0); // p11 = 7.0x^8 + 7.0x^3 + x^2
        p11 = p11.add(p0);
        p11 = p11.add(p1);
        p11 = p11.add(p2);
        p11 = p11.add(p3);
        p11 = p11.add(p4);
        p11 = p11.add(p5);

        Poly p12 = new Poly(0, 0); // p12 = 15.0x^7 + x^2 + 5.0x
        p12 = p12.add(p6);
        p12 = p12.add(p7);
        p12 = p12.add(p8);
        p12 = p12.add(p9);
        p12 = p12.add(p10);

        // creating multiplication expected result
        Poly p13 = new Poly(105, 15); // p13 = 105.0x^15 + 112.0x^10 + 50.0x^9 + 7.0x^5 + 36.0x^4 + 5.0x^3
        p13 = p13.add(new Poly(112, 10));
        p13 = p13.add(new Poly(50, 9));
        p13 = p13.add(new Poly(7, 5));
        p13 = p13.add(new Poly(36, 4));
        p13 = p13.add(new Poly(5, 3));

        // testing multiplication actual result
        Poly p14 = p11.multiply(p12); // p14 = 105.0x^15 + 112.0x^10 + 50.0x^9 + 7.0x^5 + 36.0x^4 + 5.0x^3
        assertEquals("(7.0x^8 + 7.0x^3 + x^2) * (15.0x^7 + x^2 + 5.0x) == 105.0x^15 + 112.0x^10 + 50.0x^9 + 7.0x^5 + 36.0x^4 + 5.0x^3", p13, p14);

        // creating multiple-termed polynomials
        Poly p15 = new Poly(0, 0); // p15 = 10.0x^7 + 7.0x^3 + x^2
        p15 = p15.add(p1);
        p15 = p15.add(p2);
        p15 = p15.add(p5);
        p15 = p15.add(p7);

        Poly p16 = new Poly(0, 0); // p16 = 7.0x^8 + 5.0x^7 + 4.0x
        p16 = p16.add(p3);
        p16 = p16.add(p4);
        p16 = p16.add(p8);
        p16 = p16.add(p9);

        // creating multiplication expected result
        Poly p17 = new Poly(70, 15); // p17 = 105.0x^15 + 112.0x^10 + 50.0x^9 + 7.0x^5 + 36.0x^4 + 5.0x^3
        p17 = p17.add(new Poly(50, 14));
        p17 = p17.add(new Poly(49, 11));
        p17 = p17.add(new Poly(42, 10));
        p17 = p17.add(new Poly(5, 9));
        p17 = p17.add(new Poly(40, 8));
        p17 = p17.add(new Poly(28, 4));
        p17 = p17.add(new Poly(4, 3));

        // testing multiplication actual result
        Poly p18 = p15.multiply(p16); // p18 = 105.0x^15 + 112.0x^10 + 50.0x^9 + 7.0x^5 + 36.0x^4 + 5.0x^3
        assertEquals("(10.0x^7 + 7.0x^3 + x^2) * (7.0x^8 + 5.0x^7 + 4.0x) == 70.0x^15 + 50.0x^14 + 49.0x^11 + 42.0x^10 + 5.0x^9 + 40.0x^8 + 28.0x^4 + 4.0x^3", p17, p18);
    }

    /**
     * Tests polynomial division by operating with multiple-term polynomials.
     * Assumes the following methods are correct: constructor, equals(), add().
     */
    @Test
    public void test8DivLongPoly(){
        // creating single-term polynomials
        Poly p0 = new Poly(4, 5); // p0 = 4.0x^5
        Poly p1 = new Poly(6, 7); // p1 = 6.0x^7
        Poly p2 = new Poly(3, 5); // p2 = 3.0x^5

        // creating multiple-termed polynomials
        Poly p3 = p0.add(p1); // p3 = 6.0x^7 + 4.0x^5

        // creating division expected result
        Poly p4 = new Poly(2, 2); // p4 = 2.0x^2 + 1.3333333333333333
        p4 = p4.add(new Poly(1.3333333333333333, 0));

        // testing division actual result
        Poly p5 = p3.divide(p2); // p5 = 2.0x^2 + 1.3333333333333333
        assertEquals("(6.0x^7 + 4.0x^5) / (3.0x^5) == 2.0x^2 + 1.3333333333333333", p4, p5);

        // creating single-term polynomials
        Poly p6 = new Poly(5, 8); // p6 = 5.0x^8
        Poly p7 = new Poly(2, 3); // p7 = 2.0x^3
        Poly p8 = new Poly(1, 9); // p8 = x^9

        // creating multiple-termed polynomials
        Poly p9 = p6.add(p7); // p9 = 5.0x^8 + 2.0x^3

        // creating division expected result
        Poly p10 = null; // p10 = null

        // testing division actual result
        Poly p11 = p9.divide(p8); // p11 = null
        assertEquals("(5.0x^8 + 2.0x^3) / (x^9) == indivisible", p10, p11);

        // creating single-term polynomials
        Poly p12 = new Poly(4, 6); // p12 = 4.0x^6
        Poly p13 = new Poly(7, 8); // p13 = 7.0x^8
        Poly p14 = new Poly(6, 9); // p14 = 6.0x^9
        Poly p15 = new Poly(5, 6); // p15 = 5.0x^6

        // creating multiple-termed polynomials
        Poly p16 = p12.add(p13).add(p14); // p16 = 6.0x^9 + 7.0x^8 + 4.0x^6

        // creating division expected result
        Poly p17 = new Poly(1.2, 3); // p17 = 1.2x^3 + 1.4x^2 + 0.8
        p17 = p17.add(new Poly(1.4, 2));
        p17 = p17.add(new Poly(0.8, 0));

        // testing division actual result
        Poly p18 = p16.divide(p15); // p18 = 1.2x^3 + 1.4x^2 + 0.8
        assertEquals("(6.0x^9 + 7.0x^8 + 4.0x^6) / (5.0x^6) == 1.2x^3 + 1.4x^2 + 0.8", p17, p18);
    }

    /**
     * Tests longer polynomial operations with coefficients that have a non-zero tenth decimal place.
     * e.g. 1.2x, 3.5x^2, 8.4x^5
     * Assumes the following methods are correct: constructor, equals(), add().
     */
    @Test
    public void test9NonZeroCoef(){
        // creating single-term polynomials
        Poly p0 = new Poly(1.2, 1); // p0 = 1.2x
        Poly p1 = new Poly(2.8, 2); // p1 = 2.8x^2
        Poly p2 = new Poly(10.2, 4); // p2 = 10.2x^4
        Poly p3 = new Poly(7.1, 5); // p3 = 7.1x^5
        Poly p4 = new Poly(6.2, 2); // p4 = 6.2x^2
        Poly p5 = new Poly(3.5, 4); // p5 = 3.5x^4
        Poly p6 = new Poly(6.8, 8); // p6 = 6.8x^8
        Poly p7 = new Poly(2.4, 6); // p7 = 2.4x^6
        Poly p8 = new Poly(2.2, 4); // p8 = 2.2x^4
        Poly p9 = new Poly(1.1, 2); // p9 = 1.1x^2
        // Poly p10 = new Poly(2.2, 1); // p10 = 2.2x
        // Poly p11 = new Poly(3.3, 1); // p11 = 3.3x

        // creating multiple-termed polynomials for addition and subtraction
        Poly p12 = new Poly(0, 0); // p12 = 10.2x^4 + 2.8x^2 + 1.2x
        p12 = p12.add(p0);
        p12 = p12.add(p1);
        p12 = p12.add(p2);

        Poly p13 = new Poly(0, 0); // p13 = 7.1x^5 + 3.5x^4 + 6.2x^2
        p13 = p13.add(p3);
        p13 = p13.add(p4);
        p13 = p13.add(p5);

        // creating multiple-termed polynomials for multiplication and division
        Poly p14 = new Poly(0, 0); // p14 = 6.8x^8 + 2.4x^6 + 2.2x^4 + 1.1x^2
        p14 = p14.add(p6);
        p14 = p14.add(p7);
        p14 = p14.add(p8);
        p14 = p14.add(p9);

        Poly p15 = new Poly(2.2, 1); // p15 = 2.2x

        // creating addition expected result
        Poly p16 = new Poly(0, 0); // p16 = 7.1x^5 + 13.7x^4 + 9.0x^2 + 1.2x
        p16 = p16.add(new Poly(7.1, 5));
        p16 = p16.add(new Poly(13.7, 4));
        p16 = p16.add(new Poly(9.0, 2));
        p16 = p16.add(new Poly(1.2, 1));

        // testing addition actual result
        Poly p17 = p12.add(p13); // p17 = 7.1x^5 + 13.7x^4 + 9.0x^2 + 1.2x
        assertEquals("(10.2x^4 + 2.8x^2 + 1.2x) + (7.1x^5 + 3.5x^4 + 6.2x^2) == 7.1x^5 + 13.7x^4 + 9.0x^2 + 1.2x", p16, p17);

        // creating subtraction expected result
        Poly p18 = new Poly(0, 0); // p18 = -7.1x^5 + + 6.699999999999999x^4 - 3.4000000000000004x^2 + 1.2x
        p18 = p18.add(new Poly(-7.1, 5));
        p18 = p18.add(new Poly(6.699999999999999, 4));
        p18 = p18.add(new Poly(-3.4000000000000004, 2));
        p18 = p18.add(new Poly(1.2, 1));

        // testing subtraction actual result
        Poly p19 = p12.subtract(p13); // p19 = -7.1x^5 + + 6.699999999999999x^4 - 3.4000000000000004x^2 + 1.2x
        assertEquals("(10.2x^4 + 2.8x^2 + 1.2x) - (7.1x^5 + 3.5x^4 + 6.2x^2) == 7.1x^5 + 13.7x^4 + 9.0x^2 + 1.2x", p18, p19);

        // creating multiplication expected result
        Poly p20 = new Poly(0, 0); // p20 = 14.96x^9 + 5.28x^7 + 4.840000000000001x^5 + 2.4200000000000004x^3
        p20 = p20.add(new Poly(14.96, 9));
        p20 = p20.add(new Poly(5.28, 7));
        p20 = p20.add(new Poly(4.840000000000001, 5));
        p20 = p20.add(new Poly(2.4200000000000004, 3));

        // testing multiplication actual result
        Poly p21 = p14.multiply(p15); // p21 = 14.96x^9 + 5.28x^7 + 4.840000000000001x^5 + 2.420000000000004x^3
        assertEquals("(6.8x^8 + 2.4x^6 + 2.2x^4 + 1.1x^2) * (2.2x) == 14.96x^9 + 5.28x^7 + 4.840000000000001x^5 + 2.420000000000004x^3", p20, p21);

        // creating division expected result
        Poly p22 = new Poly(0, 0); // p22 = 3.0909090909090904x^7 + 1.0909090909090908x^5 + x^3 + 0.5x
        p22 = p22.add(new Poly(3.0909090909090904, 7));
        p22 = p22.add(new Poly(1.0909090909090908, 5));
        p22 = p22.add(new Poly(1.0, 3));
        p22 = p22.add(new Poly(0.5, 1));

        // testing division actual result
        Poly p23 = p14.divide(p15); // p23 = 3.0909090909090904x^7 + 1.0909090909090908x^5 + x^3 + 0.5x
        assertEquals("(6.8x^8 + 2.4x^6 + 2.2x^4 + 1.1x^2) / (2.2x) == 3.0909090909090904x^7 + 1.0909090909090908x^5 + x^3 + 0.5x", p22, p23);
    }

    /**
     * Tests positive and negative coefficient polynomial operations with longer polynomials.
     * Assumes the following methods are correct: constructor, equals(), add().
     */
    @Test
    public void test10PosNegCoef(){
        // creating single-term polynomials
        Poly p0 = new Poly(-2, 1); // p0 = -2.0x
        Poly p1 = new Poly(9, 2); // p1 = 9.0x^2
        Poly p2 = new Poly(-7, 4); // p2 = -7.0x^4
        Poly p3 = new Poly(3, 5); // p3 = 3.0x^5
        Poly p4 = new Poly(-6.2, 2); // p4 = -6.2x^2
        Poly p5 = new Poly(3.5, 4); // p5 = 3.5x^4
        Poly p6 = new Poly(-6, 8); // p6 = -6.0x^8
        Poly p7 = new Poly(2, 4); // p7 = 2.0x^4
        Poly p8 = new Poly(-2, 2); // p8 = -2.0x^2
        Poly p9 = new Poly(-6, 1); // p9 = -6.0x

        // creating multiple-termed polynomials for addition and subtraction
        Poly p12 = new Poly(0, 0); // p12 = -7.0x^4 + 9.0x^2 - 2.0x
        p12 = p12.add(p0);
        p12 = p12.add(p1);
        p12 = p12.add(p2);

        Poly p13 = new Poly(0, 0); // p13 = 3.0x^5 + 3.5x^4 - 6.2x^2
        p13 = p13.add(p3);
        p13 = p13.add(p4);
        p13 = p13.add(p5);

        // creating multiple-termed polynomials for multiplication and division
        Poly p14 = new Poly(0, 0); // p14 = -6.0x^8 + 2.0x^4 - 2.0x^2 - 6.0x
        p14 = p14.add(p6);
        p14 = p14.add(p7);
        p14 = p14.add(p8);
        p14 = p14.add(p9);

        Poly p15 = new Poly(2, 1); // p15 = 2x

        // creating addition expected result
        Poly p16 = new Poly(0, 0); // p16 = 3.0x^5 - 3.5x^4 + 2.8x^2 - 2.0x
        p16 = p16.add(new Poly(3, 5));
        p16 = p16.add(new Poly(-3.5, 4));
        p16 = p16.add(new Poly(2.8, 2));
        p16 = p16.add(new Poly(-2, 1));

        // testing addition actual result
        Poly p17 = p12.add(p13); // p17 = 3.0x^5 - 3.5x^4 + 2.8x^2 - 2.0x
        assertEquals("(-7.0x^4 + 9.0x^2 - 2.0x) + (3.0x^5 + 3.5x^4 - 6.2x^2) == 3.0x^5 - 3.5x^4 + 2.8x^2 - 2.0x", p16, p17);

        // creating subtraction expected result
        Poly p18 = new Poly(0, 0); // p18 = -3.0x^5 - 10.5x^4 + 15.2x^2 - 2.0x
        p18 = p18.add(new Poly(-3, 5));
        p18 = p18.add(new Poly(-10.5, 4));
        p18 = p18.add(new Poly(15.2, 2));
        p18 = p18.add(new Poly(-2, 1));

        // testing subtraction actual result
        Poly p19 = p12.subtract(p13); // p19 = -3.0x^5 - 10.5x^4 + 15.2x^2 - 2.0x
        assertEquals("(-7.0x^4 + 9.0x^2 - 2.0x) + (3.0x^5 + 3.5x^4 - 6.2x^2) == -3.0x^5 - 10.5x^4 + 15.2x^2 - 2.0x", p18, p19);

        // creating multiplication expected result
        Poly p20 = new Poly(0, 0); // p20 = -12.0x^9 + 4.0x^5 - 4.0x^5 - 4.0x^3 -12.0x^2
        p20 = p20.add(new Poly(-12, 9));
        p20 = p20.add(new Poly(4, 5));
        p20 = p20.add(new Poly(-4, 3));
        p20 = p20.add(new Poly(-12, 2));

        // testing multiplication actual result
        Poly p21 = p14.multiply(p15); // p21 = -12.0x^9 + 4.0x^5 - 4.0x^5 - 4.0x^3 -12.0x^2
        assertEquals("(-6.0x^8 + 2.0x^4 - 2.0x^2 - 6.0x) * (2x) == -12.0x^9 + 4.0x^5 - 4.0x^5 - 4.0x^3 -12.0x^2", p20, p21);

        // creating division expected result
        Poly p22 = new Poly(0, 0); // p22 = -3.0x^7 + x^3 - x - 3.0
        p22 = p22.add(new Poly(-3, 7));
        p22 = p22.add(new Poly(1, 3));
        p22 = p22.add(new Poly(-1, 1));
        p22 = p22.add(new Poly(-3, 0));

        // testing division actual result
        Poly p23 = p14.divide(p15); // p23 = -3.0x^7 + x^3 - x - 3.0
        assertEquals("(-6.0x^8 + 2.0x^4 - 2.0x^2 - 6.0x) / (2x) == -3.0x^7 + x^3 - x - 3.0", p22, p23);
    }
}
