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
 * Implementation of a constant folding propagation abstraction. It can be used to
 * determine constant values in both stack an local variables. The domain used is
 * {@link CFPLattice}.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CFPAbstraction.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class CFPAbstraction extends CPAbstraction
                            implements Abstraction {
  /**
   * Creates a new <code>CFPAbstraction</code> instance.
   *
   * @param code a <code>Code</code> value
   * @param constantPoolGen a <code>ConstantPoolGen</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   */
  public CFPAbstraction(int maxStack, int maxLocals, ConstantPoolGen constantPoolGen,
			InstructionList methodInstrs, CodeException[] methodExceptions) {
    super(maxStack, maxLocals, constantPoolGen, methodInstrs, methodExceptions);
    this.l = new CFPLattice(this.maxLocals, this.maxStack);
  }

  /**
   * Creates a new <code>CFPAbstraction</code> instance.
   *
   * @param code a <code>Code</code> value
   * @param constantPool a <code>ConstantPool</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   */
  public CFPAbstraction(int maxStack, int maxLocals, ConstantPool constantPool,
			InstructionList methodInstrs, CodeException[] methodExceptions) {
    this(maxStack, maxLocals, new ConstantPoolGen(constantPool),
	 methodInstrs, methodExceptions);
  }

  
  /**
   * An abstract class for arithmetic manipulation of the stack.
   *
   */
  abstract protected class ArithmeticFunction extends CPFunction {
    /**
     * Number of operands of this function.
     *
     */
    protected int n;

    /**
     * Creates a new <code>ArithmeticFunction</code> instance.
     *
     * @param n an <code>int</code> value
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public ArithmeticFunction(int n, int consume, int produce) {
      super(consume,produce);
      this.n=n;
    }

    /**
     * Pops the operands from the stack, calls {@link #evaluate(Number[])} if the
     * operands are all numbers, and pushes the result. Otherwise, it pushes {@link
     * CFPComponentLattice#NONCONSTANTCOMPONENT} or {@link
     * CFPComponentLattice#UNKNOWNCOMPONENT}, depending on the situation.
     *
     * @param locals an <code>Object[]</code> value
     * @param stack a <code>Stack</code> value
     * @exception FunctionException if an error occurs
     */
    protected void apply(Object[] locals, Stack stack) throws FunctionException {
      if (stack!=null) {
	if (stack.size()<consume)
	  throw new FunctionException("stack too small for arith");
	boolean constants=true;
	boolean hasNonconstants=false;
	int extra=consume-n;
	Number[] values = new Number[n];
	for (int i=0; i<n; i++) {
	  Object o = stack.pop();
	  if (n-i==extra) {
	    Object p = stack.pop();
	    extra--;
	  }
	  if (o==null || !(o instanceof Number)) {
	    constants=false;
	    hasNonconstants |=
	      o.equals(CFPComponentLattice.NONCONSTANTCOMPONENT);
	  } else {
	    values[i] = (Number)o;
	  }
	}
	for (int i=produce; i>1; i--)
	  stack.push(CFPComponentLattice.UNKNOWNCOMPONENT);
	if (constants) {
	  try {
	    Number r = evaluate(values);
	    stack.push(r);
	  } catch (ArithmeticException ex) {
	    stack.push(CFPComponentLattice.NONCONSTANTCOMPONENT);
	  }
	} else if (hasNonconstants) {
	  stack.push(CFPComponentLattice.NONCONSTANTCOMPONENT);
	} else {
	  stack.push(CFPComponentLattice.UNKNOWNCOMPONENT);
	}
      }
    }

    /**
     * Supposed to do the actual operation.
     *
     * @param values a <code>Number[]</code> value
     * @return a <code>Number</code> value
     * @exception FunctionException if an error occurs
     */
    public abstract Number evaluate(Number[] values) throws FunctionException;

    public String toString() { return "<arith consume "+n+" produce "+produce+">"; };
  }

  /**
   * Binary arithmetic operations.
   *
   */
  protected class BinaryFunction extends ArithmeticFunction {
    protected static final int ADD_TYPE = 0;
    protected static final int SUB_TYPE = 1;
    protected static final int MUL_TYPE = 2;
    protected static final int DIV_TYPE = 3;
    protected static final int REM_TYPE = 4;
    protected static final int AND_TYPE = 5;
    protected static final int OR_TYPE  = 6;
    protected static final int XOR_TYPE = 7;
    protected static final int SHL_TYPE = 8;
    protected static final int SHR_TYPE = 9;
    protected static final int USHR_TYPE = 10;

    /**
     * Type of binary operation. Must be one of the constants in this class.
     *
     */
    protected int type;

    /**
     * Creates a new <code>BinaryFunction</code> instance. The type must be one of
     * the constants in this class.
     *
     * @param type an <code>int</code> value
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public BinaryFunction(int type, int consume, int produce) {
      super(2,consume,produce); this.type=type;
    }
    
    /**
     * Performs the operation on double.
     *
     * @param x a <code>double</code> value
     * @param y a <code>double</code> value
     * @return a <code>double</code> value
     */
    protected double eval(double x, double y) { 
      switch (type) {
      case ADD_TYPE: return x+y;
      case SUB_TYPE: return x-y;
      case MUL_TYPE: return x*y;
      case DIV_TYPE: return x/y;
      case REM_TYPE: return x%y;
      default: throw new Error("invalid type in BinaryFunction");
      }
    }

    /**
     * Performs the operation on float.
     *
     * @param x a <code>float</code> value
     * @param y a <code>float</code> value
     * @return a <code>float</code> value
     */
    protected float eval(float x, float y) { 
      switch (type) {
      case ADD_TYPE: return x+y;
      case SUB_TYPE: return x-y;
      case MUL_TYPE: return x*y;
      case DIV_TYPE: return x/y;
      case REM_TYPE: return x%y;
      default: throw new Error("invalid type in BinaryFunction");
      }
    }

    /**
     * Performs the operation on long.
     *
     * @param x a <code>long</code> value
     * @param y a <code>long</code> value
     * @return a <code>long</code> value
     */
    protected long eval(long x, long y) { 
      switch (type) {
      case ADD_TYPE: return x+y;
      case SUB_TYPE: return x-y;
      case MUL_TYPE: return x*y;
      case DIV_TYPE: return x/y;
      case REM_TYPE: return x%y;
      case AND_TYPE: return x&y;
      case OR_TYPE:  return x|y;
      case XOR_TYPE: return x^y;
      case SHL_TYPE: return x<<y;
      case SHR_TYPE: return x>>y;
      case USHR_TYPE: return x>>>y;
      default: throw new Error("invalid type in BinaryFunction");
      }
    }

    /**
     * Performs the operation on int.
     *
     * @param x an <code>int</code> value
     * @param y an <code>int</code> value
     * @return an <code>int</code> value
     */
    protected int eval(int x, int y) { 
      switch (type) {
      case ADD_TYPE: return x+y;
      case SUB_TYPE: return x-y;
      case MUL_TYPE: return x*y;
      case DIV_TYPE: return x/y;
      case REM_TYPE: return x%y;
      case AND_TYPE: return x&y;
      case OR_TYPE:  return x|y;
      case XOR_TYPE: return x^y;
      case SHL_TYPE: return x<<y;
      case SHR_TYPE: return x>>y;
      case USHR_TYPE: return x>>>y;
      default: throw new Error("invalid type in BinaryFunction");
      }
    }
    
    public String toString() {
      switch (type) {
      case ADD_TYPE: return "<add "+produce+">";
      case SUB_TYPE: return "<sub "+produce+">";
      case MUL_TYPE: return "<mul "+produce+">";
      case DIV_TYPE: return "<div "+produce+">";
      case REM_TYPE: return "<rem "+produce+">";
      case AND_TYPE: return "<and "+produce+">";
      case OR_TYPE:  return "<or "+produce+">";
      case XOR_TYPE: return "<xor "+produce+">";
      case SHL_TYPE: return "<shl "+produce+">";
      case SHR_TYPE: return "<shr "+produce+">";
      case USHR_TYPE: return "<ushr "+produce+">";
      default: throw new Error("invalid type in BinaryFunction");
      }
    }
    
    public Number evaluate(Number[] values) throws FunctionException {
      if (values.length!=2)
	throw new FunctionException("binary function needs two arguments");
      if (values[0] instanceof Double) {
	return new Double(eval(values[1].doubleValue(),values[0].doubleValue()));
      } else if (values[0] instanceof Float) {
	return new Float(eval(values[1].floatValue(),values[0].floatValue()));
      } else if (values[0] instanceof Integer) {
	return new Integer(eval(values[1].intValue(),values[0].intValue()));
      } else if (values[0] instanceof Long) {
	return new Long(eval(values[1].longValue(),values[0].longValue()));
      } else {
	throw new FunctionException("unexpected number type");
      }
    }
  }
  
  /**
   * Negates a number.
   *
   */
  protected class NegationFunction extends ArithmeticFunction {
    /**
     * Creates a new <code>NegationFunction</code> instance.
     *
     * @param produce an <code>int</code> value
     */
    public NegationFunction(int produce) {
      super(1,produce,produce);
    }
    
    public Number evaluate(Number[] values) throws FunctionException {
      if (values.length!=1)
	throw new FunctionException("neg needs one arguments");
      if (values[0] instanceof Double) {
	return new Double(-values[0].doubleValue());
      } else if (values[0] instanceof Float) {
	return new Float(-values[0].floatValue());
      } else if (values[0] instanceof Integer) {
	return new Integer(-values[0].intValue());
      } else if (values[0] instanceof Long) {
	return new Long(-values[0].longValue());
      } else {
	throw new FunctionException("unexpected number type");
      }
    }
    public String toString() { return "<neg "+produce+">"; }
  }
  
  /**
   * Compares to numbers.
   *
   */
  protected class CompareFunction extends ArithmeticFunction {
    /**
     * Creates a new <code>CompareFunction</code> instance.
     *
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public CompareFunction(int consume, int produce) {
      super(2,consume,produce);
    }

    /**
     * Compares two doubles.
     *
     * @param x a <code>double</code> value
     * @param y a <code>double</code> value
     * @return an <code>int</code> value
     */
    protected int eval(double x, double y) {
      return (x<y)?-1:(x==y)?0:1;
    }

    /**
     * Compares two floats.
     *
     * @param x a <code>float</code> value
     * @param y a <code>float</code> value
     * @return an <code>int</code> value
     */
    protected int eval(float x, float y) { 
      return (x<y)?-1:(x==y)?0:1;
    }

    /**
     * Compares two longs.
     *
     * @param x a <code>long</code> value
     * @param y a <code>long</code> value
     * @return an <code>int</code> value
     */
    protected int eval(long x, long y) { 
      return (x<y)?-1:(x==y)?0:1;
    }

    /**
     * Compares two ints.
     *
     * @param x an <code>int</code> value
     * @param y an <code>int</code> value
     * @return an <code>int</code> value
     */
    protected int eval(int x, int y) { 
      return (x<y)?-1:(x==y)?0:1;
    }
    
    public Number evaluate(Number[] values) throws FunctionException {
      if (values.length!=2)
	throw new FunctionException("binary function needs two arguments");
      if (values[0] instanceof Double) {
	return new Integer(eval(values[1].doubleValue(),values[0].doubleValue()));
      } else if (values[0] instanceof Float) {
	return new Integer(eval(values[1].floatValue(),values[0].floatValue()));
      } else if (values[0] instanceof Integer) {
	return new Integer(eval(values[1].intValue(),values[0].intValue()));
      } else if (values[0] instanceof Long) {
	return new Integer(eval(values[1].longValue(),values[0].longValue()));
      } else {
	throw new FunctionException("unexpected number type");
      }
    }
    public String toString() { return "<cmp "+produce+">"; }
  }

  /**
   * Conversion functions.
   *
   */
  protected class ConversionFunction extends ArithmeticFunction {
    protected static final int LONG_CONV   = 0;
    protected static final int INT_CONV    = 1;
    protected static final int DOUBLE_CONV = 2;
    protected static final int FLOAT_CONV  = 3;
    protected static final int SHORT_CONV  = 4;
    protected static final int BYTE_CONV   = 5;
    protected static final int CHAR_CONV   = 6;
  
    /**
     * Target type of conversion. Must be one of the constants in this class.
     *
     */
    protected int target;

    /**
     * Creates a new <code>ConversionFunction</code> instance.
     *
     * @param target an <code>int</code> value
     * @param consume an <code>int</code> value
     * @param produce an <code>int</code> value
     */
    public ConversionFunction(int target, int consume, int produce) {
      super(1,consume,produce);
      this.target=target;
    }

    public Number evaluate(Number[] values) throws FunctionException {
      if (values.length!=1)
	throw new FunctionException("neg needs one arguments");
      switch (target) {
      case LONG_CONV: return new Long(values[0].longValue());
      case INT_CONV: return new Integer(values[0].intValue());
      case DOUBLE_CONV: return new Double(values[0].doubleValue());
      case FLOAT_CONV: return new Float(values[0].floatValue());
      case SHORT_CONV: return new Integer(values[0].shortValue());
      case BYTE_CONV: return new Integer(values[0].byteValue());
      case CHAR_CONV: return new Integer((char)(values[0].intValue()));
      default: throw new Error("invalid type in ConversionFunction");
      }
    }

    public String toString() {
      switch (target) {
      case LONG_CONV: return "<2l "+produce+">";
      case INT_CONV:  return "<2i "+produce+">";
      case DOUBLE_CONV:  return "<2d "+produce+">";
      case FLOAT_CONV: return "<2f "+produce+">";
      case SHORT_CONV: return "<2s "+produce+">";
      case BYTE_CONV: return "<2b "+produce+">";
      case CHAR_CONV: return "<2c "+produce+">";
      default: throw new Error("invalid type in ConversionFunction");
      }
    }
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
      if (i instanceof ArithmeticInstruction) {
	if ((i instanceof DADD) || (i instanceof FADD) ||
	    (i instanceof IADD) || (i instanceof LADD)) {
	  f = new BinaryFunction(BinaryFunction.ADD_TYPE, consume, produce);
	} else if ((i instanceof DSUB) || (i instanceof FSUB) ||
		   (i instanceof ISUB) || (i instanceof LSUB)) {
	  f = new BinaryFunction(BinaryFunction.SUB_TYPE, consume, produce);
	} else if ((i instanceof DMUL) || (i instanceof FMUL) ||
		   (i instanceof IMUL) || (i instanceof LMUL)) {
	  f = new BinaryFunction(BinaryFunction.MUL_TYPE, consume, produce);
	} else if ((i instanceof DDIV) || (i instanceof FDIV) ||
		   (i instanceof IDIV) || (i instanceof LDIV)) {
	  f = new BinaryFunction(BinaryFunction.DIV_TYPE, consume, produce);
	} else if ((i instanceof DREM) || (i instanceof FREM) ||
		   (i instanceof IREM) || (i instanceof LREM)) {
	  f = new BinaryFunction(BinaryFunction.REM_TYPE, consume, produce);
	} else if ((i instanceof IAND) || (i instanceof LAND)) {
	  f = new BinaryFunction(BinaryFunction.AND_TYPE, consume, produce);
	} else if ((i instanceof IOR) || (i instanceof LOR)) {
	  f = new BinaryFunction(BinaryFunction.OR_TYPE, consume, produce);
	} else if ((i instanceof IXOR) || (i instanceof LXOR)) {
	  f = new BinaryFunction(BinaryFunction.XOR_TYPE, consume, produce);
	} else if ((i instanceof ISHL) || (i instanceof LSHL)) {
	  f = new BinaryFunction(BinaryFunction.SHL_TYPE, consume, produce);
	} else if ((i instanceof ISHR) || (i instanceof LSHR)) {
	  f = new BinaryFunction(BinaryFunction.SHR_TYPE, consume, produce);
	} else if ((i instanceof IUSHR) || (i instanceof LUSHR)) {
	  f = new BinaryFunction(BinaryFunction.USHR_TYPE, consume, produce);
	} else if ((i instanceof DNEG) || (i instanceof FNEG) ||
		   (i instanceof INEG) || (i instanceof LNEG)) {
	  f = new NegationFunction(produce);
	} else {
	  throw new IllegalArgumentException("unidentified ArithmeticInstruction "+i);
	}
      } else if ((i instanceof DCMPG) || (i instanceof FCMPG) ||
		 (i instanceof DCMPL) || (i instanceof FCMPL) ||
		 (i instanceof LCMP)) {
	f = new CompareFunction(consume, produce);
      } else if (i instanceof ConversionInstruction) {
	if ((i instanceof L2I) || (i instanceof D2I) || (i instanceof F2I)) {
	  f = new ConversionFunction(ConversionFunction.INT_CONV, consume, produce);
	} else if ((i instanceof I2L) || (i instanceof D2L) || (i instanceof F2L)) {
	  f = new ConversionFunction(ConversionFunction.LONG_CONV, consume, produce);
	} else if ((i instanceof I2D) || (i instanceof L2D) || (i instanceof F2D)) {
	  f = new ConversionFunction(ConversionFunction.DOUBLE_CONV, consume, produce);
	} else if ((i instanceof I2F) || (i instanceof L2F) || (i instanceof D2F)) {
	  f = new ConversionFunction(ConversionFunction.FLOAT_CONV, consume, produce);
	} else if ((i instanceof I2B)) {
	  f = new ConversionFunction(ConversionFunction.BYTE_CONV, consume, produce);
	} else if ((i instanceof I2C)) {
	  f = new ConversionFunction(ConversionFunction.CHAR_CONV, consume, produce);
	} else if ((i instanceof I2S)) {
	  f = new ConversionFunction(ConversionFunction.SHORT_CONV, consume, produce);
	} else {
	  throw new IllegalArgumentException("unidentified ConversionInstruction "+i);
	}
      }else {
	f = super.getAbstract(ih);
      }
    }
    return f; 
  }
}



