package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 * Classe permettant d'asserter le titre d'une bordure.
 */
public class AssertTitleBorderStep<T extends net.codjo.test.release.task.gui.AssertTitleBorderStep> extends AbstractMatchingStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component component = findOnlyOne(finder, context, 0, step);
        if (component == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (component instanceof JComponent) {
            JComponent jComponent = (JComponent)component;
            Border border = jComponent.getBorder();
            if (border instanceof TitledBorder) {
                proceed((TitledBorder)border, step);
            }
            else {
                throw new GuiAssertException(
                      "Le composant " + component.getName()
                      + " doit poss�der une bordure de type TitleBorder.");
            }
        }
        else {
            throw new GuiAssertException(
                  "Le composant " + component.getName() + " doit �tre un JComponent "
                  + "et doit poss�der une bordure de type TitleBorder.");
        }
    }

    private void proceed(TitledBorder titledBorder, T step) {
        String actualValue = titledBorder.getTitle();
        assertExpected(actualValue, step);
    }
}
