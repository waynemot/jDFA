package de.rwth.dfa.jvm.samples; // Generated package name

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.domains.*;
import de.rwth.dfa.jvm.*;

/**
 * Implementation of a stack size abstraction. It can be used to determine the number
 * of elements on the operand stack for each instruction of a JVM method.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: SSAbstraction.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class SSAbstraction implements Abstraction {
  /**
   * Contains the instance of SSLattice used by this abstraction.
   *
   */
  protected CompleteLattice lattice;
  
  /**
   * The maximal number of elements of the operand stack inside the method of this
   * abstraction.
   *
   */
  protected int maxStack;
  
  /**
   * A constant pool generator for the constant pool of the methods class.
   *
   */
  protected ConstantPoolGen cpg; 

  /**
   * Creates a new <code>SSAbstraction</code> instance.
   *
   * @param maxStack an <code>int</code> value: The maximal number of elements on the
   * operand stack of the method to consider.
   * @param constantPoolGen a <code>ConstantPoolGen</code> value: The constant pool
   * generator for the method.
   */
  public SSAbstraction(int maxStack, ConstantPoolGen constantPoolGen) {
    super();
    this.maxStack=maxStack;
    this.cpg = constantPoolGen;
    this.lattice=new SSLattice(maxStack);
  }
  
  /**
   * Creates a new <code>SSAbstraction</code> instance.
   *
   * @param maxStack an <code>int</code> value: The maximal number of elements on the
   * operand stack of the method to consider.
   * @param constantPool a <code>ConstantPool</code> value: The constant pool for the
   * method.
   */
  public SSAbstraction(int maxStack, ConstantPool constantPool) {
    this(maxStack,new ConstantPoolGen(constantPool));
  }

  /**
   * Returns <code>DIRECTION_FORWARD</code>.
   *
   * @return an <code>int</code> value
   */
  public int getDirection() { return DIRECTION_FORWARD; }
  
  /**
   * Returns <code>QUANTIFIER_ALL</code>
   *
   * @return an <code>int</code> value
   */
  public int getQuantifier() { return QUANTIFIER_ALL; }

  /**
   * This class models the functions of this abstraction.
   *
   */
  protected class SSFunction implements Function {
    /**
     * The number of operand stack elements produced by this function.
     *
     */
    protected int produce;
    
    /**
     * The number of operand stack elements consumed by this function.
     *
     */
    protected int consume;

    /**
     * Creates a new <code>SSFunction</code> instance.
     *
     * @param ih an <code>InstructionHandle</code> value
     */
    public SSFunction(InstructionHandle ih) {
      if (ih instanceof ExceptionHeaderInstructionHandle) {
	this.consume=this.produce=-1;
      } else {
	this.consume=ih.getInstruction().consumeStack(cpg);
	this.produce=ih.getInstruction().produceStack(cpg);
      }
      //System.err.println(ih+" -> produce="+produce+", consume="+consume);
    }
    
    /**
     * Models the effect of the instruction of this function on the operand stack
     * size.
     *
     * @param o an <code>Object</code> value: Must be an element of SSLattice
     * @return an <code>Object</code> value: Must be an element of SSLattice
     * @exception FunctionException if an error occurs
     */
    public Object apply(Object o) throws FunctionException {
      if (consume==-1) // ExceptionHeaderInstruction
	return new Integer(1);
      if (lattice.equals(lattice.top(),o) || lattice.equals(lattice.bottom(),o))
	return o;
      if (!(o instanceof Integer))
	throw new FunctionException("invalid argument: not an Integer");
      int size=((Integer)o).intValue();
      //System.err.println(size+" -> "+(size-consume+produce));
      if (size<0 && size>maxStack)
	return lattice.bottom();
      else
	return new Integer(size-consume+produce);
    }
    
    /**
     * Returns this abstractions instance of SSLattice.
     *
     * @return a <code>Set</code> value
     */
    public Set getDomain() { return getLattice(); }
    
    /**
     * Returns this abstractions instance of SSLattice.
     *
     * @return a <code>Set</code> value
     */
    public Set getRange() { return getLattice(); }
  }
  
  /**
   * Returns this abstractions instance of SSLattice.
   *
   * @return a <code>Lattice</code> value
   */
  public Lattice getLattice() { return lattice; }

  /**
   * Determined the initial stack size at an instruction. Returns 0 for the method
   * entry, 1 for exception handler entries and the top element for all other
   * instructions.
   *
   * @param o an <code>InstructionHandle</code> value
   * @param isRoot a <code>boolean</code> value
   * @return an <code>Object</code> value
   */
  public Object getInitialValue(InstructionHandle o, boolean isRoot) {
    Object result;
    //System.err.print("getInitialValue("+o+","+isRoot+")=");
    if (isRoot)
      if (o.getPosition()==0)
	result=new Integer(0);  // Method entry
      else
	result=new Integer(1);  // Exception handler entry
    else
      result=lattice.top();
    //System.err.println(result);
    return result;
  }
  
  /**
   * Returns a new instance of <code>SSFunction</code>.
   *
   * @param ih an <code>InstructionHandle</code> value
   * @return a <code>Function</code> value
   */
  public Function getAbstract(InstructionHandle ih) {
    return new SSFunction(ih);
  }
  
  /**
   * Returns the value of the default implementation in {@link Abstraction.Default}.
   *
   * @param ihv an <code>InstructionHandleVector</code> value
   * @param isRoot a <code>boolean</code> value
   * @return an <code>Object</code> value
   */
  public Object getInitialValue(InstructionHandleVector ihv, boolean isRoot) {
    return Abstraction.Default.getInitialValue(this, ihv, isRoot);
  }
  
  /**
   * Returns the value of the default implementation in {@link Abstraction.Default}.
   *
   * @param ihv an <code>InstructionHandleVector</code> value
   * @return a <code>Function</code> value
   */
  public Function getAbstract(InstructionHandleVector ihv) {
    return Abstraction.Default.getAbstract(this, ihv);
  }

} // class SSAbstraction
