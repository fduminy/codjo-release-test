/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.AbstractGuiStep.AUTO_MODE;
import static net.codjo.test.release.task.gui.AbstractGuiStep.DISPLAY_MODE;
import static net.codjo.test.release.task.gui.AbstractGuiStep.MODEL_MODE;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiActionException;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 * 
 */
public class AssertContainsStep<T extends net.codjo.test.release.task.gui.AssertContainsStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        if (step.getMode() == null) {
            step.setMode(AUTO_MODE);
        }
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component comp = findOnlyOne(finder, context, step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        try {
            if (comp instanceof JComboBox) {
                proceed((JComboBox)comp, step);
            }
            else if (comp instanceof JList) {
                proceed((JList)comp, step);
            }
            else {
                throw new GuiConfigurationException("Type de composant non support� : "
                    + comp.getClass().getName());
            }
        }
        catch (GuiException e) {
            throw e;
        }
        catch (Exception e) {
            throw new GuiActionException("Impossible de fixer la valeur.", e);
        }
    }


    private void proceed(final JComboBox comboBox, T step)
            throws Exception {
        if (!AUTO_MODE.equals(step.getMode()) && !DISPLAY_MODE.equals(step.getMode()) && !MODEL_MODE.equals(step.getMode())) {
            throw new GuiAssertException("Invalid value of 'mode' attribute : must be in {\"" + AUTO_MODE
                + "\", \"" + DISPLAY_MODE + "\", \"" + MODEL_MODE + "\"}.");
        }

        final ListCellRenderer renderer = comboBox.getRenderer();
        if (renderer != null && !MODEL_MODE.equals(step.getMode())) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                final Component rendererComponent =
                    renderer.getListCellRendererComponent(new JList(), comboBox.getItemAt(i), i, false, false);
                if (rendererComponent instanceof JLabel) {
                    if (((JLabel)rendererComponent).getText().equals(step.getExpected())) {
                        return;
                    }
                }
                else {
                    throw new GuiAssertException("Unexpected renderer type for ComboBox");
                }
            }
        }
        if (!DISPLAY_MODE.equals(step.getMode()) || MODEL_MODE.equals(step.getMode())) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                Object item = comboBox.getItemAt(i);
                if (item.toString().equals(step.getExpected())) {
                    return;
                }
            }
        }

        throwGuiAssertException(step);
    }


    private void proceed(final JList list, T step) throws Exception {
        if (!AUTO_MODE.equals(step.getMode()) && !MODEL_MODE.equals(step.getMode())) {
            throw new GuiAssertException("Invalid value of 'mode' attribute : must be in {\"" + AUTO_MODE
                + "\", \"" + MODEL_MODE + "\"}.");
        }

        for (int i = 0; i < list.getModel().getSize(); i++) {
            Object item = list.getModel().getElementAt(i);
            if (item.toString().equals(step.getExpected())) {
                return;
            }
        }
        throwGuiAssertException(step);
    }
    
    private void throwGuiAssertException(T step) {
    	throw new GuiAssertException("Le composant " + step.getName() + " ne contient pas la valeur " + step.getExpected());    	
    }
}
