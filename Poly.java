import java.util.*;

/**
 * Class to represent a polynomial, e.g. 3.5x^4 + 3x^2 - 4.
 * 
 * Polynomials can be added, subtracted, multiplied, and divided.
 * 
 * @author Eriche Gonzales
 * @version CMPU-102 Polynomial Calculator
 * @date May 3, 2022
 */
public class Poly{
    // instance fields
    ArrayList<Mono> list; // array list of monomials

    /**
     * Creates a new polynomial containing a single term with the coefficient
     * and exponent passed in as arguments. E.g. when called with coefficient
     * 3.5 and exponent 2, it should create a polynomial 3.5x^2.
     * 
     * @param coef the single term's coefficient.
     * @param exp the single term's exponent.
     * @return the polynomial created.
     */
    public Poly(double coef, int exp){
        list = new ArrayList<Mono>(); // creates array list of monomials
        Mono mono = new Mono(coef, exp); // creates a monomial
        list.add(mono); // adds monomial to the list
    }

    /**
     * Creates a new polynomial containing multiple terms with the array list passed in as arguments.
     * 
     * @param the array list of monomials.
     * @return the polynomial created.
     */
    public Poly(ArrayList<Mono> mpoly){
        list = new ArrayList<Mono>(); // creates array list of monomials

        for (int i=0; i < mpoly.size(); i++) {
            list.add(new Mono(mpoly.get(i).coef, mpoly.get(i).exp)); // copies monomials from mpoly to the array list
        }
    }

    /**
     * Adds the polynomial passed in as an argument, p, to the polynomial on which the 
     * method is called on (the "this" polynomial), and returns a new polynomial
     * with the result. I.e., it returns "this + p".
     * 
     * @param p the polynomial to add onto the polynomial on which the method is called on.
     * @return a polynomial representing the result of the addition.
     */
    public Poly add(Poly p){
        Poly sumPoly = new Poly(list); // creates a copy of the list to modify it

        // goes through each element of the array list
        for (int i = 0; i < p.list.size(); i++) {
            sumPoly = sumPoly.addMono(p.list.get(i)); // adding a single monomial from p to sumPoly
        }

        sumPoly.removeZeros(); // removes zeros in list and sorts it

        return sumPoly; // returns polynomial after monomial was added
    }

    /**
     * Adds the monomial passed in as an argument, m, to the monomial on which the 
     * method is called on, and returns a new polynomial with the result.
     * 
     * @param m the monomial to add onto the monomial on which the method is called on.
     * @return a polynomial representing the result of the addition.
     */
    public Poly addMono(Mono m){
        Poly p = new Poly(list); // creates a copy of the list to modify it

        // goes through each element of the array list
        for (int i = 0; i < p.list.size(); i++) {
            // adds coefficients of monomials that have the same exponent
            if (p.list.get(i).exp == m.exp) {
                p.list.get(i).coef += m.coef; // adds monomial to the list
                return p;
            }
        }

        p.list.add(m); // adds the monomial to the list if coefficients are all different
        return p; // returns the polynomial after the monomial was added
    }

    /**
     * Subtracts the polynomial passed in as an argument, p, from the polynomial on which the 
     * method is called on (the "this" polynomial), and returns a new polynomial
     * with the result. I.e., it returns "this - p".
     * 
     * @param p the polynomial to be subtracted from the polynomial on which the method is called on.
     * @return a polynomial representing the result of the subtraction.
     */
    public Poly subtract(Poly p){
        Poly subPoly = new Poly(p.list); // creates a copy of the list to modify it

        // goes through each element of the array list
        for (int i = 0; i < p.list.size(); i++) {
            subPoly.list.get(i).coef =  subPoly.list.get(i).coef * (-1); // makes the coefficients of monomials negative
        }

        subPoly = this.add(subPoly); // subtracts the polynomial using the add method

        return subPoly; // returns the polynomial after monomial was subtracted
    }

    /**
     * Multiplies the polynomial passed in as an argument, p, by the polynomial on which the 
     * method is called on (the "this" polynomial), and returns a new polynomial
     * with the result. I.e., it returns "this * p".
     * 
     * @param p the polynomial to be multiplied by the polynomial on which the method is called on.
     * @return a polynomial representing the result of the multiplication.
     */
    public Poly multiply(Poly p){
        Poly multPoly = new Poly(0, 0); // creates a copy of the list to modify it

        // goes through each element of the array list
        for (int i = 0; i < p.list.size(); i++) {
            multPoly = multPoly.add(multiplyMono(p.list.get(i))); // adds the monomials in an array after multiplying monomials
        }

        return multPoly; // returns polynomial after monomial was multiplied
    }

