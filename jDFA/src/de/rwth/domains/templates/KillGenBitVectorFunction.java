package de.rwth.domains.templates; // Generated package name

import java.util.BitSet;
import java.util.Arrays;

import de.rwth.utils.ArrayTools;

import de.rwth.domains.*;

/**
 * Implements kill/gen functions over bit vectors. To determine the result of an
 * application to an argument, a function kills bits in the * argument and then
 * generates bits. 
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: KillGenBitVectorFunction.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class KillGenBitVectorFunction implements Function {
  /**
   * The bit vector lattice to operate on.
   *
   */
  BitVectorLattice l = null;

  
  /**
   * The bit indices to kill.
   *
   */
  protected int[] kills = null;

  
  /**
   * The bit indices to generate.
   *
   */
  protected int[] gens = null;
  
  /**
   * Creates a new <code>KillGenBitVectorFunction</code> instance from explicit
   * enumeration of kills and gens.
   *
   * @param l a <code>BitVectorLattice</code> value: The bit vector lattice used as
   * domain and range.
   * @param kills an <code>int[]</code> value: The bit indices to kill.
   * @param gens[] an <code>int</code> value: The bit indices to generate.
   */
  public KillGenBitVectorFunction(BitVectorLattice l, int[] kills, int gens[]) {
    for (int i=0; i<kills.length; i++) {
      if (!(kills[i]>=0 && kills[i]<l.n))
	throw new IllegalArgumentException("kills["+i+"] isn't in range 0.."+(l.n-1));
    }
    for (int i=0; i<gens.length; i++) {
      if (!(gens[i]>=0 && gens[i]<l.n))
	throw new IllegalArgumentException("gens["+i+"] isn't in range 0.."+(l.n-1));
    }
    this.l=l;
    this.gens=(int[])gens.clone();
    this.kills=ArrayTools.minus(kills,gens);
  }

  
  /**
   * Creates a new <code>KillGenBitVectorFunction</code> instance as composition of
   * two <code>KillGenBitVectorFunction</code>. Just like {@link ComposedFunction}
   * does, but more efficient.
   *
   * @param f a <code>KillGenBitVectorFunction</code> value: The second function in
   * the composition.
   * @param g a <code>KillGenBitVectorFunction</code> value: The first function in
   * the composition.
   */
  public KillGenBitVectorFunction(KillGenBitVectorFunction f, KillGenBitVectorFunction g) {
    if (!g.getRange().equals(f.getDomain())) {
      throw new IllegalArgumentException("g's range must be the same as f's domain");
    }
    this.l=(BitVectorLattice)g.getRange();
    this.kills=ArrayTools.union(g.kills,f.kills);
    this.gens=ArrayTools.union(ArrayTools.minus(g.gens,f.kills),f.gens);
    //System.err.println(f+" o "+g+" = "+this);
    //System.err.println(g+" ; "+f+" = "+this);
  }

  
  public Set getDomain() { return l; }

  public Set getRange() { return l; }

  public Object apply(Object x) throws FunctionException {
    if (!l.isElement(x)) throw new FunctionException("argument is not element of functions domain");
    BitVectorElement bx = (BitVectorElement) x;
    BitVectorElement result = new BitVectorElement(l.n);
    result.bitSet=(BitSet)bx.bitSet.clone();
    for (int i=0; i<kills.length; i++) {
      result.bitSet.clear(kills[i]);
    }
    for (int i=0; i<gens.length; i++) {
      result.bitSet.set(gens[i]);
    }
    return result;
  }
  
  public boolean equals(Object o) {
    return Function.Tools.equals(this,o);
  }
  
  public String toString() {
    // return defaultImplementations.toString();
    String result = "(";
    for (int i=1; i<=l.n; i++) {
      result+=((i>1)?",":"")+"x"+i;
    }
    result+=")->(";
    for (int i=0; i<l.n; i++) {
      String posText="x"+(i+1);
      for (int j=0;j<kills.length;j++) {
	if (kills[j]==i) posText="0";
      }
      for (int j=0;j<gens.length;j++) {
	if (gens[j]==i) posText="1";
      }
      result+=((i>0)?",":"")+posText;
    }
    result+=")";

    result="-[";
    for (int i=0; i<kills.length; i++) {
      result+=((i>0)?",":"")+kills[i];
    }
    result+="]+[";
    for (int i=0; i<gens.length; i++) {
      result+=((i>0)?",":"")+gens[i];
    }
    result+="]";
    
    return result;
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  static public void main(String[] args) {
    BitVectorLattice l = new BitVectorLattice(4);
    try {
      Function f = new KillGenBitVectorFunction(l,new int[] {1}, new int[] {2});
      System.err.println(f);
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
  }
}
