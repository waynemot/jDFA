package de.rwth.dfa.jvm; // Generated package name

//import de.fub.bytecode.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionHandle;
/**
 * A handle for the {@link ExceptionHeaderInstruction}.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: ExceptionHeaderInstructionHandle.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class ExceptionHeaderInstructionHandle extends InstructionHandle {
  protected String text;
  protected InstructionHandle origin;
  
  /**
   * Creates a new <code>ExceptionHeaderInstructionHandle</code> instance.
   *
   * @param text a <code>String</code> value
   * @param origin an <code>InstructionHandle</code> value
   */
  public ExceptionHeaderInstructionHandle(String text, InstructionHandle origin) {
    super(new ExceptionHeaderInstruction());
    this.text=text;
    this.origin=origin;
  }

  public String toString() { return text; }

  public InstructionHandle getOrigin() { return origin; }
  
}
