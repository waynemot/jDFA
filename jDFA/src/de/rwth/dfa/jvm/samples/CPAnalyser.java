package de.rwth.dfa.jvm.samples; // Generated package name

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import de.rwth.utils.Stopwatch;

import de.rwth.domains.templates.TupleElement;

import de.rwth.dfa.jvm.*;

/**
 * An application for finding constants in the operand stack and the local variables
 * at each instruction of all methods of a class file.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CPAnalyser.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class CPAnalyser extends AbstractAnalyser {
  /**
   * Creates a new <code>CPAnalyser</code> instance.
   *
   * @param args a <code>String[]</code> value
   * @see AbstractAnalyser
   */
  public CPAnalyser(String [] args) {
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
   * Returns a {@link CPAbstraction} for a method.
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
    return new CPAbstraction(code.getMaxStack(),code.getMaxLocals(),
			     method.getConstantPool(),
			     instrs, methodExceptions);
  }


  /**
   * Returns two groups of solvers: {@link BasicBlockGraphSolver}, {@link
   * ExecutionSolver}, {@link FlowGraphSolver}, and {@link
   * FactorisedFlowGraphSolver}, {@link FactorisedBasicBlockGraphSolver}.
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
	new BasicBlockGraphSolver(abstraction, methodName, methodInstrs,
				  methodExceptions, stopwatch),
        new ExecutionSolver(abstraction, methodName, methodInstrs,
			    methodExceptions, stopwatch),
        new FlowGraphSolver(abstraction, methodName, methodInstrs,
			  methodExceptions, stopwatch),
	null,
        new FactorisedFlowGraphSolver(abstraction, methodName, methodInstrs,
				      methodExceptions, stopwatch),
	new FactorisedBasicBlockGraphSolver(abstraction, methodName, methodInstrs,
					    methodExceptions, stopwatch)
	  
    };
  }

  /**
   * Extracts constants and counts from a solution.
   *
   * @param solution an <code>Object[]</code> value
   * @return a <code>Hashtable</code> value
   */
  private Hashtable extractConstants(Object[] solution) {
    Hashtable result = new Hashtable();
    for (int i=0; i<solution.length; i++) {
      TupleElement element = (TupleElement)solution[i];
      Object[] locals = (Object[])((TupleElement)(element.getElements()[0])).getElements();
      for (int j=0; j<locals.length; j++ ) {
	Object o = locals[j];
	if (!(o==null
	      || o.equals(CFPComponentLattice.NONCONSTANTCOMPONENT)
	      || o.equals(CFPComponentLattice.UNKNOWNCOMPONENT))) {
	  Integer count = (Integer)result.get(o);
	  if (count==null) count=new Integer(1);
	  else count=new Integer(count.intValue()+1);
	  result.put(o,count);
	}
      }
      if (element.getElements()[1] instanceof Stack) {
	Stack stack = (Stack)element.getElements()[1];
	for (Enumeration e=stack.elements(); e.hasMoreElements(); ) {
	  Object o = e.nextElement();
	  if (!(o==null
		|| o.equals(CFPComponentLattice.NONCONSTANTCOMPONENT)
		|| o.equals(CFPComponentLattice.UNKNOWNCOMPONENT))) {
	    Integer count = (Integer)result.get(o);
	    if (count==null) count=new Integer(1);
	    else count=new Integer(count.intValue()+1);
	    result.put(o,count);
	  }
	}
      }
    }
    return result;
  }

  /**
   * Does additional statistics in addition to {@link
   * AbstractAnalyser#process(Method,String)}.
   *
   * @param method a <code>Method</code> value
   * @param filename a <code>String</code> value
   */
  public void process(Method method, String filename) {
    super.process(method, filename);
    String key = method.getName()+method.getSignature()+"@"+filename;
    String field = "constants";
    if (!table.hasFieldName(field)) table.addNewFieldName(field);
    table.setFieldForKey(field,key,extractConstants(refSolution));
  }
}







