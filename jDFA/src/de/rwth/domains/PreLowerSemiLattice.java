package de.rwth.domains; // Generated package name

/**
 * Interface for pre lower semi lattices. Pre lower semi lattices are partially
 * ordered sets, where the greatest lower bound might not exist for two elements.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: PreLowerSemiLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface PreLowerSemiLattice extends POSet {
  /**
   * Computes the greatest lower bound of two elements if it exists.
   *
   * <br>
   *
   * Implementations should guarantee that <code>meet</code> is commutative and
   * associative. Furthermore, <code>meet(e1,e2)=e1</code> iff <code>{@link #le
   * le(e1,e2)}</code>. If one of the arguments is <code>null</code>, the result must
   * be <code>null</code> as well.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the greatest lower bound of <code>e1</code> and <code>e2</code>
   *         or <code>null</code> if this does not exists
   *
   * @see de.rwth.domains.Domain#checkProperties(PreLowerSemiLattice lsl)
   */
  public Object meet(Object e1, Object e2);
}
