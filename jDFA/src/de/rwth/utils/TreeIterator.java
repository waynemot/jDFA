package de.rwth.utils; // Generated package name

import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * The <code>TreeIterator</code> interface provides a means for traversing a
 * tree. The methods inherited from the <code>ListIterator</code> interface traverse
 * up and down in the tree, the new methods declared here move to the left and right.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TreeIterator.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface TreeIterator extends ListIterator {
  /**
   * Returns <code>true</code> iff there is a current element available. In other
   * words, it will return false iff no call to <code>getNext</code> has been done.
   *
   * @return <code>true</code> iff there is a current element available
   */
  public boolean hasCurrent();

  /**
   * Returns the current element.
   *
   * @return The current <code>Object</code>
   * @exception NoSuchElementException if there is no current element.
   */
  public Object current()  throws NoSuchElementException;

  /**
   * Returns the index of the current element among the children of the current
   * parent. The first element after a call to <code>next</code> has index 0.
   *
   * @return the index of the current element of <code>-1</code> if there is no
   * current element.
   */
  public int currentIndex();

  /**
   * Checks if there is an element to the left in the tree. After a call to the
   * method <code>next</code> this method will always return <code>false</code>.
   *
   * @return <code>true</code> iff there is an element to the left in the tree.
   */
  public boolean hasLeft();
  
  /**
   * Checks if there is an element to the right in the tree.
   *
   * @return <code>true</code> iff there is an element to the right in the tree.
   */
  public boolean hasRight();

  /**
   * Returns the element to the left.
   *
   * @return the <code>Object</code> to the left.
   * @exception NoSuchElementException if there is no element to the left.
   */
  public Object left() throws NoSuchElementException;

  /**
   * Returns the element to the right.
   *
   * @return the <code>Object</code> to the right.
   * @exception NoSuchElementException if there is no element to the right
   */
  public Object right() throws NoSuchElementException;
  
  /**
   * Returns the index of the left element among the children of the current parent.
   *
   * @return index of the left element; <code>-1</code> if there is no element to the
   * left
   */
  public int leftIndex();
  
  /**
   * Returns the index of the right element among the children of the current parent.
   *
   * @return index of the right element; <code>-1</code> if there is no element to
   * the right
   */
  public int rightIndex();

  /**
   * Gets the current <code>position</code> in this tree.
   *
   * @return a value of type <code>Position</code>
   */
  public Position position();
} // TreeIterator
