package de.rwth.dfa.jvm; // Generated package name

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

//import de.fub.bytecode.classfile.*; 
//import de.fub.bytecode.generic.*; 
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import de.rwth.utils.Stopwatch;

import de.rwth.graph.*;
import de.rwth.domains.*;

/**
 * A JVM abstraction solver using basic block graphs.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: BasicBlockGraphSolver.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class BasicBlockGraphSolver extends FactorisedBasicBlockGraphSolver {
  /**
   * Creates a new <code>BasicBlockGraphSolver</code> instance with stop
   * watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public BasicBlockGraphSolver(Abstraction abstraction,
			       String methodName,
			       InstructionList methodInstrs,
			       CodeException[] methodExceptions,
			       Stopwatch stopwatch) {
     super(abstraction, methodName, methodInstrs, methodExceptions,stopwatch);
   }

  /**
   * Creates a new <code>BasicBlockGraphSolver</code> instance with out
   * stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public BasicBlockGraphSolver(Abstraction abstraction,
			       String methodName,
			       InstructionList methodInstrs,
			       CodeException[] methodExceptions) {
    super(abstraction, methodName, methodInstrs, methodExceptions);
   }

  /**
   * Creates a basic block graph for a method.
   *
   * @return a <code>RootedGraph</code> value
   */
  protected RootedGraph createGraph() {
    FlowGraph flowGraph = new FlowGraph(methodName, methodInstrs,
					methodExceptions);
    nodesToIndex=nodesToIndex(flowGraph);
    BasicBlockGraph basicBlockGraph = new BasicBlockGraph(flowGraph);
    if (stopwatch!=null) stopwatch.split("BB creation");
    return basicBlockGraph;
  }
}
