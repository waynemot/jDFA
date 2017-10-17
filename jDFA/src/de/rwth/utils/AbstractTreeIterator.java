package de.rwth.utils; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Describe class <code>AbstractTreeIterator</code> here.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: AbstractTreeIterator.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public abstract class AbstractTreeIterator implements TreeIterator {
  /**
   */
  protected Position position;

  //public String toString() {
  //String result=super.toString()+"=[position"+position.hashCode()+"="+position+", pathIndexes={";
  //for (int i=0; i<pathIndexes.length; i++)
  //result+=pathIndexes[i]+(i==pathIndexes.length-1?"}":",");
  //return result+"]";
  //}
    
  /**
   * Describe constructor here.
   *
   */
  public AbstractTreeIterator() {
    this.position=new Position();
  }
  
  /**
   * Describe <code>set</code> method here.
   *
   * @param o a value of type <code>Object</code>
   */
  public void set(Object o) { throw new UnsupportedOperationException(); }
  
  /**
   * Describe <code>remove</code> method here.
   *
   */
  public void remove() { throw new UnsupportedOperationException(); }
  
  /**
   * Describe <code>add</code> method here.
   *
   * @param o a value of type <code>Object</code>
   */
  public void add(Object o) { throw new UnsupportedOperationException(); }

  /**
   * Describe <code>hasCurrent</code> method here.
   *
   * @return a value of type <code>boolean</code>
   */
  public boolean hasCurrent() {
    return !position.empty();
  }
  
  /**
   * Describe <code>current</code> method here.
   *
   * @return a value of type <code>Object</code>
   * @exception NoSuchElementException if an error occurs
   */
  public Object current()  throws NoSuchElementException {
    if (!hasCurrent()) throw new  NoSuchElementException();
    return externalView(position.peek().getObject());
  }

  /**
   * Describe <code>currentIndex</code> method here.
   *
   * @return a value of type <code>int</code>
   */
  public int currentIndex() {
    if (!hasCurrent()) return -1;
    return position.size()-1;
  }
  
  /**
   */
  private Object lastLeft;
  private Object lastRight;
  
  /**
   * Describe <code>hasLeft</code> method here.
   *
   * @return a value of type <code>boolean</code>
   */
  public boolean hasLeft() {
    if (lastLeft==null) lastLeft=produceLeft();
    return lastLeft!=null;
  }
  
  /**
   * Describe <code>hasRight</code> method here.
   *
   * @return a value of type <code>boolean</code>
   */
  public boolean hasRight() {
    if (lastRight==null) lastRight=produceRight();
    return lastRight!=null;
  }
  
  /**
   * Describe <code>leftIndex</code> method here.
   *
   * @return a value of type <code>int</code>
   */
  public int leftIndex() {
    if (!hasLeft()) return -1;
    return position.peek().getIndex()-1;
  }
  
  /**
   * Describe <code>rightIndex</code> method here.
   *
   * @return a value of type <code>int</code>
   */
  public int rightIndex() {
    if (!hasRight()) return -1;
    return position.peek().getIndex()+1;
  }
  
  /**
   * Describe <code>left</code> method here.
   *
   * @return a value of type <code>Object</code>
   * @exception NoSuchElementException if an error occurs
   */
  public Object left() throws NoSuchElementException {
    int index=leftIndex();
    if (index==-1) throw new NoSuchElementException();
    position.pop();
    position.push(lastLeft, index);
    lastLeft=null;
    lastRight=null;
    lastNext=null;
    return externalView(position.peek().getObject());
  }
  
  /**
   * Describe <code>right</code> method here.
   *
   * @return a value of type <code>Object</code>
   * @exception NoSuchElementException if an error occurs
   */
  public Object right() throws NoSuchElementException {
    int index=rightIndex();
    if (index==-1) throw new NoSuchElementException();
    position.pop();
    position.push(lastRight, index);
    lastLeft=null;
    lastRight=null;
    lastNext=null;
    return externalView(position.peek().getObject());
  }

  /**
   */
  private Object lastNext;

  /**
   * Describe <code>hasNext</code> method here.
   *
   * @return a value of type <code>boolean</code>
   */
  public boolean hasNext() {
    if (lastNext==null) lastNext=produceNext();
    return lastNext!=null;
  }
  
  /**
   * Describe <code>hasPrevious</code> method here.
   *
   * @return a value of type <code>boolean</code>
   */
  public boolean hasPrevious() { return position.size()>1; }
  
  /**
   * Describe <code>next</code> method here.
   *
   * @return a value of type <code>Object</code>
   * @exception NoSuchElementException if an error occurs
   */
  public Object next() throws NoSuchElementException {
    if (!hasNext()) throw new NoSuchElementException();
    position.push(lastNext,0);
    lastLeft=null;
    lastRight=null;
    lastNext=null;
    return externalView(position.peek().getObject());
  }
  
  /**
   * Describe <code>previous</code> method here.
   *
   * @return a value of type <code>Object</code>
   * @exception NoSuchElementException if an error occurs
   */
  public Object previous() throws NoSuchElementException {
    if (!hasPrevious()) throw new  NoSuchElementException();
    position.pop();
    return externalView(position.peek().getObject());
  }
  
  /**
   * Describe <code>nextIndex</code> method here.
   *
   * @return a value of type <code>int</code>
   */
  public int nextIndex() {
    if (!hasNext()) return -1;
    return position.size();
  }
  
  /**
   * Describe <code>previousIndex</code> method here.
   *
   * @return a value of type <code>int</code>
   */
  public int previousIndex() {
    if (!hasPrevious()) return -1;
    return position.size()-2;
  }
  
  /**
   * Describe <code>position</code> method here.
   *
   * @return a value of type <code>Position</code>
   */
  public Position position() {
    Position result=new Position();
    for (Iterator i=position.iterator(); i.hasNext(); ) {
      Position.Entry e = (Position.Entry)i.next();
      result.push(externalView(e.getObject()), e.getIndex());
    }
    return result;
  }
  
  /**
   * Describe <code>produceNext</code> method here.
   *
   * @return a value of type <code>Object</code>
   */
  abstract public Object produceNext();
  
  /**
   * Describe <code>produceLeft</code> method here.
   *
   * @return a value of type <code>Object</code>
   */
  abstract public Object produceLeft();
  
  /**
   * Describe <code>produceRight</code> method here.
   *
   * @return a value of type <code>Object</code>
   */
  abstract public Object produceRight();

  /**
   * Describe <code>externalView</code> method here.
   *
   * @param o a value of type <code>Object</code>
   * @return a value of type <code>Object</code>
   */
  abstract public Object externalView(Object o);
} // AbstractTreeIterator
