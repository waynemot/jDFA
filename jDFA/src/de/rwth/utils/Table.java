package de.rwth.utils; // Generated package name

/**
 * Table.java
 *
 * Created: Tue May 15 16:14:46 2001
 *
 * @author Markus Mohnen
 * @version
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

/**
 * A class for creating tables consisting of rows and columns. Each column has a
 * field name associated with it and each row has a key name associated with it. The
 * resulting tables can be converted to a String which is suitable for CSV (comma
 * separated values) files.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Table.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class Table {
  /**
   * The field name of the key column.
   * 
   */
  protected String keyname = null;

  
  /**
   * The list of field names.
   *
   */
  protected LinkedList fieldnames = null;

  
  /**
   * Stores the rows of the table. Each row hashes the key name of the row to a
   * <code>HashMap</code> of column entries, which in turn hash a field name to the
   * value in the cell.
   *
   */
  protected HashMap keys = null;

  /**
   * The record separator char for the conversion to String.
   *
   */
  protected char recordsep = '\n';

  /**
   * The column separator char for the conversion to String.
   *
   */
  protected char columnsep = ',';

  /**
   * The quote char for the conversion to String.
   *
   */
  protected char quotechar = '"';

  /**
   * Creates a new <code>Table</code> instance with default values for record
   * separator (<code>\n</code>), column separator (<code>,</code>), and quote
   * character (<code>"</code>).
   *
   * @param keyname a <code>String</code> value with the name of the key column.
   */
  public Table(String keyname) {
    super();
    this.keyname = keyname;
    this.fieldnames = new LinkedList();
    this.keys = new HashMap();
  }
  
  /**
   * Checks if a <code>String</code> is a valid field name in this table.
   *
   * @param fieldname a <code>String</code> value: The name to check.
   * @return a <code>boolean</code> value: <code>true</code> if and only if the name
   * was the argument to the constructor of this table, or the argument of one of the
   * subsequent calls to <code>addNewFieldName</code>.
   */
  public boolean hasFieldName(String fieldname) {
    return (fieldnames.contains(fieldname) || keyname.equals(fieldname));
  }

  
  /**
   * Creates a new column in this table. 
   *
   * @param fieldname a <code>String</code> value
   * @exception IllegalArgumentException if the <code>fieldname</code> is already one
   * of the field names or the name of the key column.
   */
  public void addNewFieldName(String fieldname) throws IllegalArgumentException {
    if (fieldnames.contains(fieldname))
      throw new IllegalArgumentException("Table already contains field name "+fieldname);
    if (keyname.equals(fieldname))
      throw new IllegalArgumentException("Table already contains field name "+fieldname+" as key");
    fieldnames.add(fieldname);
  }
 
  /**
   * Creates a new row in this table.
   *
   * @param key a <code>String</code> value
   * @exception IllegalArgumentException if there is already a row with
   * <code>key</code>.
   */
  public void createNewKey(String key) throws IllegalArgumentException {
    if (keys.containsKey(key))
      throw new IllegalArgumentException("Table already contains a key "+key);
    keys.put(key, new HashMap());
  }
  
  /**
   * Sets the value of a cell for a specific row/column.
   *
   * @param fieldname a <code>String</code> value: The column of the cell.
   * @param key a <code>String</code> value: The row of the cell.
   * @param value an <code>Object</code> value
   * @exception IllegalArgumentException if either row or column do not exist or if
   * there is already a value for this cell.
   */
  public void setFieldForKey(String fieldname, String key, Object value)
    throws IllegalArgumentException {    
    if (!fieldnames.contains(fieldname))
      throw new IllegalArgumentException("Table does not have a field name "+fieldname);
    if (!keys.containsKey(key))
      throw new IllegalArgumentException("Table does not contains a key "+key);
    HashMap values = (HashMap)keys.get(key);
    if (values.containsKey(fieldname))
      throw new IllegalArgumentException("Already a value for field "+fieldname+" and key "+key);
    values.put(fieldname, value);
    keys.put(key, values);
  }

  /**
   * Gets the value of a cell for a specific row/column.
   *
   * @param fieldname a <code>String</code> value: The column of the cell.
   * @param key a <code>String</code> value: The row of the cell.
   * @return an <code>Object</code> value: The value
   * @exception IllegalArgumentException if either row or column do not exist or if
   * there is no value associated with this cell.
   */
  public Object getFieldForKey(String fieldname, String key) {
    if (!fieldnames.contains(fieldname))
      throw new IllegalArgumentException("Table does not have a field name "+fieldname);
    if (!keys.containsKey(key))
      throw new IllegalArgumentException("Table does not contains a key "+key);
    HashMap values = (HashMap)keys.get(key);
    if (!values.containsKey(fieldname))
      throw new IllegalArgumentException("No value for field "+fieldname+" and key "+key);
    return ((HashMap)keys.get(key)).get(fieldname);
  }

  
  /**
   * Returns the maybe quoted String representation of its argument.
   *
   * @param o an <code>Object</code> value
   * @return a <code>String</code> value
   */
  protected String maybequote(Object o) {
    String result = o.toString();
    
    if (result.indexOf(columnsep)!=-1 ||
	result.startsWith(" ") || result.endsWith(" ")) {
      int i=result.indexOf(quotechar);
      if (i!=-1) {
	String oldresult=result;
	result="";
	do{
	  result+=oldresult.substring(0,i-1)+quotechar+quotechar;
	  oldresult=oldresult.substring(i+1);
	  i=oldresult.indexOf(quotechar);
	} while (i!=-1);
	result+=oldresult;
      }
      result=quotechar+result+quotechar;
    }
    return result;
  }
  
  /**
   * Creates a <code>String</code> representation of this table. Rows are separated
   * with the record separator and columns with a column separator. The first row
   * contains the field names. In each row, the first entry is the key of the row,
   * followed by string representations of the values obtained by
   * <code>toString()</code>. The order of rows and keys is undetermined. If the
   * string representation of a value contains the column separator or blanks at the
   * beginning or end, it is enclosed in quotes and quotes inside are doubled.
   *
   * @return a <code>String</code> value
   */
  public String toString() {
    String result = maybequote(keyname);
    for (Iterator e=fieldnames.iterator(); e.hasNext(); ) {
      result+=columnsep+maybequote(e.next());
    }
    result+=recordsep;
    for (Iterator ke=keys.entrySet().iterator(); ke.hasNext(); ) {
      Map.Entry entry = (Map.Entry)ke.next();
      String key = (String)entry.getKey(); 
      HashMap valuesforkey = (HashMap)entry.getValue();
      result+=maybequote(key);
      for (Iterator e=fieldnames.iterator(); e.hasNext(); ) {
	String field = (String)e.next();
	if (!valuesforkey.containsKey(field))
	  throw new IllegalArgumentException("No value for field "+field+" and key "+key);
	result+=columnsep+maybequote(valuesforkey.get(field));
      }
      result+=recordsep;
    }
    return result;
  }

  
  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    System.out.println("Test Table starting");

    Table table = new Table("key");
    
    table.addNewFieldName("field1");
    table.addNewFieldName("field2");

    table.createNewKey(" \"k1");
    table.createNewKey("k2,");
    
    table.setFieldForKey("field1"," \"k1",new Integer(11));
    table.setFieldForKey("field1","k2,",new Integer(12));
    table.setFieldForKey("field2"," \"k1",new Integer(21));
    table.setFieldForKey("field2","k2,",new Integer(22));
    
    System.out.println("field1 for \"k1="+table.getFieldForKey("field1"," \"k1"));
    
    System.out.println(table);
    
    System.out.println("Test Table done.");
  }
} // Table
