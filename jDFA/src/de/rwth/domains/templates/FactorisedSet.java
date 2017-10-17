package de.rwth.domains.templates; // Generated package name

import java.util.Iterator;

import de.rwth.domains.*;

/**
 * Class for the creation of factorised sets, i.e. sets of equivalence classes.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: FactorisedSet.java,v 1.1 2002/09/27 08:31:40 mohnen Exp $
 */
public class FactorisedSet extends SimpleSet implements Set {
  protected Set set;
  protected Factorisation f;
  
  public FactorisedSet(Set set, Factorisation f) {
    super();
    this.set=set;
    this.f=f;
    if (set.size()==-1)
      throw new IllegalArgumentException("Factorisation only for finite sets");
    
    Object[] setElements = Domain.getElements(set);
    int[] representer = new int[setElements.length];
    int[] repcount    = new int[setElements.length];
    int count=0;
    for (int i=0; i<setElements.length; i++) {
      representer[i]=i;
      repcount[i]=0;
      for (int j=0; j<i && representer[i]==i; j++) {
	if (f.equivalent(setElements[i],setElements[j]))
	  representer[i]=j;
      }
      if (representer[i]==i)
	count++;
      repcount[representer[i]]++;
    }
    this.elements=new Object[count];
    count=0;
    for (int i=0; i<setElements.length; i++) {
      if (representer[i]==i) {
	Object[] representees = new Object[repcount[i]];
	int rcount=0;
	for (int j=i; j<setElements.length; j++) {
	  if (representer[j]==i)
	    representees[rcount++]=setElements[j];
	}
	this.elements[count++]=new Element(setElements[i],representees);
      }
    }
  }

  public class Element {
    protected Object e;
    protected Object[] representees;
    public Element(Object e, Object[] representees) {
      this.e=e;
      this.representees=representees;
    }
    public Object getElement() {
      return e;
    }
    public Object[] getRepresentees() {
      return representees;
    }
    public String toString() {
      String result="["+e+"]={";
      for (int i=0; i<representees.length; i++) {
	result+=(i>0?",":"")+representees[i];
      }
      result+="}";
      return result;
    }
  }
  
  public interface Factorisation {
    public boolean equivalent(Object e1, Object e2);
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      Set s = new FactorisedSet(new SimpleSet(0,4), new Factorisation() {
	  public boolean equivalent(Object e1, Object e2) {
	    return ((Integer)e1).intValue()%3==((Integer)e2).intValue()%3;
	  }
	});
      Domain.checkProperties(s);
      System.out.print("{");
      for (Iterator i=s.iterator(); i.hasNext();) {
	System.out.print(i.next()+" ");
      }
      System.out.println("}");
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
}
