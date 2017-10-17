package de.rwth.domains.templates; // Generated package name

import java.util.BitSet;

import de.rwth.domains.*;

/**
 * Representation of the elements of {@link BitVectorLattice}
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: BitVectorElement.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class BitVectorElement {
  /**
   * The fixed length of this element.
   *
   */
  protected int n = 0;
  
  /**
   * The actual implementation. Always has <code>n</code> bits.
   *
   */
  protected BitSet bitSet = null;

  
  /**
   * Creates a new <code>BitVectorElement</code> instance where all bits a set to
   * zero.
   *
   * @param n an <code>int</code> value
   */
  public BitVectorElement(int n) {
    this.n      = n;
    this.bitSet = new BitSet(n);
  }
  
  public boolean equals(Object o) {
    return
      (o instanceof BitVectorElement) &&
      ((BitVectorElement)o).n==this.n &&
      this.bitSet.equals(((BitVectorElement)o).bitSet);
  }
  
  public String toString() {
    String result = "(";
    for (int i=0; i<n; i++) {
      result += ((i==0)?"":",") + ((bitSet.get(i))?"1":"0");
    }
    result += ")";
    return result;
  }
  
  /**
   * Gets the specified bit.
   *
   * @param index an <code>int</code> value
   * @return a <code>boolean</code> value
   */
  public boolean get(int index) { return bitSet.get(index); }
  
  /**
   * Returns the length of this bit vector.
   *
   * @return an <code>int</code> value
   */
  public int size() { return n; }

  /**
   * Returns a bitSet representation of this element.
   *
   * @return a <code>BitSet</code> value
   */
  public BitSet getBitSet() { return (BitSet)bitSet.clone(); }
}
