package de.rwth.dfa.jvm.samples; // Generated package name

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.domains.*;
import de.rwth.dfa.jvm.*;

/**
 * An abstract helper class for implementing abstractions where the initial values at
 * instructions depend on the size of the operand size at this instruction.
 * 
 * <p>
 *
 * When creating an instance of a sub class, the {@link SSAbstraction stack size
 * analysis} is performed.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: AbstractSSDependingAbstraction.java,v 1.4 2002/09/17 06:53:53 mohnen Exp $
 */
public abstract class AbstractSSDependingAbstraction implements Abstraction {
  /**
   * The constant pool generator used for this abstraction.
   *
   */
  protected ConstantPoolGen cpg = null;
  
  /**
   * The association between instruction handles and stack sizes.
   *
   */
  protected Hashtable stacksizes = null;
  
  /**
   * Creates a new <code>AbstractSSDependingAbstraction</code> instance.
   *
   * @param maxStack an <code>int</code> value
   * @param constantPoolGen a <code>ConstantPoolGen</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   */
  public AbstractSSDependingAbstraction(int maxStack,
					ConstantPoolGen constantPoolGen,
					InstructionList methodInstrs,
					CodeException[] methodExceptions) {
    this.cpg = constantPoolGen;
    this.stacksizes = new Hashtable(methodInstrs.getLength());
    try {
      Object[] solution;
      SSAbstraction ssabstraction = new SSAbstraction(maxStack, constantPoolGen);
      Solver solver = new ExecutionSolver(ssabstraction,
					  "", methodInstrs, methodExceptions, null);
      solution = solver.getSolution();
      int i=0;
      for (Iterator<InstructionHandle> ne = methodInstrs.iterator(); ne.hasNext(); i++) {
	InstructionHandle ih = (InstructionHandle)ne.next();
	if (!(solution[i] instanceof Integer)) {
	  SSLattice sslattice = (SSLattice)ssabstraction.getLattice();
	  if (sslattice.bottom().equals(solution[i]))
	    throw new IllegalArgumentException("invalid stack size in method: "+
					       solution[i]+" @ "+ih);
	  else
	    solution[i]=new Integer(-1);
	}
	this.stacksizes.put(ih, solution[i]);
      }
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.toString());
    }
  }
  
  /**
   * Creates a new <code>AbstractSSDependingAbstraction</code> instance.
   *
   * @param maxStack an <code>int</code> value
   * @param constantPool a <code>ConstantPool</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   */
  public AbstractSSDependingAbstraction(int maxStack,
					ConstantPool constantPool,
					InstructionList methodInstrs,
					CodeException[] methodExceptions) {
    this(maxStack,new ConstantPoolGen(constantPool),methodInstrs, methodExceptions);
  }

  /**
   * Returns the constant pool generator used for this abstraction.
   *
   * @return a <code>ConstantPoolGen</code> value
   */
  public ConstantPoolGen getConstantPoolGen() { return cpg; }
  
  /**
   * Returns the stack size for an instruction in a method. For instructions with
   * undetermined stack size (dead code), the size is -1.
   *
   * @param ih an <code>InstructionHandle</code> value
   * @return an <code>Integer</code> value
   */
  public Integer getStacksize(InstructionHandle ih) {
    return (Integer)stacksizes.get(ih);
  }
}
