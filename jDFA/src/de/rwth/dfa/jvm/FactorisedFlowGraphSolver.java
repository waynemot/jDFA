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
 * A JVM abstraction solver using factorised flow graphs.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FactorisedFlowGraphSolver.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FactorisedFlowGraphSolver extends GraphSolver {
  Hashtable nodesToIndex = null;

  /**
   * Creates a new <code>FactorisedFlowGraphSolver</code> instance with stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public FactorisedFlowGraphSolver(Abstraction abstraction,
				   String methodName,
				   InstructionList methodInstrs,
				   CodeException[] methodExceptions,
				   Stopwatch stopwatch) {
     super(abstraction, methodName, methodInstrs, methodExceptions,stopwatch);
   }

  /**
   * Creates a new <code>FactorisedFlowGraphSolver</code> instance with out stop
   * watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public FactorisedFlowGraphSolver(Abstraction abstraction,
				   String methodName,
				   InstructionList methodInstrs,
				   CodeException[] methodExceptions) {
    super(abstraction, methodName, methodInstrs, methodExceptions);
   }

  /**
   * Computes a hash table associating nodes with indices in the solution array.
   *
   * @param graph a <code>Graph</code> value
   * @return a <code>Hashtable</code> value
   */
  protected Hashtable nodesToIndex(Graph graph) {
    Hashtable nodesToIndex = new Hashtable();
    int i=0;
    for (Iterator e=graph.getNodes(); e.hasNext(); i++) {
      Graph.Node node = (Graph.Node)e.next();
      if (node.getLabel() instanceof ExceptionHeaderInstructionHandle) {
	i--;
	nodesToIndex.put(node, new Integer(-1));
      } else 
	nodesToIndex.put(node, new Integer(i));
    }
    return nodesToIndex;
  }
  
  /**
   * Creates a factorised flow graph for a method.
   *
   * @return a <code>RootedGraph</code> value
   */
  protected RootedGraph createGraph() {
    RootedGraph flowGraph = new FactorisedFlowGraph(methodName, methodInstrs,
						    methodExceptions);
    nodesToIndex=nodesToIndex(flowGraph);
    return flowGraph;
  }

  /**
   * Computes the initial value for a node, i.e. for a single instruction.
   *
   * @param n a <code>RootedGraph.Node</code> value
   * @return an <code>Object</code> value
   */
  protected Object getInit(RootedGraph.Node n, boolean isForward) {
    InstructionHandle instrh = (InstructionHandle)n.getLabel();
    return abstraction.getInitialValue(instrh,
				       isForward?n.isRoot():n.isLeaf());
  } 
  
  /**
   * Computes the transfer function for a node, i.e. for a single instruction.
   *
   * @param n a <code>RootedGraph.Node</code> value
   * @return an <code>Object</code> value
   */
  protected Object getFunction(RootedGraph.Node n) {
    InstructionHandle instrh = (InstructionHandle)n.getLabel();
    return abstraction.getAbstract(instrh);
  }

  /**
   * Transfers the solution associated with the flow graph nodes to an array.
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
    for (Iterator e=graph.getNodes(); e.hasNext();) {
      Graph.Node node = (Graph.Node)e.next();
      int k = ((Integer)nodesToIndex.get(node)).intValue();
      if (k!=-1) solution[k]=(Object)gsolution.getColor(node);
    }
  }
}
