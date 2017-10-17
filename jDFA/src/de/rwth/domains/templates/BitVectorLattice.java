package de.rwth.domains.templates; // Generated package name

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import de.rwth.domains.*;

/**
 * Implements complete lattices of bit vectors.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: BitVectorLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class BitVectorLattice implements CompleteLattice {
  /**
   * The fixed length of the bit vectors.
   *
   */
  protected int n = -1;

  
  /**
   * Creates a new <code>BitVectorLattice</code> instance.
   *
   * @param n an <code>int</code> value
   */
  public BitVectorLattice(int n) { this.n=n; }

  
  /**
   * Returns the number of elements, which is 2<sup>n</sup>.
   *
   * @return a <code>long</code> value
   */
  public long size() {
    long result = 0;
    for (int i=0; i<n; i++) {
      if (result==0) result=2;
      else result *=2;
    }
    return result;
  }

  
  /**
   * Returns an iterator over all bit vectors. It goes like this: (0,...,0),
   * (0,...,0,1), ..., (1,...,1).
   *
   * @return an <code>Iterator</code> value
   */
  public Iterator iterator() {
    class E implements Iterator {
      protected boolean[] bits;
      protected boolean hasNext;
      
      public boolean hasNext() { return hasNext; }
      
      public Object next() throws NoSuchElementException {
	if (!hasNext()) throw new NoSuchElementException();
	
	BitVectorElement result = new BitVectorElement(n);
	for (int i=0; i<n; i++) if (bits[i]) result.bitSet.set(i);

	hasNext=false;
	for (int i=0; i<n; i++) {
	  if (!bits[i]) {
	    hasNext=bits[i]=true;
	    for (int j=0; j<i; j++) bits[j]=false;
	    break;
	  }
	}
	
	return result;
      }
      
      public void remove() { throw new UnsupportedOperationException(); }

      public E() {
	this.bits=new boolean[n];
	for (int i=0; i<n; i++) this.bits[i]=false;
	this.hasNext=(n!=0);
      }
    }
    return new E();
  }

  
  /**
   * The same as <code>size()</code>.
   *
   * @return a <code>long</code> value
   */
  public long sizeSkel() { return size(); }

  
  /**
   * The same as <code>iterator()</code>.
   *
   * @return an <code>Iterator</code> value
   */
  public Iterator iteratorSkel() { return iterator(); }
  
  public boolean isElement(Object e) {
    return (e instanceof BitVectorElement) &&
           ((BitVectorElement)e).n==n;
  }

  public boolean equals(Object e1, Object e2) {
    return e1.equals(e2);
  }
  
  /**
   * A bit vector is less or equal than another one iff all bits set in the first one
   * are also set in the second one
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean le(Object e1, Object e2) {
    if (!(isElement(e1) && isElement(e2))) return false;
    BitSet bs1 = ((BitVectorElement)e1).bitSet;
    BitSet bs2 = ((BitVectorElement)e2).bitSet;
    for (int i=0; i<n; i++)
      if (bs1.get(i) && !(bs2.get(i))) return false;
    return true;
  }

  /**
   * A bit vector is less than another one iff its less-than-or-equal but not equal
   * than the second.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return a <code>boolean</code> value
   */
  public boolean lt(Object e1, Object e2) {
    return !equals(e1,e2) && le(e1,e2);
  }

  /**
   * The join of two bit vectors is a bit vector where all those bits are set, which
   * are set in one of the bit vectors.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object join(Object e1, Object e2) {
    if (!(isElement(e1) && isElement(e2))) return null;
    BitSet bs1 = ((BitVectorElement)e1).bitSet;
    BitSet bs2 = ((BitVectorElement)e2).bitSet;
    BitVectorElement result = new BitVectorElement(n);
    BitSet bsr = ((BitVectorElement)result).bitSet;
    bsr.or(bs1);
    bsr.or(bs2);
    return result;
  }
  
  /**
   * The meet of two bit vectors is a bit vector where all those bits are set, which
   * are set in both of the bit vectors.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object meet(Object e1, Object e2) {
    if (!(isElement(e1) && isElement(e2))) return null;
    BitSet bs1 = ((BitVectorElement)e1).bitSet;
    BitSet bs2 = ((BitVectorElement)e2).bitSet;
    BitVectorElement result = new BitVectorElement(n);
    BitSet bsr = ((BitVectorElement)result).bitSet;
    bsr.or(bs1);                   // Reinkopieren von bs1
    bsr.and(bs2);
    return result;
  }

  /**
   * The top element is (1,...,1).
   *
   * @return an <code>Object</code> value
   */
  public Object top() {
    BitVectorElement result = new BitVectorElement(n);
    BitSet bs               = ((BitVectorElement)result).bitSet;
    for (int i=0; i<n; i++) bs.set(i);
    return result;
  }
  
  /**
   * The bottom element is (0,...,0).
   *
   * @return an <code>Object</code> value
   */
  public Object bottom() {
    return new BitVectorElement(n);
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    System.err.println(Domain.setToString(new BitVectorLattice(3)));
    try {
      Domain.checkProperties(new BitVectorLattice(3));
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
}
