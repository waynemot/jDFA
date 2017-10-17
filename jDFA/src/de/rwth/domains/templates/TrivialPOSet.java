package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.domains.*;

/**
 * Class for the creation of trivial partially ordered sets, where the
 * less-or-equal relation is identical with the equals relation (and
 * hence less-than is the empty relation).
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TrivialPOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class TrivialPOSet implements POSet {
  /**
   * The underlying set.
   */
  protected Set set = null;
  
  /**
   * Creates the trivial partially ordered empty set.
   */
  public TrivialPOSet() {
    super();
    set=new SimpleSet();
  }

  /**
   * Creates a trivial partially ordered set from a set.
   *
   * @param <code>set</code> the underlying set.
   */
  public TrivialPOSet(Set set) {
    super();
    this.set=set;
  }

  /**
   * Creates a trivial partially ordered set from an array of
   * <code>Objects</code>.
   *
   * @param <code>os</code> all the elements of the new set.
   */
  public TrivialPOSet(Object[] os) { this(new SimpleSet(os));}

  /**
   * Creates a trivial partially ordered set with one element.
   *
   * @param <code>o</code> the <code>Object</code> which will be the
   * element of the new set.
   */
  public TrivialPOSet(Object o) { this(new SimpleSet(o));}

  /**
   * Creates a trivial partially ordered set with one <code>int</code>
   * as element.
   *
   * @param <code>i</code> a value of type <code>int</code>
   */
  public TrivialPOSet(int i) { this(new SimpleSet(i));}

  /**
   * Creates a trivial partially ordered set with a range of iterator.
   *
   * @param <code>from</code> the first element of the set
   * @param <code>to</code> the last element of the set
   */
  public TrivialPOSet(int from, int to) {
    this(new SimpleSet(from,to));
  }
  
  /**
   * Creates a trivial partially ordered et from an array of
   * <code>ints</code>.
   *
   * @param <code>is</code> all the elements of the new set.
   */
  public TrivialPOSet(int[] is) { this(new SimpleSet(is));}
  
  /**
   * Returns true iff both parameters are equal in the underlying set.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return a value of type <code>boolean</code>
   */
  public boolean equals(Object e1, Object e2) { return set.equals(e1,e2); }

  /**
   * Returns true iff the parameter is element in the underlying set.
   *
   * @param <code>e</code> a value of type <code>Object</code>
   * @return a value of type <code>boolean</code>
   */
  public boolean isElement(Object e) { return set.isElement(e); }

  public Iterator iterator() { return set.iterator();  }

  public long size() { return set.size();  }
  
  public Iterator iteratorSkel() { return set.iteratorSkel();  }

  public long sizeSkel() { return set.sizeSkel();  }
  
  /**
   * The method <code>lt</code> always returns false.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return a value of type <code>boolean</code>
   */
  
  public boolean lt(Object e1, Object e2) { return false; }

  /**
   * The method <code>le</code> is identical to <code>equals</code>.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return a value of type <code>boolean</code>
   */
  public boolean le(Object e1, Object e2) { return equals(e1,e2); }
}
