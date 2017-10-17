package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of intervals in the integers with natural
 * order.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: IntegerCompleteLattice.java,v 1.1 2002/09/27 08:26:04 mohnen Exp $
 */
public class IntegerCompleteLattice extends IntegerLattice implements CompleteLattice {
  /**
   * Create an interval in the integers.
   *
   * @param <code>from</code> a value of type <code>int</code>
   * @param <code>to</code> a value of type <code>int</code>
   */
  public IntegerCompleteLattice(int from, int to) {
    super(from,to);
  }

  public Object bottom() {
    return elements[0];
  }

  public Object top() {
    return elements[elements.length-1];
  }
  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      CompleteLattice lo = new IntegerCompleteLattice(0,3);
      CompleteLattice hi = new LiftedCompleteLattice(new IntegerCompleteLattice(3,1),
						     new String("*"),
						     new Integer(0));
      Domain.checkProperties(lo);
      Domain.checkProperties(hi);
      CompleteLattice s = new TupleCompleteLattice(lo,hi);
      Domain.checkProperties(s);
      System.err.println(Domain.hasseDiagram(s));
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
}




