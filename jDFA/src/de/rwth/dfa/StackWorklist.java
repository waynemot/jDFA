package de.rwth.dfa; // Generated package name

import java.util.Stack;
import java.util.NoSuchElementException;

/**
 * A work list implementation using stacks.
 * <ol>
 * <li>The last recently added element is retrieved.</li>
 * <li>Elements are stored just once.</li>
 * </ol>
 * 
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: StackWorklist.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class StackWorklist extends Stack implements Worklist {
  /**
   * Creates a new <code>StackWorklist</code> instance.
   *
   */
  public StackWorklist() {
    super();
  }

  /**
   * Adds a new element on top of the stack. Does not add the element if its already
   * stored.
   *
   * @param o an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean add(Object o) {
    if (search(o)==-1) {
      push(o);
      return true;
    }
    return false;
  }

  
  /**
   * Gets the last recently added element.
   *
   * @return an <code>Object</code> value
   * @exception NoSuchElementException if an error occurs
   */
  public Object get() throws NoSuchElementException {
    if (isEmpty()) throw new NoSuchElementException();
    return pop();
  }

  
  /**
   * Moves the element to the top of this stack.
   *
   * @param o an <code>Object</code> value
   * @exception NoSuchElementException if an error occurs
   */
  public void moveToFront(Object o) throws NoSuchElementException {
    if (search(o)==-1) throw new NoSuchElementException();
    removeElement(o);
    push(o);
  }
}
