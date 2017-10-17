package de.rwth.dfa.jvm.samples; // Generated package name

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import de.rwth.utils.Stopwatch;

import de.rwth.dfa.jvm.*;

/**
 * An application for analyzing the size of the operand stack at each instruction of
 * all methods of a class file.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: SSAnalyser.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class SSAnalyser extends AbstractAnalyser {
  /**
   * Creates a new <code>SSAnalyser</code> instance.
   *
   * @param args a <code>String[]</code> value
   * @see AbstractAnalyser
   */
  public SSAnalyser(String [] args) { super(args); }
  
  /**
   * Program entry point.
   *
   * @param args a <code>String[]</code> value
   * @see AbstractAnalyser
   */
  public static void main(String [] args) {
    try {
      new SSAnalyser(args).process();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Returns a {@link SSAbstraction} for a method.
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
    return new SSAbstraction(code.getMaxStack(),method.getConstantPool());
  }

  /**
   * Returns one group of solvers: {@link ExecutionSolver}, {@link FlowGraphSolver},
   * {@link BasicBlockGraphSolver}.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   * @return a <code>Solver[]</code> value
   */
  public Solver[] getSolvers(Abstraction abstraction, String methodName,
			     InstructionList methodInstrs,
			     CodeException[] methodExceptions,
			     Stopwatch stopwatch) {
    return new Solver[] {
      new ExecutionSolver(abstraction, methodName, methodInstrs,
			  methodExceptions, stopwatch),
      new FlowGraphSolver(abstraction, methodName, methodInstrs,
			  methodExceptions, stopwatch),
      new BasicBlockGraphSolver(abstraction, methodName, methodInstrs,
				methodExceptions, stopwatch),
    };
  }
}







