package de.rwth.domains; // Generated package name

/**
 * Interface for upper semi lattices. Upper semi lattices are partially ordered sets,
 * where the least upper bound exists for any two elements.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: UpperSemiLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface UpperSemiLattice extends PreUpperSemiLattice {
  /**
   * Computes the least upper bound of two elements.
   * 
   * <br>
   * 
   * Implementations should guarantee that <code>join</code> is commutative and
   * associative. Furthermore, <code>join(e1,e2)=e2</code> iff <code>{@link #le
   * le(e1,e2)}</code>.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the least upper bound of <code>e1</code> and <code>e2</code>
   *
   * @see de.rwth.domains.Domain#checkProperties(UpperSemiLattice usl)
   */
  public Object join(Object e1, Object e2);
}
