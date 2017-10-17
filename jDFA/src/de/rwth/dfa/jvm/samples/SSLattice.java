package de.rwth.dfa.jvm.samples; // Generated package name


import java.util.Enumeration;
import java.util.NoSuchElementException;

import de.rwth.domains.*;
import de.rwth.domains.templates.*;

/**
 * The lattice used by {@link SSAbstraction}. For a maximal stack size n, the lattice
 * looks like this
 * <pre>
 *    ?size? 
 *  /  | ... \  
 * 0   1  ... n
 *  \  | ... /
 * size errornous
 * </pre>
 * 
 * An Integer represents a valid stack size. The top element represents insufficient
 * information and the bottom element represents not uniquely determined stack
 * size. The latter is not possible in class files which are accepted by a JVM.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: SSLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class SSLattice extends FlatCompleteLattice {
  /**
   * Creates a new <code>SSLattice</code> instance.
   *
   * @param maxStack an <code>int</code> value
   */
  public SSLattice(int maxStack) {
    super(new SimpleSet(0,maxStack),"size errornous","?size?");
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    Lattice lattice = new SSLattice(4);
    try {
      Domain.checkProperties(lattice);
      System.err.println(Domain.hasseDiagram(lattice));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
