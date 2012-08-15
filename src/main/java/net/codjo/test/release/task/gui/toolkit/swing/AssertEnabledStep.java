/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import junit.extensions.jfcunit.eventdata.PathData;
import junit.extensions.jfcunit.finder.JMenuItemFinder;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 * Classe permettant de v�rifier qu'un bouton {@link javax.swing.JButton} est activ� ou pas.
 */
public class AssertEnabledStep<T extends net.codjo.test.release.task.gui.AssertEnabledStep> extends AbstractAssertStep<T> {
    private void proceedMenu(JMenuItem menuFinded, String menuName, String[] labels, T step) {

        if (menuFinded == null) {
            throw new GuiFindException("Menu non trouv� : " + menuName);
        }

        if (labels.length == 1) {
            proceed(menuFinded, step);
        }
        else {
            PathData path = new PathData(labels);
            JMenuBar menuBar = (JMenuBar)path.getRoot(menuFinded);

            assertMenuEnableState(menuBar, labels, step);
        }
    }


    private void proceedPopupMenu(TestContext context, T step) {
        StringTokenizer tok = new StringTokenizer(step.getMenu(), ":");
        String[] labels = new String[tok.countTokens()];

        for (int i = 0; tok.hasMoreTokens(); i++) {
            labels[i] = tok.nextToken();
        }

        String menuName = labels[0];
        JMenuItemFinder finder = new JMenuItemFinder(menuName);
        JComponent menuFinded = (JComponent)findOnlyOne(finder, context, 0, step);

        if (menuFinded == null) {
            throw new GuiFindException("Menu non trouv� : " + menuName);
        }

        if (!findMenuItem(labels, (JPopupMenu)menuFinded, 0, null, step)) {
            throw new GuiFindException("MenuItem non trouv� : " + labels);
        }
    }

	protected void proceedOnce(TestContext context, T step) {
        if (step.getMenu() != null) {
            StringTokenizer tok = new StringTokenizer(step.getMenu(), ":");
            String[] labels = new String[tok.countTokens()];

            for (int i = 0; tok.hasMoreTokens(); i++) {
                labels[i] = tok.nextToken();
            }

            String menuName = labels[0];
            JMenuItemFinder finder = new JMenuItemFinder(menuName);
            JComponent menuFinded = (JComponent)findOnlyOne(finder, context, 0, step);
            if (menuFinded instanceof JMenuItem) {
                proceedMenu((JMenuItem)menuFinded, menuName, labels, step);
            }
            else if (menuFinded instanceof JPopupMenu) {
                proceedPopupMenu(context, step);
            }
        }
        else {
            Component component = findComponent(context, step);
            if (component instanceof JTable && !"-1".equals(step.getRow())) {
                proceed((JTable)component, step);
            }
            else {
                proceed((JComponent)component, step);
            }
        }
    }


    private Component findComponent(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component comp = findOnlyOne(finder, context, 0, step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }
        return comp;
    }


    private void proceed(JTable table, T step) {
        int realRow = -1;
        try {
            realRow = Integer.parseInt(step.getRow());
        }
        catch (NumberFormatException nfe) {
            ;
        }
        if (table == null) {
            throw new GuiFindException("La table '" + step.getName() + "' est introuvable.");
        }
        int realColumn = TableTools.searchColumn(table, step.getColumn());
        TableTools.checkTableCellExists(table, realRow, realColumn);

        boolean isEditable = table.isCellEditable(realRow, realColumn);

        assertExpected(String.valueOf(isEditable), step);
    }


    private void proceed(JComponent component, T step) {
        String actualValue;
        if (component.isEnabled()) {
            actualValue = "true";
        }
        else {
            actualValue = "false";
        }
        assertExpected(actualValue, step);
    }


    private void assertExpected(String actualValue, T step) {
        if (!step.getExpected().equals(actualValue)) {
            throwBadEnableStateException(actualValue, step);
        }
    }


    private void throwBadEnableStateException(String actualValue, T step) {
        throw new GuiAssertException("Composant '" + step.getName() + "' : attendu='" + step.getExpected() + "' obtenu='"
                                     + actualValue + "'");
    }


    static int findMenuIndex(JMenuBar menuBar, String label) {
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu currentMenu = menuBar.getMenu(i);
            if (currentMenu.getText().equals(label)) {
                return i;
            }
        }

        throw new GuiFindException("Menu non trouv� : " + label);
    }


    static int findMenuItemIndex(JPopupMenu popupMenu, String label) {
        for (int i = 0; i < popupMenu.getComponentCount(); i++) {
            Component comp = popupMenu.getComponent(i);
            if (!(comp instanceof JMenuItem)) {
                continue;
            }

            JMenuItem menuItem = (JMenuItem)comp;
            if (menuItem.getText().equals(label)) {
                return i;
            }
        }

        throw new GuiFindException("MenuItem non trouv� : " + label);
    }


    private void assertMenuEnableState(JMenuBar menuBar, String[] labels, T step) {
        int[] indexes = new int[labels.length];
        indexes[0] = findMenuIndex(menuBar, labels[0]);
        JMenu currentMenu = menuBar.getMenu(indexes[0]);

        JPopupMenu popupMenu = currentMenu.getPopupMenu();

        if (isAlreadyOk(currentMenu, step)) {
            return;
        }

        if (findMenuItem(labels, popupMenu, 1, currentMenu, step)) {
            return;
        }

        throw new GuiFindException("MenuItem non trouv� : " + labels);
    }


    private boolean findMenuItem(String[] labels, JPopupMenu popupMenu, int startIndex, JMenu currentMenu, T step) {
        int[] indexes = new int[labels.length];
        for (int i = startIndex; i < labels.length; i++) {
            indexes[i] = findMenuItemIndex(popupMenu, labels[i]);

            if (i < (labels.length - 1)) {
                Component child = popupMenu.getComponent(indexes[i]);
                if (!(child instanceof JMenu)) {
                    throw new GuiFindException(labels[i] + " n'est pas un sous-menu");
                }

                if (currentMenu != null && isAlreadyOk(currentMenu, step)) {
                    return true;
                }
                JMenu childMenu = (JMenu)child;
                popupMenu = childMenu.getPopupMenu();
            }
            else {
                Component child = popupMenu.getComponent(indexes[i]);
                if (!(child instanceof JMenuItem)) {
                    throw new GuiFindException("MenuItem non trouv� : " + labels[i]);
                }
                proceed((JMenuItem)child, step);
                return true;
            }
        }
        return false;
    }


    private boolean isAlreadyOk(JMenu currentMenu, T step) {
        if ("false".equals(step.getExpected()) && !currentMenu.isEnabled()) {
            return true;
        }
        if ("true".equals(step.getExpected()) && !currentMenu.isEnabled()) {
            throwBadEnableStateException("false", step);
        }
        return false;
    }
}
