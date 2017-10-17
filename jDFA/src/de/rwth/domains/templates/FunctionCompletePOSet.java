package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of complete partially ordered sets as set of
 * functions with other finite (complete partially ordered) sets as
 * range.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FunctionCompletePOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FunctionCompletePOSet extends FunctionPOSet implements CompletePOSet {
  /**
   * Create a function space from a domain and a range.
   *
   * @param <code>domain</code> a value of type <code>Set</code>
   * @param <code>range</code> a value of type <code>POSet</code>
   */
  public FunctionCompletePOSet(Set domain, CompletePOSet range) {
    super(domain,range);
  }

  /**
   * Create a function space from an array of domains and a range.
   *
   * @param <code>domains</code> a value of type <code>Set[]</code>
   * @param <code>range</code> a value of type <code>POSet</code>
   */
  public FunctionCompletePOSet(Set[] domains, CompletePOSet range) {
    super(domains,range);
  }

  /**
   * Create a function space from an array of domains and a range.
   *
   * @param <code>domains</code> a value of type <code>Set[]</code>
   * @param <code>range</code> a value of type <code>POSet</code>
   */
  public FunctionCompletePOSet(CompletePOSet[] domains, CompletePOSet range) {
    this(new TupleCompletePOSet(domains), range);
  }

  
  public Object bottom() {
    Object[] fValues = new Object[(int)domain.size()];
    for (int i=0; i<domain.size(); i++) {
      fValues[i]=((CompletePOSet)range).bottom();
    }
    return new TabledFunction(domain, range, fValues);
  }
}
