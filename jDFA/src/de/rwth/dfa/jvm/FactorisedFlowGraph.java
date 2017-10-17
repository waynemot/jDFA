package de.rwth.dfa.jvm; // Generated package name

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Hashtable;

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.graph.RootedGraph;

/**
 * A class for the factorised flow graph of JVM methods. For each instruction in the
 * method's bytecode, there is a node in the graph. The instruction handle for the
 * instruction is the label of the node. For each possible control flow between two
 * instructions, there is an edge from the first to the second instruction.
 * 
 * <p>
 *
 * In addition, there are nodes not representing real instruction: For each exception
 * handler, there is a node with an {@link ExceptionHeaderInstructionHandle} as
 * label. An edge goes from this node to the start node for the actual exception
 * handler code.
 *
 * <p>
 *
 * However, there are no edges to nodes with {@link ExceptionHeaderInstructionHandle}
 * as label. Hence, the control flow caused by exceptions in not represented by this
 * kind of graph. Thats why its a factorised flow graph.
 *
 * <p>
 *
 * The roots of this graph are the first instruction and the nodes with {@link
 * ExceptionHeaderInstructionHandle} as label.
 *
 * <p>
 *
 * The leafs of this graph are all exit points of the method.
 * 
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FactorisedFlowGraph.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FactorisedFlowGraph extends RootedGraph {
  /**
   * Default constructor used by the {@link FlowGraph} sub class. Simply creates an
   * empty graph.
   *
   */
  public FactorisedFlowGraph() {
    super();
  }

  
  /**
   * This method does the actual creation of nodes and edges. It is called from the
   * constructors, except from {@link #FactorisedFlowGraph()}.
   *
   * @param instrs an <code>InstructionList</code> value
   * @param exceptions a <code>CodeException[]</code> value
   * @return a <code>RootedGraph.Node[]</code> value
   */
  protected RootedGraph.Node[] makeFactorisedFlowGraph(InstructionList instrs,
						       CodeException[] exceptions) {
    RootedGraph.Node[] nodes = new RootedGraph.Node[instrs.size()];
    int[] positions = instrs.getInstructionPositions();
    Hashtable indices   = new Hashtable(positions.length);
    
    LinkedList rets = new LinkedList();
    LinkedList jsrfs = new LinkedList();

    int j=0;
    
    for(Iterator<InstructionHandle> it = instrs.iterator();it.hasNext();) {
    //for (Enumeration e = instrs.elements(); e.hasMoreElements(); ) {
      InstructionHandle instrh = (InstructionHandle)it.next();
      indices.put(instrh, new Integer(j));
      nodes[j] = this.new Node(instrh);
      j++;
    }
    j=0;
    boolean lastWasJsr=false;
    
    for (Iterator<InstructionHandle>it = instrs.iterator(); it.hasNext(); ) {
      if (lastWasJsr) jsrfs.add(nodes[j]);
      lastWasJsr=false;
      Instruction instr = ((InstructionHandle)it.next()).getInstruction();
      if (instr instanceof ReturnInstruction) leafs.add(nodes[j]);
      boolean seq = j<nodes.length-1 && !(instr instanceof ReturnInstruction);
      String seqlabel = null;
      
      if (instr instanceof BranchInstruction) {
	String brlabel  = null;
	if (instr instanceof GotoInstruction) {
	  seq=false;
	  brlabel="goto";
	} else if (instr instanceof IfInstruction) {
	  seqlabel="ff";
	  brlabel="tt";
	} else {
	  seq=false;
	  if (instr instanceof JSR_W || instr instanceof JSR) {
	    brlabel="jsr";
	    lastWasJsr=true;
	  } else {
	    if (instr instanceof Select) {
	      brlabel="default";
	      InstructionHandle[] targets = ((Select)instr).getTargets();
	      for (int l=0; l<targets.length; l++) {
		int k =
		  ((Integer)indices.get(targets[l])).intValue();
		nodes[j].new Edge(nodes[k],"br"+l);
	      }
	    } else {
	      throw new IllegalArgumentException("Invalid instruction:"+instr);
	    }
	  }
	}
	InstructionHandle target = ((BranchInstruction)instr).getTarget();
	int k = ((Integer)indices.get(target)).intValue();
	nodes[j].new Edge(nodes[k],brlabel);
      } else if (instr instanceof RET) {
	seq=false;
	rets.add(nodes[j]);
      } else if (instr instanceof ATHROW) {
	seq=false;
      }

      if (instr instanceof ATHROW || instr instanceof ReturnInstruction)
	addLeaf(nodes[j]);
      
      if (seq) nodes[j].new Edge(nodes[j+1],seqlabel);
      j++;
    } 

    if (lastWasJsr) throw new IllegalArgumentException("Trailing jsr");
      
    for (Iterator ir = rets.iterator(); ir.hasNext(); ) {
      RootedGraph.Node retnode = (RootedGraph.Node)ir.next();
      for (Iterator ij = jsrfs.iterator(); ij.hasNext(); ) {
	RootedGraph.Node jsrfnode = (RootedGraph.Node)ij.next();
	retnode.new Edge(jsrfnode,"ret");
      }
    }

    addRoot(nodes[0]);

    for (j=0; j<exceptions.length; j++) {
      int k;
      for (k=0; k<positions.length; k++) {
	if (positions[k]==exceptions[j].getHandlerPC()) break;
      }
      if (k<positions.length) {
	addRoot(nodes[k]);
      } else
	throw new IllegalArgumentException("no node for exception handler with PC "+
					   exceptions[j].getHandlerPC());
    }

    /* Erkennung von uneigentlichen Wurzeln und Bl"attern
    for (j=0; j<nodes.length; j++) {
      if (!nodes[j].getInEdges().hasNext() && !isRoot(nodes[j])) {
	addRoot(nodes[j]);
      }
      if (!nodes[j].getOutEdges().hasNext() && !isLeaf(nodes[j])) {
	addLeaf(nodes[j]);
      }
    }
    */
    
    return nodes;
  }
  
  /**
   * Creates a new <code>FactorisedFlowGraph</code> instance.
   *
   * @param name a <code>String</code> value
   * @param instrs an <code>InstructionList</code> value
   * @param exceptions a <code>CodeException[]</code> value
   */
  public FactorisedFlowGraph(String name, InstructionList instrs,
			     CodeException[] exceptions) {
    this();
    this.setLabel(name);
    makeFactorisedFlowGraph(instrs,exceptions);
  }    
  
  /**
   * Creates a new <code>FactorisedFlowGraph</code> instance.
   *
   * @param method a <code>Method</code> value
   */
  public FactorisedFlowGraph(Method method) {
    this(method.getName()+method.getSignature(),
	 new InstructionList(method.getCode().getCode()),
	 method.getCode().getExceptionTable());
  }
}
