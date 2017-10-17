package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.domains.*;

/**
 * This class represents functions by the table of their function values.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TabledFunction.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class TabledFunction implements Function { 
  /**
   * The domain of this function.
   */
  protected Set domain = null;
  
  /**
   * The range of this function.
   */
  protected Set range  = null;

  /**
   * This function's values in the sequence in which the elements of the
   * domain occur with <code>domains.iterator()</code>.
   */
  protected Object[] fValues = null;

  /**
   * Create a new function from domain, range, and function table.
   *
   * @param <code>domain</code> a value of type <code>Set</code>
   * @param <code>range</code> a value of type <code>Set</code>
   * @param <code>fValues</code>: The function's values in the sequence
   * in which the elements of the domain occur with
   * <code>domains.iterator()</code>.
   *
   * @exception <code>IllegalArgumentException<code> if
   * <ol>
   * <li> an element of <code>fvalues</code> is not in <code>range</code>
   * </li>
   * <li> <code>fValues</code> has not enough values
   * </li>
   * <li> <code>fValues</code> has too many values
   * </li>
   * </ol>
   */
  public TabledFunction(Set domain, Set range, Object[] fValues)
    throws IllegalArgumentException {
    super();
    this.domain = domain;
    this.range  = range;
    this.fValues= fValues;
    for (int i=0; i<fValues.length; i++) {
      if (!range.isElement(fValues[i]))
	throw new IllegalArgumentException("value "+fValues[i]+" is not in function's range");
    }
    if (fValues.length<domain.size())
      throw new IllegalArgumentException("not enough function values");
    if (fValues.length>domain.size())
      throw new IllegalArgumentException("too many function values");
  }

  public String toString() {
    return Function.Tools.toString(this);
  }
  
  public boolean equals(Object o) {
    return Function.Tools.equals(this,o);
  }
  
  public Set getDomain() { return domain; }
  
  public Set getRange()  { return range; }

  /**
   * Applies this function to an argument and returns the result.
   *
   * @param <code>x</code> the argument
   * @return this function's value for argument <code>x</code>
   * @exception <code>FunctionException<code> if <code>x</code> is not
   *                                          an element of the
   *                                          functions domain.
   */
  public Object apply(Object x) throws FunctionException {
    if (!domain.isElement(x))
      throw new FunctionException("parameter not in function's domain");
    int i=0;
    for (Iterator e=domain.iterator(); e.hasNext(); ) {
      if (domain.equals((Object)e.next(), x)) return fValues[i];
      i++;
    }
    throw new FunctionException("function's domain.iterator() doesn't contain parameter");
  }
}
