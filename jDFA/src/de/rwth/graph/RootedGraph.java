package de.rwth.graph; // Generated package name

import java.util.Iterator;
import java.util.HashSet;

/**
 * This class represents a graph with roots and leafs.
 *
 * @version $Id: RootedGraph.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @author Mario Jou&szlig;en &lt;joussen@i2.informatik.rwth-aachen.de&gt;
 */
public class RootedGraph extends Graph {
  /**
   * The set of root nodes.
   */
  protected HashSet roots = new HashSet();
  /**
   * The set of leaf nodes.
   */
  protected HashSet leafs = new HashSet();
  
  /**
   * Creates a new <code>RootedGraph</code> without a label.
   */
  public RootedGraph() {
    super();
  }
  
  /**
   * Creates a new <code>RootedGraph</code> with a label.
   *
   * @param label the label of the graph.
   */
  public RootedGraph(Object label) {
    super(label);
  }
  
  /**
   * Adds a node as one of the roots of this graph.
   *
   * @param node the node which should be added. It must be a node in this graph.
   * @exception IllegalArgumentException if the node is not in this graph.
   */
  public void addRoot(Graph.Node node) throws IllegalArgumentException{
    if (!nodes.contains(node))
      throw new IllegalArgumentException("trying to add an alien node");
    roots.add(node);
  }
  
  /**
   * Adds a node as one of the leafs of this graph.
   *
   * @param node the node which should be added. It must be a node in this graph.
   * @exception IllegalArgumentException if the node is not in this graph.
   */
  public void addLeaf(Graph.Node node) throws IllegalArgumentException {
    if (!nodes.contains(node))
      throw new IllegalArgumentException("trying to add an alien node");
    leafs.add(node);
  }
  
  /**
   * Removes a node from the set of root nodes.
   *
   * @param node the node which should be removed.
   */
  public void removeRoot(Graph.Node node) {
    roots.remove(node);
  }
  
  /**
   * Removes a node from the set of leaf nodes.
   *
   * @param node the node which should be removed.
   */
  public void removeLeaf(Graph.Node node) {
    leafs.remove(node);
  }
  
  /**
   * Returns an iterator of the set of root nodes.
   *
   * @return an iterator of the set of root nodes.
   */
  public Iterator getRoots() {
    return roots.iterator();
  }
  
  /**
   * Returns an iterator of the set of leaf nodes.
   *
   * @return an iterator of the set of leaf nodes.
   */
  public Iterator getLeafs() {
    return leafs.iterator();
  }
  
  /**
   * Returns an array of all root nodes.
   *
   * @return an array of all root nodes.
   */
  public Graph.Node[] getRootArray() {
    return (Graph.Node[])roots.toArray(new Graph.Node[roots.size()]);
  }
  
  /**
   * Returns an array of all leaf nodes.
   *
   * @return an array of all leaf nodes.
   */
  public Graph.Node[] getLeafArray() {
    return (Graph.Node[])leafs.toArray(new Graph.Node[leafs.size()]);
  }
  
  /**
   * Checks if the node is a root node.
   *
   * @return <code>true</code> if <code>node</code> is a root node;
   *     <code>false</code> otherwise.
   */
  public boolean isRoot(Graph.Node node) {
    return roots.contains(node);
  }
  
  /**
   * Checks if the node is a leaf node.
   *
   * @return <code>true</code> if <code>node</code> is a leaf node;
   *     <code>false</code> otherwise.
   */
  public boolean isLeaf(Graph.Node node) {
    return leafs.contains(node);
  }
  
  /**
   * This class represents a node in a {@link RootedGraph}.
   */
  public class Node extends Graph.Node
  {
    /**
     * Creates a new <code>Node</code> without a label. 
     */
    public Node() { super(); }
    
    /**
     * Creates a new <code>Node</code> with a label. 
     */
    public Node(Object label) { super(label); }

    /**
     * Removes this <code>Edge</code> from the graph.
     */
    public void remove() {
      roots.remove(this);
      leafs.remove(this);
      super.remove();
    }
    
    /**
     * Checks if this node is a root node.
     *
     * @return <code>true</code> if <code>node</code> is a root node;
     *     <code>false</code> otherwise.
     */
    public boolean isRoot() {
      return RootedGraph.this.isRoot(this);
    }
    
    /**
     * Checks if this node is a leaf node.
     *
     * @return <code>true</code> if <code>node</code> is a leaf node;
     *     <code>false</code> otherwise.
     */
    public boolean isLeaf() {
      return RootedGraph.this.isLeaf(this);
    }
  }
}
