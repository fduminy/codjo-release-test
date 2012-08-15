package net.codjo.test.release.task.gui.toolkit.swing;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
/**
 *
 */
public class ExpandAllTreeStep<T extends net.codjo.test.release.task.gui.ExpandAllTreeStep> extends AbstractGuiStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(TestContext context, GuiStep s) {
		T step = (T) s;
        NamedComponentFinder finder = new NamedComponentFinder(JTree.class, step.getName());
        JTree tree = (JTree)findOnlyOne(finder, context, 0, step);

        if (tree == null) {
            throw new GuiFindException("L'arbre '" + step.getName() + "' est introuvable.");
        }

        TreeUtils.expandSubtree(tree, new TreePath(tree.getModel().getRoot()));
    }
}
