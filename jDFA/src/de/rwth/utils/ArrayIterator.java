package de.rwth.utils; // Generated package name

import java.util.Vector;
import java.util.Iterator;
import java.util.NoSuchElementException;

import java.lang.reflect.Method;

/**
 * Creates an iterator from an array of iterator-capable Object. Each element is an
 * array of objects, consisting of elements from the component iterators. The
 * iterator goes over all possible combinations by traversing the first object first,
 * second object second and so forth.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: ArrayIterator.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class ArrayIterator implements Iterator {
  
  /**
   * The array of objects.
   *
   */
  protected Object[] objs;

  
  /**
   * For each entry in <code>objs</code>, this array contains the <code>Method</code>
   * used to create an iterator.
   *
   */
  protected Method[] iMeths;

  
  /**
   * <code>true</code> as long as there are Objects in this iterator.
   *
   */
  protected boolean hasNext;

  
  /**
   * The next element return by next.
   *
   */
  protected Object[] elements = null;

  
  /**
   * For each entry in <code>objs</code>, this array contains the current state of
   * the component iterator.
   *
   */
  protected Iterator[] iterators = null;

  /**
   * Creates a new <code>ArrayIterator</code> instance.
   *
   * @param objs an <code>Object[]</code> value: The iterator-capable objects.
   * @param name a <code>String</code> value: The name of the methods used to create
   * the iterators.
   * @exception IllegalArgumentException if this class does not have
   * <code>Iterator</code> as unique super interface or if one of the
   * <code>objs</code> is not iterator-capable via <code>name</code>.
   */
  public ArrayIterator(Object[] objs, String name) throws IllegalArgumentException {
    this.objs=(Object[])objs.clone();
    this.iterators=new Iterator[objs.length];
    this.iMeths=new Method[objs.length];
    this.elements=new Object[objs.length];
    this.hasNext=true;

    if (this.getClass().getInterfaces().length!=1)
      throw new IllegalArgumentException(this.getClass()+" must have exactly Iterator as super interface");
    Class iteratorClass = this.getClass().getInterfaces()[0];
    for (int i=0; i<objs.length; i++) {
      try {
	this.iMeths[i]=objs[i].getClass().getMethod(name,null);
	if (!this.iMeths[i].getReturnType().equals(iteratorClass))
	  throw new NoSuchMethodException();
	this.iterators[i]=iterator(i);
	if (this.iterators[i].hasNext()) {
	  this.elements[i]=(Object)this.iterators[i].next();
	} else {
	  this.hasNext=false;
	  break;
	}
      } catch (NoSuchMethodException ex) {
	throw new IllegalArgumentException("object "+i+" of class "+objs[i].getClass()+" does not have a valid method '"+iteratorClass.getName()+" "+name+"()'");
      }
    }
  }

  
  /**
   * Creates a new <code>ArrayIterator</code> instance.
   *
   * @param objs an <code>Object[]</code> value: The iterator-capable objects, which
   * must have a method <code>Iterator iterator()</code>.
   * @exception IllegalArgumentException if this class does not have
   * <code>Iterator</code> as unique super interface or if one of the
   * <code>objs</code> is not iterator-capable via <code>Iterator iterator()</code>.
   */
  public ArrayIterator(Object[] objs) throws IllegalArgumentException { 
    this(objs,"iterator");
  }

  
  /**
   * Invokes the i-th iterator method without raising exceptions.
   *
   * @param i an <code>int</code> value
   * @return an <code>Iterator</code> value
   */
  protected Iterator iterator(int i) {
    try {
      return (Iterator)this.iMeths[i].invoke(objs[i],null);
    } catch (Exception e) { throw new IllegalArgumentException(); }
  }

  
  /**
   * Returns the next element, an array of elements of the components.
   *
   * @return an <code>Object</code> value
   * @exception NoSuchElementException if there is no more element.
   */
  public Object next() throws NoSuchElementException {
    if (!hasNext()) throw new NoSuchElementException();
    this.hasNext=false;
    for (int i=0; i<elements.length && !this.hasNext; i++) {
      if (iterators[i].hasNext()) {
	this.hasNext=true;
      } else {
	iterators[i]=iterator(i);
      }
      elements[i]=(Object)iterators[i].next();
    }
    return elements.clone();
  }

  
  /**
   * Checks if there are more elements pending.
   *
   * @return a <code>boolean</code> value
   */
  public boolean hasNext() {  return this.hasNext; }

  
  /**
   * Not implemented.
   *
   * @exception UnsupportedOperationException always
   */
  public void remove() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      Vector v = new Vector();
      v.add("1");
      v.add("2");
      Iterator i=new ArrayIterator(new Object[] {v,v});
      de.rwth.utils.IteratorTools.printlnIterator(i);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
} // class ArrayIterator
