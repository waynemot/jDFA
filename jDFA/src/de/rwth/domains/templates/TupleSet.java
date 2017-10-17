package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.rwth.utils.ArrayIterator;
import de.rwth.utils.IteratorMap;

import de.rwth.domains.*;

/**
 * Class for the creation of sets consisting of tuples from elements of other sets
 * (Cartesian product).
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TupleSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class TupleSet implements Set {
  /**
   * All the component <code>Sets</code> of this set.
   */
  protected Set[] sets;

  /**
   * The size of this set.
   */
  protected long size = -1;

  /**
   * The size of the skeleton of this set.
   */
  protected long sizeSkel = -1;
  
  /**
   * Creates a <code>TupleSet</code> from an array of <code>Sets</code>.
   *
   * @param <code>sets</code> the component <code>Sets</code>
   */
  public TupleSet(Set[] sets) {
    super();
    this.sets=(Set[])sets.clone();
    this.size=1;
    for (int i=0; i<sets.length; i++) {
      if (i==0) this.size=sets[0].size();
      else if (this.size!=-1) this.size *= sets[i].size();
      else {
	this.size=-1;
	break;
      }
    }
    this.sizeSkel=1;
    for (int i=0; i<sets.length; i++) {
      if (i==0) this.sizeSkel=sets[0].sizeSkel();
      else if (this.sizeSkel!=-1) this.sizeSkel *= sets[i].sizeSkel();
      else {
	this.sizeSkel=-1;
	break;
      }
    }
  }
  
  /**
   * Creates a <code>TupleSet</code> from two <code>Sets</code>.
   *
   * @param <code>set1</code> 1st component <code>Set</code>
   * @param <code>set2</code> 2nd component <code>Set</code>
   */
  public TupleSet(Set set1, Set set2) {
    this(new Set[] {set1, set2});
  }

  /**
   * Checks if an element is contained in this set.
   *
   * @param <code>e</code> a value of type <code>Object</code>
   * @return <code>true</code> iff <code>e</code> is
   *                               <code>TupleElement</code> of
   *                               matching length and each component
   *                               of <code>e</code> is contained in
   *                               the corresponding component
   *                               <code>Set</code> of this
   *                               <code>Set</code>.
   */
  public boolean isElement(Object e) {
    if (!(e instanceof TupleElement)) return false;
    Object[] ee = ((TupleElement)e).getElements();
    if (ee.length!=sets.length) return false;
    for (int i=0; i<sets.length; i++) {
      if (!sets[i].isElement(ee[i])) return false;
    }
    return true;
  }
  
  /**
   * Checks if two elements of this set are equal.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>e1</code> and
   *                               <code>e2</code> are
   *                               <code>TupleElements</code> of
   *				   matching length and the corresponding
   *				   components of <code>e1</code> and
   *				   <code>e2</code> are equal.
   */
  public boolean equals(Object e1, Object e2) {
    if (!(e1 instanceof TupleElement && e2 instanceof TupleElement)) return false;
    Object[] ee1 = ((TupleElement)e1).getElements();
    Object[] ee2 = ((TupleElement)e2).getElements();
    if (ee1.length!=sets.length || ee2.length!=sets.length)
      return false;
    for (int i=0; i<sets.length; i++) {
      if (!(sets[i].isElement(ee1[i]) && sets[i].isElement(ee2[i])))
	return false;
      if (!(sets[i].equals(ee1[i],ee2[i]))) return false;
    }
    return true;
  }

  /**
   * Returns the size of this set.
   *
   * @return the number of elements which is the product of sizes of
   * the component sets.
   */
  public long size() { return size; }

  /**
   * Returns an <code>Iterator</code> of the elements of this set. The elements are
   * all tuples resulting from combining the elements of the component sets.
   *
   * @return an <code>Iterator</code> of all elements of this set.
   */
  public Iterator iterator() {
    return new IteratorMap(new ArrayIterator(sets), new IteratorMap.Function() {
	public Object map(Object o) {
	  return new TupleElement((Object[])o);
	}
      });
  }
 
  /**
   * Returns the size of the skeleton subset of this set.
   *
   * @return the number of elements which is the product of sizes of the skeleton
   * subsets of the component sets.
   */
  public long sizeSkel() { return sizeSkel; }
  
  /**
   * Returns an <code>Iterator</code> of the elements of the skeleton subset of this
   * set. The elements are all tuples resulting from combining the elements of the
   * skeleton subsets of the component sets.
   *
   * @return an <code>Iterator</code> of all elements of this set.
   */
  public Iterator iteratorSkel() {
    return new IteratorMap(new ArrayIterator(sets,"iteratorSkel"),
			   new IteratorMap.Function() {
			     public Object map(Object o) {
			       return new TupleElement((Object[])o);}
			   });
  }

  
  /**
   * Returns clones of the component set.
   *
   * @return a <code>Set[]</code> value
   */
  public Set[] getSets() { return (Set[])sets.clone(); }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    Set s1 = new SimpleSet(1,2);
    Set s = new TupleSet(s1,s1);
    de.rwth.utils.IteratorTools.printlnIterator(s.iterator());
    de.rwth.utils.IteratorTools.printlnIterator(s.iteratorSkel());
    s = new TupleSet(new Set[]{});
    de.rwth.utils.IteratorTools.printlnIterator(s.iterator());
  }
}
