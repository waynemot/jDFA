package de.rwth.domains; // Generated package name

/**
 * This class represents exceptions special to functions.
 * 
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FunctionException.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FunctionException extends Exception {
  /**
   * Create a FunctionException w/o message.
   */
  public FunctionException() { super(); }
  
  /**
   * Create a FunctionException with message.
   */
  public FunctionException(String s) { super(s); }
}
