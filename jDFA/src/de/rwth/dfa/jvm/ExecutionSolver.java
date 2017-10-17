package de.rwth.dfa.jvm; // Generated package name

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;

//import de.fub.bytecode.classfile.*; 
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.utils.Stopwatch;

import de.rwth.domains.*;
import de.rwth.dfa.*;

/**
 * JVM abstraction solver based on direct execution. It only supports forward
 * analyses.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: ExecutionSolver.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class ExecutionSolver extends Solver {
  /**
   * Creates a new <code>ExecutionSolver</code> instance with stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public ExecutionSolver(Abstraction abstraction, String methodName,
			 InstructionList methodInstrs, CodeException[] methodExceptions,
			 Stopwatch stopwatch) {
    super(abstraction, methodName, methodInstrs, methodExceptions,stopwatch);
    if (abstraction.getDirection()!=Abstraction.DIRECTION_FORWARD)
      throw new IllegalArgumentException("Execution Solver cannot handle Abstraction.DIRECTION_BACKWARD");
   }

  /**
   * Creates a new <code>ExecutionSolver</code> instance with out stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public ExecutionSolver(Abstraction abstraction, String methodName,
			 InstructionList methodInstrs,
			 CodeException[] methodExceptions) {
    super(abstraction, methodName, methodInstrs, methodExceptions);
    if (abstraction.getDirection()!=Abstraction.DIRECTION_FORWARD)
      throw new IllegalArgumentException("Execution Solver cannot handle Abstraction.DIRECTION_BACKWARD");
  }


  protected boolean maybeTransfer(Object[] solution, Lattice l, boolean isAll, Object e, Integer targetIndex, Hashtable exFunctions) throws FunctionException {
    //System.err.print("maybe @"+targetIndex+" old="+solution[targetIndex]+", new="+e);
    int targetindex = targetIndex.intValue();
    if (exFunctions.containsKey(targetIndex)) {
      Function f = (Function)exFunctions.get(targetIndex);
      //System.err.println("transformed "+e+" to "+f.apply(e));
      e=f.apply(e);
    }
    Object newval = (isAll)?l.meet(e,solution[targetindex])
                            :l.join(e,solution[targetindex]);
    //System.err.print(", "+((isAll)?"meet=":"join=")+newval);
    if (l.equals(solution[targetindex],newval)) {
      //System.err.println("  --> false");
      return false;
    } else {
      solution[targetindex]=newval;
      //System.err.println("  --> true");
      return true;
    }
  }
  
  protected void compute(Object[] solution, boolean isForward, boolean isAll) {
    if (!isForward)
      throw new IllegalArgumentException("Execution Solver cannot handle Abstraction.DIRECTION_BACKWARD");

    Lattice l=abstraction.getLattice();
      
    Function[] functions = new Function[methodInstrs.getLength()];
    Instruction[] instructions = new Instruction[methodInstrs.getLength()];
    Hashtable indices = new Hashtable(methodInstrs.getLength());
    Hashtable exFunctions = new Hashtable(methodExceptions.length);
    int[] afterjsrInstr = new int[0];

    int i=0;
    Integer Int[] = new Integer[solution.length];
    //System.err.println("X Solver/"+methodName+": functions");
    
    for (Iterator<InstructionHandle> ne = methodInstrs.iterator(); ne.hasNext(); i++) {
      InstructionHandle ih = (InstructionHandle)ne.next();
      Int[i]=new Integer(i);
      boolean isRoot=(i==0);
      // System.err.println(ih);
      if (!isRoot) {
	int[] positions = methodInstrs.getInstructionPositions();
	for (int j=0; j<methodExceptions.length; j++) {
	  if (positions[i]==methodExceptions[j].getHandlerPC()) {
	    isRoot=true;
	    InstructionHandle exh = new ExceptionHeaderInstructionHandle("",ih);
	    exFunctions.put(Int[i], abstraction.getAbstract(exh));
	  }
	}
      }
      indices.put(ih, Int[i]);
      solution[i]     = abstraction.getInitialValue(ih,isRoot);
      functions[i]    = abstraction.getAbstract(ih);
      instructions[i] = ih.getInstruction();
      if (instructions[i] instanceof JsrInstruction) {
	int[] newafterjsrInstr = new int[afterjsrInstr.length+1];
	for (int j=0; j<afterjsrInstr.length; j++)
	  newafterjsrInstr[j]=afterjsrInstr[j];
	newafterjsrInstr[afterjsrInstr.length]=i+1;
	afterjsrInstr=newafterjsrInstr;
      }
    }
    
    Worklist worklist = new StackWorklist();
    for (i=solution.length-1; i>=0 ;i--)
      worklist.add(Int[i]);

    iterations=0;
    
    if (stopwatch!=null) stopwatch.split("init");

    // System.err.println("start solving =====================================================");
    while (!worklist.isEmpty()) {
      iterations++;
      //System.err.println(worklist);
      int current=((Integer)worklist.get()).intValue();
      //System.err.println("worklist get: "+current);

      while (current!=-1) try {
	//worklist.remove(Int[current]);
	Instruction instr=instructions[current];
	//System.err.print(current+": "+instr+" ");
	if (instr instanceof ReturnInstruction || instr instanceof ATHROW) {
	  //System.err.println("is case 0 ");
	  current=-1;
	  break;
	}
	Object newvalue=functions[current].apply(solution[current]);
	if (instr instanceof GotoInstruction || instr instanceof JsrInstruction) {
	  //System.err.print("is case 1 ");
	  i=((Integer)indices.get(((BranchInstruction)instr).getTarget())).intValue();
	  if (maybeTransfer(solution,l,isAll,newvalue,Int[i],exFunctions))
	    current=i;
	  else
	    current=-1;
	} else if (instr instanceof IfInstruction) {
	  //System.err.print("is case 2 ");
	  Integer t=(Integer)indices.get(((BranchInstruction)instr).getTarget());
	  if (maybeTransfer(solution,l,isAll,newvalue,t,exFunctions))
	    worklist.add(t);
	  if (maybeTransfer(solution,l,isAll,newvalue,Int[current+1],exFunctions))
	    current++;
	  else
	    current=-1;
	} else if (instr instanceof Select) {
	  //System.err.print("is case 3 ");
	  InstructionHandle[] targets = ((Select)instr).getTargets();
	  for (int j=0; j<targets.length; j++) {
	    Integer t=(Integer)indices.get(targets[j]);
	    if (maybeTransfer(solution,l,isAll,newvalue,t,exFunctions))
	      worklist.add(t);
	  }
	  i=((Integer)indices.get(((BranchInstruction)instr).getTarget())).intValue();
	  if (maybeTransfer(solution,l,isAll,newvalue,Int[i],exFunctions))
	    current=i;
	  else
	    current=-1;
	} else if (instr instanceof RET) {
	  current=-1;
	  //System.err.print("is case 4 ");
	  if (afterjsrInstr.length!=0) {
	    for (int j=0; j<afterjsrInstr.length-1; j++) {
	      if (maybeTransfer(solution,l,isAll,newvalue,Int[afterjsrInstr[j]],exFunctions))
		worklist.add(Int[afterjsrInstr[j]]);
	    }
	    i=afterjsrInstr[afterjsrInstr.length-1];
	    if (maybeTransfer(solution,l,isAll,newvalue,Int[i],exFunctions))
	      current=i;
	  }
	} else {
	  //System.err.print("is case 5 ");
	  if (current!=solution.length-1 &&  // dead code at end possible!!
	      maybeTransfer(solution,l,isAll,newvalue,Int[current+1],exFunctions))
	    current++;
	  else
	    current=-1;
	}
	//System.err.println("-> "+current);
      } catch (FunctionException ex) {
	throw new IllegalArgumentException(ex.toString());
      }
    }
    // System.err.println("end solving ==========================================================\n");
  }
}

