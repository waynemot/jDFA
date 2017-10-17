package de.rwth.dfa.jvm.samples; // Generated package name

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.EmptyStackException;
import java.util.Stack;

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import de.rwth.utils.ArrayTools;

import de.rwth.domains.*;
import de.rwth.domains.templates.*;

import de.rwth.dfa.jvm.*;

/**
 * Implementation of a constant propagation abstraction. It can be used to determine
 * constant values in both stack an local variables. The domain used is {@link
 * CFPLattice}.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CPAbstraction.java,v 1.4 2002/09/17 06:53:53 mohnen Exp $
 */
public class CPAbstraction extends AbstractSSDependingAbstraction
                           implements Abstraction {
  /**
   * The domain used in this abstraction.
   *
   */
  protected CFPLattice l = null;
  
  /**
   * The maximal number of local variable slots in the method for this abstraction.
   *
   */
  protected int maxLocals;
  
  /**
   * The maximal stack size in the method for this abstraction.
   *
   */
  protected int maxStack;
  
  /**
   * Creates a new <code>CFPAbstraction</code> instance.
   *
   * @param maxStack an <code>int</code> value
   * @param maxLocals an <code>int</code> value
   * @param constantPool a <code>ConstantPool</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   */
  public CPAbstraction(int maxStack, int maxLocals,
		       ConstantPoolGen constantPoolGen,
		       InstructionList methodInstrs,
		       CodeException[] methodExceptions) {
    super(maxStack, constantPoolGen, methodInstrs, methodExceptions);
    this.maxLocals=maxLocals;
    this.maxStack =maxStack;
    this.l = new CFPLattice(this.maxLocals, this.maxStack);
  }
  
  /**
   * Creates a new <code>CFPAbstraction</code> instance.
   *
   * @param maxStack an <code>int</code> value
   * @param maxLocals an <code>int</code> value
   * @param constantPool a <code>ConstantPool</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   */
  public CPAbstraction(int maxStack, int maxLocals,
		       ConstantPool constantPool,
		       InstructionList methodInstrs,
		       CodeException[] methodExceptions) {
    this(maxStack, maxLocals, new ConstantPoolGen(constantPool),
	 methodInstrs, methodExceptions);
  }
  
  /**
   * Returns the {@link CFPLattice} for this abstraction.
   *
   * @return a <code>Lattice</code> value
   */
  public Lattice getLattice() { return l; }
  
  /**
   * Returns the initial value for an instruction.
   * <ol>
   * <li>The component for the local variable slots consists of the entries
   * <dl>
   * <dt>{@link CFPComponentLattice#NONCONSTANTCOMPONENT}
   * <dd>if the instruction is the entry point of the method or the entry point of an
   * exception handler.
   * <dt>{@link CFPComponentLattice#UNKNOWNCOMPONENT}
   * <dd>for all other instructions.
   * </dl>
   * </li>
   * <li>The stack component has the appropriate size.</li>
   * <li>Each entry of the stack component is 
   * <dl>
   * <dt>{@link CFPComponentLattice#NONCONSTANTCOMPONENT}
   * <dd>if the instruction is the entry point of the method or the entry point of an
   * exception handler.
   * <dt>{@link CFPComponentLattice#UNKNOWNCOMPONENT}
   * <dd>for all other instructions.
   * </dl>
   * </li>
   * </ol>
   *
   * @param ih an <code>InstructionHandle</code> value
   * @param isRoot a <code>boolean</code> value
   * @return an <code>Object</code> value
   */
  public Object getInitialValue(InstructionHandle ih, boolean isRoot) {
    Object result;
    Object[] locals = ArrayTools.fill(new Object[maxLocals],
				      (isRoot)
				      ?CFPComponentLattice.NONCONSTANTCOMPONENT
				      :CFPComponentLattice.UNKNOWNCOMPONENT);
    Object stack;
    Integer size;
    
    if (ih instanceof ExceptionHeaderInstructionHandle) {
      size=new Integer(1);
    } else {
      size=getStacksize(ih);
    }
    
    if (size==null) {
      stack=CFPStackLattice.UNKNOWNSTACK;
    } else {
      Stack sstack=new Stack();
      for (int i=0; i<size.intValue(); i++)
	sstack.push((isRoot)?CFPComponentLattice.NONCONSTANTCOMPONENT
		    :CFPComponentLattice.UNKNOWNCOMPONENT);
      stack=sstack;
    }
    
    result=l.makeElement(locals,stack);
    return result;
  }

  /**
   * Returns <code>DIRECTION_FORWARD</code>.
   *
   * @return an <code>int</code> value
   */
  public int getDirection() { return DIRECTION_FORWARD; }

  /**
   * Returns <code>QUANTIFIER_ALL</code>.
   *
   * @return an <code>int</code> value
   */
  public int getQuantifier() { return QUANTIFIER_ALL; }

  /**
   * The abstract base class of all function of this abstraction.
   *
   */
  public abstract class CPFunction implements Function {
    /**
     * The number of elements consumed from the operand stack by this function.
     *
     */
    protected int consume;
    
    /**
     * The number of elements produced by this function on the operand stack.
     *
     */
    protected int produce;
    
    /**
     * Creates a new <code>CPFunction</code> instance.
     *
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public CPFunction(int consume, int produce) {
      super();
      this.consume=consume;
      this.produce=produce;
    }
    
    /**
     * Returns the {@link CFPLattice} for this abstraction.
     *
     * @return a <code>Set</code> value
     */
    public Set getRange() { return getLattice(); }

    /**
     * Returns the {@link CFPLattice} for this abstraction.
     *
     * @return a <code>Set</code> value
     */
    public Set getDomain() { return getRange(); }

    /**
     * Applies this function to an element of the {@link CFPLattice} for this
     * abstraction and returns the result. It extracts the components and calls
     * {@link #apply(Object[],Stack)}. If the stack component is not a stack, but top
     * or bottom of {@link CFPStackLattice}, the second argument in
     * <code>null</code>. 
     *
     * @param x an <code>Object</code> value
     * @return an <code>Object</code> value
     * @exception FunctionException if an error occurs
     */
    public Object apply(Object x) throws FunctionException {
      Object result = null;
      Object[] locals=l.getLocals(x);
      Object   stack =l.getStack(x);
      if (stack instanceof Stack)
	apply(locals,(Stack)stack);
      else
	apply(locals,null);
      result=l.makeElement(locals,stack);
      //System.err.println("/* apply "+this+"("+x+")="+result+" */");
      return result;
    }

    /**
     * Supposed to perform the actual operation. The modification is done on the
     * arguments, therfore there is no return value.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    abstract protected void apply(Object[] locals, Stack stack) throws FunctionException;
  }

  /**
   * A generic function. It leaves the locals component untouched and simply modifies
   * the stack. New entries on the stack are {@link
   * CFPComponentLattice#UNKNOWNCOMPONENT}.
   *
   */
  protected class GenericFunction extends CPFunction {
    /**
     * Creates a new <code>GenericFunction</code> instance.
     *
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public GenericFunction(int consume, int produce) {
      super(consume,produce);
    }

    /**
     * Performs a generic operation on the stack only.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	try {
	  for (int i=consume; i>0; i--) stack.pop();
	  for (int i=produce; i>0; i--)
	    stack.push(CFPComponentLattice.UNKNOWNCOMPONENT);
	} catch (EmptyStackException ex) { }
      }
    }
    public String toString() { return "<consume "+consume+" produce "+produce+">"; }
  }
  
  /**
   * A function for pushing a constant onto the stack.
   *
   */
  protected class PushConstantFunction extends CPFunction {
    /**
     * The constant to push.
     *
     */
    protected Object value=null;

    /**
     * Creates a new <code>PushConstantFunction</code> instance.
     *
     * @param value an <code>Object</code> value
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public PushConstantFunction(Object value, int consume, int produce) {
      super(consume,produce);
      this.value=value;
    }

    /**
     * Pushes the constant after pushing as many {@link
     * CFPComponentLattice#UNKNOWNCOMPONENT} as necessary to fulfill correct stack
     * behavior.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	for (int i=produce; i>1; i--)
	  stack.push(CFPComponentLattice.UNKNOWNCOMPONENT);
	stack.push(value);
      }
    }

    public String toString() { return "<const "+value+">"; }
  }

  /**
   * Pushes the content of a local variable slot onto the stack.
   *
   */
  protected class PushLocalFunction extends CPFunction {
    /**
     * The index of the local variable to push.
     *
     */
    protected int index;

    /**
     * Creates a new <code>PushLocalFunction</code> instance.
     *
     * @param index an <code>int</code> value
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public PushLocalFunction(int index, int consume, int produce) {
      super(consume,produce);
      this.index=index;
    }

    /**
     * Pushes the content of the local variable slot of this function after pushing
     * as many {@link CFPComponentLattice#UNKNOWNCOMPONENT} as necessary to fulfill
     * correct stack behavior.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	for (int i=produce; i>1; i--)
	  stack.push(CFPComponentLattice.UNKNOWNCOMPONENT);
	stack.push(locals[index]);
      }
    }

    public String toString() { return "<load #"+index+">"; }
  }
  
  /**
   * Removes entries from the stack.
   *
   */
  protected class PopFunction extends CPFunction {
    /**
     * Creates a new <code>PopFunction</code> instance.
     *
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public PopFunction(int consume, int produce) {
      super(consume,produce);
    }

    /**
     * Pops as many entries from the stack as necessary to fulfill correct stack
     * behavior.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	if (stack.size()<consume)
	  throw new FunctionException("stack too small for pop");
	for (int i=consume;i>0;i--) stack.pop();
      }
    }

    public String toString() { return "<pop "+consume+">"; }
  }

  /**
   * Swaps the top element and the element below that on the stack.
   *
   */
  protected class SwapFunction extends CPFunction {
    /**
     * Creates a new <code>SwapFunction</code> instance.
     *
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public SwapFunction(int consume, int produce) {
      super(consume,produce);
    }

    /**
     * Swaps the top element and the element below that on the stack.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	if (stack.size()<2) throw new FunctionException("stack too small for swap");
	Object value1 = stack.pop();
	Object value2 = stack.pop();
	stack.push(value2);
	stack.push(value1);
      }
    }

    public String toString() { return "<swap>"; }
  }
  
  /**
   * Duplicates n elements on the stack under x other elements.
   *
   */
  protected class DupFunction extends CPFunction {
    /**
     * The number of elements on top to duplicate.
     *
     */
    protected int n;

    /**
     * The number of elements under the n original elements.
     *
     */
    protected int x;

    /**
     * Creates a new <code>DupFunction</code> instance.
     *
     * @param x an <code>int</code> value
     * @param n an <code>int</code> value
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public DupFunction(int x, int n, int consume, int produce) {
      super(consume,produce);
      this.x=x;
      this.n=n;
    }

    /**
     * Removes the n top elements from the stack, then removes x elements from the
     * remaining stack, pushes the n elements, pushed the x elements, and pushes the
     * n elements again.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	if (stack.size()<x+n)
	  throw new FunctionException("stack with "+stack.size()+
				      " too small for dup with x="+x+" and n="+n);
	Object[] nValues = new Object[n];
	for (int i=0; i<n; i++) nValues[i] = stack.pop();
	Object[] xValues = new Object[x];
	for (int i=0; i<x; i++) xValues[i] = stack.pop();
	
	for (int i=n-1; i>=0; i--) stack.push(nValues[i]);
	for (int i=x-1; i>=0; i--) stack.push(xValues[i]);
	for (int i=n-1; i>=0; i--) stack.push(nValues[i]);
      }
    }

    public String toString() { return "<dup"+((n>1)?""+n:"")+((x>0)?"_X"+x:"")+">"; }
  }

  /**
   * Stores the top element in a local variable slot.
   *
   */
  protected class StoreLocalFunction extends CPFunction {
    /**
     * The local variable slot to store.
     *
     */
    protected int index;

    /**
     * Creates a new <code>StoreLocalFunction</code> instance.
     *
     * @param index an <code>int</code> value
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public StoreLocalFunction(int index, int consume, int produce) {
      super(consume,produce);
      this.index=index;
    }

    /**
     * Pops the top element from the stack and stores it in the local variable slot
     * of this function. In addition, it Pops as many entries from the stack as
     * necessary to fulfill correct stack behavior.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	if (stack.size()<consume) throw new FunctionException("stack empty to small");
	locals[index]=stack.pop();
	for (int i=consume; i>1; i--) stack.pop();
      }
    }

    public String toString() { return "<store "+consume+" #"+index+">"; }
  }

  /**
   * Increments the content of a local variable slot.
   *
   */
  protected class IncLocalFunction extends CPFunction {
    /**
     * The local variable slot to increment.
     *
     */
    protected int index;

    /**
     * The increment to use.
     *
     */
    protected int increment;

    /**
     * Creates a new <code>IncLocalFunction</code> instance.
     *
     * @param index an <code>int</code> value
     * @param increment an <code>int</code> value
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public IncLocalFunction(int index, int increment, int consume, int produce) {
      super(consume,produce);
      this.index=index;
      this.increment=increment;
    }

    /**
     * Increments the local variable slot of this function by this functions
     * increment.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      Object o = locals[index];
      if (o!=null && (o instanceof Integer)) {
	locals[index]=new Integer(((Integer)o).intValue()+1);
      }
    }

    public String toString() { return "<inc #"+index+" by "+increment+">"; }
  }
  
  /**
   * Pushes a {@link CFPComponentLattice#NONNULLCOMPONENT} onto the stack.
   *
   */
  protected class AllocationFunction extends CPFunction {
    /**
     * Creates a new <code>AllocationFunction</code> instance.
     *
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public AllocationFunction(int consume, int produce) {
      super(consume,produce);
    }

    /**
     * Removes as many elements from the stack as necessary, pushes as many {@link
     * CFPComponentLattice#UNKNOWNCOMPONENT} as necessary, and pushed a {@link
     * CFPComponentLattice#NONNULLCOMPONENT}.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	if (stack.size()<consume)
	  throw new FunctionException("stack too small for new");
	for (int i=0; i<consume; i++) stack.pop();
	for (int i=0; i<produce-1; i++)
	  stack.push(CFPComponentLattice.UNKNOWNCOMPONENT);
	stack.push(CFPComponentLattice.NONNULLCOMPONENT);
      }
    }

    public String toString() { return "<new>"; }
  }
  
  /**
   * Creates a new stack with only a {@link CFPComponentLattice#NONNULLCOMPONENT}.
   *
   */
  protected class ExceptionHeaderFunction extends CPFunction {
    /**
     * Creates a new <code>ExceptionHeaderFunction</code> instance.
     *
     */
    public ExceptionHeaderFunction() {
      super(0,0);
    }

    /**
     * Creates a new stack with only a {@link CFPComponentLattice#NONNULLCOMPONENT}.
     *
     * @param x an <code>Object</code> value
     * @return an <code>Object</code> value
     * @exception FunctionException if an error occurs
     */
    public Object apply(Object x) throws FunctionException {
      Stack stack=new Stack();
      stack.push(CFPComponentLattice.NONNULLCOMPONENT);
      return l.makeElement(l.getLocals(x), stack);
    }

    /**
     * Always throws an IllegalArgumentException.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      throw new IllegalArgumentException("should never be called!");
    }

    public String toString() { return "<ex header>"; }
  }
  
  /**
   * Creates an instance of CPFunction for an instruction.
   *
   * @param ih an <code>InstructionHandle</code> value
   * @return a <code>Function</code> value
   */
  public Function getAbstract(InstructionHandle ih) {
    Function f = null;
    if (ih instanceof ExceptionHeaderInstructionHandle) {
      f =  new ExceptionHeaderFunction();
    } else {
      Instruction i = ih.getInstruction();
      int consume = i.consumeStack(getConstantPoolGen());
      int produce = i.produceStack(getConstantPoolGen());
      if (i instanceof ConstantPushInstruction) {
        f = new PushConstantFunction(((ConstantPushInstruction)i).getValue(),
      			       consume, produce);
      } else if (i instanceof ACONST_NULL) {
        f = new PushConstantFunction(null, consume, produce);
      } else if (i instanceof POP) {
        f = new PopFunction(consume, produce);
      } else if (i instanceof POP2) {
        f = new PopFunction(consume, produce);
      } else if (i instanceof SWAP) {
        f = new SwapFunction(consume, produce);
      } else if (i instanceof DUP) {
        f = new DupFunction(0,1,consume,produce);
      } else if (i instanceof DUP_X1) {
        f = new DupFunction(1,1,consume,produce);
      } else if (i instanceof DUP_X2) {
        f = new DupFunction(2,1,consume,produce);
      } else if (i instanceof DUP2) {
        f = new DupFunction(0,2,consume,produce);
      } else if (i instanceof DUP2_X1) {
        f = new DupFunction(1,2,consume,produce);
      } else if (i instanceof DUP2_X2) {
        f = new DupFunction(2,2,consume,produce);
      } else if (i instanceof IINC) {
        f = new IncLocalFunction(((IINC)i).getIndex(),((IINC)i).getIncrement(),
      			   consume, produce);
      } else if (i instanceof LocalVariableInstruction) {
        if (consume>produce)
          f =  new StoreLocalFunction(((LocalVariableInstruction)i).getIndex(),
      				consume, produce);
        else
          f = new PushLocalFunction(((LocalVariableInstruction)i).getIndex(),
      			      consume, produce);
      } else if ((i instanceof LDC2_W) || (i instanceof LDC) || (i instanceof LDC_W)) {
        CPInstruction cpi = (CPInstruction)i;
        Constant constant = getConstantPoolGen().getConstant(cpi.getIndex());
        if (constant instanceof ConstantInteger) {
          f = new PushConstantFunction(new Integer(((ConstantInteger)constant).getBytes()),
      				 consume, produce);
        } else if (constant instanceof ConstantLong) {
          f = new PushConstantFunction(new Long(((ConstantLong)constant).getBytes()),
      			       consume, produce);
        } else if (constant instanceof ConstantFloat) {
          f = new PushConstantFunction(new Float(((ConstantFloat)constant).getBytes()),
      				 consume, produce);
        } else if (constant instanceof ConstantDouble) {
        f = new PushConstantFunction(new Double(((ConstantDouble)constant).getBytes()),
      			       consume, produce);
        } else {
          f = new GenericFunction(consume, produce);
        }
      } else if (i instanceof AllocationInstruction) {
        f = new AllocationFunction(consume, produce);
      } else if (i instanceof CHECKCAST) {
        f = new IdentityFunction(l);
      } else if (i instanceof UnconditionalBranch) {
        f = new IdentityFunction(l);
      } else {
        f = new GenericFunction(consume, produce);
      }
    }
    return f; 
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
}



