package de.rwth.domains; // Generated package name

import java.util.Iterator;

/**
 * Interface for describing mathematical functions.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Function.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface Function {
  /**
   * Returns the domain of this function.
   *
   * @return a value of type <code>Set</code>
   */
  public Set getDomain();
  
  /**
   * Returns the range (co-domain) of this function.
   *
   * @return a value of type <code>Set</code>
   */
  public Set getRange();

  /**
   * Applies this function to an argument and returns the result.
   *
   * @param <code>x</code> the argument
   * @return this function's value for argument <code>x</code>
   * @exception <code>FunctionException<code> if <code>x</code> is not
   *                                          an element of the
   *                                          functions domain.
   */
  public Object apply(Object x) throws FunctionException;


  
  /**
   * Contains implementations of useful tools for functions.
   *
   */
  public static class Tools {
    /**
     * Creates a <code>String</code> representation of a function. For each
     * argument/value combination, it calls the <code>toString</code> methods of
     * domain and range and concatenates them with <code>"->"</code>. All thes are
     * concatenated with <code>", "</code> and surrounded this with
     * <code>"{"</code>...<code>"}"</code>.
     *
     * @param f a <code>Function</code> value
     * @return the <code>String</code> representing this function.
     */
    public static String toString(Function f) {
      String result = "{";
      String separator="";
      int i=0;
      for (Iterator e=f.getDomain().iterator(); e.hasNext(); ) {
	Object x = (Object)e.next();
	try {
	  result += separator+x+"->"+ f.apply(x);
	} catch (FunctionException ex) {
	  throw new Error(ex.toString());
	}
	separator=", ";
	i++;
      }
      result+="}";
      return result;
    }
    
    /**
     * The method <code>equals</code> checks if a function and an object are equal.
     * It does that by checking:
     * <ol>
     * <li> if neither is <code>null</code>
     * <li> if the object is a function
     * <li> with same domain and range as the function
     * <li> and with same function result for all arguments
     * </ol>
     *
     * @param f a <code>Function</code> value
     * @param <code>o</code> a value of type <code>Object</code>
     * @return <code>true</code> iff <code>o</code> is equal to this function
     */
    public static boolean equals(Function f, Object o) {
      if (f==null || o==null || !(o instanceof Function)) return false;
      Function g = (Function)o;
      if (!f.getDomain().equals(g.getDomain()) ||
	  !f.getRange().equals(g.getRange())) return false;
      for (Iterator e=f.getDomain().iterator(); e.hasNext(); ) {
	Object x = (Object)e.next();
	try {
	  if (!f.apply(x).equals(g.apply(x))) return false;
	} catch (FunctionException ex) {
	  throw new Error(ex.toString());
	}
      }
      return true;
    }
  }
}
