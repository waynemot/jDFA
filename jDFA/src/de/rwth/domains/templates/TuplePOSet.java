package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of partially ordered sets as tuple from other partially
 * ordered sets.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TuplePOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class TuplePOSet extends TupleSet implements POSet {
  /**
   * Creates a <code>TuplePOSet</code> from an array of <code>POSets</code>.
   *
   * @param <code>posets</code> the component <code>POSet</code>
   */
  public TuplePOSet(POSet[] posets) {
    super(posets);
  }
  
  /**
   * Creates a <code>TuplePOSet</code> from two <code>POSets</code>.
   *
   * @param <code>poset1</code> 1st component <code>Set</code>
   * @param <code>poset2</code> 2nd component <code>Set</code>
   */
  public TuplePOSet(POSet poset1, POSet poset2) {
    this(new POSet[] {poset1, poset2});
  }

  /**
   * Checks if one element is less than an other element.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>e1</code> and
   *                               <code>e2</code> are
   *                               <code>TupleElements</code> of
   *                               matching length and the components
   *				   of <code>e1</code> are less or equals
   *                               than the corresponding components
   *                               of <code>e2</code>.
   */
  public boolean lt(Object e1, Object e2) {
    Object[] ee1 = ((TupleElement)e1).getElements();
    Object[] ee2 = ((TupleElement)e2).getElements();
    if (ee1.length!=sets.length || ee2.length!=sets.length)
      throw new IllegalArgumentException();
    boolean oneislt=false;
    for (int i=0; i<sets.length; i++) {
      if (!(sets[i].isElement(ee1[i]) && sets[i].isElement(ee2[i])))
	throw new IllegalArgumentException();
      if (!((POSet)sets[i]).le(ee1[i],ee2[i])) return false;
      oneislt|=((POSet)sets[i]).lt(ee1[i],ee2[i]);
    }
    return oneislt;
  }
  
  /**
   * Checks if one element is less than or equal than an other element.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>e1</code> and
   *                               <code>e2</code> are
   *                               <code>TupleElements</code> of
   *				   matching length and the components
   *				   of <code>e1</code> are less than
   * 				   the corresponding components of
   * 				   <code>e2</code>.
   */
  public boolean le(Object e1, Object e2) {
    Object[] ee1 = ((TupleElement)e1).getElements();
    Object[] ee2 = ((TupleElement)e2).getElements();
    if (ee1.length!=sets.length || ee2.length!=sets.length)
      throw new IllegalArgumentException();
    for (int i=0; i<sets.length; i++) {
      if (!(sets[i].isElement(ee1[i]) &&
	    sets[i].isElement(ee2[i]))) throw new IllegalArgumentException();
      if (!(((POSet)sets[i]).le(ee1[i],ee2[i]))) return false;
    }
    return true;
  }
}
