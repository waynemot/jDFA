package de.rwth.graph; // Generated package name

import java.util.LinkedList;
import java.util.Iterator;

/**
 * Class for graph data structures consisting of nodes connected with edges. Nodes
 * and edges can be labelled with arbitrary <code>Object</code>s.
 *
 * <p>
 *
 * This is just a class for <b>data structures</b>! There is no integrated creation,
 * editing, or visualization of graphs.
 *
 * <p>
 *
 * However, graphs can be visualized by using an external program: <a
   href="http://www.research.att.com/sw/tools/graphviz/">dotty</a>.
 *
 * @version $Id: Graph.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @author Mario Jou&szlig;en &lt;joussen@i2.informatik.rwth-aachen.de&gt;
 */
public class Graph extends Object {
  /**
   * Contains all nodes of this graph. All Elements are of type <code>Node</code>.
   */
  protected LinkedList nodes = new LinkedList();

  /**
   * The label of this graph.
   */
  protected Object label;
    
  /**
   * The number of the last node allocated in this graph.
   */
  protected int curNumber = 0;
  
  /**
   * Creates an empty graph without label. 
   */
  public Graph() {
    super();
    setLabel((Object)super.toString());
  }
  
  /**
   * Creates an empty graph with label. 
   */
  public Graph(Object label) {
    this();
    setLabel(label);
  }

  /**
   * Represents nodes in this graph.
   */
  public class Node {
    /**
     * Contains all nodes to which an edge starting in this node goes. All Elements
     * are of type <code>Edge</code>.
     */
    protected LinkedList outEdges = new LinkedList();
    
    /**
     * Contains all edges ending in this node. All Elements are of type
     * <code>Edge</code>.
     */
    protected LinkedList inEdges = new LinkedList();

    /**
     * Gets the containing graph.
     *
     * @return The graph.
     */
    public Graph getGraph() { return Graph.this; }
    
    /**
     * Represents edges starting or ending in this node.
     */
    public class Edge {
      /**
       * The start node of this edge.
       */
      protected Node startNode;
      
      /**
       * The end node of this edge.
       */
      protected Node endNode;
      
      /**
       * The label of this edge.
       */
      protected Object label;

      /**
       * Creates a new edge without label staring at this node. 
       *
       * @param endNode The end node of the edge. It must be in the same graph.
       * @exception IllegalArgumentException if endNode is not a node in the
       *   same graph.
       */
      public Edge(Node endNode) {
	super();
	if (getGraph()!=endNode.getGraph())
	  throw new IllegalArgumentException("trying to end an edge in an " +
                                             "alien graph");
	this.startNode=Node.this;
	this.endNode=endNode;
	outEdges.add(this);
	endNode.inEdges.add(this);
	setLabel((Object)super.toString());
	getGraph().sizeEdges++;
      }

      /**
       * Creates a new edge with label starting at this node.
       *
       * @param endNode The end node of the edge.
       * @param label The label of the edge.
       * @exception IllegalArgumentException if endNode is not a node in the
       *   same graph.
       */
      public Edge(Node endNode, Object label) {
	this(endNode);
	setLabel(label);
      }

      /**
       * Sets the label of this edge.
       */
      public void setLabel(Object label) { this.label=label; }
      
      /**
       * Gets the label of this edge.
       *
       * @return The <code>Object</code> which is the label of this edge.
       */
      public Object getLabel() { return label;}

      /**
       * Gets the starting <code>Node</code> of this edge.
       *
       * @return The <code>Node</code> where the edge starts.
       */
      public Node getStart() { return startNode; }

      /**
       * Sets the starting <code>Node</code> of this edge.
       * @param node the new starting <code>Node</code>.
       * @exception IllegalArgumentException if node is not a node in the
       *   same graph.
       */
      public void setStart(Node node) {
        if (endNode.getGraph() != node.getGraph())
          throw new IllegalArgumentException("trying to start an edge " +
                                             "in an alien graph");
        startNode.outEdges.remove(this);
        startNode = node;
        startNode.outEdges.add(this);
      }

      /**
       * Gets the ending <code>Node</code> of this edge.
       *
       * @return The <code>Node</code> where the edge ends.
       */
      public Node getEnd() { return endNode; }
      