    /**
     * Multipies the monomial passed in as an argument, m, to the monomial on which the 
     * method is called on, and returns a new polynomial with the result.
     * 
     * @param m the monomial to multiply onto the monomial on which the method is called on.
     * @return a polynomial representing the result of the multiplication.
     */
    public Poly multiplyMono(Mono m){
        Poly p = new Poly(list); // creates a copy of the list to modify it

        // goes through each element of the array list
        for (int i = 0; i < list.size(); i++) {
            p.list.get(i).coef = p.list.get(i).coef * m.coef; // multiplies the coefficient of the monomials
            p.list.get(i).exp = p.list.get(i).exp + m.exp; // increases the exponent after multiplication
        }

        return p; // returns the polynomial after the monomial was multiplied
    }

    /**
     * Divides the polynomial on which the method is called on (the "this" polynomial), by
     * the polynomial passed in as an argument, p, and returns a new polynomial
     * with the resulting quotient. I.e., it returns "this / p".
     * 
     * The division should be performed according to the polynomial long division algorithm
     * ( https://en.wikipedia.org/wiki/Polynomial_long_division ).
     * 
     * Polynomial division may end with a non-zero remainder, which means the polynomials are
     * indivisible. In this case the method should return null. A division by zero should also
     * yield a null return value.
     * 
     * @param p the polynomial to be multiplied by the polynomial on which the method is called on.
     * @return a polynomial representing the quotient of the division, or null if they're indivisible.
     */    
    public Poly divide(Poly p){
        /* from Pseudocode in [https://en.wikipedia.org/wiki/Polynomial_long_division]
        function n / d is
        require d ≠ 0
        q ← 0
        r ← n             // At each step n = d × q + r

        while r ≠ 0 and degree(r) ≥ degree(d) do
        t ← lead(r) / lead(d)       // Divide the leading terms
        q ← q + t
        r ← r − t × d

        return (q, r)
         */

        Poly q = new Poly(0, 0); // creates a new polynomial that represents the divisor
        Poly r = new Poly(list); // creates a polynomial with elements from list that represents the dividend 
        Poly d = p; // sets a polynomial to a variable that represents the polynomial passed in as an argument
        r.removeZeros(); // remove zeros and sorts
        d.removeZeros(); // remove zeros and sorts

        if (d.list.size() == 0) return null; // if polynomial passed in is empty, return null;
        if (r.list.size() == 0) return q; // if 'this' polynomial is empty, return an empty polynomial

        while (r.list.size() != 0 && (r.list.get(0).exp >= d.list.get(0).exp)) {
            double coef = r.list.get(0).coef/d.list.get(0).coef; // coef after dividing the first term of the dividend by the polynomial argument
            int exp = r.list.get(0).exp-d.list.get(0).exp; // exp after dividing first term of the dividend by the polynomial argument

            Poly t = new Poly(coef, exp); // creates a single-termed polynomial after dividing first terms

            q = q.add(t); // adds the single-termed polynomial to the divisor 

            // multiplies the first term of the divisor with the polynomial argument and subtracts it from the dividend
            r = r.subtract((t.multiply(d))); 
        }

        if (r.list.size() == 0) return q; // if the size of the dividend, return an empty polynomial
        return null; // return null if polynomials are not divisible
    }

    /**
     * Compares the polynomial on which the method is called (the "this" polynomial), 
     * to the object passed in as argument, o. o is to be considered equal to the "this"
     * polynomial if they both represent equivalent polynomials.
     * 
     * E.g., "3.0x^4 + 0.0x^2 + 5.0" and "3.0x^4 + 5.0" should be considered equivalent.
     * "3.0x^4 + 5.0" and "3.0x^4 + 3.0" should not.
     * 
     * @param o the object to be compared against the polynomial the method is called on.
     * @return true if o is a polynomial equivalent to the one the method was called on,
     * and false otherwise.
     */
    public boolean equals(Object o){
        if (o instanceof Poly) {
            // cast the object o into a polynomial
            Poly p = (Poly) o;

            // checks if polynomial passed in as an argument is equal to 'this' polynomial by implementing the subtract method
            Poly equalsPoly = p.subtract(this);

            // iterate through both array lists of monomials
            for (int i = 0; i < equalsPoly.list.size(); i++) {
                if (equalsPoly.list.get(i).coef != 0) return false; // if result is not zero, the polynomials are not equal and return false
            }
            return true; // if all monomials are equal, return true
        }
        else return false;
    }

    /**
     * Removes the monomial in the polynomial that contains a coefficient of 0.
     * 
     * E.g., "3.0x^4 + 0.0x^2 + 5.0" will become "3.0x^4 + 5.0".
     * 
     * @return the updated polynomial.
     */
    private void removeZeros(){
        for (int i = list.size() - 1; i >= 0; i--){
            if (list.get(i).coef == 0) list.remove(i); // remove monomial with a zero coefficient
        }
        Collections.sort(list, new SortByExp()); // sort the list after removing zeros
    }

