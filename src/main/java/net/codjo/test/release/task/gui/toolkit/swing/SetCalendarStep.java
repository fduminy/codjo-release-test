package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JComponent;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.metainfo.Introspector;
import net.codjo.test.release.task.gui.metainfo.SetCalendarDescriptor;
/**
 *
 */
public class SetCalendarStep<T extends net.codjo.test.release.task.gui.SetCalendarStep> extends AbstractGuiStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(net.codjo.test.release.task.gui.TestContext context, GuiStep s) {
		T step = (T) s;
        step.setValue(context.replaceProperties(step.getValue()));
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        final Component component = findOnlyOne(finder, context, step.getTimeout(), step);
        if (component == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (!component.isEnabled()) {
            throw new GuiConfigurationException(step.computeUneditableComponent(step.getName()));
        }

        final SetCalendarDescriptor descriptor =
              Introspector.getTestBehavior(component.getClass(), SetCalendarDescriptor.class);
        if (descriptor != null) {
            try {
                runAwtCode(context,
                           new Runnable() {
                               public void run() {
                                   descriptor.setCalendar(component, SetCalendarStep.this);
                               }
                           });
            }
            catch (Exception e) {
                throw new GuiException("" + e.getMessage());
            }
        }
    }
}
