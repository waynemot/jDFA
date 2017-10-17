package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
* Implements functions by composition of two functions.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: ComposedFunction.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class ComposedFunction implements Function {
  /**
   * The second function in this composition.
   *
   */
  protected Function f = null;
  
  /**
   * The first function in this composition.
   *
   */
  protected Function g = null;

  
  /**
   * Creates a new <code>ComposedFunction</code> instance. Read as "<code>f</code> o
   * <code>g</code>" in the usual mathematical sense: The resulting function applies
   * <code>g</code> to an argument and subsequently <code>f</code> to the result.
   *
   * @param f a <code>Function</code> value: The second function.
   * @param g a <code>Function</code> value: The first function.
   * @exception IllegalArgumentException if the range of <code>g</code> is not the
   * domain of <code>f</code>
   */
  public ComposedFunction(Function f, Function g) throws IllegalArgumentException {
    if (!g.getRange().equals(f.getDomain())) {
      throw new IllegalArgumentException("g's range must be the same as f's domain");
    }
    this.f=f;
    this.g=g;
  }

  public Object apply(Object x) throws FunctionException {
    return f.apply(g.apply(x));
  }

  public Set getDomain() { return g.getDomain(); } 
  
  public Set getRange() { return f.getRange(); }

  public boolean equals(Object o) {
    return Function.Tools.equals(this,o);
  }

  public String toString() {
    return f.toString()+" o "+g.toString();
  }
  
  static public void main(String[] args) {
    CompleteLattice l = new BitVectorLattice(2);
    try {
      Function f = new ConstantFunction(l,l,l.top());
      Function g = new ConstantFunction(l,l,l.bottom());
      System.err.println(new ComposedFunction(g,f));
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
  }
  
}
