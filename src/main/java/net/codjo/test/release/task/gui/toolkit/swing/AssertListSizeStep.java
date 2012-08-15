/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 * Classe de base pour l'assertSize d'une : {@link javax.swing.JTable} ou une {@link javax.swing.JList} ou une
 * {@link javax.swing.JComboBox}
 *
 * @version $Revision: 1.12 $
 */
public class AssertListSizeStep<T extends net.codjo.test.release.task.gui.AssertListSizeStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component comp = findOnlyOne(finder, context, 0, step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (comp instanceof JTable) {
            proceed(((JTable)comp).getModel().getRowCount(), step);
        }
        else if (comp instanceof JComboBox) {
            proceed(((JComboBox)comp).getModel().getSize(), step);
        }
        else if (comp instanceof JList) {
            proceed(((JList)comp).getModel().getSize(), step);
        }
        else if (comp instanceof JPopupMenu) {
            proceed(((JPopupMenu)comp).getSubElements().length, step);
        }
        else if (comp instanceof JTree) {
            proceed(((JTree)comp).getRowCount(), step);
        }
        else {
            throw new GuiConfigurationException("Type de composant non support� : "
                                                + comp.getClass().getName());
        }
    }


    private void proceed(int actualSize, T step) {
        if (step.getExpected() != actualSize) {
            throw new GuiAssertException("Composant '" + step.getName() + "' : Nombre de lignes attendu='"
                                         + step.getExpected() + "' obtenu='" + actualSize + "'");
        }
    }
}
