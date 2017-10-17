package de.rwth.dfa.jvm.samples; // Generated package name

import java.util.Enumeration;
import java.util.Vector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import de.rwth.dfa.jvm.*;
import de.rwth.domains.*;

import de.rwth.utils.AbstractClassProcessor;
import de.rwth.utils.Stopwatch;
import de.rwth.utils.Table;

/**
 * An abstract class for constructing applications for analyzing the JVM methods of a
 * class file.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: AbstractAnalyser.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public abstract class AbstractAnalyser extends AbstractClassProcessor {
  /**
   * The table for collection the statistics.
   *
   */
  protected Table table=new Table("Location");
  
  /**
   * Creates a new <code>AbstractAnalyser</code> instance. It accepts the following
   * command line options:
   * <dl>
   * <dt><code>-v</code>
   * <dd>Verbose mode. Dump all kind of information to standard error.
   * <dt><code>-a</code>
   * <dd>Use all solvers. Default is to use only the first solver. This also
   * activates the cross checking of (comparable) solver results.
   * <dt><code>-s</code>
   * <dd>Statistics mode. At the end, a comma separated table with timing statistics,
   * memory usage is printed to the standard output.
   * </dl>
   * All non-native methods of the class file on the command line are processed and
   * the code with the computed solution is printed to standard output.
   * 
   * @param args a <code>String[]</code> value: The command line arguments.
   */
  public AbstractAnalyser(String [] args) {
    super(args, Arrays.asList(new String[] {"v","a","s"}));
  }

  /**
   * Checks if verbose mode was requested on the command line.
   *
   * @return a <code>boolean</code> value
   */
  protected boolean isVerboseMode() {
    return flags.contains("v");
  }
  
  /**
   * Checks if all solvers were requested on the command line.
   *
   * @return a <code>boolean</code> value
   */
  protected boolean isAllMode() {
    return flags.contains("a");
  }
  
  /**
   * Checks if statistics mode was requested on the command line.
   *
   * @return a <code>boolean</code> value
   */
  protected boolean isStatisticsMode() {
    return flags.contains("s");
  }
  
  /**
   * Print the code with the solution to standard output.
   *
   * @param instrs an <code>InstructionList</code> value
   * @param solution an <code>Object[]</code> value
   */
  protected void dumpResult(InstructionList instrs, Object[] solution) {
    //Enumeration ne = instrs.elements();
	Iterator<InstructionHandle> ne = instrs.iterator();
    for (int j=0; j<solution.length; j++) {
      InstructionHandle handle=(InstructionHandle)ne.next();
      System.out.println(handle+"\t"+solution[j]);
    }
  }
  
  /**
   * Cross checks results and reports errors in an IllegalArgumentException.
   *
   * @param lattice a <code>Lattice</code> value
   * @param instrs an <code>InstructionList</code> value
   * @param solution an <code>Object[]</code> value
   * @param refSolution an <code>Object[]</code> value
   */
  protected void checkResult(Lattice lattice, InstructionList instrs,
			     Object[] solution, Object[] refSolution) {
    String eresult="";
    boolean someerror=false;
    
    if (isVerboseMode()) System.err.print("enter check ");
    
    if (refSolution==null) 
      throw new IllegalArgumentException("no refSolution for checkResult");
    
    Iterator<InstructionHandle> ne = instrs.iterator();
    if (solution.length!=refSolution.length) {
      eresult+="Solutions differ in length ("+solution.length+
	       " vs. "+refSolution.length+")\n";
      someerror=true;
    }
    for (int j=0; !someerror && j<solution.length; j++) {
      InstructionHandle handle=(InstructionHandle)ne.next();
      if ((solution[j]!=refSolution[j] && solution[j]==null || refSolution[j]==null)
	  || !lattice.equals(solution[j],refSolution[j])) {
	eresult+=handle+"\t"+solution[j]+", ref="+refSolution[j]+"  ERROR!!!\n";
	someerror=true;
      } else {
	if (isVerboseMode()) eresult+=(handle+" "+solution[j]+"\n");
      }
    }
    if (ne.hasNext()) {
      someerror=true;
      eresult+="+-- missing elements in solutions:\n";
      while (ne.hasNext()) {
	InstructionHandle handle=(InstructionHandle)ne.next();
	eresult+="| "+handle+"\n";
      }
      eresult+="+------------------------\n";
    }
    if (isVerboseMode()) System.err.print("exit check ");
    
    if (someerror) {
      throw new IllegalArgumentException("error(s): \n"+eresult);
    }
  }
  
  /**
   * Transfers the results of a single solver run to the table.
   *
   * @param stopwatch a <code>Stopwatch</code> value
   * @param solver a <code>Solver</code> value
   * @param key a <code>String</code> value
   */
  protected void transferToTable(Stopwatch stopwatch, Solver solver, String key) {
    Vector timings = stopwatch.get();
    String field;
    for (int i=0; i<timings.size(); i++) {
      field=(String)timings.get(i);
      if (!table.hasFieldName(field)) table.addNewFieldName(field);
      i++;
      Long time=(Long)timings.get(i);
      table.setFieldForKey(field,key,time);
    }
    if (solver!=null) {
      field=stopwatch.getDescription()+"iterations";
      if (!table.hasFieldName(field)) table.addNewFieldName(field);
      table.setFieldForKey(field,key,new Long(solver.getIterations()));
      
      field=stopwatch.getDescription()+"memory";
      if (!table.hasFieldName(field)) table.addNewFieldName(field);
      table.setFieldForKey(field,key,new Long(solver.getMemory()));
    }
  }
  
  /**
   * Supposed to return all available solvers. Solvers with comparable results should
   * be grouped in sequence together, the groups must be separated with
   * <code>null</code> entries. The solution of the first solver in a group is used
   * as reference solution for the group.
   *
   * @param abstraction an <code>Abstraction</code> value
   * @param methodName a <code>String</code> value
   * @param methodInstrs an <code>InstructionList</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param stopwatch a <code>Stopwatch</code> value
   * @return a <code>Solver[]</code> value
   */
  public abstract Solver[] getSolvers(Abstraction abstraction, String methodName,
				      InstructionList methodInstrs,
				      CodeException[] methodExceptions,
				      Stopwatch stopwatch);

  /**
   * Supposed to return the abstraction used for a specific method.
   *
   * @param method a <code>Method</code> value
   * @param code a <code>Code</code> value
   * @param methodExceptions a <code>CodeException[]</code> value
   * @param instrs an <code>InstructionList</code> value
   * @return an <code>Abstraction</code> value
   */
  public abstract Abstraction getAbstraction(Method method, Code code,
					     CodeException[] methodExceptions,
					     InstructionList instrs);
  
  /**
   * The reference solution of the current group of solvers. It is <code>null</code>
   * for the first solver of a group.
   *
   */
  protected Object[] refSolution;
  
  /**
   * Processes a method. 
   *
   * @param method a <code>Method</code> value
   * @param filename a <code>String</code> value
   */
  public void process(Method method, String filename) {
    String key = method.getName()+method.getSignature()+"@"+filename;
    table.createNewKey(key);

    Code code=method.getCode();
    CodeException[] methodExceptions = code.getExceptionTable();
    InstructionList instrs = new InstructionList(code.getCode());
    Abstraction abstraction = getAbstraction(method,code,methodExceptions,instrs);
    Stopwatch stopwatch = new Stopwatch();
    Solver[] solvers = getSolvers(abstraction,method.getName(),
				  instrs, methodExceptions, stopwatch);
    Object[] solution;
    refSolution=null;
    if (isVerboseMode()) System.err.println(key+":");

    for (int i=0; i<(isAllMode()?solvers.length:1); i++) {
      if (solvers[i]==null) {
	refSolution=null;
	if (isVerboseMode()) 
	  System.err.println("- resetting reference solution");
	
      } else {
	stopwatch.reset(solvers[i].getClass().getName());
	if (isVerboseMode())
	  System.err.print("- "+solvers[i].getClass().getName()+" . ");
	solution=solvers[i].getSolution();
	if (refSolution!=null)
	  checkResult(abstraction.getLattice(),instrs,solution,refSolution);
	if (isVerboseMode()) System.err.println("ok");
	System.out.println(method.getName()+method.getSignature());
	dumpResult(instrs,solution);
	if (refSolution==null) {
	  refSolution=new Object[solution.length];
	  for (int j=0; j<solution.length; j++) 
	    refSolution[j]=solution[j];
	  if (isVerboseMode() && isAllMode())
	    System.err.println("- making this the reference solution");
	}
	
	transferToTable(stopwatch, solvers[i], key);
      }
    }
  }

  /**
   * Processes a class file.
   *
   * @param jclass a <code>JavaClass</code> value
   * @param filename a <code>String</code> value
   */
  public void process(JavaClass jclass, String filename) {
    if (isVerboseMode()) System.err.println(getClass().getName()+": "+filename);
    Method[] methods = jclass.getMethods();
    
    for (int i=0; i<methods.length; i++) 
      if (!methods[i].isAbstract() && !methods[i].isNative())
	process(methods[i], filename);
    
    if (isVerboseMode())
      System.err.println(getClass().getName()+": "+filename+" done");
    
    if (isStatisticsMode())
      System.out.println(table);
  }
}