      /**
       * Sets the ending <code>Node</code> of this edge.
       * @param node the new ending <code>Node</code>.
       * @exception IllegalArgumentException if node is not a node in the
       *   same graph.
       */
      public void setEnd(Node node) {
        if (startNode.getGraph() != node.getGraph())
          throw new IllegalArgumentException("trying to end an edge " +
                                             "in an alien graph");
        endNode.inEdges.remove(this);
        endNode = node;
        endNode.inEdges.add(this);
      }

      /**
       * Removes this <code>Edge</code> from the graph.
       */
      public void remove() {
        startNode.outEdges.remove(this);
        endNode.inEdges.remove(this);
        getGraph().sizeEdges--;
      }
    }

    /**
     * The unique number of this node in the graph.
     */
    protected int number = curNumber++;

    /**
     * The label of this node.
     */
    protected Object label;

    
    /**
     * Additional <a href="http://www.research.att.com/sw/tools/graphviz/">dotty</a>
     * attributes used by the <code>toString</code> method.
     */
    protected String attributes;

    /**
     * Creates a new node without label or additional attributes in the graph.
     */
    public Node() {
      super();
      nodes.add(this);
      setLabel((Object)super.toString());
      setAttributes("");
    }
    
    /**
     * Creates a new node with label but without additional attributes in the graph.
     *
     * @param label The label of the node.
     */
    public Node(Object label) {
      this();
      setLabel(label);
    }

    /**
     * Creates a new node with label and attributes in the graph.
     *
     * @param label The label of the node.
     * @param attributes The attributes of the node
     */
    public Node(Object label, String attributes) {
      this(label);
      setAttributes("");
    }

    /**
     * Sets the label of this node.
     */
    public void setLabel(Object label) { this.label=label;}
    
    /**
     * Gets the label of this node.
     *
     * @return The <code>Object</code> which is the label of this node.
     */
    public Object getLabel() { return label;}

    
    /**
     * Sets the attributes of this node.
     *
     * @param attributes a <code>String</code> value
     */
    public void setAttributes(String attributes) {
      this.attributes=attributes;
    }
    
    /**
     * Gets the attributes of this node.
     *
     * @return The <code>String</code> which are the attributes.
     */
    public String getAttributes() {
      return this.attributes;
    }
  
    /**
     * Gets the number of edges with this node as target.
     *
     * @return The number of edges.
     */
    public int getInDegree()  { return inEdges.size(); }
    
    /**
     * Gets the number of edges with this node as source.
     *
     * @return The number of edges.
     */
    public int getOutDegree() { return outEdges.size(); }

    /**
     * Gets all incoming edges of this node.
     *
     * @return An <code>Iterator</code> consisting of incoming <code>Edges</code>.
     */
    public Iterator getInEdges() {
      class Itr implements Iterator
      {
        Iterator iterator;
        Edge edge;

        public Itr() {
          iterator = inEdges.iterator();
        }

        public boolean hasNext() { return iterator.hasNext(); }

        public Object next() {
          edge = (Edge)iterator.next();
          return edge;
        }

        public void remove() {
          edge.startNode.outEdges.remove(edge);
          getGraph().sizeEdges--;
          iterator.remove();
        }
      }
      return new Itr();
    }
    
    /**
     * Gets all outgoing edges of this node.
     *
     * @return An <code>Iterator</code> consisting of outgoing <code>Edges</code>.
     */
    public Iterator getOutEdges() {
      class Itr implements Iterator
      {
        Iterator iterator;
        Edge edge;

        public Itr() {
          iterator = outEdges.iterator();
        }

        public boolean hasNext() { return iterator.hasNext(); }

        public Object next() {
          edge = (Edge)iterator.next();
          return edge;
        }

        public void remove() {
          edge.endNode.inEdges.remove(edge);
          getGraph().sizeEdges--;
          iterator.remove();
        }
      }
      return new Itr();
    }

    /**
     * Returns an array of all incoming edges of this node.
     *
     * @return an array of all incoming edges.
     */
    public Edge[] getInArray() {
      return (Edge[])inEdges.toArray(new Edge[inEdges.size()]);
    }

    /**
     * Returns an array of all outgoing edges of this node.
     *
     * @return an array of all outgoing edges.
     */
    public Edge[] getOutArray() {
      return (Edge[])outEdges.toArray(new Edge[outEdges.size()]);
    }

