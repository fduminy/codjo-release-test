/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.AbstractGuiStep.AUTO_MODE;
import static net.codjo.test.release.task.gui.AbstractGuiStep.DISPLAY_MODE;
import static net.codjo.test.release.task.gui.AbstractGuiStep.MODEL_MODE;
import static net.codjo.test.release.task.gui.metainfo.Introspector.getTestBehavior;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.metainfo.AssertTableDescriptor;

/**
 * Classe permettant de v�rifier le contenu d'une {@link javax.swing.JTable}.
 */
public class AssertTableStep<T extends net.codjo.test.release.task.gui.AssertTableStep> extends AbstractMatchingStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        step.setExpected(context.replaceProperties(step.getExpected()));
        if (step.getMode() == null) {
        	step.setMode(AUTO_MODE);
        }
        if (!AUTO_MODE.equals(step.getMode()) && !DISPLAY_MODE.equals(step.getMode()) && !MODEL_MODE
              .equals(step.getMode())) {
            throw new GuiAssertException("Invalid value of 'mode' attribute : must be in {\"" + AUTO_MODE
                                         + "\", \"" + DISPLAY_MODE + "\", \"" + MODEL_MODE + "\"}.");
        }
        NamedComponentFinder finder = new NamedComponentFinder(JTable.class, step.getName());

        JTable table = (JTable)findOnlyOne(finder, context, 0, step);
        if (table == null) {
            throw new GuiFindException("La table '" + step.getName() + "' est introuvable.");
        }

        if (!proceedTableBehavior(table)) {
            if (!proceedRendererBehavior(table, step)) {
                proceedDefault(table, step);
            }
        }
    }


    private boolean proceedTableBehavior(JTable table) {
        final AssertTableDescriptor descriptor =
              getTestBehavior(table.getClass(), AssertTableDescriptor.class);
        if (descriptor != null) {
            descriptor.assertTable(table, this);
            return true;
        }
        return false;
    }


    private boolean proceedRendererBehavior(JTable table, T step) {
        if (!MODEL_MODE.equals(step.getMode())) {
            final AssertTableDescriptor descriptor =
                  getTestBehavior(table.getCellRenderer(step.getRow(), getRealColumnIndex(table, step)).getClass(),
                                  AssertTableDescriptor.class);
            if (descriptor != null) {
                descriptor.assertTable(table, this);
                return true;
            }
        }
        return false;
    }


    private void proceedDefault(JTable table, T step) {
        int realColumn = getRealColumnIndex(table, step);
        TableCellRenderer renderer = table.getCellRenderer(step.getRow(), realColumn);
        assertExpected(getActualValue(table, realColumn, renderer, step), step);
        assertBackground(getActualBackground(table, renderer, realColumn, step), step);
        assertCellRenderer(renderer, step);
    }


    private Color getActualBackground(JTable table, TableCellRenderer renderer, int realColumn, T step) {
        Object actualValue = table.getValueAt(step.getRow(), realColumn);
        Component rendererComponent =
              renderer.getTableCellRendererComponent(table, actualValue, false, false, step.getRow(), realColumn);

        return rendererComponent.getBackground();
    }


    private void assertBackground(Color actualBackground, T step) {
        if (step.getBackground() == null) {
            return;
        }
        boolean equals = actualBackground.getRed() == step.getBackground().getRed()
                         && actualBackground.getGreen() == step.getBackground().getGreen()
                         && actualBackground.getBlue() == step.getBackground().getBlue();

        if (!equals) {
            throw new GuiAssertException("Couleur de fond du composant '" + step.getName() + "' : attendu='"
                                         + step.getBackground() + "' obtenu='" + actualBackground + "'");
        }
    }


    private void assertCellRenderer(TableCellRenderer renderer, T step) {
        if (step.getExpectedCellRenderer() == null) {
            return;
        }

        String actualRendererName = renderer.getClass().getName();
        boolean equals = step.getExpectedCellRenderer().equals(actualRendererName);

        if (!equals) {
            throw new GuiAssertException("Composant " + step.getComponentName()
                                         + " : renderer attendu='" + step.getExpectedCellRenderer()
                                         + "' obtenu='" + actualRendererName + "'");
        }
    }


    private int getRealColumnIndex(JTable table, T step) {
        int realColumn = TableTools.searchColumn(table, step.getColumn());
        TableTools.checkTableCellExists(table, step.getRow(), realColumn);
        return realColumn;
    }


    private String getActualValue(JTable table, int realColumn, TableCellRenderer renderer, T step) {
        Object actualValue = table.getValueAt(step.getRow(), realColumn);
        if (!MODEL_MODE.equals(step.getMode())) {
            if (renderer != null) {
                final Component rendererComponent =
                      renderer
                            .getTableCellRendererComponent(table, actualValue, false, false, step.getRow(), realColumn);

                final AssertTableDescriptor descriptor =
                      getTestBehavior(table.getClass(), AssertTableDescriptor.class);

                if (descriptor != null) {
                }
                else if (rendererComponent instanceof JLabel) {
                    actualValue = ((JLabel)rendererComponent).getText();
                }
                else if (rendererComponent instanceof JCheckBox) {
                    actualValue = String.valueOf(((JCheckBox)rendererComponent).isSelected());
                }
                else {
                    throw new GuiAssertException("Unexpected renderer type for Table");
                }

                if ((!DISPLAY_MODE.equals(step.getMode())) && (!compareWithExpectedValue(actualValue.toString(), step))) {
                    actualValue = table.getValueAt(step.getRow(), realColumn);
                }
            }
        }
        return String.valueOf(actualValue);
    }
}
