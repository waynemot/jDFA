package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of intervals in the integers with natural
 * order.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: IntegerLattice.java,v 1.1 2002/09/27 08:26:04 mohnen Exp $
 */
public class IntegerLattice extends IntegerPOSet implements Lattice {
  /**
   * Create an interval in the integers.
   *
   * @param <code>from</code> a value of type <code>int</code>
   * @param <code>to</code> a value of type <code>int</code>
   */
  public IntegerLattice(int from, int to) {
    super(from,to);
  }

  public Object meet(Object e1, Object e2) {
    if (smalltolarge)
      return new Integer(Math.min(((Integer)e1).intValue(),
				  ((Integer)e2).intValue()));
    else
      return new Integer(Math.max(((Integer)e1).intValue(),
				  ((Integer)e2).intValue()));
  }
  
  public Object join(Object e1, Object e2) {
    if (smalltolarge)
      return new Integer(Math.max(((Integer)e1).intValue(),
				  ((Integer)e2).intValue()));
    else
      return new Integer(Math.min(((Integer)e1).intValue(),
				  ((Integer)e2).intValue()));
  }
}  
