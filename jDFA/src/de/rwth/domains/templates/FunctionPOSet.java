package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of partially ordered sets as set of functions
 * with other finite (partially ordered) sets as domain and range.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FunctionPOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FunctionPOSet extends FunctionSet implements POSet {
  /**
   * Create a function space from a domain and a range.
   *
   * @param <code>domain</code> a value of type <code>Set</code>
   * @param <code>range</code> a value of type <code>POSet</code>
   */
  public FunctionPOSet(Set domain, POSet range) {
    super(domain,range);
  }

  /**
   * Create a function space from an array of domains and a range.
   *
   * @param <code>domains</code> a value of type <code>Set[]</code>
   * @param <code>range</code> a value of type <code>POSet</code>
   */
  public FunctionPOSet(Set[] domains, POSet range) {
    super(domains,range);
  }

  /**
   * The method <code>lt</code> checks if a function is less than
   * another function of this set. This is done argument-wise.
   *
   * @param <code>f1</code> a value of type <code>Object</code>
   * @param <code>f2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>f1</code> and
   *                               <code>f2</code> are functions in
   *                               this set and for all <code>x</code>
   *                               in the domain holds
   *                               <code>range.lt(f1(x),f2(x))</code>
   */
  public boolean lt(Object f1, Object f2) {
    return le(f1,f2) && !equals(f1,f2);
  }

  /**
   * The method <code>le</code> checks if a function is less or equal
   * than another function of this set. This is done argument-wise.
   *
   * @param <code>f1</code> a value of type <code>Object</code>
   * @param <code>f2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>f1</code> and
   *                               <code>f2</code> are functions in
   *                               this set and for all <code>x</code>
   *                               in the domain holds
   *                               <code>range.le(f1(x),f2(x))</code>
   */
  public boolean le(Object f1, Object f2) {
    if (!(isElement(f1) && isElement(f2))) return false;
    for (int i=0; i<((TabledFunction)f1).fValues.length; i++)
      if (!((POSet)range).le(((TabledFunction)f1).fValues[i],
					   ((TabledFunction)f2).fValues[i]))
	return false;
    return true;
  }
}
