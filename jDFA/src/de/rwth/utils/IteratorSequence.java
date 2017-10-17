package de.rwth.utils; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator which goes over an array of base iterators in sequence.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: IteratorSequence.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class IteratorSequence implements Iterator {
  /**
   * The array of base iterators.
   *
   */
  protected Iterator[] iterators;
  
  /**
   * The current index in <code>iterators</code>
   *
   */
  protected int count;

  
  /**
   * Advances <code>count</code> to the next base iterator which still has elements
   * pending.
   *
   */
  protected void proceedToNext() {
    while (count<iterators.length && !iterators[count].hasNext())
      count++;
  }
  
  /**
   * Creates a new <code>IteratorSequence</code> instance from an array of base
   * iterators..
   *
   * @param iterators an <code>Iterator[]</code> value: The base iterators.
   */
  public IteratorSequence(Iterator[] iterators) {
    this.iterators=iterators;
    this.count=0;
    proceedToNext();
  }

  
  /**
   * Creates a new <code>IteratorSequence</code> instance from two base iterators.
   *
   * @param i1 an <code>Iterator</code> value: First base iterator.
   * @param i2 an <code>Iterator</code> value: Second base iterator.
   */
  public IteratorSequence(Iterator i1, Iterator i2) {
    this(new Iterator[] {i1,i2});
  }

  
  public Object next() throws NoSuchElementException {
    if (!hasNext()) throw new NoSuchElementException();
    Object result=iterators[count].next();
    proceedToNext();
    return result;
  }
  
  public boolean hasNext() {
    return count<iterators.length;
  }

  public void remove() {
    if (!hasNext()) throw new IllegalStateException();
    iterators[count].remove();
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    java.util.Vector v1 = new java.util.Vector();
    v1.add("1");
    v1.add("2");
    System.out.print("v1.iterator=");
    IteratorTools.printlnIterator(v1.iterator());
    
    java.util.Vector v2 = new java.util.Vector();
    v2.add("3");
    v2.add("4");
    System.out.print("v2.iterator=");
    IteratorTools.printlnIterator(v2.iterator());
    
    System.out.print("IteratorSequence(v1.iterator,v2.iterator)=");
    IteratorTools.printlnIterator(new IteratorSequence(v1.iterator(),v2.iterator()));
  }
} // class IteratorSequence
