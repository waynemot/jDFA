package de.rwth.dfa.jvm.samples; // Generated package name

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import de.rwth.utils.Stopwatch;

import de.rwth.domains.templates.BitVectorElement;

import de.rwth.dfa.jvm.*;

/**
 * An application for finding live variables in the local variables at each
 * instruction of all methods of a class file.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: LVAnalyser.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class LVAnalyser extends AbstractAnalyser {
    /**
   * Creates a new <code>LVAnalyser</code> instance.
   *
   * @param args a <code>String[]</code> value
   * @see AbstractAnalyser
   */
  public LVAnalyser(String [] args) {
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
      new LVAnalyser(args).process();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Returns a {@link LVAbstraction} for a method.
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
    return new LVAbstraction(method);
  }

  /**
   * Returns two groups of solvers: {@link FlowGraphSolver}, {@link
   * BasicBlockGraphSolver}, and {@link FactorisedFlowGraphSolver}, {@link
   * FactorisedBasicBlockGraphSolver}.
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
        new FlowGraphSolver(abstraction, methodName, methodInstrs,
			  methodExceptions, stopwatch),
	new BasicBlockGraphSolver(abstraction, methodName, methodInstrs,
				  methodExceptions, stopwatch),
	null,
        new FactorisedFlowGraphSolver(abstraction, methodName, methodInstrs,
				      methodExceptions, stopwatch),
	new FactorisedBasicBlockGraphSolver(abstraction, methodName, methodInstrs,
					    methodExceptions, stopwatch)
	  
    };
  }

  /**
   * Extracts the live/total ration from a solution.
   *
   * @param solution an <code>Object[]</code> value
   * @return a <code>Double</code> value
   */
  private Double extractLiveRatio(Object[] solution) {
    double livecount =0;
    double totalcount=0;
    for (int i=0; i<solution.length; i++) {
      BitVectorElement bv=(BitVectorElement)solution[i];
      totalcount+=bv.size();
      for (int j=0; j<bv.size(); j++)
	if (bv.get(j)) livecount++;
    }

    if (totalcount==0)
      return new Double(1);
    else
      return new Double(livecount/totalcount);
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
    String field = "live ratio";
    if (!table.hasFieldName(field)) table.addNewFieldName(field);
    table.setFieldForKey(field,key,extractLiveRatio(refSolution));
  }
}







