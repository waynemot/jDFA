package de.rwth.domains; // Generated package name

/**
 * Interface for complete lattices. Complete lattices are lattices where every subset
 * has a least upper and greatest lower bound. Consequently, they have a least and a
 * greatest element.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CompleteLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface CompleteLattice extends Lattice, CompletePOSet {
  /**
   * Gets the least element of this <code>CompleteLattice</code>.
   *
   * <p>
   * 
   * Implementations should guarantee that <code>bottom</code> is indeed least
   * element of this set, this is to say that <code>isElement</code> recognises the
   * value returned by <code>bottom</code> and <code>lt(bottom(),e)==true</code> for
   * all elements <code>e</code>.
   *
   * @return the least element in this set
   *
   * @see de.rwth.domains.Domain#checkProperties(CompleteLattice cl)
   */
  public Object bottom();

  /**
   * Gets the greatest element of this <code>CompleteLattice</code>.
   * 
   * <p>
   * 
   * Implementations should guarantee that <code>bottom</code> is indeed least
   * element of this set, this is to say that <code>isElement</code> recognises the
   * value returned by <code>bottom</code> and <code>lt(bottom(),e)==true</code> for
   * all elements <code>e</code>.
   *
   * @return the greatest element in this set
   *
   * @see de.rwth.domains.Domain#checkProperties(CompleteLattice cl)
   */
  public Object top();
}
