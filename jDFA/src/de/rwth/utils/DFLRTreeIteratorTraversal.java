package de.rwth.utils; // Generated package name

/**
 * Implements a depth-first left-to-right traversal of a <code>TreeIterator</code>.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: DFLRTreeIteratorTraversal.java,v 1.2 2002/09/17 06:53:53 mohnen Exp $
 */
public class DFLRTreeIteratorTraversal implements TreeIteratorTraversal {

  /**
   * Creates a new <code>DFLRTreeIteratorTraversal</code> instance.
   */
  public DFLRTreeIteratorTraversal() { super(); }

  
  /**
   * Performs the traversal.
   *
   * @param ti a <code>TreeIterator</code> value
   * @param actions a 'TreeIteratorTraversal.Actions' value
   */
  public void traverse(TreeIterator ti, TreeIteratorTraversal.Actions actions) {
    actions.atEntry(ti);
    boolean more;
    boolean root=true;
    do {
      more=true;
      if (ti.hasNext()) {
	ti.next();
	if (root) {
	  root=false;
	  actions.atRoot(ti);
	}
	actions.atNext(ti);
	if (!ti.hasNext()) actions.atLeaf(ti);
      } else {
	while (!ti.hasRight() && ti.hasPrevious()) {
	  ti.previous();
	  actions.atPrevious(ti);
	}
	if (ti.hasRight()) {
	  ti.right();
	  actions.atRight(ti);
	  if (!ti.hasNext()) actions.atLeaf(ti);
	} else {
	  more=false;
	}
      }
    } while (more);
    actions.atExit(ti);
  }
} // DFLRTreeIteratorTraversal
