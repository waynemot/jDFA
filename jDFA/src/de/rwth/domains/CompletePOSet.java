package de.rwth.domains; // Generated package name

/**
 * Interface for complete partially ordered sets. Complete partially ordered sets are
 * partially ordered sets with a least element and the property that every infinite
 * directed set has a least upper bound.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CompletePOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface CompletePOSet extends POSet {
  /**
   * Gets the least element of this <code>CompletePOSet</code>.
   * 
   * <P>
   *
   * Implementations should guarantee that <code>bottom</code> is indeed least
   * element of this set, this is to say that <code>isElement</code> recognises the
   * value returned by <code>bottom</code> and <code>lt(bottom(),e)==true</code> for
   * all elements <code>e</code>.
   *
   * @return the least element in this set
   *
   * @see de.rwth.domains.Domain#checkProperties(CompletePOSet cposet)
   */
  public Object bottom();
}