    /**
     * Returns a textual representation of the polynomial the method is called on.
     * The textual representation should be a sum of monomials, with each monomial 
     * being defined by a double coefficient, the letters "x^", and an integer exponent.
     * Exceptions to this rule: coefficients of 1.0 should be omitted, as should "^1",
     * and "x^0".
     * 
     * Terms should be listed in decreasing-exponent order. Terms with zero-coefficient
     * should be omitted. Each exponent can only appear once. 
     * 
     * Rules for separating terms, applicable to all terms other that the largest exponent one:
     *   - Terms with positive coefficients should be preceeded by " + ".
     *   - Terms with negative coefficients should be preceeded by " - ".
     * 
     * Rules for the highest exponent term (i.e., the first):
     *   - If the coefficient is negative it should be preceeded by "-". E.g. "-3.0x^5".
     *   - If the coefficient is positive it should not preceeded by anything. E.g. "3.0x^5".
     * 
     * The zero/empty polynomial should be represented as "0.0".
     * 
     * Examples of valid representations: 
     *   - "2.0x^2 + 3.0"
     *   - "3.5x + 3.0"
     *   - "4.0"
     *   - "-2.0x"
     *   - "4.0x - 3.0"
     *   - "0.0"
     *   
     * Examples of invalid representations: 
     *   - "+2.0x^2+3.0x^0"
     *   - "3.5x -3.0"
     *   - "- 4.0 + x"
     *   - "-4.0 + x^7"
     *   - ""
     * 
     * @return a textual representation of the polynomial the method was called on.
     */
    public String toString(){
        String poly = ""; // initialize an empty string
        removeZeros(); // remove zeros in list

        if (list.size() == 0) return "0.0"; // return 0 if list is empty

        for (int i=0; i < this.list.size(); i++) {
            // case if it is the first element in the list
            if (i==0) {
                // case if negative coefficient
                if (list.get(i).coef < 0) {
                    if (list.get(i).coef == -1) poly += "-";
                    else poly += "-" + (list.get(i).coef*-1);
                }
                // case if positive coefficient
                else if (list.get(i).coef > 0) {
                    if (list.get(i).coef == 1) poly += "";
                    else poly += list.get(i).coef;
                }
                // case if exponent is zero
                if (list.get(i).exp == 0) {
                    if (list.get(i).coef == 1 || list.get(i).coef == -1) poly += "1.0";
                    else poly += "";
                }
                // case if exponent is one
                else if (list.get(i).exp == 1) poly += "x";
                else poly += "x^" + list.get(i).exp;
            }
            // case if it is not the first element in the list
            else {
                // case if negative coefficient
                if (list.get(i).coef < 0) {
                    if (list.get(i).coef == -1) poly += " - ";
                    else poly += " - " + (list.get(i).coef*-1);
                }
                // case if positive coefficient
                else if (list.get(i).coef > 0) {
                    if (list.get(i).coef == 1) poly += " + ";
                    else poly += " + " + list.get(i).coef;
                }
                // case if exponent is zero
                if (list.get(i).exp == 0) {
                    if (list.get(i).coef == 1 || list.get(i).coef == -1) poly += "1.0";
                    else poly += "";
                }
                // case if exponent is one
                else if (list.get(i).exp == 1) poly += "x";
                else poly += "x^" + list.get(i).exp;
            }
        }
        return poly; // return final string representing a polynomial
    }

    /**
     * Reads the polynomial passed in from the user input by identifying the integer and exponent components of the scanner.
     * 
     * E.g., For user input "2 1": 2 is read in as a double coefficient, and 1 is read in as an integer exponent.
     * 
     * @param letter the character that keeps track of whether it is the first or second polynomial being read in.
     * @return Poly the polynomial created from the user input, or null if the user chooses to quit the program.
     */
    private static Poly readPoly(char letter) {
        Poly polyInput = new Poly(0, 0); // creates an empty polynomial

        for(boolean validInput = false; !validInput;) {
            System.out.println("Polynomial " + letter + ":"); // prints out which polynomial it is (a or b)
            Scanner keybScanner = new Scanner(System.in); // creates a scanner
            String line = keybScanner.nextLine(); // scanner that scans the user input

            if (line.equals("quit")) return null; // return null if user input is 'quit'

            Scanner lineScanner = new Scanner(line); // creates another scanner

            // goes through each element in the scanner while there are still elements
            while(lineScanner.hasNext()) {
                try {
                    // scanner that scans for the next double in the user input and saves it in a variable as the coefficient
                    double coef = lineScanner.nextDouble();
                    if (lineScanner.hasNext()) {
                        // scanner that scans for the next integer in the user input and saves it in a variable as the exponent
                        int exp = lineScanner.nextInt();
                        // creates a new polynomial with the coefficient and exponent read it and adds it to the polynomial
                        polyInput = polyInput.add(new Poly(coef, exp));
                        // goes to the next element in the user input
                        validInput = true;
                    }
                    else {
                        // prints error message if the user does not enter a pair of coefficient and exponent
                        System.out.println("Invalid polynomial input. Please refer to the instructions."); 
                        // restarts the loop so user can enter input again
                        validInput = false;
                    }
                }
                catch (InputMismatchException e) {
                    // prints error message if user enters any input that is not a double or integer
                    System.out.println("Invalid polynomial input. Please refer to the instructions.");
                    // restarts the loop so user can enter input again
                    validInput = false;
                    break; // breaks out of while loop
                }
            }
            break; // breaks out of for loop
        }

        System.out.println("Read " + letter + " = " + polyInput); // prints user input as a polynomial
        return polyInput; // returns the polynomial of the user input
    }

