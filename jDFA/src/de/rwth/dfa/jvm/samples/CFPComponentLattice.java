package de.rwth.dfa.jvm.samples; // Generated package name

import java.util.Enumeration;
import java.util.NoSuchElementException;

import de.rwth.domains.*;
import de.rwth.domains.templates.*;

/**
 * The lattice used by the constant folding propagation lattices to describe a single
 * value. Its Hasse diagram looks like this: <pre>
 *        ?
 *  / ... | ... \
 * <> ...(any Number) ...
 *  \ ... | ... / 
 *        !
 * </pre>
 * <ol>
 * <li>The top element <code>?</code> represents insufficient information.</li>
 * <li>The element <code>&lt;&gt;</code> is a pseudo constant for a non
 * <code>null</code> reference value.</li>
 * <li>The <code>Number</code> elements represent the numbers.</li>
 * <li>The bottom element <code>!</code> represents a value which is not constant</li>
 * </ol>
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CFPComponentLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class CFPComponentLattice extends FlatCompleteLattice {
  /**
   * The top element of all these lattices.
   *
   */
  public static final Object UNKNOWNCOMPONENT = new Object() {
      public String toString() { return "?"; }
    } ;
  
  /**
   * The pseudo constant for non <code>null</code> references of all these lattices.
   *
   */
  public static final Object NONNULLCOMPONENT = new Object() {
      public String toString() { return "<>"; }
    } ;
  
  /**
   * The bottom element of all these lattices.
   *
   */
  public static final Object NONCONSTANTCOMPONENT = new Object() {
      public String toString() { return "!"; }
    } ;

  /**
   * Creates a new <code>CFPComponentLattice</code> instance.
   *
   */
  public CFPComponentLattice() {
    super(new SumSet(new Set[] {new SimpleSet(new Object[] {null}),
				new SimpleSet(NONNULLCOMPONENT),
				new NumberSet()}),
	  NONCONSTANTCOMPONENT, UNKNOWNCOMPONENT);
  }
 
  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    de.rwth.domains.POSet poset = new CFPComponentLattice();
    try {
      System.err.println(Domain.hasseDiagram(poset));
      Domain.checkProperties(poset);
      } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
