package de.rwth.dfa.jvm.samples; // Generated package name

import java.util.Stack;

import de.rwth.utils.ArrayTools;

import de.rwth.domains.*;
import de.rwth.domains.templates.*;

/**
 * The lattice used for constant folding propagation. It is the Cartesian product of
 * a {@link CFPLocalsLattice} and a {@link CFPStackLattice}.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: CFPLattice.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class CFPLattice extends TupleCompleteLattice {
  /**
   * The lattice for the local variable slots in this lattice.
   *
   */
  protected CFPLocalsLattice llattice = null;
  
  /**
   * The lattice for the stacks in this lattice.
   *
   */
  protected CFPStackLattice slattice = null;
    
  /**
   * Creates a new <code>CFPLattice</code> instance.
   *
   * @param maxLocals an <code>int</code> value
   * @param maxStack an <code>int</code> value
   */
  public CFPLattice(int maxLocals, int maxStack) {
    super(new CompleteLattice[] {
      new CFPLocalsLattice(maxLocals), new CFPStackLattice(maxStack)});
    Set[] sets=getSets();
    this.llattice=(CFPLocalsLattice)sets[0];
    this.slattice=(CFPStackLattice)sets[1];
  }

  /**
   * Extracts the information for local variable slots from an element of this
   * lattice.
   *
   * @param o an <code>Object</code> value
   * @return an <code>Object[]</code> value
   */
  public Object[] getLocals(Object o) {
    if (!(o instanceof TupleElement)) return null;
    return llattice.getLocals(((TupleElement)o).getElements()[0]);
  }
  
  /**
   * Extracts the information for the stack from an element of this lattice.
   *
   * @param o an <code>Object</code> value
   * @return an <code>Object[]</code> value
   */
  public Object getStack(Object o) {
    if (!(o instanceof TupleElement)) return null;
    return slattice.getStack(((TupleElement)o).getElements()[1]);
  }
  
  /**
   * Creates an element of this lattice from valid components.
   *
   * @param locals an <code>Object[]</code> value
   * @param stack an <code>Object</code> value
   * @return an <code>Object</code> value
   */
  public Object makeElement(Object[] locals, Object stack) {
    return new TupleElement(llattice.makeElement(locals),
			    slattice.makeElement(stack));
  }
  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    {
      Lattice lattice = new CFPLattice(0,1);
      try {
	System.err.println(Domain.hasseDiagram(lattice));
	Domain.checkProperties(lattice);
      } catch (Exception ex) {
	ex.printStackTrace();
      }
    }
    
    {
      Lattice lattice = new CFPLattice(1,0);
      try {
	System.err.println(Domain.hasseDiagram(lattice));
	Domain.checkProperties(lattice);
      } catch (Exception ex) {
	ex.printStackTrace();
      }
    }
    {
      Lattice lattice = new CFPLattice(1,1);
      try {
	System.err.println(Domain.hasseDiagram(lattice));
	Domain.checkProperties(lattice);
      } catch (Exception ex) {
	ex.printStackTrace();
      }
    }
    System.exit(1);    
    try {
      CFPLattice l = new CFPLattice(4,4);
      Object e1 =
	l.makeElement(ArrayTools.fill(new Object[4], CFPComponentLattice.UNKNOWNCOMPONENT),new Stack());
      Object[] locals = l.getLocals(e1);
      Stack stack     = (Stack)l.getStack(e1);
      stack.push(new Integer(3));
      stack.push(new Integer(2));
      stack.push(new Integer(1));
      stack.push(CFPComponentLattice.UNKNOWNCOMPONENT);
      locals[1]=new Integer(3);
      locals[2]=new Integer(2);
      e1=l.makeElement(locals,stack);
      System.err.println(e1+" "+l.equals(e1,e1));
      
      locals = ArrayTools.fill(new Object[4], CFPComponentLattice.UNKNOWNCOMPONENT);
      stack  = new Stack();
      stack.push(CFPComponentLattice.UNKNOWNCOMPONENT);
      stack.push(new Integer(3));
      stack.push(new Integer(1));
      stack.push(CFPComponentLattice.NONCONSTANTCOMPONENT);
      locals[0]=new Integer(2);
      locals[1]=new Integer(2);
      locals[2]=new Integer(2);
      Object e2 = l.makeElement(locals,stack);
      System.err.println(e2+" "+l.equals(e1,e2));
      
      System.err.println(l.meet(e1,e2));
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
  }
}
