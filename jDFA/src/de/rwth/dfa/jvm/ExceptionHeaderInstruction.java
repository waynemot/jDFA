package de.rwth.dfa.jvm; // Generated package name

//import de.fub.bytecode.generic.Instruction;
//import de.fub.bytecode.generic.Visitor;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.Visitor;
/**
 * A class used to describe the entry of exception handlers.  This pseudo instruction
 * is inserted before the first real instruction of an exception handler.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: ExceptionHeaderInstruction.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class ExceptionHeaderInstruction extends Instruction {
  /**
   * Creates a new <code>ExceptionHeaderInstruction</code> instance.
   *
   */
  public ExceptionHeaderInstruction() { super((short)0,(short)0); }
  
  public void accept(Visitor v) { }
}
