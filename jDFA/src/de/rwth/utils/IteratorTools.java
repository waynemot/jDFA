package de.rwth.utils; // Generated package name

import java.io.PrintStream;
import java.util.Iterator;

/**
 * A collection of useful methods for iterators.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: IteratorTools.java,v 1.3 2002/09/27 08:50:48 mohnen Exp $
 */
public class IteratorTools {
  public interface IteratorFolder {
    public void init();
    public void next(Object o, boolean hasNext);
  }
  
  public static void foldlIterator(Iterator i, IteratorFolder f) {
    f.init();
    while (i.hasNext()) {
      f.next(i.next(), i.hasNext());
    }
  }
  
  /**
   * Prints all pending elements of an iterator to a <code>PrintStream</code>.
   *
   * @param i an <code>Iterator</code> value
   * @param s a <code>PrintStream</code> value
   */
  public static void printIterator(Iterator i, final PrintStream s) {
    //;
    //while (i.hasNext()) {
    //s.print(i.next());
    // if (i.hasNext()) System.out.print(", ");
    //}
    //s.print("]");
    foldlIterator(i, new IteratorFolder() {
	public void init() {
	  s.print("[");
	}
	public void next(Object o, boolean hasNext) {
	  s.print(o);
	  s.print(hasNext?", ":"]");
	}
      });
  }
  
  /**
   * Prints all pending elements of an iterator to a <code>PrintStream</code> and
   * prints a newline character afterwards.
   *
   * @param i an <code>Iterator</code> value
   * @param s a <code>PrintStream</code> value
   */
  public static void printlnIterator(Iterator i, PrintStream s) {
    printIterator(i);
    s.println();
  }

  
  /**
   * Prints all pending elements of an iterator to <code>System.out</code>.
   *
   * @param i an <code>Iterator</code> value
   */
  public static void printIterator(Iterator i) {
    printIterator(i,System.out);
  }
  
  /**
   * Prints all pending elements of an iterator to <code>System.out</code> and prints
   * a newline character afterwards.
   *
   * @param i an <code>Iterator</code> value
   * @param s a <code>PrintStream</code> value
   */
  public static void printlnIterator(Iterator i) {
    printlnIterator(i,System.out);
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      java.util.Vector v = new java.util.Vector();
      v.add("1");
      v.add("2");
      v.add("3");
      printlnIterator(v.iterator());
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
} // class IteratorTools
