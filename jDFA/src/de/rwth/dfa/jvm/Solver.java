package de.rwth.dfa.jvm; // Generated package name

//import de.fub.bytecode.classfile.*; 
//import de.fub.bytecode.generic.*; 
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.utils.Stopwatch;

/**
 * An abstract class for a JVM abstraction solver. It contains the basic data
 * structures describing the JVM abstraction and the method to be
 * analyzed. Furthermore, it provides the general framework for runtime and memory
 * statistics. To get a working implementation, the method <code>compute()</code>
 * must be provided. <strong>Implementations should guarantee that all computation is
 * done there and not in constructors!</strong>. Otherwise, the statistics will be
 * wrong.
 * 
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Solver.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public abstract class Solver  {
  /**
   * The JVM abstraction of this solver.
   *
   */
  protected Abstraction abstraction = null;
  
  /**
   * The name of the method to be analyses.
   *
   */
  protected String methodName = null;

  /**
   * The instruction list of the method to be analyses.
   *
   */
  protected InstructionList methodInstrs = null;

  /**
   * The code exception array of the method to be analyses.
   *
   */
  protected CodeException[] methodExceptions = null;
  
  /**
   * The stop watch to be used for timing the computation. This may be
   * <code>null</code> if there is no stop watch to be used.
   *
   */
  protected Stopwatch stopwatch = null;

  /**
   * The number of iterations used by the computation.
   *
   */
  protected long iterations;

  /**
   * The approximation of the amount of memory used by the solution.
   *
   */
  protected long memory;
  
  /**
   * Creates a new <code>Solver</code> instance with stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   */
  public Solver(Abstraction abstraction, String methodName,
		InstructionList methodInstrs, CodeException[] methodExceptions,
		Stopwatch stopwatch) {
    super();
    this.abstraction=abstraction;
    this.methodName = methodName;
    this.methodInstrs = methodInstrs;
    this.methodExceptions = methodExceptions;
    this.stopwatch = stopwatch;
  }

  /**
   * Creates a new <code>Solver</code> instance without stop watch.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   */
  public Solver(Abstraction abstraction, String methodName,
		InstructionList methodInstrs, CodeException[] methodExceptions) {
    this(abstraction, methodName, methodInstrs, methodExceptions, null);
  }

  /**
   * Implementations must provide this method. Given the JVM abstraction and the
   * method in this classes data structures, the method must compute a correct
   * solution, i.e. an approximation of the meet over paths solution (for
   * <code>isAll==true</code>). This method is called from
   * <code>getSolution()</code>.
   *
   * <ol>
   *
   * <li>Implementations must set the variable <code>iterations</code> to a sensible
   * value.
   * </li>
   * 
   * <li>If there is a stop watch, it is already running. Implementations of this
   * method may use the <code>split()</code> method as often as they wish. They may
   * not call <code>start()</code> or <code>stop()</code>.
   * </li>
   *
   * <li>An approximation of the memory usage is done by
   * <code>getSolution()</code>. Hence, setting <code>memory</code> in this method
   * has no effect.
   * </li>
   * 
   * </ol>
   *
   * @param solution an <code>Object[]</code> value: A newly created array for the
   * solution. It has one entry for each instruction.
   * @param isForward a <code>boolean</code> value: Analysis control flow
   * @param isAll a <code>boolean</code> value: Use meet or join
   */
  protected abstract void compute(Object[] solution, boolean isForward, boolean isAll);
  
  /**
   * Computes the solution by calling <code>compute()</code> and does memory and
   * timing statistics.
   *
   * @return an <code>Object[]</code> value
   */
  public Object[] getSolution() {
    System.gc();
    long beforeMemory=Runtime.getRuntime().totalMemory()
                     -Runtime.getRuntime().freeMemory();
    if (stopwatch!=null) stopwatch.start("total");
    Object[] solution=new Object[methodInstrs.getLength()];
    compute(solution,
	    abstraction.getDirection()==Abstraction.DIRECTION_FORWARD,
	    abstraction.getQuantifier()==Abstraction.QUANTIFIER_ALL);
    if (stopwatch!=null) stopwatch.stop("result");
    System.gc();
    memory=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()
           -beforeMemory;
    return solution;
  }

  /**
   * Returns the number of iterations during the computation.
   *
   * @return a <code>long</code> value
   */
  public long getIterations() {
    return iterations;
  }
  
  /**
   * Returns an approximation of the amount of memory used during the computation.
   *
   * @return a <code>long</code> value
   */
  public long getMemory() {
    return memory;
  }
}
