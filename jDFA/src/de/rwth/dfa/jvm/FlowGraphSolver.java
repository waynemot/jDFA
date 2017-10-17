package de.rwth.dfa.jvm; // Generated package name

import java.util.Hashtable;
import java.util.Iterator;

//import de.fub.bytecode.classfile.*; 
//import de.fub.bytecode.generic.*; 
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.utils.Stopwatch;

import de.rwth.graph.*;

/**
 * A JVM abstraction solver using flow graphs.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FlowGraphSolver.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FlowGraphSolver extends FactorisedFlowGraphSolver {
  /**
   * Creates a new <code>FlowGraphSolver</code> instance with stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public FlowGraphSolver(Abstraction abstraction,
			 String methodName,
			 InstructionList methodInstrs,
			 CodeException[] methodExceptions,
			 Stopwatch stopwatch) {
     super(abstraction, methodName, methodInstrs, methodExceptions,stopwatch);
   }

  /**
   * Creates a new <code>FlowGraphSolver</code> instance with out stop
   * watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public FlowGraphSolver(Abstraction abstraction,
			 String methodName,
			 InstructionList methodInstrs,
			 CodeException[] methodExceptions) {
    super(abstraction, methodName, methodInstrs, methodExceptions);
   }

  /**
   * Creates a flow graph for a method.
   *
   * @return a <code>RootedGraph</code> value
   */
  protected RootedGraph createGraph() {
    RootedGraph flowGraph = new FlowGraph(methodName, methodInstrs,
					  methodExceptions);
    nodesToIndex=nodesToIndex(flowGraph);
    return flowGraph;
  }
}
