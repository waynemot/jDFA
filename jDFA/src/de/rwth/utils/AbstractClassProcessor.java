package de.rwth.utils; // Generated package name

import java.io.*;

import java.util.Collection;
import java.util.HashSet;

//import de.fub.bytecode.classfile.*;
//import de.fub.bytecode.generic.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

/**
 * An abstract class for building applications which can process the methods in a
 * class file based on BCEL. It does command line processing.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: AbstractClassProcessor.java,v 1.4 2002/09/17 06:53:53 mohnen Exp $
 */
public abstract class AbstractClassProcessor {
  /**
   * The flags detected by this processor. 
   *
   */
  protected Collection flags;

  /**
   * The file to process.
   *
   */
  protected File file;
  
  /**
   * Creates a new <code>AbstractClassProcessor</code> instance. Prints an error
   * message if the command line is not of the form '[options] file'. Otherwise, it
   * sets the flags and the file according to the command line. Implementations must
   * override {@link #process()} and call it.
   *
   * @param args a <code>String[]</code> value: The command line arguments.
   * @param validFlags a <code>Collection</code> value: A set containing the base
   * names of options, i.e. "v" for "-v".
   */
  public AbstractClassProcessor(String [] args, Collection validFlags) {
    this.flags=new HashSet();
    for (int i=0; i<args.length-1; i++) {
      if (!args[i].startsWith("-"))
	usage(args,i,i+"th argument not an option",validFlags);
      String flag=args[i].substring(1);
      if (!validFlags.contains(flag))
	usage(args,i,args[i]+" is not a valid option",validFlags);
      this.flags.add(flag);
    }
    if (args.length!=0)
      file=new File(args[args.length-1]);
    else
	usage(args,0,"no arguments",validFlags);
  }
  
  /**
   * Prints a usage message to standard error and exits.
   *
   * @param args a <code>String[]</code> value
   * @param conflictIndex an <code>int</code> value
   * @param errorText a <code>String</code> value
   * @param validFlags a <code>Collection</code> value
   */
  public void usage(String[] args, int conflictIndex, String errorText, Collection validFlags) {
    System.err.println("Error: "+errorText);
    System.err.println("Usage: java "+getClass().getName()+" "+validFlags+" file");
    System.exit(1);
  }
  
  /**
   * Processes the file. It opens a FileInputStream, parses the class file using BCEL
   * and passes control to <code>process(JavaClass,String)</code>.
   *
   * @exception FileNotFoundException if an error occurs
   * @exception IOException if an error occurs
   */
  public void process() throws FileNotFoundException, IOException {
    process(new ClassParser(new FileInputStream(file),
			    file.getName()).parse(),
	    file.getPath());
  }
  
  /**
   * Supposed to process a BCEL class data structure.
   *
   * @param jclass a <code>JavaClass</code> value
   * @param filename a <code>String</code> value
   */
  public abstract void process(JavaClass jclass, String filename);
}
