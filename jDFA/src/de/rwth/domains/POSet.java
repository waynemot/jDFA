package de.rwth.domains; // Generated package name

/**
 * Interface for partially ordered sets. Partially ordered sets have a binary
 * less-than-or-equals relation le which must be reflexive, symmetric, and
 * transitive. However, for two element a,b it can happen that they are incomparable,
 * this is to say that le(a,b)=le(b,a)=false is allowed.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: POSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface POSet extends Set {
  /**
   * Checks if one element is less than an other element.
   * 
   * <P>
   * 
   * Implementations should guarantee that <code>lt</code> is reflexive, transitive
   * and anti-symmetric. Furthermore, <code>lt(e1,e2)</code> should be the same as
   * <code>{@link #le le(e1,e2)}&&!{@link #equals equals(e1,e2)}</code>.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>e1</code> and
   *                           <code>e2</code> are elements
   * 	                       in this set and <code>e1</code> is
   *			       less or equals than <code>/e2</code>.
   *
   * @see de.rwth.domains.Domain#checkProperties(POSet poset)
   */
  public boolean lt(Object e1, Object e2);

  /**
   * Checks if one element is less than or equal than an other element.
   * 
   * <p>
   * 
   * Implementations should guarantee that <code>le</code> is reflexive, transitive
   * and symmetric. Furthermore, <code>le(e1,e2)</code> should be the same as
   * <code>{@link #lt lt(e1,e2)}||{@link #equals equals(e1,e2)}</code>.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>e1</code> and
   *                               <code>e2</code> are elements in
   *                               this set and <code>e1</code> is
   *                               less than <code>e2</code>.
   *
   * @see de.rwth.domains.Domain#checkProperties(POSet poset)
   */
  public boolean le(Object e1, Object e2);
}
