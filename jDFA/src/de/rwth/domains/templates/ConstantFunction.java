package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;


/**
 * Implements functions with a constant value.
 * 
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: ConstantFunction.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class ConstantFunction implements Function {
  
  /**
   * The domain of this constant function.
   *
   */
  protected Set domain = null;

  
  /**
   * The range of this constant function.
   *
   */
  protected Set range = null; 

  
  /**
   * The constant value of this function.
   *
   */
  protected Object value = null;

  
  /**
   * Creates a new <code>ConstantFunction</code> instance.
   *
   * @param domain a <code>Set</code> value: The domain. 
   * @param range a <code>Set</code> value: The range.
   * @param value an <code>Object</code> value: The value
   * @exception IllegalArgumentException if <code>value</code> is not an element of
   * <code>domain</code>.
   */
  public ConstantFunction(Set domain, Set range, Object value)
    throws IllegalArgumentException {
    if (!range.isElement(value)) {
      throw new IllegalArgumentException("value is not element of constant functions range");
    }
    this.domain = domain;
    this.range  = range;
    this.value  = value;
  }

  public Set getDomain() { return domain; }

  public Set getRange() { return range; }

  public Object apply(Object arg) { return value; }

  public String toString() {
    return Function.Tools.toString(this);
  }
  
  public boolean equals(Object o) {
    return Function.Tools.equals(this,o);
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  static public void main(String[] args) {
    CompleteLattice l = new BitVectorLattice(2);
    try {
      Function f = new ConstantFunction(l,l,l.top());
      System.err.println(f);
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
  }
}
