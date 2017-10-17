package de.rwth.domains.templates; // Generated package name

import java.util.Stack;

import de.rwth.domains.*;

/**
 * Class for (maybe infinite) pre lattices which consists of stacks of elements from
 * a lattice. These are pre lattices, because join and meet are defined only for
 * elements with the same stack height.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: StackPreLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class StackPreLattice extends StackPOSet implements PreLattice {
  /**
   * Creates a new <code>StackPreLattice</code> instance with unlimited stack size
   * and skeleton stack size StackSet.SKELMAXSIZE.
   *
   * @param lattice a <code>Lattice</code> value
   */
  public StackPreLattice(Lattice lattice) {
    super(lattice);
  }

  /**
   * Creates a new <code>StackPOSet</code> instance with maximal stack size.
   *
   * @param lattice a <code>Lattice</code> value
   * @param maxSize an <code>int</code> value
   */
  public StackPreLattice(Lattice lattice, int maxSize) {
    super(lattice, maxSize);
  }

  /**
   * Computes the meet of two elements. This is done in the following way:
   * <ol>
   * <li>If the stack sizes differ, the result is <code>null</code></li>
   * <li>Otherwise, the result has the same size and each element is the meet of the
   * corresponding components in the underlying lattice.
   * </ol>
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object meet(Object e1, Object e2) {
    if (e1==null || e2==null) return null;
    Stack s1 = (Stack)e1;
    Stack s2 = (Stack)e2;
    if (s1.size()!=s2.size()) return null;
    Stack result = new Stack();
    for (int i=0; i<s1.size(); i++) {
      if (!(set.isElement(s1.elementAt(i)) && set.isElement(s2.elementAt(i))))
	throw new IllegalArgumentException();
      result.push(((Lattice)set).meet(s1.elementAt(i),s2.elementAt(i)));
    }
    return result;
  }
  
  /**
   * Computes the meet of two elements. This is done in the following way:
   * <ol>
   * <li>If the stack sizes differ, the result is <code>null</code></li>
   * <li>Otherwise, the result has the same size and each element is the join of the
   * corresponding components in the underlying lattice.
   * </ol>
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object join(Object e1, Object e2) {
    if (e1==null || e2==null) return null;
    Stack s1 = (Stack)e1;
    Stack s2 = (Stack)e2;
    if (s1.size()!=s2.size()) return null;
    Stack result = new Stack();
    for (int i=0; i<s1.size(); i++) {
      if (!(set.isElement(s1.elementAt(i)) && set.isElement(s2.elementAt(i))))
	throw new IllegalArgumentException();
      result.push(((Lattice)set).join(s1.elementAt(i),s2.elementAt(i)));
    }
    return result;
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      Lattice s1 = new BitVectorLattice(1);
      PreLattice s = new StackPreLattice(s1);
      System.err.println(Domain.hasseDiagram(s));
      Domain.checkProperties(s);
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
  
} // class StackPreLattice
