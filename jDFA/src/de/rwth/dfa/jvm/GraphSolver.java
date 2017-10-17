package de.rwth.dfa.jvm; // Generated package name

import java.util.Iterator;

//import de.fub.bytecode.classfile.*; 
//import de.fub.bytecode.generic.*; 
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.utils.Stopwatch;

import de.rwth.graph.*;
import de.rwth.dfa.*;
import de.rwth.domains.*;

/**
 * An abstract class for a graph based JVM abstraction solver. The solution is
 * computed using the classical iterative algorithm provided by the class {@link
 * de.rwth.dfa.DataFlowSolver}. 
 *
 * <p>
 * 
 * To get a working implementation, the methods <code>createGraph()</code>,
 * <code>getInit(Graph.Node,boolean)</code>, <code>getFunction(Graph.Node)</code>,
 * and
 * <code>transferSolution(FlowGraph,Graph.NodeDyer,boolean,boolean,Object[]))</code>must
 * be provided.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: GraphSolver.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public abstract class GraphSolver extends Solver {
  /**
   * Creates a new <code>GraphSolver</code> instance with stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public GraphSolver(Abstraction abstraction, String methodName,
		      InstructionList methodInstrs, CodeException[] methodExceptions,
		      Stopwatch stopwatch) {
     super(abstraction,  methodName, methodInstrs, methodExceptions,stopwatch);
   }

  /**
   * Creates a new <code>GraphSolver</code> instance with out stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public GraphSolver(Abstraction abstraction, String methodName,
		      InstructionList methodInstrs, CodeException[] methodExceptions) {
     super(abstraction,  methodName, methodInstrs, methodExceptions);
   }

  /**
   * Implements the graph based solution:
   * <ol>
   * 
   * <li>A graph is created using <code>createGraph()</code>.
   * </li>
   *
   * <li>If there is a stop watch, <code>stopwatch.split("graph creation")</code> is
   * executed.
   * </li>
   *
   * <li>An initial coloring of the graph is done using <code>getInit(Node)</code>.
   * </li>
   * 
   * <li>The transfer functions of the nodes are computed using
   * <code>getFunction(Node)</code>.  </li>
   * 
   * <li>If there is a stop watch, <code>stopwatch.split("solver init")</code> is
   * executed.
   * </li>
   *
   * <li>The solution and the number of iterations are computed using
   * {@link de.rwth.dfa.DataFlowSolver}.
   * </li>
   *
   * <li>If there is a stop watch, <code>stopwatch.split("solving")</code> is
   * executed.
   * </li>
   *
   * <li>The solution is transfered from graph level to instruction level using
   * <code>transferSolution(FlowGraph,Graph.NodeDyer,boolean,boolean,Object[]))</code>.
   * </li>
   * 
   * <li>If there is a stop watch, <code>stopwatch.split("transfer")</code> is
   * executed.
   * </li>
   * 
   * </ol>
   * 
   * @param solution an <code>Object[]</code> value
   * @param isForward a <code>boolean</code> value
   * @param isAll a <code>boolean</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  protected void compute(Object[] solution, boolean isForward, boolean isAll)
    throws IllegalArgumentException {
    RootedGraph g=createGraph();
    if (stopwatch!=null) stopwatch.split("graph creation");

    Graph.NodeDyer inits = g.new NodeDyer();
    Graph.NodeDyer functions = g.new NodeDyer();
    for (Iterator e=g.getNodes(); e.hasNext(); ) {
      RootedGraph.Node node = (RootedGraph.Node)e.next();
      inits.setColor(node,getInit(node,isForward));
      functions.setColor(node,getFunction(node));
    }
    
    if (stopwatch!=null) stopwatch.split("solver init");
    DataFlowSolver dfaSolver =
      new DataFlowSolver(g, inits, functions, isAll, isForward);

    try {
      Graph.NodeDyer gsolution = dfaSolver.solve();
      iterations=dfaSolver.getIterations();
      if (stopwatch!=null) stopwatch.split("solving");
      transferSolution(g, gsolution, isAll, isForward, solution);
      if (stopwatch!=null) stopwatch.split("transfer");
    } catch (FunctionException ex) {
      throw new IllegalArgumentException(ex.toString());
    } catch (IllegalAccessException ex) {
      throw new IllegalArgumentException(ex.toString());
    } catch (InstantiationException ex) {
      throw new IllegalArgumentException(ex.toString());
    }
  }

  /**
   * Supposed to compute the graph from the method to be analyzed.
   *
   * @return a <code>RootedGraph</code> value
   */
  protected abstract RootedGraph createGraph();

  /**
   * Supposed to compute the initial value of a node. It must be an element of the
   * lattice of the abstraction.
   *
   * @param n a <code>Graph.Node</code> value
   * @param isForward a <code>boolean</code> value
   * @return an <code>Object</code> value
   */
  protected abstract Object getInit(RootedGraph.Node n, boolean isForward);
  
  /**
   * Supposed to compute the transfer function value of a node. It must be an
   * monotonic endomorphism on the lattice of the abstraction.
   *
   * @param n a <code>Graph.Node</code> value
   * @return an <code>Object</code> value
   */
  protected abstract Object getFunction(RootedGraph.Node n);

  /**
   * Supposed to transfer a graph based solution to an array.
   *
   * @param graph a <code>RootedGraph</code> value
   * @param gsolution a <code>Graph.NodeDyer</code> value
   * @param isForward a <code>boolean</code> value
   * @param isAll a <code>boolean</code> value
   * @param solution an <code>Object[]</code> value
   */
  protected abstract void transferSolution(RootedGraph graph, Graph.NodeDyer gsolution,
					   boolean isForward, boolean isAll,
					   Object[] solution);
}


