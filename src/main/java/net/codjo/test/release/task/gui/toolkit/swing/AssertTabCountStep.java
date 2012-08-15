package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JTabbedPane;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 *
 */
public class AssertTabCountStep<T extends net.codjo.test.release.task.gui.AssertTabCountStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JTabbedPane.class, step.getName());
        Component comp = findOnlyOne(finder, context, 0, step);

        if (comp == null) {
            throw new GuiFindException(
                  "Le conteneur d'onglets (JTabbedPane) portant le nom " + step.getName() + " est introuvable");
        }

        JTabbedPane tabbedPane = (JTabbedPane)comp;

        if (tabbedPane.getTabCount() != step.getTabCount()) {

            throw new GuiFindException(new StringBuilder()
                  .append("Le conteneur d'onglets ").append(step.getName()).append(" contient ").append(tabbedPane.getTabCount())
                  .append(" onglet(s) au lieu de ").append(step.getTabCount()).toString());
        }
    }
}
