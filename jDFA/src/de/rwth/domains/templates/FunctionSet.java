package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.rwth.utils.IteratorMap;
import de.rwth.utils.IteratorTools;

import de.rwth.domains.*;

/**
 * Class for the creation of sets of functions with other finite (partially ordered)
 * sets as domain and range.
 *
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FunctionSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FunctionSet implements Set {
  /**
   * The domain of the functions in this set.
   */
  protected Set domain = null;
  
  /**
   * The range of the functions in this set.
   */
  protected Set range  = null;

  /**
   * Create a function set from a domain and a range.
   *
   * @param <code>domain</code> a value of type <code>Set</code>
   * @param <code>range</code> a value of type <code>POSet</code>
   */
  public FunctionSet(Set domain, Set range) {
    super();
    this.domain=domain;
    this.range =range;
  }

  /**
   * Create a function set from an array of domains and a range.
   *
   * @param <code>domains</code> a value of type <code>Set[]</code>
   * @param <code>range</code> a value of type <code>POSet</code>
   */
  public FunctionSet(Set[] domains, Set range) {
    this(new TupleSet(domains), range);
  }

  /**
   * Checks if a function equals another function of this set. This is done
   * argument-wise.
   *
   * @param <code>f1</code> a value of type <code>Object</code>
   * @param <code>f2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>f1</code> and
   *                               <code>f2</code> are functions in
   *                               this set and for all <code>x</code>
   *                               in the domain holds
   *                               <code>range.equals(f1(x),f2(x))</code>
   */
  public boolean equals(Object e1, Object e2) {
    if (!(isElement(e1) && isElement(e2))) return false;
    TabledFunction f1 = (TabledFunction)e1;
    TabledFunction f2 = (TabledFunction)e2;
    if (f1.fValues.length!=f2.fValues.length) return false;
    for (int i=0; i<f1.fValues.length; i++)
      if (!range.equals(f1.fValues[i],f2.fValues[i])) return false;
    return true;
  }

  public boolean isElement(Object e) {
    return
      e instanceof TabledFunction &&
      ((TabledFunction)e).range.equals(range) &&
      ((TabledFunction)e).domain.equals(domain);
  }

  public Iterator iterator() {
    Set[] help = new Set[(int)domain.size()];
    for (int i=0; i<help.length; i++) help[i]=range;
    return new IteratorMap(new TupleSet(help).iterator(), new IteratorMap.Function() {
	public Object map(Object o) {
	  try {
	    return new TabledFunction(domain, range, ((TupleElement)o).getElements());
	  } catch (IllegalArgumentException ex) {
	    throw new NoSuchElementException(ex.toString());
	  }
	}
      });
  }

  /**
   * Returns the size of the set.
   *
   * @return the number of elements, which is
   * <code>range.size()<code><sup><code>domain.size()<code></sup>
   */
  public long size() {
    long result = 0;
    for (long i=0; i<domain.size(); i++) {
      if (i==0) result=range.size();
      else result*=range.size();
    }
    return result;
  }
  
  /**
   * Returns the size of the skeleton subset of this set.
   *
   * @return the number of skeleton elements, which is
   * <code>range.sizeSkel()<code><sup><code>domain.sizeSkel()<code></sup>
   */
  public long sizeSkel() {
    long result = 0;
    for (long i=0; i<domain.sizeSkel(); i++) {
      if (i==0) result=range.sizeSkel();
      else result*=range.sizeSkel();
    }
    return result;
  }

 
  public Iterator iteratorSkel() {
    Set[] help = new Set[(int)domain.sizeSkel()];
    for (int i=0; i<help.length; i++) help[i]=range;
    return new IteratorMap(new TupleSet(help).iteratorSkel(),
			   new IteratorMap.Function() {
	public Object map(Object o) {
	  try {
	    return new TabledFunction(domain, range, ((TupleElement)o).getElements());
	  } catch (IllegalArgumentException ex) {
	    throw new NoSuchElementException(ex.toString());
	  }
	}
      });
  }

  /*
  public static void main(String[] args) {
    Set s1 = new SimpleSet(1,2);
    Set s = new FunctionSet(s1,s1);

    try {
      Domain.checkProperties(s);
    
      IteratorTools.printlnIterator(s.iterator());
      IteratorTools.printlnIterator(s.iteratorSkel());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  */
}
