package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.utils.SingletonIterator;
import de.rwth.utils.IteratorSequence;

import de.rwth.domains.*;

/**
 * Class for the creation of lifted partially ordered sets, which are partially
 * ordered sets with an unique additional least element.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: LiftedPOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class LiftedPOSet implements POSet {
  /**
   * The underlying partially ordered set.
   */
  protected POSet poset = null;

  /**
   * The unique new bottom element of this lifted partially ordered set.
   */
  protected Object bottom;
  
  /**
   * Lifts an existing partially ordered set by adding a new unique least element.
   *
   * @param <code>poset</code> the partially ordered set to be lifted
   */
  public LiftedPOSet(POSet poset, Object bottom) {
    super();
    this.poset=poset;
    this.bottom=bottom;
  }
  
  /**
   * Lifts an existing partially ordered set by adding a new unique least element.
   *
   * @param <code>poset</code> the partially ordered set to be lifted
   */
  public LiftedPOSet(POSet poset, final String botlabel) {
    this(poset,new Object() { public String toString() { return botlabel;}});
  }
  
  /**
   * Lifts an existing partially ordered set by adding a new unique least element.
   *
   * @param <code>poset</code> the partially ordered set to be lifted
   */
  public LiftedPOSet(POSet poset) {
    this(poset,"bot");
  }

  /**
   * Returns <code>true</code> either if both <code>e1</code> and <code>e2</code> are
   * the unique least element of this set of they are equal in the underlying set.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> if <code>e1</code> and <code>e2</code>
   *                           are equal in this set.
   */
  public boolean equals(Object e1, Object e2) {
    return (e1==bottom && e2==bottom)
      || (e1!=bottom && e2!=bottom && poset.equals(e1,e2));
  }
  
  /**
   * Returns <code>true</code> either if <code>e1</code> is the unique least element
   * of this set and <code>e2</code> is not or if <code>e1</code> is less than
   * <code>e2</code> in the underlying set.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> if <code>e1</code> is less than
   *                           <code>e2</code> in this set.
   */
  public boolean lt(Object e1, Object e2) {
    boolean result=(e1==bottom && e2!=bottom)
      || (e1!=bottom && e2!=bottom && poset.lt(e1,e2));
    //System.out.println("LiftedPOSet.lt("+e1+", "+e2+")="+result);
    return result;
  }
  
  /**
   * Returns <code>true</code> either if <code>e1</code> is the unique least element
   * of this set or if <code>e1</code> is less or equal than <code>e2</code> in the
   * underlying set.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> if <code>e1</code> is less than or 
   *                           equal than <code>e2</code> in this set.
   */
  public boolean le(Object e1, Object e2) {
    return (e1==bottom) || (e1!=bottom && e2!=bottom && poset.le(e1,e2));
  }
  
  /**
   * Returns the size of the set.
   *
   * @return -1 if the underlying set has an infinite number of elements and one more
   * than the size reported by the underlying set otherwise.
   */
  public long size() { return (poset.size()==-1)?(-1):(poset.size()+1);  }
  
  /**
   * Returns an <code>Iterator</code> of the elements of this set, staring with the
   * unique least element.
   *
   * @return an <code>Iterator</code> of all elements of this set.
   */
  public Iterator iterator() {
    return new IteratorSequence(poset.iterator(), new SingletonIterator(bottom)); 
  }
  
  /**
   * Returns the size of the set skeleton.
   *
   * @return -1 if the underlying set does not have a skeleton and one more than the
   * size reported by the underlying set otherwise.
   */
  public long sizeSkel() { return (poset.sizeSkel()==-1)?(-1):(poset.sizeSkel()+1);  }
  
  /**
   * Returns an <code>Iterator</code> of the elements of this set, staring with the
   * unique least element.
   *
   * @return an <code>Iterator</code> of all elements of this set.
   */
  public Iterator iteratorSkel() {
    return new IteratorSequence(poset.iteratorSkel(), new SingletonIterator(bottom)); 
  }

  /**
   * Returns <code>true</code> if <code>e</code> is the unique least element of this
   * set or if <code>e</code> is element of the underlying set.
   *
   * @param <code>e</code> a value of type <code>Object</code>
   * @return a value of type <code>boolean</code>
   */
  public boolean isElement(Object e) {
    return e==bottom || poset.isElement(e);
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    Set set = new SimpleSet(1, 5);
    POSet poset1 = new TrivialPOSet(set);
    LiftedPOSet poset = new LiftedPOSet(poset1);
    try {
      Domain.checkProperties(poset1);
      System.err.println(Domain.hasseDiagram(poset));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
