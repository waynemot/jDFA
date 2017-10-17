package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.utils.SingletonIterator;

import de.rwth.domains.*;

/**
 * Class for the set of all <code>Number</code> objects.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: NumberSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 * @see java.lang.Number
 */
public class NumberSet implements Set {
   /**
   * Creates a new 'NumberSet' instance.
   *
   */
  public NumberSet() { super(); }
  
  /**
   * Two <code>Numbers</code> objects are equal in this set, if the first is not
   * <code>null</code> and is equal to the second.
   *
   * @param param1 <description>
   * @param param2 <description>
   * @return <description>
   */
  public boolean equals(Object param1, Object param2) {
    return param1!=null && param1.equals(param2);
  }
  
  /**
   * All <code>Number</code> objects are element of this set.
   *
   * @param param1 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean isElement(Object param1) {
    return (param1 instanceof Number);
  }
  
  /**
   * Always throws an <code>IllegalArgumentException</code>.
   *
   * @return <description>
   */
  public Iterator iterator() { throw new IllegalArgumentException(); }
  
  /**
   * Returns an iterator, which has 42 as only element.
   *
   * @return an <code>Iterator</code> value
   */
  public Iterator iteratorSkel() {
    return new SingletonIterator(new Integer(42));
  }
  
  /**
   * Always returns -1.
   *
   * @return a <code>long</code> value
   */
  public long size() { return -1; }
  
  /**
   * Always returns 1.
   *
   * @return a <code>long</code> value
   */
  public long sizeSkel() { return 1; }
  
} // class NumberSet
