package de.rwth.dfa; // Generated package name

import java.util.NoSuchElementException;

/**
 * This interface describes the requirements for work list implementations used the
 * the {@link DataFlowSolver}. A work list is just a collection data structure,
 * capable of storing elements. The strategy in which elements are retrieved is
 * implementation dependent. Despite its name, it does not need to be a list: The
 * same element does not have to be stored more than once.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Worklist.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface Worklist  {
  /**
   * This methods adds an element to the work list. This method may be called
   * multiple times with the same element without calls to <code>get()</code> in
   * between. An implementation is free to choose if at any time, such element are
   * contained more than once.
   *
   * @param o an <code>Object</code> value
   * @return a <code>boolean</code> value: whether the element was added or not.
   */
  public boolean add(Object o);

 
  /**
   * Checks if there are elements in the work list.
   *
   * @return a <code>boolean</code> value
   */
  public boolean isEmpty();
  
  /**
   * Chooses a previously added element from the work list and returns
   * it. Implementations are free to choose which element to select.
   *
   * @return an <code>Object</code> value
   * @exception NoSuchElementException if there are no elements in this work list
   */
  public Object get() throws NoSuchElementException;

  /**
   * Maybe makes a previously added element the next to be retrieved by
   * <code>get()</code>. An implementation is free to ignore calls.
   *
   * @param o an <code>Object</code> value
   * @exception NoSuchElementException if <code>o</code> is not in the list
   */
  public void moveToFront(Object o) throws NoSuchElementException;

  /**
   * Returns the number of elements stored in this work list.
   *
   * @return an <code>int</code> value
   */
  public int size();
}
