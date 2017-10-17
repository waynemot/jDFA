package de.rwth.utils; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides a default implementation for the <code>Iterator</code> interface. To get
 * a working implementation of <code>Iterator</code> only the methods {@link
 * #produceNext() produceNext()} and {@link #externalView(Object o)
 * externalView(Object o)} must be defined in a subclass.
 * 
 * <p>
 * 
 * This class allows to distinguish between internal (seen inside this class) and
 * external representation (seen by the user of the <code>Iterator</code>).
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: AbstractIterator.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public abstract class AbstractIterator implements Iterator {
  /**
   * The object which stores the result of the last call to produceNext().
   */
  private Object lastNext;
  
  public boolean hasNext() {
    if (lastNext==null) lastNext=produceNext();
    return lastNext!=null;
  }
  
  public Object next() throws NoSuchElementException {
    if (!hasNext()) throw new NoSuchElementException();
    Object result=lastNext;
    lastNext=null;
    return externalView(result);
  }
  
  public void remove() {throw new UnsupportedOperationException();}

  /**
   * The <code>produceNext</code> method is supposed to produce the internal
   * representation of the next element of the <code>Iterator</code>. If there are no
   * more elements, it should indicate this byte returning <code>null</code>. This
   * method is guaranteed to be called exactly once to produce a new element,
   * independently on how often {@link #hasNext() hasNext()} is called before a call
   * to {@link #next() next()}.
   *
   * @return a value of type <code>Object</code>
   */
  public abstract Object produceNext();
  
  /**
   * The <code>externalView</code> method is supposed to convert internal to external
   * representation.
   *
   * @param o a value of type <code>Object</code>
   * @return a value of type <code>Object</code>
   */
  public abstract Object externalView(Object o);
} // class AbstractIterator
