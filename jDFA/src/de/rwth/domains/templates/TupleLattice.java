package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of lattices as tuple from other lattices.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TupleLattice.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class TupleLattice extends TuplePOSet implements Lattice {
  /**
   * Creates a new 'TupleLattice' instance.
   *
   */
  public TupleLattice(Lattice[] lattices) {
    super(lattices);
  }

  /**
   * Computes the greatest lower bound of two elements by computing it
   * component-wise.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the greatest lower bound of <code>e1</code> and <code>e2</code>
   *
   * @see de.rwth.domains.Domain#checkProperties(Lattice l)
   */
  public Object meet(Object e1, Object e2) {
    Object[] ee1 = ((TupleElement)e1).getElements();
    Object[] ee2 = ((TupleElement)e2).getElements();
    if (ee1.length!=sets.length || ee2.length!=sets.length)
      throw new IllegalArgumentException();
    Object[] elements = new Object[sets.length];
    for (int i=0; i<sets.length; i++) {
      if (!(sets[i].isElement(ee1[i]) && sets[i].isElement(ee2[i])))
	throw new IllegalArgumentException();
      elements[i]=((Lattice)sets[i]).meet(ee1[i],ee2[i]);
    }
    return new TupleElement(elements);
  }

  /**
   * Computes the least upper bound of two elements by computing it component-wise.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the least upper bound of <code>e1</code> and <code>e2</code>
   *
   * @see de.rwth.domains.Domain#checkProperties(Lattice l)
   */
  public Object join(Object e1, Object e2) {
    Object[] ee1 = ((TupleElement)e1).getElements();
    Object[] ee2 = ((TupleElement)e2).getElements();
    if (ee1.length!=sets.length || ee2.length!=sets.length)
      throw new IllegalArgumentException();
    Object[] elements = new Object[sets.length];
    for (int i=0; i<sets.length; i++) {
      if (!(sets[i].isElement(ee1[i]) && sets[i].isElement(ee2[i])))
	throw new IllegalArgumentException();
      elements[i]=((Lattice)sets[i]).join(ee1[i],ee2[i]);
    }
    return new TupleElement(elements);
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    Lattice clattice = new LiftedCompleteLattice(new BitVectorLattice(1));
    TupleLattice lattice = new TupleLattice(new Lattice[] {clattice,clattice});
    try {
      Domain.checkProperties(lattice);
      System.err.println(Domain.hasseDiagram(lattice));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

} // class TupleLattice
