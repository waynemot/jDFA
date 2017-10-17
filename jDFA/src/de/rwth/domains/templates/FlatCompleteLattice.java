package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.utils.IteratorSequence;
import de.rwth.utils.SingletonIterator;

import de.rwth.domains.*;

/**
 * Creates a flat complete lattice from a set and explicit top and bottom
 * elements. The Hasse diagram of the resulting structure looks like this:
 * <PRE>
 *       TOP
 *  / ... | ... \
 *  (set elements)
 *  \ ... | ... /
 *      BOTTOM
 * </PRE>
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FlatCompleteLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FlatCompleteLattice implements CompleteLattice {
  
  /**
   * The top element.
   *
   */
  protected Object bottom = null;
  
  /**
   * The bottom element.
   *
   */
  protected Object top = null;
  /**
   * The set inside.
   *
   */
  protected Set set = null;
  
  /**
   * Creates a new <code>FlatCompleteLattice</code> instance from a set and explicit
   * top and bottom elements.
   *
   * @param set a <code>Set</code> value
   * @param bottom an <code>Object</code> value
   * @param top an <code>Object</code> value
   */
  public FlatCompleteLattice(Set set, Object bottom, Object top) {
    this.set=set;
    this.bottom=bottom;
    this.top=top;
  }
  
  /**
   * Creates a new <code>FlatCompleteLattice</code> instance from a set and names for
   * newly created unique top and bottom elements.
   *
   * @param set a <code>Set</code> value
   * @param botlabel a <code>String</code> value
   * @param toplabel a <code>String</code> value
   */
  public FlatCompleteLattice(Set set, final String botlabel, final String toplabel) {
    this(set,new Object() { public String toString() { return botlabel;}},
	     new Object() { public String toString() { return toplabel;}});
  }

  /**
   * Creates a new <code>FlatCompleteLattice</code> instance from a set. Top and
   * bottom elements are newly created and named "top" and "bot".
   *
   * @param set a <code>Set</code> value
   */
  public FlatCompleteLattice(Set set) {
    this(set,"bot","top");
  }
  
  public boolean equals(Object e1, Object e2) {
    return e1==e2 || set.equals(e1,e2);
  };

  
  /**
   * Elements are those which are either top, bottom or one of the elements of the
   * set.
   *
   * @param e an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean isElement(Object e) {
    return e==top || e==bottom || set.isElement(e);
  }
  
  public Iterator iterator() {
    return new IteratorSequence(new Iterator[] {
      set.iterator(), new SingletonIterator(bottom), new SingletonIterator(top)}); 
  }

  
  /**
   * The size of the set plus 2, or -1 if the set is infinite.
   *
   * @return a <code>long</code> value
   */
  public long size() { return (set.size()==-1)?(-1):(set.size()+2);  }
  
  /**
   * The size of the set skeleton plus 2, or -1 if the set skeleton is infinite.
   *
   * @return a <code>long</code> value
   */
  public long sizeSkel() { return (set.sizeSkel()==-1)?(-1):(set.sizeSkel()+2); }

  public Iterator iteratorSkel() {
    return new IteratorSequence(new Iterator[] {
      set.iteratorSkel(), new SingletonIterator(bottom), new SingletonIterator(top)}); 
  }

  
  /**
   * Bottom is less-or-equal to everything, everything is less-or-equal to top, and
   * set elements are never less-than each other.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean le(Object e1, Object e2) {
    return (e1==bottom || e2==top || equals(e1,e2));
  }

  public boolean lt(Object e1, Object e2) {
    return !equals(e1,e2) && le(e1,e2);
  }
  
  /**
   * If the arguments are equal, then the meet is, too; Otherwise, its bottom.
   *
   * @param o1 an <code>Object</code> value
   * @param o2 an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object meet(Object o1, Object o2) {
    if (le(o1,o2)) return o1;
    else if (le(o2,o1)) return o2;
    else return bottom;
  }

  /**
   * If the arguments are equal, then the join is, too; Otherwise, its top.
   *
   * @param o1 an <code>Object</code> value
   * @param o2 an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object join(Object o1, Object o2) {
    if (le(o1,o2)) return o2;
    else if (le(o2,o1)) return o1;
    else return top;
  }
  
  public Object top() { return top;}
  
  public Object bottom() { return bottom; }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    FlatCompleteLattice lattice = new FlatCompleteLattice(new SimpleSet(1,2));
    try {
      System.err.println(Domain.hasseDiagram(lattice));
      Domain.checkProperties(lattice);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
} // class FlatCompleteLattice
