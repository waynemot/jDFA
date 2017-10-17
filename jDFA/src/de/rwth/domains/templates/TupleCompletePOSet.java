package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of complete partially ordered sets as tuple from other
 * complete partially ordered sets.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TupleCompletePOSet.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class TupleCompletePOSet extends TuplePOSet implements CompletePOSet {
  /**
   * Creates a <code>TupleCompletePOSet</code> from an array of
   * <code>CompletePOSets</code>.
   *
   * @param <code>cposets</code> the component <code>CompletePOSet</code>
   */
  public TupleCompletePOSet (CompletePOSet[] cposets) {
    super((POSet [])cposets);
  }

  /**
   * Creates a <code>TupleCompletePOSet</code> from two <code>CompletePOSets</code>.
   *
   * @param <code>cposet1</code> 1st component <code>Set</code>
   * @param <code>cposet2</code> 2nd component <code>Set</code>
   */
  public TupleCompletePOSet (CompletePOSet cposet1, CompletePOSet cposet2) {
    this(new CompletePOSet[] {cposet1, cposet2});
  }

  /**
   * The method <code>bottom</code> gets the least element of this
   * <code>TupleCompletePOSet</code> which is the tuple of the least elements of the
   * component sets.
   *
   * @return the least element in this set
   */
  public Object bottom() {
    Object[] bots = new Object[sets.length];
    for (int i=0; i< sets.length; i++) {
      bots[i]=((CompletePOSet)sets[i]).bottom();
    }
    return new TupleElement(bots);
  }
}
