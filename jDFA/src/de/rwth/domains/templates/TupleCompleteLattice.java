package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of complete lattices as tuple from other complete lattices.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TupleCompleteLattice.java,v 1.3 2002/09/27 08:27:32 mohnen Exp $
 */
public class TupleCompleteLattice extends TupleLattice implements CompleteLattice {
 /**
   * Creates a new <code>TupleCompleteLattice</code> instance from an array of
   * lattices.
   *
   */
  public TupleCompleteLattice(CompleteLattice[] lattices) {
    super(lattices);
  }

  /**
   * Creates a <code>TupleCompleteLattice</code> from two complete lattices.
   *
   * @param lattice1 a <code>CompleteLattice</code> value
   * @param lattice2 a <code>CompleteLattice</code> value
   */
  public TupleCompleteLattice(CompleteLattice lattice1, CompleteLattice lattice2) {
    this(new CompleteLattice[] {lattice1, lattice2});
  }

  
  /**
   * Returns the top element, which is the tuple of top elements from the component
   * lattices.
   *
   * @return an <code>Object</code> value
   */
  public Object top() {
    Object[] tops = new Object[sets.length];
    for (int i=0; i< sets.length; i++) {
      tops[i]=((CompleteLattice)sets[i]).top();
    }
    return new TupleElement(tops);
  }
  
  /**
   * Returns the bottom element, which is the tuple of bottom elements from the
   * component lattices.
   *
   * @return an <code>Object</code> value
   */
  public Object bottom() {
    Object[] bots = new Object[sets.length];
    for (int i=0; i< sets.length; i++) {
      bots[i]=((CompleteLattice)sets[i]).bottom();
    }
    return new TupleElement(bots);
  }
  
} // class TupleCompleteLattice
