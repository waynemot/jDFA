package de.rwth.utils; // Generated package name

/**
 * The interface <code>TreeIteratorTraversal</code> describes generic traversals of
 * <code>TreeIterator</code> objects.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: TreeIteratorTraversal.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public interface TreeIteratorTraversal {
  /**
   * The inner interface <code>Actions</code> describes how a
   * <code>TreeTraversal</code> should act during the traversal.
   */
  public interface Actions {
    /**
     * Called once at the beginning of the traversal, before visiting the first entry
     * in the tree.
     *
     * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
     * returning the current entry
     */
    public void atEntry(TreeIterator ti);

    /**
     * Called once at the end of the traversal, after visiting the last entry in the
     * tree.
     *
     * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
     * returning the current entry
     */
    public void atExit(TreeIterator ti);
    
    /**
     * Called once at the root entry, before calling <code>atNext</code> for this
     * node.
     *
     * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
     * returning the current entry
     */
    public void atRoot(TreeIterator ti);
    
    /**
     * Called once at each leaf entry, after calling <code>atNext</code> or
     * <code>atRight</code> for this node.
     *
     * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
     * returning the current entry
     */
    public void atLeaf(TreeIterator ti);
    
    /**
     * Called always after executing <code>next</code> on the
     * <code>TreeIterator</code>.
     *
     * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
     * returning the current entry
     */
    public void atNext(TreeIterator ti);

    /**
     * Called always after executing <code>previous</code> on the
     * <code>TreeIterator</code>.
     *
     * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
     * returning the current entry
     */
    public void atPrevious(TreeIterator ti);

    /**
     * Called always after executing <code>left</code> on the
     * <code>TreeIterator</code>.
     *
     * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
     * returning the current entry
     */
    public void atLeft(TreeIterator ti);

    /**
     * Called always after executing <code>right</code> on the
     * <code>TreeIterator</code>.
     *
     * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
     * returning the current entry
     */
    public void atRight(TreeIterator ti);
  }
  
  /**
   * Perform the traversal of the <code>TreeIterator</code>.
   *
   * @param ti the <code>TreeIterator</code> being traversed, with 'ti.current()'
   * returning the current entry
   * @param actions a 'TreeIteratorTraversal.Actions' value describing what actions
   * should be performed on the entries.
   */
  public void traverse(TreeIterator ti, TreeIteratorTraversal.Actions actions);
} // TreeIteratorTraversal
