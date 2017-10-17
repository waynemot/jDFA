package de.rwth.utils; // Generated package name

import java.util.Arrays;

/**
 * A collection of useful methods for arrays.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: ArrayTools.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class ArrayTools {
  /**
   * Fills an array and returns the array.
   * 
   * @param a an <code>Object[]</code> value
   * @param v an <code>Object</code> value
   * @return an <code>Object[]</code> value
   * 
   * @see java.util.Arrays#fill(Object[],Object)
   */
  public static Object[] fill(Object[] a, Object v) {
    Arrays.fill(a,v);
    return a;
  }

  
  /**
   * Creates a new array with those elements which are in the array <code>a</code>,
   * but not in the array <code>b</code>. 
   *
   * @param a an <code>int[]</code> value
   * @param b an <code>int[]</code> value
   * @return an <code>int[]</code> value
   */
  public static int[] minus(int[] a, int[] b) {
    int miss=0;
    int[] help=new int[a.length];
    for (int i=0; i<a.length; i++) {
      if (Arrays.binarySearch(b,a[i])>=0) {
	miss++;
      } else {
	help[i-miss]=a[i];
      }
    }
    int[] result=new int[a.length-miss];
    System.arraycopy(help,0,result,0,a.length-miss);
    //printArray(a);System.out.print("-");
    //printArray(b);System.out.print("=");
    //printArray(result);System.out.println();
    return result;
  }
  
  /**
   * Creates a new array with those elements which are either in the array
   * <code>a</code> or the array <code>b</code>.
   *
   * @param a an <code>int[]</code> value
   * @param b an <code>int[]</code> value
   * @return an <code>int[]</code> value
   */
  public static int[] union(int[] a, int[] b) {
    int miss=0;
    int[] help=new int[a.length+b.length];
    System.arraycopy(a,0,help,0,a.length);
    for (int i=0; i<b.length; i++) {
      if (Arrays.binarySearch(a,b[i])>=0) {
	miss++;
      } else {
	help[a.length+i-miss]=b[i];
      }
    }
    int[] result=new int[a.length+b.length-miss];
    System.arraycopy(help,0,result,0,a.length+b.length-miss);
    //printArray(a);System.out.print("u");
    //printArray(b);System.out.print("=");
    //printArray(result);System.out.println();
    return result;
  }

  
  /**
   * Prints an array on <code>System.out</code>.
   *
   * @param a an <code>int[]</code> value
   */
  public static void printArray(int[] a) {
    System.out.print("[");
    for (int i=0; i<a.length; i++) {
      if (i>0) System.out.print(",");
      System.out.print(a[i]);
    }
    System.out.print("]");
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    //int[] a = new int[] {};
    //int[] b = new int[] {3,3,5};
    int[] a = new int[] {1,2,3};
    int[] b = new int[] {3,3,5};
    //printArray(a);System.out.println();
    //printArray(b);System.out.println();
    printArray(minus(a,b));System.out.println();
    minus(a,b);
    union(a,b);
  }
} // class ArrayTools
