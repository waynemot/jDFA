package de.rwth.dfa.jvm; // Generated package name

import java.util.Vector;
import java.util.Enumeration;
//import de.fub.bytecode.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionHandle;

/**
 * A type safe vector class for {@link de.fub.bytecode.generic.InstructionHandle}
 * objects.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: InstructionHandleVector.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class InstructionHandleVector extends Vector {
  /**
   * Creates a new <code>InstructionHandleVector</code> instance.
   *
   */
  InstructionHandleVector() { super(); }
  
  /**
   * The type safe version of <code>addElement(Object)</code>.
   *
   * @param i an <code>InstructionHandle</code> value
   */
  public void append(InstructionHandle i) { addElement(i); }

  /**
   * Creates a string representation, where the elements are separated by newline
   * characters.
   *
   * @return a <code>String</code> value
   */
  public String toString() {
    String result = "";
    for (Enumeration e=elements(); e.hasMoreElements(); ) {
      result += e.nextElement()+"\n";
    }
    return result;
  }
}
