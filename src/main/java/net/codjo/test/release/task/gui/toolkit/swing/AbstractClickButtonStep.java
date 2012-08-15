package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;

import junit.extensions.jfcunit.eventdata.AbstractMouseEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiActionException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
/**
 *
 */
public abstract class AbstractClickButtonStep<T extends net.codjo.test.release.task.gui.AbstractClickButtonStep> extends AbstractClickPopupMenuStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(TestContext context, GuiStep s) {
		T step = (T) s;
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component comp = findOnlyOne(finder, context, step.getFinderTimeout(), step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        try {
            if (!acceptAndProceed(context, comp, step)) {
                throw new GuiConfigurationException("Type de composant non support� : "
                                                    + comp.getClass().getName());
            }
        }
        catch (GuiException e) {
            throw e;
        }
        catch (Exception e) {
            throw new GuiActionException("Impossible de s�lectionner le composant.", e);
        }
    }


    protected abstract AbstractMouseEventData getMouseEventData(TestContext context,
                                                                JTable table,
                                                                int realColumn, T step);


    protected abstract boolean acceptAndProceed(TestContext context, Component comp, T step) throws Exception;
}
