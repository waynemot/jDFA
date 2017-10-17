package de.rwth.domains.templates; // Generated package name

import de.rwth.domains.*;


/**
 * Class for representing the elements of <code>TupleSet</code>.
 *
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TupleElement.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class TupleElement {
  /**
   * All the component elements which are contained in this tuple.
   */
  protected Object[] elements = null;
  
  /**
   * Creates a new <code>TupleElement</code> instance from an array of components.
   *
   * @param elements an <code>Object[]</code> value
   */
  public  TupleElement(Object[] elements) {
    super();
    this.elements=(Object[])elements.clone();
  }
  
  /**
   * Creates a new <code>TupleElement</code> instance from two components.
   *
   * @param e1 an <code>Object</code> value
   * @param e2 an <code>Object</code> value
   */
  public  TupleElement(Object e1, Object e2) {
    this(new Object[] {e1,e2});
  }
  
  /**
   * Returns an array of all elements of this tuple.
   *
   * @return an <code>Object[]</code> value
   */
  public Object[] getElements() {
    return (Object[])elements.clone();
  }
  
  /**
   * The method <code>toString</code> creates a <code>String</code>
   * representation. It calls the <code>toString</code> methods of
   * the components, concatenates them with <code>", "</code> and
   * surrounds this with <code>"("</code>...<code>")"</code>.
   *
   * @return the <code>String</code> representing this tuple.
   */
  public String toString() {
    String result = "(";
    for (int i=0; i<elements.length; i++)
      result+=((i==0)?"":", ")+elements[i];
    result+=")";
    return result;
  }
}