    /**
     * Bends all incoming edges of this node to <code>node</code>.
     *
     * @exception IllegalArgumentException if <code>node</code> is not a node
     *   in the same graph.
     */
    public void bendInEdges(Node node) {
      if (getGraph() != node.getGraph())
        throw new IllegalArgumentException("trying to bend to a node " +
                                           "in an alien graph");
      Object[] edges = inEdges.toArray();
      for (int i = 0; i < edges.length; i++)
        ((Edge)edges[i]).setEnd(node);
    }

    /**
     * Bends all outgoing edges of this node to <code>node</code>.
     *
     * @exception IllegalArgumentException if <code>node</code> is not a node
     *   in the same graph.
     */
    public void bendOutEdges(Node node) {
      if (getGraph() != node.getGraph())
        throw new IllegalArgumentException("trying to bend to a node " +
                                           "in an alien graph");
      Object[] edges = outEdges.toArray();
      for (int i = 0; i < edges.length; i++)
        ((Edge)edges[i]).setStart(node);
    }

    /**
     * Removes this node from the graph.
     */
    public void remove() {
      for (Iterator edges = getInEdges(); edges.hasNext(); )
        { edges.next(); edges.remove(); }
      for (Iterator edges = getOutEdges(); edges.hasNext(); )
        { edges.next(); edges.remove(); }
      nodes.remove(this);
    }

    /**
     * Merges this <code>Node</code> with the given node, i.e. all edges of
     * <code>node</code> are bended to this node and <code>node</code> is
     * removed.
     *
     * @param node a node.
     * @exception IllegalArgumentException if <code>node</code> is not a node
     *   in the same graph.
     */
    public void merge(Node node) {
      if (getGraph() != node.getGraph())
        throw new IllegalArgumentException("trying to merge with a node " +
                                           "in an alien graph");
      node.bendInEdges(this);
      node.bendOutEdges(this);
      node.remove();
    }
  }

  /**
   * Gets the number of nodes in this graph.
   *
   * @return Number of nodes.
   */
  public int sizeNodes() { return nodes.size(); }


  protected int sizeEdges=0;
  /**
   * Gets the number of edges in this graph.
   *
   * @return Number of edges.
   */
  public int sizeEdges() { return sizeEdges; }

  /**
   * Gets all nodes in this graph.
   *
   * @return An <code>Iterator</code> with all nodes of this graph;
   */
  public Iterator getNodes() {
    class Itr implements Iterator
    {
      Iterator iterator;
      Node node;

      public Itr() {
        iterator = nodes.iterator();
      }

      public boolean hasNext() { return iterator.hasNext(); }

      public Object next() {
        node = (Node)iterator.next();
        return node;
      }

      public void remove() {
        for (Iterator edges = node.getInEdges(); edges.hasNext();)
          { edges.next(); edges.remove(); }
        for (Iterator edges = node.getOutEdges(); edges.hasNext();)
          { edges.next(); edges.remove(); }
        iterator.remove();
      }
    }
    return new Itr();
  }
  
  /**
   *  Sets the label of this graph.
   */
  public void setLabel(Object label) { this.label=label;}

  /**
   * Gets the label of this graph.
   *
   * @return The <code>Object</code> which is the label of this graph.
   */
  public Object getLabel() { return label;}

  /**
   * Represents a colouring of this graph's nodes. Nodes added after creation of a
   * <code>Dyer</code> can not be used in the <code>NodeDyer</code>.
   */
  public class NodeDyer {
    /**
     * An array with the color for each node in the graph.
     */
    protected Object[] colors = new Object[curNumber];
    
    /**
     * Creates a new colouring.
     */
    public NodeDyer() { super(); }

    /**
     * Sets the colour of a node.
     *
     * @param node The <code>Node</code> which color is to be set.
     * @param color The <code>Object</code> which is the color of this node.
     * @exception IllegalArgumentException if a node of an alien graph is
     *   passed as argument.
     */
    public void setColor(Node node, Object color) {
      if (Graph.this!=node.getGraph())
	throw new IllegalArgumentException("trying to set the color of a " +
                                           "node of an alien graph");
      colors[node.number]=color;
    }
    
