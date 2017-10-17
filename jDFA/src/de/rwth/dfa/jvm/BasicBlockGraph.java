package de.rwth.dfa.jvm; // Generated package name

import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;
import java.util.LinkedList;

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import de.rwth.graph.Graph;
import de.rwth.graph.RootedGraph;

/**
 * A class for the basic block graph of JVM methods. A basic block is a sequence of
 * instructions in a method, where the control flow is linear, i.e. no jumps (or
 * exceptions) can occur inside. For each basic block of a method, there is a node in
 * the graph. The label of the node is an {@link InstructionHandleVector} of the
 * instructions.. For each possible control flow between two basic blocks, there is
 * an edge from the first to the second instruction.
 *
 * <p>
 *
 * The roots of this graph are those nodes, where the first instruction is a root in
 * the flow graph.
 *
 * <p>
 *
 * The leafs of this graph are those nodes, where the last instruction is a leaf in
 * the flow graph.
 * 
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: BasicBlockGraph.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class BasicBlockGraph extends FactorisedFlowGraph {
  /**
   * Associates the nodes in the flow graph with the corresponding nodes in this
   * graph.
   *
   */
  protected Hashtable blockNodes = null;
  
  /**
   * Associates a node in this graph with a vector of corresponding nodes in the flow
   * graph.
   *
   */
  protected Hashtable flowNodess = null;
  
  /**
   * Finds the basic blocks starting at a node in a flow graph.
   *
   * @param flowGraph a <code>FactorisedFlowGraph</code> value
   * @param startNode a <code>Graph.Node</code> value
   * @param visitedNodes a <code>Graph.NodeDyer</code> value
   * @param newNode a <code>Graph.Node</code> value: An already created node in this
   * graph.
   */
  protected void findBasicBlocks(FactorisedFlowGraph flowGraph,
				 Graph.Node startNode,
				 Graph.NodeDyer visitedNodes,
				 Graph.Node newNode) {
    InstructionHandleVector instrs = new InstructionHandleVector();
    instrs.append((InstructionHandle)startNode.getLabel());
    visitedNodes.setColor(startNode,newNode);
    if (flowGraph.isRoot(startNode)) this.addRoot(newNode);
    if (flowGraph.isLeaf(startNode)) this.addLeaf(newNode);
			      
    blockNodes.put(startNode, newNode);
    Vector flowNodes = new Vector();
    flowNodes.addElement(startNode);
    
    Graph.Node node = startNode;
    Instruction instr = ((InstructionHandle)node.getLabel()).getInstruction();
    boolean endOfBasicBlock =
      node.getOutDegree()!=1
      || ((Graph.Node.Edge)node.getOutEdges().next()).getLabel()!=null;
    /*
      || (instr instanceof BranchInstruction)
      || (instr instanceof RET)
      || (instr instanceof ReturnInstruction)
      || (instr instanceof ExceptionHeaderInstruction);
    */
    while (!endOfBasicBlock) {
      Graph.Node succ=((Graph.Node.Edge)node.getOutEdges().next()).getEnd();
      endOfBasicBlock =
	(visitedNodes.getColor(succ)!=null) || succ.getInDegree()!=1;
      
      if (!endOfBasicBlock) {
	node=succ;
	visitedNodes.setColor(node,newNode);
	blockNodes.put(node, newNode);
	flowNodes.addElement(node);
	instrs.append((InstructionHandle)node.getLabel());
	if (flowGraph.isRoot(node)) this.addRoot(newNode);
	if (flowGraph.isLeaf(node)) this.addLeaf(newNode);

	endOfBasicBlock=node.getOutDegree()!=1
	  || ((Graph.Node.Edge)node.getOutEdges().next()).getLabel()!=null;
	/*
	  || (instr instanceof BranchInstruction)
	  || (instr instanceof RET)
	  || (instr instanceof ReturnInstruction)
	  || (instr instanceof ExceptionHeaderInstruction);
	*/
      }
    }
    newNode.setLabel(instrs);
    flowNodess.put(newNode, flowNodes);
    
    for (Iterator e=node.getOutEdges(); e.hasNext(); ) {
      Graph.Node.Edge out  = (Graph.Node.Edge)e.next();
      Graph.Node end       = out.getEnd();
      Graph.Node newEnd    = (Graph.Node)visitedNodes.getColor(end);
      if (newEnd==null) {
	newEnd = this.new Node();
	findBasicBlocks(flowGraph,end,visitedNodes,newEnd);
      }
      newNode.new Edge(newEnd,out.getLabel());
    }
  }
  
  /**
   * Creates a new <code>BasicBlockGraph</code> instance from a (factorised) flow
   * graph.
   *
   * @param flowGraph a <code>FactorisedFlowGraph</code> value
   */
  public BasicBlockGraph(FactorisedFlowGraph flowGraph) {
    this.setLabel(flowGraph.getLabel());
    this.blockNodes=new Hashtable();
    this.flowNodess=new Hashtable();
    Graph.NodeDyer nodeDyer = flowGraph.new NodeDyer();
    for (Iterator e=flowGraph.getRoots(); e.hasNext(); ) {
      Graph.Node root = (Graph.Node)e.next();
      if (nodeDyer.getColor(root)==null) 
	findBasicBlocks(flowGraph,root,nodeDyer, this.new Node());
    }
    for (Iterator e=flowGraph.getNodes(); e.hasNext(); ) {
      Graph.Node node = (Graph.Node)e.next();
      if (nodeDyer.getColor(node)==null) {
	//System.err.println("additional root:"+node);
	findBasicBlocks(flowGraph,node,nodeDyer, this.new Node());
      }
    }
    
  }

  /**
   * Returns the corresponding node in this graph for a node in the underlying flow
   * graph.
   *
   * @param flowNode a <code>Graph.Node</code> value
   * @return a <code>Graph.Node</code> value
   */
  public Graph.Node blockNode(Graph.Node flowNode) {
    return (Graph.Node)blockNodes.get(flowNode);
  }

  /**
   * Returns a vector of corresponding nodes in the flow graph for a node in this
   * graph.
   *
   * @param blockNode a <code>Graph.Node</code> value
   * @return a <code>Vector</code> value
   */
  public Vector flowNodes(Graph.Node blockNode) {
    return (Vector)flowNodess.get(blockNode);
  }
}

