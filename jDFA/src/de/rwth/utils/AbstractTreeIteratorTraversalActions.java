package de.rwth.utils; // Generated package name

/**
 * This class provides default implementations for the
 * <code>TreeIteratorTraversal.Actions</code> interface. This class actually
 * implements all methods, but does nothing.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: AbstractTreeIteratorTraversalActions.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
abstract public class AbstractTreeIteratorTraversalActions
implements TreeIteratorTraversal.Actions {
  public AbstractTreeIteratorTraversalActions() { super();  }
  public void atEntry(TreeIterator ti) {}
  public void atExit(TreeIterator ti) {}
  public void atRoot(TreeIterator ti) {}
  public void atLeaf(TreeIterator ti) {}
  public void atNext(TreeIterator ti) {}
  public void atPrevious(TreeIterator ti) {}
  public void atLeft(TreeIterator ti) {}
  public void atRight(TreeIterator ti) {}
} // class AbstractTreeIteratorTraversalActions
