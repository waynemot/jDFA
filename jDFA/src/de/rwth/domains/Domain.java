package de.rwth.domains; // Generated package name

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.rwth.graph.Graph;

/**
 * This class contains methods for debugging and visualisation of implementations for
 * the interfaces in this package.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Domain.java,v 1.3 2002/09/27 08:02:16 mohnen Exp $
 */
public class Domain {
  /**
   * Gets the elements of a finite set, or the elements of the skeleton subset of an
   * infinite set with finite skeleton subset.
   *
   * @param set a <code>Set</code> value
   * @return an <code>Object[]</code> value
   * @exception IllegalArgumentException neither the set nor it skeleton subset is finite
   */
  public static Object[] getElements(Set set) {
    Object[] es;
    Iterator e;
    if (set.size()==-1) {
      if (set.sizeSkel()==-1)
	throw new IllegalArgumentException(set+" is infinite and not skeletisable: cannot check");
      es = new Object[(int)set.sizeSkel()];
      e=set.iteratorSkel();
    } else {
      es=new Object[(int)set.size()];
      e=set.iterator();
    }
    int count=0;
    
    try {
      while (e.hasNext()) {
	Object elem = (Object)e.next();
	if (count<es.length) es[count]=elem;
	count++;
      }
    } catch (NoSuchElementException ex) {}
    if (count>es.length)
      throw new IllegalArgumentException(set+" violates set properties:\n"+
			  "size()="+es.length+
			  " < "+count+"=number of elements returned by iterator()");
    
    if (count<es.length)
      throw new IllegalArgumentException(set+" violates set properties:\n"+
			  "size()="+es.length+
			  " > "+count+" number of elements returned by iterator()");
    return es;
  }

  
  /**
   * Checks the properties of a set.
   *
   * @param set a <code>Set</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(Set set, Object[] es) {
    String result = "";
    for (int i=0; i<es.length; i++) {
      if (!set.isElement(es[i]))
	result += "- isElement() doesn't recognize element "+es[i]+"\n";
      if (!set.equals(es[i],es[i]))
	result += "- equals() isn't reflexive for element "+es[i]+"\n";
      if (!es[i].equals(es[i]))
	result += "- element.equals() isn't reflexive for element "+es[i]+"\n";
    }
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	if (i==j) continue;
	if (set.equals(es[i],es[j])) {
	  result += "- equals()=true for elements "+es[i]+" and "+es[j]+" supposed to be unequal\n";
	}
	if (set.equals(es[i],es[j])!=set.equals(es[j],es[i])) {
	  result += "- equals() isn't symmetric for elements "+es[i]+", "+es[j]+"\n";
	}
      }
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(set+" violates set properties:\n"+result);
  }

  
  /**
   * Checks the properties of a set.
   *
   * @param set a <code>Set</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(Set set) {
    checkProperties(set,getElements(set));
  }

  
  /**
   * Checks the properties of a partially ordered set.
   *
   * @param poset a <code>POSet</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(POSet poset, Object[] es) {
    checkProperties((Set)poset,es);
    String result="";
    for (int i=0; i<es.length; i++) {
      if (!poset.le(es[i],es[i]))
	result += "- le() isn't reflexive for element "+es[i]+"\n";
      if (poset.lt(es[i],es[i]))
	result += "- lt() is reflexive for element "+es[i]+"\n";
    }
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	if (poset.le(es[i],es[j])!=
	    (poset.lt(es[i],es[j]) || poset.equals(es[i],es[j])))
	  result += "- le() isn't equal to lt()||equal() for elements "+
	    es[i]+", "+es[j]+": "+poset.le(es[i],es[j])+" vs. "+(poset.lt(es[i],es[j]) || poset.equals(es[i],es[j]))+"\n";
				    
	if (i==j) continue;
	if (poset.lt(es[i],es[j]) && poset.lt(es[j],es[i])) {
	  result += "- lt() isn't antisymmetric for elements "+es[i]+", "+es[j]+"\n";
	}
      }
    }
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	for (int k=0; k<es.length; k++) {
	  if (i==j || i==k || j==k) continue;
	  if (poset.lt(es[i],es[j]) && poset.lt(es[j],es[k]) && !poset.lt(es[i],es[k]))
	    result += "- lt() isn't transitive for elements "+
	      es[i]+", "+es[j]+", "+es[k]+"\n";
	}
      }
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(poset+
			  " violates partially ordered set properties:\n"+
			  result);
  }

  
  /**
   * Checks the properties of a partially ordered set.
   *
   * @param poset a <code>POSet</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(POSet poset) {
    checkProperties(poset, getElements(poset));
  }

  
  /**
   * Checks the properties of a complete partially ordered set.
   *
   * @param cposet a <code>CompletePOSet</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(CompletePOSet cposet, Object[] es) {
    checkProperties((POSet)cposet,es);
    String result="";
    for (int i=0; i<es.length; i++) {
      if (!cposet.le(cposet.bottom(),es[i]))
	result +=
	  "- bottom() isn't least element: !le("+cposet.bottom()+","+es[i]+")";
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(cposet+
			  " violates complete partially ordered set properties:\n"+
			  result);
  }
  
  /**
   * Checks the properties of a complete partially ordered set.
   *
   * @param cposet a <code>CompletePOSet</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(CompletePOSet cposet) {
    checkProperties(cposet, getElements(cposet));
  }

  /**
   * Checks the properties of a pre upper semi lattice.
   *
   * @param pusl a <code>PreUpperSemiLattice</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(PreUpperSemiLattice pusl, Object[] es) {
    checkProperties((POSet)pusl,es);
    String result="";
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	Object o1=pusl.join(es[i],es[j]);
	Object o2=pusl.join(es[j],es[i]);
	if (((o1==null || o2==null) && (o1!=o2)) ||
	    (o1!=null && o2!=null && !pusl.equals(o1,o2)))
	  result += "- join() isn't commutative for elements "+
	    es[i]+", "+es[j]+": "+o1+" vs. "+o2+"\n";
      }
    }
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	for (int k=0; k<es.length; k++) {
	  Object o1=pusl.join(es[i],pusl.join(es[j],es[k]));
	  Object o2=pusl.join(pusl.join(es[i],es[j]),es[k]);
	  if (((o1==null || o2==null) && (o1!=o2)) ||
	      (o1!=null && o2!=null && !pusl.equals(o1,o2)))
	    result += "- join() isn't associative for elements "+
	      es[i]+", "+es[j]+", "+es[k]+"\n";
	}
      }
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(pusl+
			  " violates pre upper semi lattice properties:\n"+
			  result);
  }
  
  /**
   * Checks the properties of a pre upper semi lattice.
   *
   * @param pusl a <code>PreUpperSemiLattice</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(PreUpperSemiLattice pusl) {
    checkProperties(pusl, getElements(pusl));
  }
  
  /**
   * Checks the properties of a pre lower semi lattice.
   *
   * @param pusl a <code>PreUpperSemiLattice</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(PreLowerSemiLattice plsl, Object[] es) {
    checkProperties((POSet)plsl,es);
    String result="";
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	Object o1=plsl.meet(es[i],es[j]);
	Object o2=plsl.meet(es[j],es[i]);
	if (((o1==null || o2==null) && (o1!=o2)) ||
	    (o1!=null && o2!=null && !plsl.equals(o1,o2)))
	    result += "- meet() isn't commutative for elements "+
	              es[i]+", "+es[j]+": "+o1+" vs. "+o2+"\n";
      }
    }
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	for (int k=0; k<es.length; k++) {
	  Object o1=plsl.meet(es[i],plsl.meet(es[j],es[k]));
	  Object o2=plsl.meet(plsl.meet(es[i],es[j]),es[k]);
	  if (((o1==null || o2==null) && (o1!=o2)) ||
	      (o1!=null && o2!=null && !plsl.equals(o1,o2)))
	    result += "- meet() isn't associative for elements "+
	      es[i]+", "+es[j]+", "+es[k]+": "+
	      plsl.meet(es[i],plsl.meet(es[j],es[k]))+" vs. "+
	      plsl.meet(plsl.meet(es[i],es[j]),es[k])+"\n";
	}
      }
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(plsl+
			  " violates pre lower semi lattice properties:\n"+
			  result);
  }
  
  /**
   * Checks the properties of a pre lower semi lattice.
   *
   * @param pusl a <code>PreUpperSemiLattice</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(PreLowerSemiLattice plsl) {
    checkProperties(plsl, getElements(plsl));
  }
  
  /**
   * Checks the properties of an upper semi lattice.
   *
   * @param usl an <code>UpperSemiLattice</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(UpperSemiLattice usl, Object[] es) {
    checkProperties((PreUpperSemiLattice)usl,es);
    String result="";
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	if (usl.join(es[i],es[j])==null)
	  result += "- join() must exist for all elements "+es[i]+", "+es[j]+"\n";
      }
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(usl+
			  " violates upper semi lattice properties:\n"+
			  result);
  }
  
  /**
   * Checks the properties of an upper semi lattice.
   *
   * @param usl an <code>UpperSemiLattice</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(UpperSemiLattice usl) {
    checkProperties(usl, getElements(usl));
  }
  
  /**
   * Checks the properties of a lower semi lattice.
   *
   * @param lsl a <code>LowerSemiLattice</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(LowerSemiLattice lsl, Object[] es) {
    checkProperties((POSet)lsl,es);
    String result="";
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	if (lsl.meet(es[i],es[j])==null)
	  result += "- meet() must exist for all elements "+es[i]+", "+es[j]+"\n";
      }
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(lsl+
			  " violates upper semi lattice properties:\n"+
			  result);
  }
  
  /**
   * Checks the properties of a lower semi lattice.
   *
   * @param lsl a <code>LowerSemiLattice</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(LowerSemiLattice lsl) {
    checkProperties(lsl, getElements(lsl));
  }

  /**
   * Checks the properties of a pre lattice.
   *
   * @param l a <code>PreLattice</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(PreLattice l, Object[] es) {
    checkProperties((PreLowerSemiLattice)l,es);
    checkProperties((PreUpperSemiLattice)l,es);
    String result="";
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	Object o1=l.meet(l.join(es[i],es[j]),es[i]);
	Object o2=l.join(es[i],l.meet(es[j],es[i]));
	if (o1!=null && !l.equals(o1,es[i]))
	  result += "- join() and meet() violate absorption law #1 for elements "+
	    es[i]+", "+es[j]+": "+o1+" should be "+es[i]+"\n";
	if (o2!=null && !l.equals(o2,es[i]))
	  result += "- join() and meet() violate absorption law #2 for elements "+
	    es[i]+", "+es[j]+": "+o2+" should be "+es[i]+"\n";
      }
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(l+
			  " violates lattice properties:\n"+
			  result);
  }
  
  /**
   * Checks the properties of a pre lattice.
   *
   * @param l a <code>PreLattice</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(PreLattice l) {
    checkProperties(l, getElements(l));
  }

  
  /**
   * Checks the properties of a lattice.
   *
   * @param l a <code>Lattice</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(Lattice l, Object[] es) {
    checkProperties((LowerSemiLattice)l,es);
    checkProperties((UpperSemiLattice)l,es);
    checkProperties((PreLattice)l,es);
  }
  
  /**
   * Checks the properties of a lattice.
   *
   * @param l a <code>Lattice</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(Lattice l) {
    checkProperties(l, getElements(l));
  }
 
  /**
   * Checks the properties of a complete lattice.
   *
   * @param cl a <code>CompleteLattice</code> value
   * @param es an <code>Object[]</code> the elements of the set.
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(CompleteLattice cl, Object[] es) {
    checkProperties((Lattice)cl,es);
    String result="";
    for (int i=0; i<es.length; i++) {
      if (!cl.le(cl.bottom(),es[i]))
	result +=
	  "- bottom() isn't least element: !le("+cl.bottom()+","+es[i]+")\n";
    }
    for (int i=0; i<es.length; i++) {
      if (!cl.le(es[i],cl.top()))
	result +=
	  "- top() isn't greatest element: !le("+es[i]+","+cl.top()+")\n";
    }
    if (!result.equals(""))
      throw new IllegalArgumentException(cl+
			  " violates complete lattice properties:\n"+
			  result);
  }
  
  /**
   * Checks the properties of a complete lattice.
   *
   * @param cl a <code>CompleteLattice</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static void checkProperties(CompleteLattice cl) {
    checkProperties(cl, getElements(cl));
  }

  /**
   * Creates a string representation of a set by enclosing all elements in
   * braces. This will loop forever for infinite sets.
   *
   * @param set a <code>Set</code> value
   * @return a <code>String</code> value
   */
  public static String setToString(Set set) {
    String result = "{";
    for (Iterator e=set.iterator(); e.hasNext(); ) {
      result+=e.next();
      if (e.hasNext()) result+=", ";
    }
    result += "}";
    return result;
  }

  
  /**
   * Checks if a function is monotonic. This will loop forever if the domain of the
   * function in infinite.
   *
   * @param f a <code>Function</code> value
   * @return a <code>boolean</code> value
   * @exception FunctionException if either domain or range are not partially ordered.
   */
  public static boolean isMonotonic(Function f) throws FunctionException {
    if (!(f.getDomain() instanceof POSet))
      throw new FunctionException("function's domain is not a partially ordered set");
    if (!(f.getRange() instanceof POSet))
      throw new FunctionException("function's range is not a partially ordered set");
    POSet domain = (POSet)f.getDomain();
    POSet range  = (POSet)f.getRange();
    for (Iterator e1=domain.iterator(); e1.hasNext(); ) {
      Object x=(Object)e1.next();
      for (Iterator e2=domain.iterator(); e2.hasNext(); ) {
	Object y=(Object)e2.next();
	if (domain.le(x,y) && !(range.le(f.apply(x), f.apply(y))))
	  return false;
      }
    }
    return true;
  }

  
  /**
   * Computes the covered relation induced by a partial order.
   *
   * @param poset a <code>POSet</code> value: The underlying order.
   * @param es an <code>Object[]</code> value: The array of elements.
   * @param i an <code>int</code> value: The index of the first element.
   * @param j an <code>int</code> value: The index of the second element.
   * @return a <code>boolean</code> value: true iff es[i] is covered by es[j]
   */
  protected static boolean isCovered(POSet poset, Object[] es, int i, int j) {
    if (i==j || !poset.lt(es[i],es[j])) return false;
    for (int k=0; k<es.length; k++) {
      if (i==k || j==k) continue;
      if (poset.lt(es[i],es[k]) && poset.lt(es[k],es[j])) return false;
    }
    return true;
  }
  
  
  /**
   * Computes the Hasse diagram of a partially ordered set.
   *
   * @param poset a <code>POSet</code> value
   * @return a <code>Graph</code> value
   * @exception IllegalArgumentException if an error occurs
   */
  public static Graph hasseDiagram(POSet poset) {
    Graph g = new Graph("Hasse diagram for "+poset);
    Object[] es = Domain.getElements(poset);
    Graph.Node[] nodes = new Graph.Node[es.length];
    
    for (int i=0; i<es.length; i++) {
      nodes[i]=g.new Node(es[i]);
    }
    for (int i=0; i<es.length; i++) {
      for (int j=0; j<es.length; j++) {
	if (isCovered(poset,es,i,j)) {
	  nodes[j].new Edge(nodes[i],"");
	}
      }
    }
    return g;
  }
  
  /*
  static final void main(String[] args) {
    try {
      POSet poset = new de.rwth.domains.templates.LiftedPOSet(new TrivialPOSet(1));
      checkProperties(poset);
      System.err.println(setToString(poset));
      poset = new de.rwth.domains.templates.TrivialPOSet(new int[] {1,2,3, 4, 5, 6, 7});
      checkProperties(poset);
      System.err.println(setToString(poset));
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
    System.exit(0);
  }
  */
}
