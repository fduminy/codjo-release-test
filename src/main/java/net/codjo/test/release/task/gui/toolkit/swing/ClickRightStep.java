/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.AbstractClickPopupMenuStep.INITIAL_ROW_VALUE;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import junit.extensions.jfcunit.eventdata.AbstractMouseEventData;
import junit.extensions.jfcunit.eventdata.EventDataConstants;
import junit.extensions.jfcunit.eventdata.JListMouseEventData;
import junit.extensions.jfcunit.eventdata.JTableMouseEventData;
import junit.extensions.jfcunit.eventdata.JTreeMouseEventData;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
/**
 *
 */
public class ClickRightStep<T extends net.codjo.test.release.task.gui.ClickRightStep> extends AbstractClickButtonStep<T> {
    @Override
    protected boolean acceptAndProceed(net.codjo.test.release.task.gui.TestContext context, Component comp, T step) throws Exception {
        if (comp instanceof JTable) {
            proceedTable((TestContext) context, (JTable)comp, step);
        }
        else if (comp instanceof JTree) {
            proceedTree((TestContext) context, (JTree)comp, step);
        }
        else if (comp instanceof JComboBox) {
            proceedCombo((TestContext) context, (JComboBox)comp, step);
        }
        else if (comp instanceof JList) {
            proceedList((TestContext) context, (JList)comp, step);
        }
        else if (comp instanceof JTextField) {
            proceedTextField((TestContext) context, (JTextField)comp);
        }
        else {
            return false;
        }
        return true;
    }


    private void proceedCombo(TestContext context, JComboBox combo, T step) {
        if (step.getRow() >= combo.getModel().getSize()) {
            throw new GuiFindException("La ligne '" + step.getRow() + "' est introuvable dans la combo '"
                                       + step.getName() + "'");
        }

        combo.setSelectedIndex(step.getRow());
        MouseEventData eventData = new ClickMouseEventData(context.getTestCase(),
                                                           combo, 1,
                                                           InputEvent.BUTTON3_MASK,
                                                           true,
                                                           EventDataConstants.DEFAULT_SLEEPTIME);
        showPopupAndSelectItem(combo, context, eventData, step);
    }


    protected void proceedTable(TestContext context, JTable table, T step) {
        if (step.getRow() >= table.getRowCount()) {
            throw new GuiFindException("La ligne '" + step.getRow() + "' est introuvable dans la table '"
                                       + step.getName() + "'");
        }
        int realColumn = 0;
        if (!"".equals(step.getColumn())) {
            realColumn = TableTools.searchColumn(table, step.getColumn());
        }
        if (realColumn >= table.getColumnCount()) {
            throw new GuiFindException("La colonne '" + step.getColumn() + "' est introuvable dans la table '"
                                       + step.getName() + "'");
        }

        table.setRowSelectionInterval(step.getRow(), step.getRow());
        table.setColumnSelectionInterval(realColumn, realColumn);

        showPopupAndSelectItem(table, context, getMouseEventData(context, table, realColumn, step), step);
    }


    protected void proceedTextField(TestContext context, JTextField jTextField) {
        MouseEventData eventData = new ClickMouseEventData(context.getTestCase(),
                                                           jTextField, 1,
                                                           InputEvent.BUTTON3_MASK,
                                                           true,
                                                           EventDataConstants.DEFAULT_SLEEPTIME);

        context.getHelper().enterClickAndLeave(eventData);
    }

	@Override
	protected AbstractMouseEventData getMouseEventData(
			net.codjo.test.release.task.gui.TestContext context, JTable table,
			int realColumn, T step) {
        return new JTableMouseEventData(((TestContext) context).getTestCase(), table, step.getRow(), realColumn,
                                        MouseEvent.BUTTON1,
                                        true, step.getTimeout());
    }


    private void proceedList(TestContext context, JList list, T step) {
        if (step.getRow() >= list.getModel().getSize()) {
            throw new GuiFindException("La ligne '" + step.getRow() + "' est introuvable dans la liste '"
                                       + step.getName() + "'");
        }

        list.setSelectionInterval(step.getRow(), step.getRow());

        JListMouseEventData eventData = new JListMouseEventData(context.getTestCase(), list, step.getRow(), 1, 1, true,
                                                                step.getTimeout());
        showPopupAndSelectItem(list, context, eventData, step);
    }


    private void proceedTree(TestContext context, final JTree tree, T step)
          throws Exception {
        if (step.getRow() != INITIAL_ROW_VALUE) {
            throw new GuiConfigurationException("L'attribut row n'est pas support�.");
        }

        if (step.getPath() == null) {
            throw new GuiConfigurationException("Le path n'a pas �t� renseign�.");
        }

        final TreePath treePath =
              TreeUtils.convertIntoTreePath(tree, step.getPath(), TreeStepUtils.getConverter(step.getMode()));

        JTreeMouseEventData eventData =
              new JTreeMouseEventData(context.getTestCase(), tree, treePath, 1, true, step.getTimeout());
        showPopupAndSelectItem(tree, context, eventData, step);
    }
}
