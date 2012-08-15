package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiActionException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;

public class ScrollToVisibleStep<T extends net.codjo.test.release.task.gui.ScrollToVisibleStep> extends AbstractGuiStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(net.codjo.test.release.task.gui.TestContext context, GuiStep s) {
		T step = (T) s;
        NamedComponentFinder finder = new NamedComponentFinder(JScrollPane.class, step.getScrollPane());
        final JScrollPane container = (JScrollPane)findOnlyOne(finder, context, step.getTimeout(), step);
        if (container == null) {
            throw new GuiFindException("Le JScrollPane '" + step.getScrollPane() + "' est introuvable.");
        }

        JComponent comp = getComponentByName(container, step.getName());
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        Rectangle bounds = comp.getBounds();
        if (container != comp.getParent()) {
            bounds = SwingUtilities.convertRectangle(comp.getParent(), bounds, container);
        }

        final Rectangle finalBounds = bounds;
        try {
            runAwtCode(context,
                       new Runnable() {
                           public void run() {
                               container.getViewport().scrollRectToVisible(finalBounds);
                               container.repaint();
                           }
                       });
        }
        catch (Exception e) {
            throw new GuiActionException("Impossible de scroller sur le composant.", e);
        }
    }

    public static List<JComponent> getAllJComponents(Container container) {
        List<JComponent> components = new ArrayList<JComponent>();
        getAllJComponents(container, components);
        return components;
    }


    private static void getAllJComponents(Container container, Collection<JComponent> collection) {
        if (container instanceof JComponent) {
            JComponent component = (JComponent)container;
            collection.add(component);
        }

        Component[] children = container.getComponents();
        if (children != null) {
            for (Component childComponent : children) {
                if (childComponent instanceof Container) {
                    getAllJComponents((Container)childComponent, collection);
                }
            }
        }
    }


    public static JComponent getComponentByName(Container parentContainer, String componentName) {
        for (JComponent component : getAllJComponents(parentContainer)) {
            if (componentName.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }
}
