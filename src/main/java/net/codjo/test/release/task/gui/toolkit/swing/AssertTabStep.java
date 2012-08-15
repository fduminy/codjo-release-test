package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JTabbedPane;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 *
 */
public class AssertTabStep<T extends net.codjo.test.release.task.gui.AssertTabStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JTabbedPane.class, step.getName());
        Component comp = findOnlyOne(finder, context, 0, step);

        if (comp == null) {
            throw new GuiFindException(
                  "Le conteneur d'onglets (JTabbedPane) portant le nom " + step.getName() + " est introuvable");
        }

        JTabbedPane tabbedPane = (JTabbedPane)comp;

        int indexFound = -1;

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String title = tabbedPane.getTitleAt(i);
            if (title.equals(step.getTabLabel())) {
                indexFound = i;
                break;
            }
        }
        if (indexFound == -1) {
            throw new GuiFindException("L'onglet portant le nom " + step.getTabLabel() + " est introuvable");
        }
        else if (step.getTabIndex() != -1 && indexFound != step.getTabIndex()) {
            throw new GuiFindException("L'onglet " + step.getTabLabel() + " en position " + indexFound
                                       + " ne se trouve pas en position " + step.getTabIndex());
        }

        if (step.isSelectedAttributeIsSet()) {
            boolean isSelected = (indexFound == tabbedPane.getSelectedIndex());
            if (step.isSelected() && !isSelected) {
                throw new GuiAssertException("L'onglet '" + step.getTabLabel() + "' n'est pas s�lectionn�.");
            }
            if (!step.isSelected() && isSelected) {
                throw new GuiAssertException("L'onglet '" + step.getTabLabel() + "' est s�lectionn�.");
            }
        }

        if (step.getEnabled() != null && step.getEnabled() != tabbedPane.isEnabledAt(indexFound)) {
            throw new GuiAssertException(
                  "L'onglet '" + step.getTabLabel() + "' est ou n'est pas actif contrairement � ce qui a �t� sp�cifi�");
        }
    }
}
