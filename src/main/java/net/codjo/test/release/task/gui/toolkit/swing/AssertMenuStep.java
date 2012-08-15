/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JRadioButtonMenuItem;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 * Classe permettant de verifier le contenu d'un menu (radio check ) et la taille d'un menu
 */
public class AssertMenuStep<T extends net.codjo.test.release.task.gui.AssertMenuStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component comp = findOnlyOne(finder, context, 0, step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (comp instanceof JCheckBoxMenuItem) {
            proceed((JCheckBoxMenuItem)comp, step);
        }
        else if (comp instanceof JRadioButtonMenuItem) {
            proceed((JRadioButtonMenuItem)comp, step);
        }
        else {
            throw new GuiConfigurationException("Type de composant non support� : "
                + comp.getClass().getName());
        }
    }


    private void proceed(JCheckBoxMenuItem checkBox, T step) {
        assertChecked(checkBox.isSelected(), step);
        if (step.getLabel() != null) {
            assertLabel(checkBox.getText(), step);
        }
    }


    private void assertLabel(String text, T step) {
        if (!step.getLabel().equals(text)) {
            throw new GuiAssertException("Composant '" + step.getName() + "' : label  attendu='" + step.getLabel()
                + "' obtenu='" + text + "'");
        }
    }


    private void assertChecked(boolean selected, T step) {
        if (step.isChecked() != selected) {
            throw new GuiAssertException("Composant '" + step.getName() + "' :  attendu='" + step.isChecked()
                + "' obtenu='" + selected + "'");
        }
    }


    private void proceed(JRadioButtonMenuItem radio, T step) {
        assertChecked(radio.isSelected(), step);
        if (step.getLabel() != null) {
            assertLabel(radio.getText(), step);
        }
    }
}
