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
 * A JVM abstraction solver using factorised basic block graphs.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FactorisedBasicBlockGraphSolver.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FactorisedBasicBlockGraphSolver extends FactorisedFlowGraphSolver {
  /**
   * Creates a new <code>FactorisedBasicBlockGraphSolver</code> instance with stop
   * watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public FactorisedBasicBlockGraphSolver(Abstraction abstraction,
					 String methodName,
					 InstructionList methodInstrs,
					 CodeException[] methodExceptions,
					 Stopwatch stopwatch) {
     super(abstraction, methodName, methodInstrs, methodExceptions,stopwatch);
   }

  /**
   * Creates a new <code>FactorisedBasicBlockGraphSolver</code> instance with out
   * stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public FactorisedBasicBlockGraphSolver(Abstraction abstraction,
					 String methodName,
					 InstructionList methodInstrs,
					 CodeException[] methodExceptions) {
    super(abstraction, methodName, methodInstrs, methodExceptions);
   }

  /**
   * Creates a factorised basic block graph for a method.
   *
   * @return a <code>RootedGraph</code> value
   */
  protected RootedGraph createGraph() {
    FactorisedFlowGraph flowGraph = (FactorisedFlowGraph)super.createGraph();
    BasicBlockGraph basicBlockGraph = new BasicBlockGraph(flowGraph);
    if (stopwatch!=null) stopwatch.split("BB creation");
    return basicBlockGraph;
  }

  /**
   * Computes the initial value for a node, i.e. for a basic block.
   *
   * @param n a <code>RootedGraph.Node</code> value
   * @return an <code>Object</code> value
   */
  protected Object getInit(RootedGraph.Node n, boolean isForward) {
    InstructionHandleVector ihv = (InstructionHandleVector)n.getLabel();
    return abstraction.getInitialValue(ihv, isForward?n.isRoot():n.isLeaf());
  } 
  
  /**
   * Computes the transfer function for a node, i.e. for a basic block..
   *
   * @param n a <code>RootedGraph.Node</code> value
   * @return an <code>Object</code> value
   */
  protected Object getFunction(RootedGraph.Node n) {
    InstructionHandleVector ihv = (InstructionHandleVector)n.getLabel();
    return abstraction.getAbstract(ihv);
  }

  /**
   * Transfers the solution associated with the basic blocks to an array.
   *
   * @param graph a <code>RootedGraph</code> value
   * @param gsolution a <code>Graph.NodeDyer</code> value
   * @param isForward a <code>boolean</code> value
   * @param isAll a <code>boolean</code> value
   * @param solution an <code>Object[]</code> value
   */
  protected void transferSolution(RootedGraph graph, Graph.NodeDyer gsolution,
				  boolean isForward, boolean isAll,
				  Object[] solution) {
    BasicBlockGraph basicBlockGraph = (BasicBlockGraph)graph;
    for (Iterator e=basicBlockGraph.getNodes(); e.hasNext(); ) {
      Graph.Node node = (Graph.Node)e.next();
      Vector flowNodes = basicBlockGraph.flowNodes(node);
      Object newSolution = null;
      Function lastF = null;
      InstructionHandleVector ihv = (InstructionHandleVector)node.getLabel();
      for (int i=(isForward?0:flowNodes.size()-1);
	   (isForward && i<flowNodes.size()) || (!isForward && i>=0);
	   i+=isForward?1:-1) {
	Graph.Node flowNode=(Graph.Node)flowNodes.elementAt(i);
	InstructionHandle instrh = (InstructionHandle)flowNode.getLabel();
	if (newSolution==null)
	  newSolution=(Object)gsolution.getColor(node);
	else {
	  try {
	    newSolution= lastF.apply(newSolution);
	  } catch (FunctionException ex) {
	    throw new IllegalArgumentException(ex.toString());
	  }
	}
	lastF=abstraction.getAbstract(instrh);
	int k = ((Integer)nodesToIndex.get(flowNode)).intValue();
	if (k!=-1) solution[k]=newSolution;
      }
    }
  }
}
