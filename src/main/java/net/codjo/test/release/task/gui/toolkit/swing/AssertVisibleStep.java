/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.RootPaneContainer;

import junit.extensions.jfcunit.finder.Finder;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.finder.GlassPaneComponentFinder;
/**
 * Classe permettant de v�rifier qu'un composant est visible ou non.
 */
public class AssertVisibleStep<T extends net.codjo.test.release.task.gui.AssertVisibleStep> extends AbstractAssertStep<T> {
    private static final String VISIBLE_COMPONENT_MESSAGE = "Le composant '%s' est visible.";
    private static final String NOT_VISIBLE_COMPONENT_MESSAGE = "Le composant '%s' n'est pas visible.";

	protected void proceedOnce(TestContext context, T step) {
        Component comp = find(context, new GlassPaneComponentFinder(step.getName()), step);
        if (comp != null) {
            proceedRootPaneContainer((RootPaneContainer)comp, step);
            return;
        }

        comp = find(context, new NamedComponentFinder(JComponent.class, step.getName()), step);
        if (comp != null) {
            proceedComponent(comp, step);
            return;
        }

        if (step.isExpected()) {
            throw new GuiConfigurationException(computeNotVisibleComponentMessage(step.getName()));
        }
    }


    private void proceedRootPaneContainer(RootPaneContainer rootPaneContainer, T step) {
        boolean internalFrameCondition
              = !JInternalFrame.class.isInstance(rootPaneContainer)
                || ((JInternalFrame)rootPaneContainer).isSelected();
        if (!step.isExpected() && rootPaneContainer.getGlassPane().isVisible() && internalFrameCondition) {
            throw new GuiConfigurationException(computeVisibleComponentMessage(step.getName()));
        }
    }


    private void proceedComponent(Component comp, T step) {
        if (!step.isExpected() && comp.isVisible()) {
            throw new GuiConfigurationException(computeVisibleComponentMessage(step.getName()));
        }
    }


    private Component find(TestContext context, Finder finder, T step) {
        return findOnlyOne(finder, context, (!step.isExpected() ? 0 : step.getTimeout() / 1000), step);
    }


    static String computeVisibleComponentMessage(String componentName) {
        return String.format(VISIBLE_COMPONENT_MESSAGE, componentName);
    }


    static String computeNotVisibleComponentMessage(String componentName) {
        return String.format(NOT_VISIBLE_COMPONENT_MESSAGE, componentName);
    }
}