    /**
     * Reads the operator passed in from the user input and checks if it is a valid input or not. 
     * 
     * @return String the operator inputted by the user, or null if the user chooses to quit the program.
     */
    static String readOperator() {
        Scanner keybScanner = new Scanner(System.in); // scanner for user input

        String operator = ""; // creates an empty string
        boolean isValidInput = true; // creates a boolean for the while loop

        // a loop so user can re-enter chosen operator, or quit
        while(isValidInput) {
            System.out.println("Operation (+, -, *, /):");

            operator = keybScanner.nextLine(); // saves the string input into a variable
            operator = operator.replaceAll(" ", ""); // remove spaces from the input

            if (operator.equals("quit")) return null; // return null if 'quit'
            // print error message if user does not enter a valid operator
            if (!(operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/"))){
                System.out.println("Invalid operation. Only listed ones supported.");
            } 
            // breaks out of the while loop if operator is valid
            else isValidInput = false;
        }

        return operator; // returns the valid operator
    }   

    /**
     * Executes the operation that the user chose. 
     * 
     * @param operator the string that represents the operator that the user inputted.
     * @param polyA the first polynomial that the user inputted.
     * @param polyB the second polynomial that the user inputted.
     * @return Poly the polynomial after the operation is executed.
     */
    private static Poly operate(String operator, Poly polyA, Poly polyB) {
        Poly p = new Poly(0, 0); // creates a new polynomial

        if (operator.equals("+")) p = polyA.add(polyB); // executes polynomial addition
        else if (operator.equals("-")) p = polyA.subtract(polyB); // executes polynomial subtraction
        else if (operator.equals("*")) p = polyA.multiply(polyB); // executes polynomial multiplication
        else if (operator.equals("/")) p = polyA.divide(polyB); // executes polynomial division

        return p; // returns the final polynomial
    }

    /**
     * Runs consecutive polynomial computations until the user gets tired and quits. When the user quits, the system prints a farewell message and exits the program.
     * @param args The first argument represents the first polynomial to operate on, the second argument represents the operator that executes an operation, and the third argument represents the second polynomial that operates on the first polynomial.
     */
    public static void main(String[] args) {
        // prints the welcome message and instructions for entering a polynomial or quitting the ptrogram
        String welcome = ("/*** Welcome to CMPU-102's Polynomial Calculator! ***/"
                + "\n - Polynomials are specificed using space-separated pairs of coefficient and exponent."
                + "\n       E.g., '2.5x^2 - 1' should be input as '2.5 2 -1 0'."
                + "\n - 'quit' can be used anytime to exit the program.");
        System.out.println(welcome);
        
        // initializes a variable that tracks the number of computations the user has completed
        int nComputations = 1;

        while(true){
            int i = 0; // keeps track of whether the user is entering the first or secondpolynomial
            char letter = (char) (97+i); // casts it into a character so that the letters can ascend

            // prints the number of computations the user has completed
            System.out.println("\n --- Computation #" + nComputations + " ---");

            Poly polyA = readPoly(letter); // reads the first polynomial entered
            if (polyA == null) break; // if user chose to quit, exit the loop
            letter++; // change to second polynomial b

            String operator = readOperator(); // reads the operator entered
            if (operator == null) break; // if user chose to quit, exit the loop

            Poly polyB = readPoly(letter); // reads the second polynomial entered
            if (polyB == null) break; // if user chose to quit, exit the loop

            Poly result = operate(operator, polyA, polyB); // creates a polynomial with all of the user inputs
            // prints the indivisible polynomial operation if the final polynomial is null
            if (result == null) System.out.println("\n" + polyA + " " + operator + " " + polyB + " = indivisible");
            // prints the final polynomial operation if the operations are valid
            else System.out.println("\n" + polyA + " " + operator + " " + polyB + " = " + result);

            nComputations++; // increases the number of computations completed
        }
        System.out.print("Thank you, goodbye!"); // prints goodbye message when user chooses to quit the program
    }
}
