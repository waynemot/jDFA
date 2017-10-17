package de.rwth.dfa.jvm; // Generated package name

//import de.fub.bytecode.generic.*; 
import org.apache.bcel.generic.*;

import de.rwth.domains.*;
import de.rwth.domains.templates.ComposedFunction;

/**
 * An interface for describing an abstract model of JVM execution. It describes
 * everything which is necessary to do data flow analysis on JVM byte code.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Abstraction.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface Abstraction {
  /**
   * Returns the lattice used to do all computations.
   *
   * @return a <code>Lattice</code> value
   */
  public Lattice getLattice();
  
  /**
   * For a JVM instruction in a method, it returns the initial value of the
   * computations. It must be an element of the lattice of this abstraction.
   *
   * <p>
   *
   * In addition to the classes defined in the BCEL API, the instruction handle may
   * also be an instance of {@link ExceptionHeaderInstructionHandle}. This pseudo
   * instruction is inserted before the first real instruction of an exception
   * handler. If the operand stack state of the real JVM execution is modeled by this
   * abstraction, the pseudo instruction must be used to make the abstract value
   * represent a state with a stack with only a non null reference to an exception as
   * the only entry.
   * 
   * @param ih an <code>InstructionHandle</code> value
   * @param isEntry a <code>boolean</code> value: <code>true</code> if this
   * instruction is an entry with respect to the direction of control flow.
   * @return an <code>Object</code> value
   */
  public Object getInitialValue(InstructionHandle ih, boolean isEntry);
  
  /**
   * For a sequence of JVM instructions without jumps in a method, it returns the
   * initial value of the computations. It must be an element of the lattice of this
   * abstraction.
   *
   * @param ihs an <code>InstructionHandleVector</code> value
   * @param isEntry a <code>boolean</code> value: <code>true</code> if this
   * instruction is an entry with respect to the direction of control flow.
   * @return an <code>Object</code> value
   *
   * @see Abstraction.Default
   */
  public Object getInitialValue(InstructionHandleVector ihs, boolean isEntry);
  
  /**
   * The constant <code>DIRECTION_FORWARD</code> may be used as result of
   * <code>getDirection()</code> to indicate forward control flow.
   *
   */
  public static final int DIRECTION_FORWARD = 0;

  /**
   * The constant <code>DIRECTION_BACKWARD</code> may be used as result of
   * <code>getDirection()</code> to indicate backward control flow.
   *
   */
  public static final int DIRECTION_BACKWARD = 1;
  
  /**
   * Returns the direction of control flow in this abstraction. For forward flow, the
   * entry point of a method is considered to be the entry of the data flow
   * analysis. For backward flow, the exit points are considerer as entries.
   *
   * @return an <code>int</code> value: Either <code>DIRECTION_FORWARD</code> or
   * <code>DIRECTION_BACKWARD</code>.
   */
  public int getDirection();
  
  /**
   * The constant <code>QUANTIFIER_ALL</code> may be used as result of
   * <code>getQuantifier()</code> to indicate universal quantification.
   *
   */
  public static final int QUANTIFIER_ALL = 0;

  /**
   * The constant <code>QUANTIFIER_EXISTS</code> may be used as result of
   * <code>getQuantifier()</code> to indicate existential quantification.
   *
   */
  public static final int QUANTIFIER_EXISTS = 1;
  
  /**
   * Returns the quantification of this abstraction. For universal quantification,
   * the meet operation of the lattice is used to combine values at point where
   * controls flows merge. For existential quantification, the join operation of the
   * lattice is used.
   *
   * @return an <code>int</code> value: Either <code>QUANTIFIER_ALL</code> or
   * <code>QUANTIFIER_EXISTS</code>.
   */
  public int getQuantifier();

  /**
   * Returns the transfer function associated by this abstraction with an instruction
   * in a method. It must be a endomorphism on the lattice used by this
   * abstraction. Furthermore, it must be a <strong>monotone</strong> function!
   * Otherwise, the computation may not terminate.
   *
   * @param ih an <code>InstructionHandle</code> value
   * @return a <code>Function</code> value
   */
  public Function getAbstract(InstructionHandle ih);
  
  /**
   * Returns the transfer function associated by this abstraction with a sequence of
   * JVM instructions without jumps in a method. It must be a endomorphism on the
   * lattice used by this abstraction. Furthermore, it must be a
   * <strong>monotone</strong> function!  Otherwise, the computation may not
   * terminate.
   *
   * @param ih an <code>InstructionHandle</code> value
   * @return a <code>Function</code> value
   * 
   * @see Abstraction.Default
   */
  public Function getAbstract(InstructionHandleVector ihv);

  /**
   * Container class for default implementations of methods.
   *
   */
  public static class Default {
    /**
     * A default implementation of
     * {@link #getInitialValue(InstructionHandleVector,boolean)}.
     * For forward flow, it returns the value returned by
     * {@link #getInitialValue(InstructionHandle,boolean)} for the first
     * instruction in the sequence. For backward flow, it returns the value returned by
     * {@link #getInitialValue(InstructionHandle,boolean)} for the last
     * instruction in the sequence.
     * 
     * @param abstraction an <code>Abstraction</code> value
     * @param ihv an <code>InstructionHandleVector</code> value
     * @param isEntry a <code>boolean</code> value
     * @return an <code>Object</code> value
     */
    public static Object getInitialValue(Abstraction abstraction,
					 InstructionHandleVector ihv,
					 boolean isEntry) {
      if (abstraction.getDirection()==DIRECTION_FORWARD)
	return abstraction.getInitialValue((InstructionHandle)ihv.get(0),isEntry);
      else
	return abstraction.getInitialValue((InstructionHandle)ihv.get(ihv.size()-1),
					   isEntry && ihv.size()==1);
    }
    
    /**
     * A default implementation of
     * {@link #getAbstract(InstructionHandleVector)}. It computes the composition of
     * the functions returned by {@link #getAbstract(InstructionHandle)} for the
     * elements of the sequence. For forward flow, the function associated with the
     * first element of the sequence is the first function in the composition. For
     * backward flow, the first function is the composition is the function
     * associated with the last element of the sequence.
     *
     * @param abstraction an <code>Abstraction</code> value
     * @param ihv an <code>InstructionHandleVector</code> value
     * @return a <code>Function</code> value
     */
    public static Function getAbstract(Abstraction abstraction,
				       InstructionHandleVector ihv) {
      boolean isForward = abstraction.getDirection()==DIRECTION_FORWARD;
      Function f = abstraction.getAbstract((InstructionHandle)ihv.elementAt((isForward)?0:ihv.size()-1));
      for (int i=(isForward?1:ihv.size()-2);
	   (isForward && i<ihv.size()) || (!isForward && i>=0);
	   i+=isForward?1:-1) {
	f=new ComposedFunction(abstraction.getAbstract((InstructionHandle)ihv.elementAt(i)),f);
      }
      return f;
    }
  }
}
