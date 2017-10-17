package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;

/**
 * Class for the creation of intervals in the integers with natural
 * order.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: IntegerPOSet.java,v 1.3 2002/09/27 08:22:58 mohnen Exp $
 */
public class IntegerPOSet extends SimpleSet implements POSet {
  protected boolean smalltolarge;
  /**
   * Create an interval in the integers.
   *
   * @param <code>from</code> a value of type <code>int</code>
   * @param <code>to</code> a value of type <code>int</code>
   */
  public IntegerPOSet(int from, int to) {
    super(from,to);
    smalltolarge=from<=to;
  }

  /**
   * The method <code>lt</code> checks if one element is less than
   * an other element.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>e1</code> and
   *                               <code>e2</code> are
   *                               <code>Integer</code> and first <
   *                               second.
   */
  public boolean lt(Object e1, Object e2) {
    if (!isElement(e1) || !isElement(e2)) return false;
    if (smalltolarge)
      return ((Integer)e1).intValue()<((Integer)e2).intValue();
    else
      return ((Integer)e1).intValue()>((Integer)e2).intValue();
  }
  
  /**
   * The method <code>le</code> checks if one element is less than
   * an other element.
   *
   * @param <code>e1</code> a value of type <code>Object</code>
   * @param <code>e2</code> a value of type <code>Object</code>
   * @return <code>true</code> iff both <code>e1</code> and
   *                               <code>e2</code> are
   *                               <code>SimpleElements</code>
   *                               encapsulating an
   *                               <code>Integer</code> and first <=
   *                               second.
   */
  public boolean le(Object e1, Object e2) {
    if (!isElement(e1) || !isElement(e2)) return false;
    if (smalltolarge)
      return ((Integer)e1).intValue() <= ((Integer)e2).intValue();
    else
      return ((Integer)e1).intValue() >= ((Integer)e2).intValue();
  }
}




