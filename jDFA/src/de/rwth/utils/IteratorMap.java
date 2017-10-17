package de.rwth.utils; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator created by applying an function to all elements of a base iterator.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: IteratorMap.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class IteratorMap implements Iterator {
  /**
   * The interface used to describe a function.
   *
   */
  public interface Function { public Object map(Object o); }

  
  /**
   * The base iterator.
   *
   */
  protected Iterator i;

  
  /**
   * The function to apply to the arguments.
   *
   */
  protected Function f;
  
  /**
   * Creates a new <code>IteratorMap</code> instance.
   *
   * @param i an <code>Iterator</code> value
   * @param f a <code>Function</code> value
   */
  public IteratorMap(Iterator i, Function f) {
    this.i=i;
    this.f=f;
  }


  /**
   * Returns the next element of the base iterator, with the function applied to it.
   *
   * @return an <code>Object</code> value
   * @exception NoSuchElementException if there is no more element.
   */
  public Object next() throws NoSuchElementException {
    if (!hasNext()) throw new NoSuchElementException();
    return f.map(i.next());
  }

  
  /**
   * Checks if there is another element pending.
   *
   * @return a <code>boolean</code> value
   */
  public boolean hasNext() { return i.hasNext(); }

  
  public void remove() { i.remove(); }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    java.util.Vector v = new java.util.Vector();
    v.add(new Integer(1));
    v.add(new Integer(2));
    v.add(new Integer(3));
    System.out.print("v.iterator=");
    IteratorTools.printlnIterator(v.iterator());
    
    System.out.print("IteratorMap(v.iterator,'+1')=");
    IteratorTools.printlnIterator(new IteratorMap(v.iterator(), new Function() {
	public Object map(Object o) {
	  return new Integer(((Integer)o).intValue()+1);
	};
      }));
  }

} // class IteratorMap
