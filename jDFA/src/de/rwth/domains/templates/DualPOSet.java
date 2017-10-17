package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.domains.*;

/**
 * Create partially ordered sets as dual of existing ones: invert less-than relation.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: DualPOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class DualPOSet implements POSet {
  /**
   * The original partially ordered set.
   *
   */
  protected POSet poset = null;
  
  /**
   * Creates a new <code>DualPOSet</code> instance from an existing partially ordered
   * set.
   *
   * @param poset a <code>POSet</code> value
   */
  public DualPOSet(POSet poset) {
    super();
    this.poset=poset;
  }

  
  /**
   * Returns the same as the corresponding method of the constructor argument of this
   * partially ordered set.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean equals(Object e1, Object e2) { return poset.equals(e1,e2); }
  
  /**
   * Returns the same as the corresponding method of the constructor argument of this
   * partially ordered set.
   *
   * @param e an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean isElement(Object e) { return poset.isElement(e); }

  /**
   * Returns the same as the corresponding method of the constructor argument of this
   * partially ordered set.
   *
   * @return an <code>Iterator</code> value
   */
  public Iterator iterator() { return poset.iterator(); }

  /**
   * Returns the same as the corresponding method of the constructor argument of this
   * partially ordered set.
   *
   * @return a <code>long</code> value
   */
  public long size() { return poset.size(); }

  /**
   * Returns the same as the corresponding method of the constructor argument of this
   * partially ordered set.
   *
   * @return an <code>Iterator</code> value
   */
  public Iterator iteratorSkel() { return poset.iteratorSkel(); }

  /**
   * Returns the same as the corresponding method of the constructor argument of this
   * partially ordered set.
   *
   * @return a <code>long</code> value
   */
  public long sizeSkel() { return poset.sizeSkel(); }

  
  /**
   * Reverses the argument order with respect to the corresponding method of the
   * constructor argument of this partially ordered set. 
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean lt(Object e1, Object e2) { return poset.lt(e2,e1); }
  
  /**
   * Reverses the argument order with respect to the corresponding method of the
   * constructor argument of this partially ordered set. 
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean le(Object e1, Object e2) { return poset.le(e2,e1); }
}
