package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;

	/**
	 * Degree of term.
	 */
	public int degree;

	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
				other instanceof Term &&
				coeff == ((Term)other).coeff &&
				degree == ((Term)other).degree;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {

	/**
	 * Term instance. 
	 */
	Term term;

	/**
	 * Next node in linked list. 
	 */
	Node next;

	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {

	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;

	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;

		poly = null;

		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}


	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		Polynomial addition = new Polynomial();
		Polynomial subract = new Polynomial();
		Node x = poly;
		do 
		{
			Term l1 = null, l2 = null;

			if (p.poly != null)
				l1 = p.poly.term;

			if (x != null)
				l2 = x.term;

			if (l1 == null) 
			{
				addition.poly = new Node(l2.coeff, l2.degree, addition.poly);
				x = x.next;
			} 
			else 
				if (l2 == null) 
				{
					addition.poly = new Node(l1.coeff, l1.degree, addition.poly);
					p.poly = p.poly.next;
				} 
				else 
				{
					if (l1.degree < l2.degree) 
					{
						addition.poly = new Node(l1.coeff, l1.degree, addition.poly);
						p.poly = p.poly.next;
					}

					if (l2.degree < l1.degree) 
					{
						addition.poly = new Node(l2.coeff, l2.degree, addition.poly);
						x = x.next;
					}

					if (l1.degree == l2.degree) 
					{
						addition.poly = new Node(l1.coeff + l2.coeff, l1.degree,addition.poly);
						p.poly = p.poly.next;
						x = x.next;
					}

				}

		} 
		while (p.poly != null || x != null);

		while (addition.poly != null) 
		{
			if (addition.poly.term.coeff != 0)
				subract.poly = new Node(addition.poly.term.coeff, addition.poly.term.degree, subract.poly);

			addition.poly = addition.poly.next;
		}

		return subract;
	}

	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		Polynomial temp = new Polynomial();
		temp.poly = this.poly;
		Polynomial Summed = new Polynomial();
		Polynomial x = new Polynomial();

		int deg1, deg2;
		float co1, co2;
		Polynomial PolyClone, added;
		
		while (temp.poly != null) 
		{

			deg1 = temp.poly.term.degree;
			co1 = temp.poly.term.coeff;
			added = new Polynomial();

			PolyClone = new Polynomial();
			PolyClone.poly = p.poly;

			while (PolyClone.poly != null) 
			{
				deg2 = PolyClone.poly.term.degree;
				co2 = PolyClone.poly.term.coeff;

				added.poly = new Node(co1 * co2, deg1 + deg2,added.poly);

				PolyClone.poly = PolyClone.poly.next;
			}

			while (added.poly != null) 
			{
				if (added.poly.term.coeff != 0)
					x.poly = new Node(added.poly.term.coeff,added.poly.term.degree, x.poly);

				added.poly = added.poly.next;
			}

			Summed = Summed.add(x);
			temp.poly = temp.poly.next;

		}
		return Summed;
	}


	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {

		Node l1 = poly;

		float z = 0;

		if(l1 == null)
			return 0;

		while(l1!= null){

			z+= l1.term.coeff* (float) Math.pow(x, l1.term.degree);
			l1= l1.next;			

		}

		return z;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;

		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
					current != null ;
					current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
