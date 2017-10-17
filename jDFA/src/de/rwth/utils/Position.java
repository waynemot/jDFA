package de.rwth.utils; // Generated package name

import java.util.Stack;
import java.util.Iterator;

/**
 * Describes positions in a tree as sequence of child selection indices from the
 * root.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Position.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class Position {
  /**
   * The representation of the position.
   *
   */
  protected Stack stack;

  
  /**
   * Describes a single child selection index;
   *
   */
  public class Entry {
    /**
     * The object at the position.
     *
     */
    protected Object o;
    
    /**
     * The index with respect to the parent node in the tree.
     *
     */
    protected int index;

    
    /**
     * Creates a new <code>Entry</code> instance.
     *
     * @param o an <code>Object</code> value
     * @param index an <code>int</code> value
     */
    public Entry(Object o, int index) {
      this.o=o;
      this.index=index;
    }
    
    /**
     * Returns the index with respect to the parent node of this Entry..
     *
     * @return an <code>int</code> value
     */
    public int getIndex() { return index; }

    
    /**
     * Returns the object at this entry.
     *
     * @return an <code>Object</code> value
     */
    public Object getObject() { return o; }

    public String toString() {
      return "<"+o+","+index+">";
    }
    
  }

  
  /**
   * Creates a new <code>Position</code> instance.
   *
   */
  public Position() {
    this.stack=new Stack();
  }

  
  /**
   * Checks if the <code>Position</code> has no entries.
   *
   * @return a <code>boolean</code> value
   */
  public boolean empty() { return stack.empty(); }

  
  /**
   * Looks at the object at the top of this position without removing it.
   *
   * @return an <code>Entry</code> value
   */
  public Entry peek() { return (Entry)stack.peek(); }

  
  /**
   * Returns the number of components.
   *
   * @return an <code>int</code> value
   */
  public int size() { return stack.size(); }

  /**
   * Pushes an entry onto the top of this position.
   *
   * @param e an <code>Entry</code> value
   */
  public void push(Entry e) { stack.push(e); }
  
  /**
   * Creates a new Entry and pushes it onto the top of this position.
   *
   * @param o an <code>Object</code> value
   * @param i an <code>int</code> value
   */
  public void push(Object o, int i) { push(new Entry(o,i)); }

  /**
   * Removes the entry at the top of this posotion and returns that entry.
   *
   * @return an <code>Entry</code> value
   */
  public Entry pop() { return (Entry)stack.pop(); }
  
  /**
   * Returns the element at the specified index (0 for beginning). If the position is
   * less than zero, the positive index counts from the end of the position.
   *
   * @param i an <code>int</code> value: postion
   * @return an <code>Entry</code> value
   */
  public Entry get(int i) {
    if (i<0)
      return (Entry)stack.get(stack.size()+i);
    else
      return (Entry)stack.get(i);
  }

  /**
   * Returns an iterator over the entries in this position in proper sequence.
   *
   * @return an <code>Iterator</code> value
   */
  public Iterator iterator() { return stack.iterator(); }

  
  /**
   * Checks if this position is below another position <code>p</code>. This is the
   * case iff <code>p</code> is a prefix of this position.
   *
   * @param p a <code>Position</code> value
   * @return a <code>boolean</code> value
   */
  public boolean isBelow(Position p) {
    if (stack.size()<=p.stack.size()) return false;
    for (int i=0; i<p.stack.size(); i++)
      if (((Entry)stack.get(i)).getIndex()!=
	  ((Entry)p.stack.get(i)).getIndex()) return false;
    return true;
  }

  
  /**
   * Creates a short string representation by omitting the objects in the entries.
   *
   * @return a <code>String</code> value
   */
  public String toShortString() {
    String result = "[";
    Iterator i=stack.iterator();
    i.next();
    while (i.hasNext())
      result+=((Entry)i.next()).getIndex()+(i.hasNext()?",":"");
    result+="]";
    return result;
  }
  
  public String toString() {
    return stack.toString();
  }
  
} // class Position

