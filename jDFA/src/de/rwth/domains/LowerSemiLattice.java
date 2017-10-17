package de.rwth.domains; // Generated package name

/**
 * Interface for lower semi lattices. Lower semi lattices are partially ordered sets,
 * where the greatest lower bound exists for any two elements.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: LowerSemiLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface LowerSemiLattice extends PreLowerSemiLattice {
  /**
   * Computes the greatest lower bound of two elements.
   * 
   * <br>
   * 
   * Implementations should guarantee that <code>meet</code> is commutative and
   * associative. Furthermore, <code>meet(e1,e2)=e1</code> iff <code>{@link #le
   * le(e1,e2)}</code>.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the greatest lower bound of <code>e1</code> and <code>e2</code>
   *
   * @see de.rwth.domains.Domain#checkProperties(LowerSemiLattice lsl)
   */
  public Object meet(Object e1, Object e2);
}
