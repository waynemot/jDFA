package de.rwth.domains; // Generated package name

/**
 * Interface for pre upper semi lattices. Pre upper semi lattices are partially
 * ordered sets, where the least upper bound might not exist for two elements.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: PreUpperSemiLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface PreUpperSemiLattice extends POSet {
  /**
   * Computes the least upper bound of two elements if it exists.
   *
   * <br>
   *
   * Implementations should guarantee that <code>join</code> is commutative and
   * associative. Furthermore, <code>join(e1,e2)=e2</code> iff <code>{@link #le
   * le(e1,e2)}</code>. If one of the arguments is <code>null</code>, the result must
   * be <code>null</code> as well.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the least upper bound of <code>e1</code> and <code>e2</code>
   *         or <code>null</code> if this does not exists
   *
   * @see de.rwth.domains.Domain#checkProperties(PreUpperSemiLattice pusl)
   */
  public Object join(Object e1, Object e2);
}
