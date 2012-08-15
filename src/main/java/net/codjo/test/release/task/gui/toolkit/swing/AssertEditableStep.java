/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 *
 */
public class AssertEditableStep<T extends net.codjo.test.release.task.gui.AssertEditableStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component component = findOnlyOne(finder, context, 0, step);
        if (component == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }
        if (component instanceof JTable) {
            proceed((JTable)component, step);
        }
        else {
            try {
                Method method = component.getClass().getMethod("isEditable");
                if (method != null) {
                    assertExpected(method.invoke(component).toString(), step);
                }
            }
            catch (NoSuchMethodException e) {
                if (component instanceof JCheckBox) {

                    proceed((JCheckBox)component, step);
                }
                else {
                    proceed((JComponent)component, step);
                }
            }
            catch (IllegalAccessException e) {
                throw new GuiAssertException(
                      "La m�thode isEditable est inaccessible sur le composant " + component.getName());
            }
            catch (InvocationTargetException e) {
                throw new GuiAssertException(
                      "Une exception est survenue lors de l'ex�cution de la methode 'isEditable' ");
            }
        }
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


    private void proceed(JCheckBox component, T step) {
        String actualValue;
        if (component.isEnabled()) {
            actualValue = "true";
        }
        else {
            actualValue = "false";
        }
        assertExpected(actualValue, step);
    }


    private void proceed(JComponent component, T step) {
        throw new GuiAssertException(component.getName()
                                     + " : Composant non support� par la balise assertEditable ("
                                     + component.getClass().getName()
                                     + ")");
    }


    private void assertExpected(String actualValue, T step) {
        if (!step.getExpected().equals(actualValue)) {
            throw new GuiAssertException("Composant '" + step.getName() + "' : attendu='" + step.getExpected()
                                         + "' obtenu='" + actualValue + "'");
        }
    }
}
