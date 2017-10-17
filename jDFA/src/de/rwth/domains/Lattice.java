package de.rwth.domains; // Generated package name

/**
 * Interface for lattices. Lattices are structures which are both lower and upper
 * semi lattice where the absorption law holds.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Lattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface Lattice extends LowerSemiLattice, UpperSemiLattice, PreLattice {
   /**
   * Computes the greatest lower bound of two elements.
   * 
   * <p>
   * 
   * In addition to the restrictions on <code>meet</code> from {@link
   * de.rwth.domains.LowerSemiLattice#meet here}, implementations should guarantee
   * that <code>meet</code> fulfills the absortion laws:
   * <code>equals(meet(join(e1,e2),e1),e1)</code> and
   * <code>equals(join(e1,meet(e1,e2)),e1)</code> for all elements <code>e1</code>
   * and <code>e2</code>
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the greatest lower bound of <code>e1</code> and <code>e2</code>
   *
   * @see de.rwth.domains.Domain#checkProperties(Lattice l)
   */
  public Object meet(Object e1, Object e2);
  
   /**
   * Computes the least upper bound of two elements.
   * 
   * <p>
   * 
   * In addition to the restrictions on <code>join</code> from {@link
   * de.rwth.domains.UpperSemiLattice#join here}, implementations should guarantee
   * that <code>join</code> fulfills the absortion laws:
   * <code>equals(meet(join(e1,e2),e1),e1)</code> and
   * <code>equals(join(e1,meet(e1,e2)),e1)</code> for all elements <code>e1</code>
   * and <code>e2</code>
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the least upper bound of <code>e1</code> and <code>e2</code>
   *
   * @see de.rwth.domains.Domain#checkProperties(Lattice l)
   */
  public Object join(Object e1, Object e2);
}
