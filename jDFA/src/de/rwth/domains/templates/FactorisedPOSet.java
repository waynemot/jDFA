package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.domains.*;

/**
 * Class for the creation of factorised partially ordered sets,
 * i.e. partially ordered sets of equivalence classes.
 * 
 * <p>
 *
 * <strong>The method <code>equivalent()</code> must be compatible with the ordering! 
 * There is not check for this in this class!  </strong>
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FactorisedPOSet.java,v 1.1 2002/09/27 08:31:40 mohnen Exp $
 */
public class FactorisedPOSet extends FactorisedSet implements POSet {
  public FactorisedPOSet(POSet poset, Factorisation f) {
    super(poset,f);
  }

  public boolean lt(Object o1, Object o2) {
    if (equals(o1,o2)) return false;
    Object[] r1=((Element)o1).getRepresentees();
    Object[] r2=((Element)o2).getRepresentees();
    POSet poset=(POSet)set; 
    for (int i=0; i<r1.length; i++) {
      for (int j=0; j<r2.length; j++) {
	if (poset.lt(r1[i],r2[j])) {
	  return true;
	}
      }
    }
    return false;
  }
  
  public boolean le(Object o1, Object o2) {
    if (equals(o1,o2)) return true;
    Object[] r1=((Element)o1).getRepresentees();
    Object[] r2=((Element)o2).getRepresentees();
    POSet poset=(POSet)set; 
    for (int i=0; i<r1.length; i++) {
      for (int j=0; j<r2.length; j++) {
	if (poset.le(r1[i],r2[j])) {
	  return true;
	}
      }
    }
    return false;
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      POSet s1 = new TuplePOSet(new IntegerPOSet(0,2),
				new IntegerPOSet(0,2));
      POSet s = new FactorisedPOSet(s1,
				    new Factorisation() {
        public boolean equivalent(Object e1, Object e2) {
	  Object[] t1 = ((TupleElement)e1).getElements();
	  Object[] t2 = ((TupleElement)e2).getElements();
	  return
	    (((Integer)t1[0]).intValue()+((Integer)t1[1]).intValue())>2
	    &&
	    (((Integer)t2[0]).intValue()+((Integer)t2[1]).intValue())>2;
	}
      });
      Domain.checkProperties(s);
      System.err.println(Domain.hasseDiagram(s1));
      System.err.println(Domain.hasseDiagram(s));
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }

}
