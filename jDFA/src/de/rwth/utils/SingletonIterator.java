package de.rwth.utils; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements an <code>Iterator</code> with a single, non-<code>null</code> entry.
 * 
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: SingletonIterator.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 * @see Iterator
 */
public class SingletonIterator implements Iterator {
  
  /**
   * Contains the entry, or <code>null</code> if this element has already been
   * retrieved.
   */
  protected Object o;
  
  /**
   * Creates a new <code>SingletonIterator</code> instance.
   *
   * @param o an <code>Object</code> value. If this is <code>null</code>, then
   * the <code>Iterator</code> is empty.
   */
  public SingletonIterator(Object o) {
    this.o=o;
  }

  /**
   * Implements the <code>hasNext</code> method from the <code>Iterator</code>
   * interface.
   *
   * @return a <code>boolean</code> value
   */
  public boolean hasNext() { return (o!=null); }

  
  /**
   * Implements the <code>next</code> method from the <code>Iterator</code>
   * interface.
   *
   * @return an <code>Object</code> value
   */
  public Object next() {
    if (!hasNext()) throw new NoSuchElementException();
    Object result=o;
    o=null;
    return result;
  }
  
  /**
   * Implements the <code>next</code> method from the <code>Iterator</code> interface
   * by always throwing an <code>UnsupportedOperationException</code>.
   *
   */
  public void remove() {throw new UnsupportedOperationException();}
  
} // class SingletonIterator
