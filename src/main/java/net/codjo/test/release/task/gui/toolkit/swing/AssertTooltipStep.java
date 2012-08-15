package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTable;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 *
 */
public class AssertTooltipStep<T extends net.codjo.test.release.task.gui.AssertTooltipStep> extends AbstractMatchingStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component component = findOnlyOne(finder, context, 0, step);
        if (component == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (component instanceof JComponent) {
            JComponent jComponent = (JComponent)component;
            String actualValue = jComponent.getToolTipText();

            if (actualValue == null && jComponent instanceof JTable) {
                actualValue = getTableTooltip((JTable)jComponent, step);
            }

            if (actualValue == null) {
                if ((step.getExpected() != null) && (step.getExpected().length() > 0)) {
                    throw new GuiAssertException("Le composant '" + step.getName() + "' n'a pas de tooltip.");
                }
            }
            else {
                assertExpected(actualValue, step);
            }
        }
        else {
            throw new GuiAssertException(
                  "Le composant '" + component.getName() + "' doit �tre un JComponent.");
        }
    }


    private String getTableTooltip(JTable jTable, T step) {
        MouseEvent mouseEvent = new MouseEvent(jTable,
                                               (int)Math.random(),
                                               new Date().getTime(),
                                               0,
                                               jTable.getCellRect(step.getRow(), 0, false).x,
                                               jTable.getCellRect(step.getRow(), 0, false).y,
                                               0,
                                               false);
        return jTable.getToolTipText(mouseEvent);
    }
}
