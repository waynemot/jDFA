package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of lifted complete partially ordered sets,
 * which are partially ordered sets with an unique additional least
 * element and which are complete after this addition.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: LiftedCompletePOSet.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 *
 */
public class LiftedCompletePOSet extends LiftedPOSet implements CompletePOSet {
  /**
   * Lifts an existing partially ordered set by adding a new unique least element.
   * <br> <strong>There is no check if the resulting structure fulfills the specified
   * {@link de.rwth.domains.Domain#checkProperties(CompletePOSet cposet)
   * constraints}!</strong>
   *
   * @param <code>poset</code> the partially ordered set to be lifted
   */
  public LiftedCompletePOSet(POSet poset) {
    super(poset);
  }

  /**
   * The method <code>bottom</code> gets the least element of this
   * <code>CompletePOSet</code>, which is the
   * unique new bottom element of this lifted partially ordered set.
   *
   * @return the least element in this set
   */
  public Object bottom() { return bottom; }
}



