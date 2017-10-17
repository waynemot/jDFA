package de.rwth.dfa.jvm.samples; // Generated package name

import de.rwth.utils.ArrayTools;

import de.rwth.domains.*;
import de.rwth.domains.templates.*;

/**
 * The lattice used by the constant folding propagation lattice to describe the state
 * of the local variable slots. Each element is a tuple with one entry for each slot.
 * Each entry is an element of {@link CFPComponentLattice}.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CFPLocalsLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class CFPLocalsLattice extends TupleCompleteLattice {
  /**
   * Creates a new <code>CFPLocalsLattice</code> instance.
   *
   * @param maxLocals an <code>int</code> value
   */
  public CFPLocalsLattice(int maxLocals) {
    super((CompleteLattice[])ArrayTools.fill(new CompleteLattice[maxLocals],
					     new CFPComponentLattice()));
  }

  /**
   * Extracts the values for the components as an array for an element of this
   * lattice.
   *
   * @param o an <code>Object</code> value
   * @return an <code>Object[]</code> value
   */
  public Object[] getLocals(Object o) {
    if (!isElement(o))
      throw new IllegalArgumentException(o+" is not an element");
    return ((TupleElement)o).getElements();
  }

  /**
   * Creates an element of this lattice from a valid component array.
   *
   * @param locals an <code>Object[]</code> value
   * @return an <code>Object</code> value
   */
  public Object makeElement(Object[] locals) {
    TupleElement o = new TupleElement(locals);
    if (!isElement(o))
      throw new IllegalArgumentException(o+" is not an element");
    return o;
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    Lattice lattice = new CFPLocalsLattice(2);
    try {
      System.err.println(Domain.hasseDiagram(lattice));
      Domain.checkProperties(lattice);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
