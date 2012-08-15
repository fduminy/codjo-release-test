/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.SelectTabStep.INITIAL_INDEX_VALUE;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiActionException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
/**
 * Classe permettant de Selectionner un onglet dans une {@link javax.swing.JTabbedPane}
 */
public class SelectTabStep<T extends net.codjo.test.release.task.gui.SelectTabStep> extends AbstractGuiStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(net.codjo.test.release.task.gui.TestContext context, GuiStep s) {
		T step = (T) s;
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component comp = findOnlyOne(finder, context, step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }
        if (comp instanceof JTabbedPane) {
            checkAttributesUsage(step);
            if (step.getTabIndex() != INITIAL_INDEX_VALUE) {
                step.setTabLabel(String.valueOf(step.getTabIndex()));
            }
            final JTabbedPane tabbedPane = (JTabbedPane)comp;
            final int index = findTabIndex(tabbedPane, step);
            if (index == -1 || index >= tabbedPane.getTabCount()) {
                throw new GuiFindException("L'onglet '" + step.getTabLabel() + "' est introuvable dans le composant '"
                    + step.getName() + "'");
            }

            try {
                runAwtCodeLater(context,
                    new Runnable() {
                        public void run() {
                            tabbedPane.setSelectedIndex(index);
                        }
                    });
            }
            catch (Exception e) {
                throw new GuiActionException("Impossible de s�lectionner l'onglet.", e);
            }
        }
        else {
            throw new GuiConfigurationException("Type de composant non support� : "
                + comp.getClass().getName());
        }
    }


    static String computeIllegalUsageOfAttributes(String name) {
        return "Les attributs 'tabIndex' et 'tabLabel' du composant '" + name
        + "' ne peuvent pas �tre utilis�s en m�me temps.";
    }


    private void checkAttributesUsage(T step) {
        if (step.getTabIndex() != INITIAL_INDEX_VALUE && step.getTabLabel() != null) {
            throw new GuiConfigurationException(computeIllegalUsageOfAttributes(step.getName()));
        }
    }


    private int findTabIndex(JTabbedPane tabbedPane, T step) {
        if (step.getTabLabel() == null) {
            return -1;
        }
        try {
            return Integer.parseInt(step.getTabLabel());
        }
        catch (NumberFormatException nfe) {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (step.getTabLabel().equals(tabbedPane.getTitleAt(i))) {
                    return i;
                }
            }
        }
        return -1;
    }
}
