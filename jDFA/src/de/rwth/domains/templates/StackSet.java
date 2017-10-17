package de.rwth.domains.templates; // Generated package name

import java.util.Stack;
import java.util.Iterator;
import java.util.Arrays;

import de.rwth.utils.SingletonIterator;
import de.rwth.utils.ArrayIterator;
import de.rwth.utils.IteratorSequence;
import de.rwth.utils.IteratorMap;

import de.rwth.domains.*;

/**
 * Class for (maybe infinite) sets which consists of stacks of elements from a set.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: StackSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class StackSet implements Set  {
  /**
   * The set for the elements of the stacks of this set.
   *
   */
  protected Set set=null;

  /**
   * The default value for the maximal stack size for the skeleton subset of this
   * set.
   *
   */
  protected final static int SKELMAXSIZE=3;

  
  /**
   * The maximal stack size of the elements of this set. Maybe -1 for unlimited stack
   * size.
   *
   */
  protected int maxSize;
  
  /**
   * The maximal stack size of the elements of skeleton subset of this set.
   *
   */
  protected int skelMaxSize;

  
  /**
   * The size of this set.
   *
   */
  protected long size;
  
  /**
   * The size of this skeleton subset of this set.
   *
   */
  protected long sizeSkel;

  
  /**
   * Compute the size of a stack set which maximal <code>maxSize</code> stack size
   * and <code>baseSize</code> elements for each entry in the stack.
   *
   * @param maxSize an <code>int</code> value
   * @param baseSize a <code>long</code> value
   * @return a <code>long</code> value
   */
  protected static long computeSize(int maxSize, long baseSize) {
    long result=1;
    for (int i=1; i<=maxSize; i++) {
      long help=1;
      for (int j=1; j<=i; j++) help*=baseSize;
      result+=help;
    }
    return result;
  }
  
  /**
   * Creates a new <code>StackSet</code> instance with unlimited stack size and
   * skeleton stack size SKELMAXSIZE.
   *
   * @param set a <code>Set</code> value
   */
  public StackSet(Set set) {
    this.set=set;
    this.maxSize=-1;
    this.skelMaxSize=SKELMAXSIZE;
    this.size=-1;
    if (set.sizeSkel()!=-1)
      this.sizeSkel=computeSize(skelMaxSize, set.sizeSkel());
    else
      this.sizeSkel=-1;
  }
  
  /**
   * Creates a new <code>StackSet</code> instance with maximal stack size.
   *
   * @param set a <code>Set</code> value
   * @param maxSize an <code>int</code> value
   */
  public StackSet(Set set, int maxSize) {
    this(set);
    this.skelMaxSize=maxSize;
    this.maxSize=maxSize;
    if (set.size()!=-1)
      this.sizeSkel=this.size=computeSize(maxSize, set.size());
    else {
      this.size=-1;
      if (set.sizeSkel()!=-1)
	this.sizeSkel=computeSize(skelMaxSize, set.sizeSkel());
      else
	this.sizeSkel=-1;
    }
  }


  public boolean equals(Object param1, Object param2) {
    return param1.equals(param2);
  }
  
  public boolean isElement(Object param1) {
    if (!(param1 instanceof Stack)) {
      System.err.println("not a stack"+param1);
      return false;
    }
    Stack stack=(Stack)param1;
    if (maxSize!=-1 && maxSize<stack.size()) {
      System.err.println("stack too long"+stack);
      return false;
    }
    for (int i=0; i<stack.size(); i++) {
      if (!(set.isElement(stack.elementAt(i)))) {
	System.err.println("set does not recognize"+stack.elementAt(i));
	return false;
      }
    }
    return true;
  }
  
  public Iterator iterator() {
    if (size==-1) return null;
    Iterator result=new SingletonIterator(new Stack());
    for (int i=1; i<=maxSize; i++) {
      Set[] sets=new Set[i];
      Arrays.fill(sets,set);
      result=new IteratorSequence(result,
				  new IteratorMap(new ArrayIterator(sets),
						  new IteratorMap.Function() {
						    public Object map(Object o) {
						      Object[] a=(Object[])o;
						      Stack s=new Stack();
						      s.addAll(Arrays.asList(a));
						      return s;
						    }
						  }));
    }
    return result;

  }
  
  public Iterator iteratorSkel() {
    if (size!=-1) return iterator();
    Iterator result=new SingletonIterator(new Stack());
    for (int i=1; i<=this.skelMaxSize; i++) {
      Set[] sets=new Set[i];
      Arrays.fill(sets,set);
      result=new IteratorSequence(result,
				  new IteratorMap(new ArrayIterator(sets,"iteratorSkel"),
						  new IteratorMap.Function() {
						    public Object map(Object o) {
						      Object[] a=(Object[])o;
						      Stack s=new Stack();
						      s.addAll(Arrays.asList(a));
						      return s;
						    }
						  }));
    }
    return result;
  }
  
  public long size() { return size; }
  public long sizeSkel() { return sizeSkel; }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      Set s1 = new SimpleSet(1,2);
      Set s = new StackSet(s1,2);
      de.rwth.utils.IteratorTools.printlnIterator(s.iterator());
      de.rwth.utils.IteratorTools.printlnIterator(s.iteratorSkel());
      Domain.checkProperties(s);
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }

  }
}
