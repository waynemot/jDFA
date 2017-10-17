package de.rwth.graph; // Generated package name

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class transforms a given {@link RootedGraph} into a
 * <code>RootedGraph</code> where the nodes becomes edges and vice versa.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @author Mario Jou&szlig;en &lt;joussen@i2.informatik.rwth-aachen.de&gt;
 * @version $Id: SwappedGraph.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class SwappedGraph extends RootedGraph
{
    /**
     * The original graph.
     */
    private RootedGraph graph;
    /**
     * The mapping of the original edges and nodes to the new nodes and edges.
     */
    private HashMap mapping;

    /**
     * Constructs a new <code>SwappedGraph</code> by transforming the given
     * <code>graph</code>.
     *
     * @param graph the graph which should be transformed.
     */
    public SwappedGraph(RootedGraph graph) {
        super(graph.getLabel());
        Node node, newNode;
        Node.Edge edge, inEdge, outEdge, newEdge;
        this.graph = graph;
        mapping = new HashMap();
        // Transforms the edges into nodes.
        for (Iterator nodes = graph.getNodes(); nodes.hasNext(); ) {
            node = (Node)nodes.next();
            for (Iterator edges = node.getOutEdges(); edges.hasNext(); ) {
                edge = (Node.Edge)edges.next();
                newNode = this.new Node(edge.getLabel());
                mapping.put(edge, newNode);
            }
        }
        // Transforms the nodes into edges.
        for (Iterator nodes = graph.getNodes(); nodes.hasNext(); ) {
            node = (Node)nodes.next();
            if (graph.isRoot(node) && !graph.isLeaf(node)) {
                newNode = this.new Node(null);
                this.addRoot(newNode);
                for (Iterator outEdges = node.getOutEdges();
                     outEdges.hasNext(); ) {
                    newEdge = newNode.
                        new Edge((Node)mapping.get(outEdges.next()),
                                 node.getLabel());
                    mapping.put(node, newEdge);
                }
            }
            if (graph.isLeaf(node) && !graph.isRoot(node)) {
                newNode = this.new Node(null);
                this.addLeaf(newNode);
                for (Iterator inEdges = node.getInEdges();
                     inEdges.hasNext(); ) {
                    newEdge = ((Node)mapping.get(inEdges.next())).
                        new Edge(newNode, node.getLabel());
                    mapping.put(node, newEdge);
                }
            }
            if (!graph.isRoot(node) && !graph.isLeaf(node))
                for (Iterator inEdges = node.getInEdges();
                     inEdges.hasNext(); ) {
                    inEdge = (Node.Edge)inEdges.next();
                    for (Iterator outEdges = node.getOutEdges();
                         outEdges.hasNext(); ) {
                        outEdge = (Node.Edge)outEdges.next();
                        newEdge = ((Node)mapping.get(inEdge)).
                          new Edge((Node)mapping.get(outEdge),node.getLabel());
                        mapping.put(node, newEdge);
                }
            }
        }
    }

    /**
     * Copies the labels of the nodes and edges of the swapped graph back to
     * the original graph.
     */
    public void swapBack() {
        Node node;
        Node.Edge edge;
        for (Iterator nodes = graph.getNodes(); nodes.hasNext(); ) {
            node = (Node)nodes.next();
            if (mapping.containsKey(node))
                node.setLabel(((Node.Edge)mapping.get(node)).getLabel());
            for (Iterator edges = node.getOutEdges(); edges.hasNext(); ) {
                edge = (Node.Edge)edges.next();
                if (mapping.containsKey(edge))
                    edge.setLabel(((Node)mapping.get(edge)).getLabel());
            }
        }
    }
}
