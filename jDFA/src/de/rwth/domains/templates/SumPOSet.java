package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for creating partially ordered sets as the sum of partially ordered sets.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: SumPOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class SumPOSet extends SumSet implements POSet {
  /**
   * Creates a new <code>SumPOSet</code> instance from an array of components.
   *
   * @param posets a <code>POSet[]</code> value
   */
  public SumPOSet(POSet[] posets) {
    super(posets);
  }
  
  /**
   * Creates a new <code>SumPOSet</code> instance from two components.
   *
   * @param poset1 a <code>POSet</code> value
   * @param poset2 a <code>POSet</code> value
   */
  public SumPOSet(POSet poset1, POSet poset2) {
    super(new POSet[] {poset1, poset2});
  }

  
  /**
   * Returns true, if the relation holds in at least one component.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean lt(Object e1, Object e2) {
    for (int i=0;i<sets.length; i++) {
      if (((POSet)sets[i]).lt(e1,e2)) return true;
    }
    return false;
  }

  /**
   * Returns true, if the relation holds in at least one component.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean le(Object e1, Object e2) {
    for (int i=0;i<sets.length; i++) {
      if (((POSet)sets[i]).le(e1,e2)) return true;
    }
    return false;
  }
}