    /**
     * Gets the color of a node.
     *
     * @param <code>node</code> The <code>Node</code> which color is wanted.
     * @return The color of this node or <code>null</code> if setColor was not
     *         called for this node.
     * @exception IllegalArgumentException if a node of an alien graph is
     *   passed as argument.
     */
    public Object getColor(Node node) {
      if (Graph.this!=node.getGraph())
	throw new IllegalArgumentException("trying to get the color of a " +
                                           "node of an alien graph");
      return colors[node.number];
    }

    /**
     * Creates a string representation of this NodeDyer. It is suited for layouting
     * the graph using <a
     * href="http://www.research.att.com/sw/tools/graphviz/">dotty</a>.  The shape of
     * this nodes is <code>box</code>.
     *
     * @return dotty compatible String representation
     */
    public String toString() {
      String result = "digraph \""+dottyfy(Graph.this.label)+"\" {\n  label=\""+dottyfy(Graph.this.label)+"\"\n";
      for (Iterator ns=Graph.this.nodes.listIterator(); ns.hasNext(); ) {
	Node node = (Node)ns.next();
	try {
	  result+="  n"+node.number+" [shape=box,label=\""+dottyfy(this.getColor(node))+" "+dottyfy(node.getLabel())+"\"";
	  result+=node.attributes;
	  result+="];\n";
	} catch (Exception ex) {
	  throw new Error(ex.toString());
	}
	for (Iterator es=node.outEdges.listIterator(); es.hasNext(); ) {
	  Node.Edge edge= (Node.Edge)es.next();
	  result+="  n"+node.number+" -> n"+
	    edge.endNode.number+" [label=\""+dottyfy(edge.label)+"\"];\n";
	}
      }
      result+="}\n";
      return result;
    }
  }
  
  /**
   * Creates a <a href="http://www.research.att.com/sw/tools/graphviz/">dotty</a>
   * compatible string representation of an object.
   *
   * @param s An <code>Object</code> to be translated.
   * @return Translated string.
   */
  static String dottyfy(Object o) {
    if (o==null) return "null";
    String s=o.toString();
    String result = "";
    int i = s.indexOf("\n");
    while (i!=-1) {
      result += s.substring(0,i)+"\\l";
      s=s.substring(i+1);
      i = s.indexOf("\n");
    }
    result +=s;
    return result;
  }

  /**
   * Creates a string representation of this graph. It is suited for layouting the
   * graph using <a href="http://www.research.att.com/sw/tools/graphviz/">dotty</a>.
   *
   * @param shape A <code>String</code> for the shape of this nodes
   * @return dotty compatible String representation
   */

  public String toString(String shape) {
    String result = "digraph \""+dottyfy(label)+"\" {\n  label=\""+dottyfy(label)+"\"\n";
    for (Iterator ns=nodes.listIterator(); ns.hasNext(); ) {
      Node node = (Node)ns.next();
      result+="  n"+node.number+" [shape="+shape+",label=\""+dottyfy(node.label)+"\"";
      result+=node.attributes;
      result+="];\n";
      for (Iterator es=node.outEdges.listIterator(); es.hasNext(); ) {
	Node.Edge edge= (Node.Edge)es.next();
	result+="  n"+node.number+" -> n"+
	  edge.endNode.number+" [label=\""+dottyfy(edge.label)+"\"];\n";
      }
    }
    result+="}\n";
    return result;
  }

  /**
   * Creates a string representation of this graph. It is suited for layouting the
   * graph using <a href="http://www.research.att.com/sw/tools/graphviz/">dotty</a>.
   * The shape of the nodes is <code>box</code>.
   *
   * @return dotty compatible String representation
   */
  
  public String toString() { return toString("box"); }

  /**
   * Mini test environment.
   *
   * @param args a value of type <code>String[]</code>
   */
  
  public static void main(String[] args) {
    try {
      Graph g = new Graph("v3");
      Node[] n = new Node[] {g.new Node("1"), g.new Node("2"), g.new Node("3")};

      for (int i=0; i<n.length; i++) {
	for (int j=0; j<n.length; j++) {
	  if (i!=j)
	    n[i].new Edge(n[j],"e"+i+j);
	}
      }
      System.out.println(g.sizeEdges());
      n[0].setAttributes(",color=blue");
      System.out.println(g);
    }
    catch (Exception e) {
      System.err.println("exception "+e);
      e.printStackTrace(System.err);
    }
  }
}
