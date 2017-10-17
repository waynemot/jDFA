package de.rwth.domains.templates; // Generated package name

import java.util.Stack;

import de.rwth.domains.*;

/**
 * Class for (maybe infinite) partially ordered sets which consists of stacks of
 * elements from a set.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: StackPOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class StackPOSet extends StackSet implements POSet  {
  /**
   * Creates a new <code>StackPOSet</code> instance with unlimited stack size and
   * skeleton stack size SKELMAXSIZE.
   *
   * @param poset a <code>POSet</code> value
   */
  public StackPOSet(POSet poset) {
    super(poset);
  }
  
  /**
   * Creates a new <code>StackPOSet</code> instance with maximal stack size.
   *
   * @param poset a <code>POSet</code> value
   * @param maxSize an <code>int</code> value
   */
  public StackPOSet(POSet poset, int maxSize) {
    super(poset, maxSize);
  }

  
  /**
   * Returns <code>true</code> if both elements are stacks of the same height,
   * corresponding entries are in the <code>le()</code> relation of the underlying
   * partially ordered set, and at least one pair of corresponding entries are in the
   * <code>lt()</code> relation.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean lt(Object e1, Object e2) {
    Stack s1=(Stack)e1;
    Stack s2=(Stack)e2;
    if (s1.size()!=s2.size()) return false;
    boolean oneislt=false;
    for (int i=0; i<s1.size(); i++) {
      Object o1=s1.elementAt(i);
      Object o2=s2.elementAt(i);
      if (!(set.isElement(o1) && set.isElement(o2)))
	throw new IllegalArgumentException();
      if (!((POSet)set).le(o1,o2)) return false;
      oneislt|=((POSet)set).lt(o1,o2);
    }
    return oneislt;
  }
  
  /**
   * Returns <code>true</code> if both elements are stacks of the same height and
   * corresponding entries are in the <code>le()</code> relation of the underlying
   * partially ordered set.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean le(Object e1, Object e2) {
    Stack s1=(Stack)e1;
    Stack s2=(Stack)e2;
    if (s1.size()!=s2.size()) return false;
    for (int i=0; i<s1.size(); i++) {
      Object o1=s1.elementAt(i);
      Object o2=s2.elementAt(i);
      if (!(set.isElement(o1) && set.isElement(o2)))
	throw new IllegalArgumentException();
      if (!((POSet)set).le(o1,o2)) return false;
    }
    return true;
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      POSet s1 = new IntegerPOSet(1,2);
      POSet s = new StackPOSet(s1);
      System.err.println(Domain.hasseDiagram(s));
      Domain.checkProperties(s);
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
}
