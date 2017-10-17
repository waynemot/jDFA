package de.rwth.dfa; // Generated package name

import java.util.Iterator;

import de.rwth.domains.Function;
import de.rwth.domains.FunctionException;
import de.rwth.domains.Lattice;

import de.rwth.graph.Graph;
import de.rwth.graph.RootedGraph;

/**
 * Implements the text-book graph based data flow analysis algorithm. Given a data
 * flow problem, it computes the maximum fixed point (MPF) solution.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: DataFlowSolver.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class DataFlowSolver  {
  
  /**
   * The graph to operate on.
   *
   */
  protected RootedGraph g = null;

  /**
   * The initial values for the computation. Always a coloring of <code>g</code>
   *
   */
  protected Graph.NodeDyer inits = null;

  /**
   * The transfer functions for the computation. Always a coloring of <code>g</code>
   *
   */
  protected Graph.NodeDyer functions = null;

  
  /**
   * The lattice for the values and on which the functions operate.
   *
   */
  protected Lattice l = null;

  
  /**
   * Universal (meet) or existential (join) data flow problem?
   *
   */
  protected boolean isAll;

  /**
   * Forward (along the edges) or backward (opposite the edges) data flow problem?
   *
   */
  protected boolean forward;

  
  /**
   * Number of iterations computed so far.
   *
   */
  protected long iterations;

  
  /**
   * Upper bound for the number of iterations.
   *
   */
  protected long iterationsBound;

  /**
   * The class used to create a new work list.
   *
   */
  protected Class worklistFactory;
  
  
  /**
   * Creates a new <code>DataFlowSolver</code> instance. The data flow problem is
   * described by a graph <code>g</code>, colorings of the graph giving initial
   * values and transfer function, kind of problem, and direction of control flow in
   * the graph. <strong>The functions must be monotonic on the underlying
   * lattice!</strong> Otherwise, the computation may be complete nonsense and
   * terminate only when the upper bound of iterations is reached.
   *
   * @param g a <code>RootedGraph</code> value
   * @param inits a <code>Graph.NodeDyer</code> value
   * @param functions a <code>Graph.NodeDyer</code> value
   * @param isAll a <code>boolean</code> value
   * @param forward a <code>boolean</code> value
   * @param iterationsBound a <code>long</code> value
   * @param worklistFactory a <code>Class</code> value
   * @exception IllegalArgumentException if
   * <ol>
   * 
   * <li>One of the colorings in <code>functions</code> is not a {@link
   * de.rwth.domains.Function}.</li>
   * 
   * <li>One of the colorings in <code>functions</code> does not have identical
   * domain and range (endomorphism).</li>
   * 
   * <li>One of the colorings in <code>functions</code> does not have a {@link
   * de.rwth.domains.Lattice} as domain and range.</li>
   * 
   * <li>Not all colorings in <code>functions</code> have the same domain and
   * range.</li>
   *
   * <li>One of the colorings in <code>inits</code> is not an element of the unique
   * {@link de.rwth.domains.Lattice} determined by <code>functions</code>.</li>
   *  
   * </ol>
   */
  public DataFlowSolver(RootedGraph g, Graph.NodeDyer inits, Graph.NodeDyer functions,
			boolean isAll, boolean forward, long iterationsBound,
			Class worklistFactory) throws IllegalArgumentException {
    this.l = null;
    for (Iterator e=g.getNodes(); e.hasNext(); ) {
      Graph.Node node= (Graph.Node)e.next();
      
      if (!(functions.getColor(node)!=null &&
	    functions.getColor(node) instanceof Function)) {
	throw new IllegalArgumentException("functions.getColor("+node+
                                           ") is not a function");
      }
      Function f = (Function)functions.getColor(node);
      if (!f.getDomain().equals(f.getRange())) {
	throw new IllegalArgumentException("functions.getColor("+node+
                                           ") is not an endomorphism");
      }
      if (!(f.getDomain() instanceof Lattice)) {
	throw new IllegalArgumentException("functions.getColor("+node+
                                           ").getDomain() is not a lattice");
      }
      if (this.l!=null) {
	if (!this.l.equals(f.getDomain())) {
	  throw new IllegalArgumentException("functions.getColor(node)."+
                                 "getDomain() must be the same for all nodes");
	}
      } else {
	this.l=(Lattice)f.getDomain();
      }
      
      Object v = (Object)inits.getColor(node);
      if (!this.l.isElement(v)) {
	throw new IllegalArgumentException("inits.getColor("+node+
                                         ") is not an element of the lattice");
      }
    }
    this.g=g;
    this.inits=inits;
    this.functions=functions;
    this.isAll=isAll;
    this.forward=forward;
    this.iterations=-1;
    this.iterationsBound=iterationsBound;
    this.worklistFactory=worklistFactory;
    //System.err.println("/* BOUND: "+this.iterationsBound+"*/");
    //System.err.println("/* INIT=================== */");
    //System.err.println(inits);	  
    //System.err.println("/* END=================== */");
    //System.err.println("/* FNS=================== */");
    //System.err.println(functions);	  
    //System.err.println("/* END=================== */");
    
  }

  /**
   * Creates a new <code>DataFlowSolver</code> instance with default upper bound and
   * work list {@link StackWorklist}.
   *
   * @param g a <code>RootedGraph</code> value
   * @param inits a <code>Graph.NodeDyer</code> value
   * @param functions a <code>Graph.NodeDyer</code> value
   * @param isAll a <code>boolean</code> value
   * @param forward a <code>boolean</code> value
   * @exception IllegalArgumentException if an error occurs
   * @see #DataFlowSolver(RootedGraph, Graph.NodeDyer, Graph.NodeDyer, boolean , boolean, long, Class)
   */
  public DataFlowSolver(RootedGraph g, Graph.NodeDyer inits, Graph.NodeDyer functions,
			boolean isAll, boolean forward) throws IllegalArgumentException {
    this(g,inits,functions,isAll,forward,5*g.sizeNodes()*g.sizeNodes(),null);
    try {
      this.worklistFactory=
	Class.forName(getClass().getPackage().getName()+".StackWorklist");
    } catch (ClassNotFoundException ex) {
      throw new IllegalArgumentException(ex.toString());
    }
  }

  
  /**
   * Returns the number of iterations used in <code>solve()</code>.
   *
   * @return a <code>long</code> value: If solve was not called so for, <code>-1</code>
   */
  public long getIterations() { return iterations; }

  
  /**
   * Recomputes the value for <code>node</code> in the graph of this data flow solver.
   *
   * @param node a <code>Graph.Node</code> value
   * @param values a <code>Graph.NodeDyer</code> value
   * @return an <code>Object</code> value
   * @exception FunctionException if on the functions in the <code>functions</code>
   * of this data flow solver throws this exception.
   */
  protected Object recompute(Graph.Node node, Graph.NodeDyer values)
    throws FunctionException {
    //System.err.println("recompute "+node.getLabel());

    Object newvalue = (Object)values.getColor(node);
    for (Iterator e = (forward) ? node.getInEdges() : node.getOutEdges();
	 e.hasNext();
	 ) {
      Graph.Node.Edge edge = (Graph.Node.Edge)e.next();
      Graph.Node dependOnNode = (forward) ? edge.getStart() : edge.getEnd();
      Object x  = (Object)values.getColor(dependOnNode);
      Function f = (Function)functions.getColor(dependOnNode);
      Object fx = f.apply(x);
      newvalue = (isAll)?l.meet(newvalue, fx):l.join(newvalue, fx);
    }
    return newvalue;
  }

  
  /**
   * Adds all nodes which depend on a specific node in the graph of this solver to a
   * work list.
   *
   * @param worklist a <code>Worklist</code> value
   * @param node a <code>Graph.Node</code> value
   */
  protected void addDepending(Worklist worklist, Graph.Node node) {
    for (Iterator e = (forward) ? node.getOutEdges() : node.getInEdges();
	 e.hasNext();
	 ) {
      Graph.Node.Edge edge = (Graph.Node.Edge)e.next();
      Graph.Node dependsOnNode = (forward) ? edge.getEnd() : edge.getStart();
      worklist.add(dependsOnNode);
    }
  }

  /**
   * Solves this data flow problem.
   *
   * @return a <code>Graph.NodeDyer</code> value: The results as coloring of the graph.
   * @exception InstantiationException if this exception is caused by creating a new
   * instance of the work list factory.
   * @exception IllegalAccessException if this exception is caused by creating a new
   * instance of the work list factory.
   * @exception ClassCastException if the new instance created by the work list
   * factory is not a work list.
   * @exception FunctionException if one of the functions throws it
   * @exception IllegalArgumentException if the number of iterations reaches the
   * upper bound.
   */
  public Graph.NodeDyer solve()
    throws InstantiationException, IllegalAccessException,
           ClassCastException, FunctionException, IllegalArgumentException {
    Graph.NodeDyer values = g.new NodeDyer();
    for (Iterator e=g.getNodes(); e.hasNext(); ) {
      Graph.Node node = (Graph.Node)e.next();
      values.setColor(node,inits.getColor(node));
    }
    
    Worklist worklist = (Worklist)worklistFactory.newInstance();
    for (Iterator e=g.getNodes(); e.hasNext(); ) {
      Graph.Node node = (Graph.Node)e.next();
      worklist.add(node);
    }
    
    for (Iterator e=(forward?g.getRoots():g.getLeafs()); e.hasNext(); ) {
      Graph.Node node = (Graph.Node)e.next();
      for (Iterator d = (forward) ? node.getOutEdges() : node.getInEdges();
	   d.hasNext();
	   ) {
	Graph.Node.Edge edge = (Graph.Node.Edge)d.next();
	Graph.Node dependsOnNode = (forward) ? edge.getEnd() : edge.getStart();
	worklist.moveToFront(dependsOnNode);
      }
    }
    
    iterations =0;
    while (!worklist.isEmpty()) {
      //System.err.print(worklist.size()+" ");
      //System.err.print(".");
      //System.err.println(worklist);
      //System.err.println(values);
      /*
      for (Iterator e=g.getNodes(); e.hasNext(); ) {
	Graph.Node node = (Graph.Node)e.next();
	System.err.println(node+" "+values.getColor(node));
      }
      */
      
      iterations++;
      if (iterations>iterationsBound)
	throw new IllegalArgumentException("DataFlowSolver aborted due to possible non-termination after "+iterations+" iterations");
      Graph.Node node = (Graph.Node)worklist.get();
      
      Object newvalue = recompute(node, values);
	
      if (!l.equals(newvalue, (Object)values.getColor(node))) {
	values.setColor(node,newvalue);
	addDepending(worklist, node);
      }
    }
    //System.err.println();

    /*
    for (Iterator e=g.getNodes(); e.hasNext(); ) {
      Graph.Node node = (Graph.Node)e.next();
      if (!l.equals(recompute(node, values),(Object)values.getColor(node))) {
	throw new Exception("wrong result at node "+node);
      }
    }
    */
    
    //System.err.println("/* SOL=================== */");
    //System.err.println(values);	  
    //System.err.println("/* END=================== */");
    return values;
  }
}
