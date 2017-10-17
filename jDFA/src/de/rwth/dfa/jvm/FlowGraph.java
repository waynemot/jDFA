package de.rwth.dfa.jvm; // Generated package name

import java.util.Enumeration;
import java.util.Iterator;

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.graph.Graph;

/**
 * A class for the flow graph of JVM methods. For each instruction in the method's
 * bytecode, there is a node in the graph. The instruction handle for the instruction
 * is the label of the node. For each possible control flow between two instructions,
 * there is an edge from the first to the second instruction.
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
 * All instructions which may throw an exception are connected to all nodes with
 * {@link ExceptionHeaderInstructionHandle} as label, if this node belongs to an
 * active handler for the instruction. Hence, the control flow caused by exceptions
 * is represented by this kind of graph.
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
 * @version $Id: FlowGraph.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class FlowGraph extends FactorisedFlowGraph {
  /**
   * Creates a new <code>FlowGraph</code> instance.
   *
   * @param name a <code>String</code> value
   * @param instrs an <code>InstructionList</code> value
   * @param exceptions a <code>CodeException[]</code> value
   */
  public FlowGraph(String name, InstructionList instrs, CodeException[] exceptions) {
    super();
    this.setLabel(name);

    Graph.Node[] nodes = makeFactorisedFlowGraph(instrs,exceptions);
    
    int[] positions = instrs.getInstructionPositions();
    
    for (int j=0; j<exceptions.length; j++) {
      int k;
      Graph.Node exnode = null;
      for (k=0; k<positions.length; k++) {
	if (positions[k]==exceptions[j].getHandlerPC()) break;
      }
      if (k<positions.length) exnode=nodes[k];
      else throw new IllegalArgumentException("no node for exception handler with PC "+
					      exceptions[j].getHandlerPC());
      k=0;
      
      Graph.Node.Edge[] inEdges = exnode.getInArray();

      for (int i=0; i<inEdges.length; i++ ) {
	if ("ret".equals(inEdges[i].getLabel())) {
	  Graph.Node exHeader = this.new Node(new ExceptionHeaderInstructionHandle("Ex"+j+" header", (InstructionHandle)nodes[k].getLabel()));
	  exHeader.new Edge(exnode,"ex"+j);
	  inEdges[i].setEnd(exHeader);
	}
      }
	Iterator<InstructionHandle> it = instrs.iterator();
	while(it.hasNext()) {
		Instruction instr = it.next().getInstruction();
	//}
    //for (Enumeration e = instrs.elements(); e.hasMoreElements(); ) {
	//Instruction instr = ((InstructionHandle)e.nextElement()).getInstruction();
	  if ((instr instanceof ExceptionThrower) &&
	    positions[k]>=exceptions[j].getStartPC() &&
	    positions[k]<exceptions[j].getEndPC()) {
	  Graph.Node exHeader = this.new Node(new ExceptionHeaderInstructionHandle("Ex"+j+" header", (InstructionHandle)nodes[k].getLabel()));
	  exHeader.new Edge(exnode,"ex"+j);
	  nodes[k].new Edge(exHeader,"ex prepare"+j);
	}
	k++;
      }
    }
  }

  /**
   * Creates a new <code>FlowGraph</code> instance.
   *
   * @param method a <code>Method</code> value
   */
  public FlowGraph(Method method) {
    this(method.getName()+method.getSignature(),
	 new InstructionList(method.getCode().getCode()),
	 method.getCode().getExceptionTable());
  }
}
