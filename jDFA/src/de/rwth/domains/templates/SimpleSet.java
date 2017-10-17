package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.rwth.domains.*;

/**
 * Class for the creation of simple sets by explicitly providing all elements.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: SimpleSet.java,v 1.4 2002/09/27 09:39:46 mohnen Exp $
 */
public class SimpleSet implements Set {
  /**
   * Contains all elements of this set.
   */
  protected Object[] elements = null;
  
  /**
   * Creates the empty set.
   */
  public SimpleSet() {
    super();
    elements=new Object[0];
  }
  
  /**
   * Creates a set from an array of <code>Objects</code>.
   *
   * @param <code>os</code> all the elements of the new set.
   */
  public SimpleSet(Object[] os) {
    super();
    elements=(Object[])os.clone();
  }
  
  /**
   * Creates a set with one element.
   *
   * @param <code>o</code> the <code>Object</code> which will be the
   * element of the new set.
   */
  public SimpleSet(Object o) {this(new Object[] {o});}
  
  /**
   * Creates a set with one <code>int</code> as element.
   *
   * @param <code>i</code> the <code>int</code> which will be the
   * element of the new set.
   */
  public SimpleSet(int i) { this(new Integer(i));}

  /**
   * Creates a set with a range of elements.
   *
   * @param <code>from</code> the first element of the set
   * @param <code>to</code> the last element of the set
   */
  public SimpleSet(int from, int to) {
    super();
    if (from<=to) {
      elements=new Object[to-from+1];
      for (int i=from; i<=to; i++) {
	elements[i-from]=new Integer(i);
      }
    } else {
      elements=new Object[from-to+1];
      for (int i=from; i>=to; i--) {
	elements[i-to]=new Integer(i);
      }
    }
  }
  
  /**
   * Creates a set from an array of <code>ints</code>.
   *
   * @param <code>is</code> all the elements of the new set.
   */
  public SimpleSet(int[] is) {
    super();
    elements=new Object[is.length];
    for (int i=0; i<elements.length; i++)
      elements[i]=new Integer(is[i]);
  }
  
  /**
   * Checks if an <code>Object</code> is contained in this set. It uses the method
   * {@link #equals} with the parameter <code>e</code> as second parameter to check
   * for equality.
   *
   * @param <code>e</code> a value of type <code>Object</code>
   * @return a value of type <code>boolean</code>
   */
  public boolean isElement(Object e) {
    for (int i=0; i<elements.length; i++)
      if ((elements[i]==null && e==null)
	  || (elements[i]!=null && e!=null && equals(elements[i],e))) return true;
    return false;
  }

  /**
   * Checks if two objects are equal.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return a value of type <code>boolean</code>
   */
  public boolean equals(Object e1, Object e2) {
    return (e1==null && e2==null) || (e1!=null && e1.equals(e2));
  }
  
  /**
   * Returns an <code>Iterator</code> of the elements of this set.
   *
   * @return an <code>Iterator</code> of all elements of this set.
   */
  public Iterator iterator() {
    return new Iterator() {
      protected int i=0;
      public boolean hasNext() {return i<elements.length;}
      public Object next() throws NoSuchElementException {
	if (i<elements.length) {
	  return elements[i++];
	} else {
	  throw new NoSuchElementException();
	}
      }
      public void remove() {throw new UnsupportedOperationException();}
    };
  }

  /**
   * Returns the size of this set.
   *
   * @return the number of elements
   */
  public long size() { return elements.length; }
  
  /**
   * Returns the size of the skeleton subset. This is the same as
   * <code>size()</code>.
   *
   * @return the number of elements in the skeleton subset.
   */
  public long sizeSkel() { return size(); }

  /**
   * Returns an <code>Iterator</code> of the elements of the skeleton subset of this
   * set. This is the same as <code>iterator</code>.
   *
   * @return an <code>Iterator</code> of all elements of the skeleton
   * of this set.
   */
  public Iterator iteratorSkel() { return iterator();}

  public String toString() {
    String result="{";
    for (int i=0; i<elements.length; i++) {
      result+=((i==0)?"":",")+elements[i];
    }
    result+="}";
    return result;
  }
}
