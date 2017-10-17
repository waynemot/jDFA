package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.utils.IteratorSequence;
import de.rwth.utils.IteratorTools;

import de.rwth.domains.*;

/**
 * Class for the creation of sets as the sum of other sets.
 *
 * <strong>Warning:</strong> This will violate the set properties, the same element
 * is contained in more than one component.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: SumSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class SumSet implements Set {
  /**
   * The sets contained in this set.
   *
   */
  protected Set[] sets = null;

  
  /**
   * Its size, which is the sum of the sizes of the component sets. However, of one
   * of the component sets in infinite, then this is -1.
   *
   */
  protected long size = -1;
  
  /**
   * The size of the skeleton subset, which is the sum of the sizes of the skeleton
   * subsets of the component sets. However, of one of these is -1, then this is -1
   * as well.
   *
   */
  protected long sizeSkel = -1;

  
  /**
   * Creates a new <code>SumSet</code> instance from an array of component sets.
   *
   * @param sets a <code>Set[]</code> value
   */
  public SumSet(Set[] sets) {
    super();
    this.sets=(Set[])sets.clone();;
    this.size=(sets.length>0)?sets[0].size():0;
    this.sizeSkel=(sets.length>0)?sets[0].sizeSkel():0;
    for (int i=1; i<sets.length; i++) {
      if (this.size!=-1 && sets[i].size()!=-1) this.size += sets[i].size();
      else this.size=-1;
      
      if (this.sizeSkel!=-1 && sets[i].sizeSkel()!=-1)
	this.sizeSkel += sets[i].sizeSkel();
      else this.sizeSkel=-1;
    }
  }

  
  /**
   * Creates a new <code>SumSet</code> instance from two component sets.
   *
   * @param set1 a <code>Set</code> value
   * @param set2 a <code>Set</code> value
   */
  public SumSet(Set set1, Set set2) {
    this(new Set[] {set1, set2});
  }

  
  /**
   * Returns true, if the equality holds in at least one of the components of this
   * set.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean equals(Object e1, Object e2) {
    for (int i=0;i<sets.length; i++) {
      if (sets[i].equals(e1,e2)) return true;
    }
    return false;
  }
  
  
  /**
   * Returns true, if the object is element of one of the components of this set.
   *
   * @param e an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean isElement(Object e) {
    for (int i=0;i<sets.length; i++) {
      if (sets[i].isElement(e)) return true;
    }
    return false;
  }

  
  /**
   * Returns an iterator, which will iterate over all component set iterators.
   *
   * @return an <code>Iterator</code> value
   */
  public Iterator iterator() {
    Iterator[] iterators = new Iterator[sets.length];
    for (int i=0;i<sets.length; i++) {
      iterators[i]=sets[i].iterator();
    }
    return new IteratorSequence(iterators);
  }
  
  public long size() { return size; }
  
  public long sizeSkel() { return sizeSkel; }
  
  /**
   * Returns an iterator, which will iterate over all skeleton subset component set
   * iterators.
   *
   * @return an <code>Iterator</code> value
   */
  public Iterator iteratorSkel() {
    Iterator[] iterators = new Iterator[sets.length];
    for (int i=0;i<sets.length; i++) {
      iterators[i]=sets[i].iteratorSkel();
    }
    return new IteratorSequence(iterators);
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    Set s1 = new SimpleSet(1,2);
    Set s2 = new SimpleSet(3,4);
    Set s = new SumSet(s1,s2);

    try {
      Domain.checkProperties(s);
    
      IteratorTools.printlnIterator(s.iterator());
      IteratorTools.printlnIterator(s.iteratorSkel());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
