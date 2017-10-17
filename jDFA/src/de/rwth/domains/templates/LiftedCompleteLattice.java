package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.utils.IteratorSequence;
import de.rwth.utils.SingletonIterator;

import de.rwth.domains.*;

/**
 * Class for the creation of lifted complete lattices by adding an unique additional
 * top element to a pre lattice.
 *
 * <p> <strong>There is no check if the resulting structure fulfills the specified
 * {@link de.rwth.domains.Domain#checkProperties(PreLattice lattice)
 * constraints}!</strong>
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: LiftedCompleteLattice.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class LiftedCompleteLattice extends LiftedPOSet implements CompleteLattice {
  /**
   * The unique new top element of this lifted lattice.
   */
  protected Object top; 
  
  /**
   * Lifts an existing pre lattice  to a lattice by adding a top element.
   *
   * @param prelattice a <code>PreLattice</code> value
   * @param top an <code>Object</code> value
   */
  public LiftedCompleteLattice(PreLattice prelattice, Object top) {
    super(prelattice);
    this.top=top;
  }
  
  /**
   * Lifts an existing pre lattice  to a lattice by adding newly created
   * unique top element name toplabel.
   *
   * @param prelattice a <code>PreLattice</code> value
   * @param toplabel a <code>String</code> value
   */
  public LiftedCompleteLattice(PreLattice prelattice, final String toplabel) {
    this(prelattice,new Object() { public String toString() { return toplabel;}});
  }
  
  /**
   * Lifts an existing pre lattice to a lattice by adding newly created unique top
   * element name "top".
   *
   * @param prelattice a <code>PreLattice</code> value
   */
  public LiftedCompleteLattice(PreLattice prelattice) {
    this(prelattice,"top");
  }
  
  /**
   * Lifts an existing partially ordered set to a lattice by adding newly created
   * unique top element name "top" and newly created unique bottom element name
   * "bottom".
   *
   * @param poset a <code>POSet</code> value
   * @param bottom an <code>Object</code> value
   * @param top an <code>Object</code> value
   */
  public LiftedCompleteLattice(POSet poset, Object bottom, Object top) {
    super(poset,bottom);
    this.top=top;
  }
  
  /**
   * Returns <code>true</code> either if both <code>e1</code> and <code>e2</code> are
   * the unique top element of this set of they are equal in the underlying set.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> if <code>e1</code> and <code>e2</code>
   *                           are equal in this set.
   */
  public boolean equals(Object e1, Object e2) {
    return (e1==top && e2==top) || (e1!=top && e2!=top && super.equals(e1,e2));
  }

  /**
   * Returns <code>true</code> either if <code>e1</code> is the unique top element of
   * this set and <code>e2</code> is not or if <code>e1</code> is less than
   * <code>e2</code> in the underlying set.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> if <code>e1</code> is less than
   *                           <code>e2</code> in this set.
   */
  public boolean lt(Object e1, Object e2) {
    boolean result=(e1!=top && e2==top) || (e1!=top && e2!=top && super.lt(e1,e2));
    return result;
  }
  
  /**
   * Returns <code>true</code> either if <code>e1</code> is the unique top element of
   * this set or if <code>e1</code> is less or equal than <code>e2</code> in the
   * underlying set.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> if <code>e1</code> is less than or 
   *                           equal than <code>e2</code> in this set.
   */
  public boolean le(Object e1, Object e2) {
    boolean result=(e2==top) || (e1!=top && e2!=top && super.le(e1,e2));
    return result;
  }
  
  /**
   * The method <code>iterator</code> returns an
   * <code>Iterator</code> of the elements of this set, staring
   * with the unique least element.
   *
   * @return an <code>Iterator</code> of all elements of this set.
   */
  public Iterator iterator() {
    return new IteratorSequence(super.iterator(), new SingletonIterator(top));
  }
  
  /**
   * The method <code>size</code> returns the size of the set.
   *
   *  @return -1 if the underlying set has an infinite number of
   *  elements and one more than the size reported by the underlying
   *  set otherwise.
   */
  
  public long sizeSkel() { return (super.sizeSkel()==-1)?(-1):(super.sizeSkel()+1);  }
  
  /**
   * The method <code>iterator</code> returns an
   * <code>Iterator</code> of the elements of this set, staring
   * with the unique least element.
   *
   * @return an <code>Iterator</code> of all elements of this set.
   */
  public Iterator iteratorSkel() {
    return new IteratorSequence(super.iteratorSkel(), new SingletonIterator(top));
  }
  
  /**
   * The method <code>size</code> returns the size of the set.
   *
   *  @return -1 if the underlying set has an infinite number of
   *  elements and one more than the size reported by the underlying
   *  set otherwise.
   */
  public long size() { return (super.size()==-1)?(-1):(super.size()+1);  }
  
  /**
   * The method <code>isElement</code> returns <code>true</code> if
   * <code>e</code> is the unique least element of this set or if
   * <code>e</code> is element of the underlying set.
   *
   * @param <code>e</code> a value of type <code>Object</code>
   * @return a value of type <code>boolean</code>
   */
  public boolean isElement(Object e) {
    return e==top || super.isElement(e);
  }

  /**
   * Computes the greatest lower bound of two elements in the following way: If
   * <code>le(e1,e2)</code> holds then this is <code>e1</code> and vice
   * versa. Otherwise, its <code>bottom()</code>.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the greatest lower bound of <code>e1</code> and <code>e2</code>
   *
   * @see de.rwth.domains.Domain#checkProperties(PreLattice l)
   */
  public Object meet(Object e1, Object e2) {
    Object result;
    if (e1==bottom || e2==bottom) result=bottom;
    else if (e1==top) result=e2;
    else if (e2==top) result=e1;
    else result=((PreLattice)poset).meet(e1,e2);
    if (result==null) result=bottom;
    //System.out.println(this+": meet("+e1+", "+e2+")="+result);
    return result;
  }
  
  /**
   * Computes the least upper bound of two elements in the following way: If
   * <code>le(e1,e2)</code> holds then this is <code>e2</code> and vice
   * versa. Otherwise, its <code>top()</code>.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return the least upper bound of <code>e1</code> and <code>e2</code>
   *
   * @see de.rwth.domains.Domain#checkProperties(PreLattice l)
   */
  public Object join(Object e1, Object e2) {
    Object result;
    if (e1==top || e2==top) result=top;
    else if (e1==bottom) result=e2;
    else if (e2==bottom) result=e1;
    else result=((PreLattice)poset).join(e1,e2);
    if (result==null) result=top;
    return result;
  }

  public Object top() {
    return top;
  }
  
  public Object bottom() {
    return bottom;
  }

  /*
  public static void main(String[] args) {
    PreLattice prelattice1 = new StackPreLattice(new BitVectorLattice(1));
    LiftedCompleteLattice lattice = new LiftedCompleteLattice(new LiftedCompleteLattice(prelattice1,"bi","ti"),"bo","to");
    try {
      System.err.println(ANALYSIS.HasseDiagram.hasseDiagram(lattice));
      Domain.checkProperties(lattice);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  */
} // class LiftedCompleteLattice
