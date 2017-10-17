package de.rwth.dfa.jvm.samples; // Generated package name

import java.util.Stack;

import de.rwth.domains.*;
import de.rwth.domains.templates.*;

/**
 * The lattice used by the constant folding propagation lattice to describe the state
 * of the operand stack. The Hasse diagram looks like this:
 * <pre>
 *    unknown stack
 *   /            \
 *   ... stacks ...
 *   \            /
 *    invalid stack
 * </pre>
 *
 * Each proper stack element is a stack with the maximal height of the operand stack
 * of the method. Each entry is an element of {@link CFPComponentLattice}.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CFPStackLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class CFPStackLattice extends LiftedCompleteLattice {
  /**
   * The top element for unknown stacks of all these lattices.
   *
   */
  protected static final Object UNKNOWNSTACK = new Object() {
      public String toString() { return "unknown stack"; }
    };

  /**
   * The top element for invalid stacks of all these lattices.
   *
   */
  protected static final Object INVALIDSTACK = new Object() {
      public String toString() { return "invalid stack"; }
    };
  
  /**
   * Creates a new <code>CFPStackLattice</code> instance.
   *
   * @param maxStack an <code>int</code> value
   */
  public CFPStackLattice(int maxStack) {
    super(new StackPreLattice(new CFPComponentLattice(),maxStack),
	  INVALIDSTACK,UNKNOWNSTACK);
  }

  /**
   * Returns the contained stack for all elements except top and bottom. Otherwise,
   * returns its argument.
   *
   * @param o an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object getStack(Object o) {
    if (!isElement(o)) throw new IllegalArgumentException(o+" is not an element");
    if (o==INVALIDSTACK || o==UNKNOWNSTACK) return o;
    return (Stack)(((Stack)o).clone());
  }

  /**
   * Checks if the argument is an element and returns it.
   *
   * @param stack an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object makeElement(Object stack) {
    if (!isElement(stack))
      throw new IllegalArgumentException(stack+" is not an element");
    return stack;
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    Lattice lattice = new CFPStackLattice(2);
    try {
      System.err.println(Domain.hasseDiagram(lattice));
      Domain.checkProperties(lattice);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
