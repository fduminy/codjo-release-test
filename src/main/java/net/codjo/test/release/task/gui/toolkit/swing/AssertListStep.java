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
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 * Classe permettant de v�rifier le contenu d'une {@link javax.swing.JTable}.
 */
public class AssertListStep<T extends net.codjo.test.release.task.gui.AssertListStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        step.setExpected(context.replaceProperties(step.getExpected()));

        if (step.getMode() == null) {
        	step.setMode(AUTO_MODE);
        }
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());

        Component comp = findOnlyOne(finder, context, 0, step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        else if (comp instanceof JComboBox) {
            int listSize = ((JComboBox)comp).getModel().getSize();
            verifyRow(listSize, step);
            proceed((JComboBox)comp, step);
        }
        else if (comp instanceof JList) {
            int listSize = ((JList)comp).getModel().getSize();
            verifyRow(listSize, step);

            proceed((JList)comp, step);
        }
        else if (comp instanceof JPopupMenu) {
            int listSize = ((JPopupMenu)comp).getSubElements().length;
            verifyRow(listSize, step);

            proceed((JPopupMenu)comp, step);
        }
        else {
            throw new GuiConfigurationException("Type de composant non support� : "
                                                + comp.getClass().getName());
        }
    }


    private void proceed(JComboBox comboBox, T step) {
        isValidMode(step);

        String actualValue = "";
        final ListCellRenderer renderer = comboBox.getRenderer();
        if (renderer != null) {
            final Component rendererComponent =
                  renderer.getListCellRendererComponent(new JList(),
                                                        comboBox.getItemAt(step.getRow()),
                                                        step.getRow(),
                                                        false,
                                                        false);
            if (rendererComponent instanceof JLabel) {
                actualValue = ((JLabel)rendererComponent).getText();
            }
            else {
                throw new GuiAssertException("Unexpected renderer type for ComboBox");
            }
        }

        if (!step.getExpected().equals(actualValue)
            && !DISPLAY_MODE.equals(step.getMode())
            || MODEL_MODE.equals(step.getMode())
            || "".equals(actualValue)) {
            actualValue = (String)comboBox.getModel().getElementAt(step.getRow());
        }

        assertExpected(actualValue, step);
    }


    private void proceed(JList list, T step) {
        isValidMode(step);
        String actualValue = "";
        final ListCellRenderer renderer = list.getCellRenderer();
        if (renderer != null) {
            final Component rendererComponent =
                  renderer.getListCellRendererComponent(new JList(), list.getModel().getElementAt(step.getRow()), step.getRow(),
                                                        false, false);
            if (rendererComponent instanceof JLabel) {
                actualValue = ((JLabel)rendererComponent).getText();
            }
            else {
                throw new GuiAssertException("Unexpected renderer type for JList");
            }
        }

        if (!step.getExpected().equals(actualValue)
            && !DISPLAY_MODE.equals(step.getMode())
            || MODEL_MODE.equals(step.getMode())
            || "".equals(actualValue)) {
            actualValue = (String)list.getModel().getElementAt(step.getRow());
        }

        assertExpected(actualValue, step);
    }


    private void isValidMode(T step) {
        if (!AUTO_MODE.equals(step.getMode()) && !DISPLAY_MODE.equals(step.getMode())
            && !MODEL_MODE.equals(step.getMode())) {
            throw new GuiAssertException("Invalid value of 'mode' attribute : must be in {\"" + AUTO_MODE
                                         + "\", \"" + DISPLAY_MODE + "\", \"" + MODEL_MODE + "\"}.");
        }
    }


    private void proceed(JPopupMenu popupMenu, T step) {
        String actualValue = ((JMenuItem)popupMenu.getSubElements()[step.getRow()].getComponent()).getText();

        assertExpected(actualValue, step);
    }


    private void assertExpected(String actualValue, T step) {
        if (!actualValue.equals(step.getExpected())) {
            throw new GuiAssertException("Composant '" + step.getName() + "' : attendu='" + step.getExpected()
                                         + "' obtenu='" + actualValue + "'");
        }
    }


    private void verifyRow(int listSize, T step) {
        if (step.getRow() < 0 || step.getRow() >= listSize) {
            throw new GuiFindException("La cellule [row=" + step.getRow() + "] n'existe pas dans la liste " + step.getName());
        }
    }
}
