package de.rwth.dfa.jvm.samples; // Generated package name

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import de.rwth.dfa.jvm.*;

/**
 * An application for finding constants in the operand stack and the local variables
 * at each instruction of all methods of a class file. In addition to {@link
 * CPAnalyser} this also includes constants computed in the code at run time
 * (folding).
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CFPAnalyser.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class CFPAnalyser extends CPAnalyser {
  /**
   * Creates a new <code>CFPAnalyser</code> instance.
   *
   * @param args a <code>String[]</code> value
   * @see AbstractAnalyser
   */
  public CFPAnalyser(String [] args) {
    super(args);
  }
  
  /**
   * Program entry point.
   *
   * @param args a <code>String[]</code> value
   * @see AbstractAnalyser
   */
  public static void main(String [] args) {
    try {
      new CFPAnalyser(args).process();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  /**
   * Returns a {@link CFPAbstraction} for a method.
   *
   * @param method a <code>Method</code> value
   * @param code a <code>Code</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param instrs an <code>InstructionList</code> value
   * @return an <code>Abstraction</code> value
   */
  public Abstraction getAbstraction(Method method, Code code,
				    CodeException[] methodExceptions,
				    InstructionList instrs) {
    return new CFPAbstraction(code.getMaxStack(),code.getMaxLocals(),
			      method.getConstantPool(),
			      instrs, methodExceptions);
  }
}







