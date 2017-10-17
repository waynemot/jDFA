package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Implements functions which always return their argument.
 * 
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: IdentityFunction.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class IdentityFunction implements Function {
  /**
   * The domain and range of the function.
   *
   */
  protected Set set = null;
  
  /**
   * Creates a new <code>IdentityFunction</code> instance.
   *
   * @param set a <code>Set</code> value
   */
  public IdentityFunction(Set set) {
    super();
    this.set=set;
  }

  public Set getDomain() { return set; }
  
  public Set getRange() { return set; }

  
  /**
   * Returns the argument if it is an element of the domain.
   *
   * @param x an <code>Object</code> value
   * @return an <code>Object</code> value
   * @exception FunctionException if the argument is not an element of the domain.
   */
  public Object apply(Object x) throws FunctionException {
    if (!set.isElement(x))
      throw new FunctionException("value is not element of constant functions range");
    return x;
  }

  
  /**
   * Returns "id".
   *
   * @return a <code>String</code> value
   */
  public String toString() { return "id"; }
}
