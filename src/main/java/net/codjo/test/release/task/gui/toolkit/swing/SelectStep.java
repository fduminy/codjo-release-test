/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.SelectStep.INITIAL_VALUE;
import static net.codjo.test.release.task.gui.SelectStep.MULTIPLE_UNSUPPORTED_MESSAGE;

import java.awt.Component;

import javax.accessibility.Accessible;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import junit.extensions.jfcunit.eventdata.JMenuMouseEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiActionException;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;

/**
 * Classe permettant de Selectionner une ligne dans une {@link javax.swing.JList} ou {@link javax.swing.JTable} ou
 * {@link javax.swing.JPopupMenu}.
 */
public class SelectStep<T extends net.codjo.test.release.task.gui.SelectStep> extends AbstractGuiStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(net.codjo.test.release.task.gui.TestContext context, GuiStep s) {
		T step = (T) s;
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component comp = findOnlyOne(finder, context, step.getTimeout(), step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (step.getName() == null) {
            step.setName(comp.getName());
        }

        if (!JTree.class.isInstance(comp) && (step.getMode() != null)) {
            throw new GuiAssertException("Component doesn't support the attribute 'mode'");
        }

        if (!comp.isEnabled()) {
            throw new GuiConfigurationException(step.computeUneditableComponent(step.getName()));
        }
        try {
            if (comp instanceof JTable) {
                proceedTable(context, (JTable)comp, step);
            }
            else if (comp instanceof JList) {
                proceedList(context, (JList)comp, step);
            }
            else if (comp instanceof JTree) {
                proceedTree(context, (JTree)comp, step);
            }
            else if (comp instanceof JComboBox) {
                proceedCombo(context, ((JComboBox)comp), step);
            }
            else if (comp instanceof JPopupMenu) {
                proceedPopup(context, (JPopupMenu)comp, step);
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
            throw new GuiActionException("Impossible de s�lectionner le composant.", e);
        }
    }


    private void proceedPopup(net.codjo.test.release.task.gui.TestContext context, final JPopupMenu popupMenu, T step)
          throws Exception {
        if (step.isMultiple()) {
            throw new GuiConfigurationException(MULTIPLE_UNSUPPORTED_MESSAGE);
        }
        if (step.getLabel() == null && step.getRow() <= -1) {
            throw new GuiConfigurationException("Le label ou le num�ro de ligne doivent �tre renseign�s");
        }
        else if (step.getLabel() != null && step.getRow() > -1) {
            throw new GuiConfigurationException(step.computeIllegalUsageOfLabelAndRowMessage(step.getName()));
        }

        final int foundItem = findComponentToSelect(popupMenu, step);
        JMenuMouseEventData eventData =
              new JMenuMouseEventData(((TestContext) context).getTestCase(), popupMenu, new int[]{foundItem});
        eventData.setSleepTime(step.getPopupDelay());
        ((TestContext) context).getHelper().enterClickAndLeave(eventData);
    }


    int findComponentToSelect(JPopupMenu popupMenu, T step) {
        final Component[] items = popupMenu.getComponents();
        int foundItem = -1;
        if (step.getLabel() != null) {
            int count = 0;
            for (int i = 0; i < items.length; i++) {
                final Component itemComponent = items[i];
                if ((itemComponent instanceof JMenuItem) && (step.getLabel().equals(((JMenuItem)itemComponent).getText()))) {
                    foundItem = i;
                    count++;
                }
            }
            if (count == 0) {
                throw new GuiFindException(step.computeUnknownLabelMessage(step.getLabel(), step.getName()));
            }
            if (count > 1) {
                throw new GuiFindException(step.computeDoubleLabelMessage(step.getLabel(), step.getName()));
            }
        }
        else if (step.getRow() > -1 && step.getRow() < items.length) {
            foundItem = step.getRow();
        }
        else if (step.getRow() >= items.length) {
            throw new GuiFindException(step.computeBadRowMessage(step.getRow(), step.getName()));
        }
        return foundItem;
    }


    private void proceedCombo(net.codjo.test.release.task.gui.TestContext context, final JComboBox comboBox, T step)
          throws Exception {
        if (step.isMultiple()) {
            throw new GuiConfigurationException(MULTIPLE_UNSUPPORTED_MESSAGE);
        }

        // Get the popup
        Accessible popup = comboBox.getUI().getAccessibleChild(comboBox, 0);
        if (popup != null && popup instanceof javax.swing.plaf.basic.ComboPopup) {
            // get the popup list
            final JList list = ((javax.swing.plaf.basic.ComboPopup)popup).getList();
            final int index = findIndexToSelect(list, step);
            runAwtCode(context,
                       new Runnable() {
                           public void run() {
                               comboBox.setSelectedIndex(index);
                           }
                       });
        }
    }


    private void proceedTable(net.codjo.test.release.task.gui.TestContext context, final JTable table, final T step)
          throws Exception {
        if (step.getRow() == INITIAL_VALUE && step.getLabel() != null && !"".equals(step.getLabel().trim())) {
            step.setRow(findTableRowByLabel(table, step.getLabel(), step));
            if (step.getRow() == -1) {
                throw new GuiFindException("Le label '" + step.getLabel() + "' n'existe pas dans la table " + step.getName());
            }
        }
        if (step.getRow() < 0 || step.getRow() >= table.getRowCount()) {
            throw new GuiFindException("La ligne " + step.getRow() + " n'existe pas dans la table " + step.getName());
        }
        if (step.isMultiple()) {
            if (step.getColumn() < 0) {
                runAwtCode(context,
                           new Runnable() {
                               public void run() {
                                   toggleRows(table, step.getRow(), step.isMultiple());
                               }
                           });
            }
            else {
                throw new GuiActionException("L'option multiple=\"true\" n'est pas support�e lorsque les "
                                             + "attributs row et column sont sp�cifi�s.");
            }
        }
        else {
            if (step.getColumn() < 0) {
                runAwtCode(context,
                           new Runnable() {
                               public void run() {
                                   toggleRows(table, step.getRow(), step.isMultiple());
                               }
                           });
            }
            else {
                runAwtCode(context,
                           new Runnable() {
                               public void run() {
                                   table.changeSelection(step.getRow(), step.getColumn(), false, false);
                               }
                           });
            }
        }
    }


    private int findTableRowByLabel(JTable table, String theLabel, T step) {
        int rows = table.getRowCount();
        int cols = table.getColumnCount();
        int found = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (theLabel.equals(getTableCellValue(table, i, j))) {
                    if (found == -1) {
                        found = i;
                    }
                    else {
                        throw new GuiFindException(
                              "Le label '" + theLabel + "' est pr�sent plusieurs fois dans la table " + step.getName());
                    }
                }
            }
        }
        return found;
    }


    private String getTableCellValue(JTable table, int rowIndex, int columnIndex) {
        Object value = table.getModel().getValueAt(rowIndex, columnIndex);
        Component component = table.getCellRenderer(rowIndex, columnIndex)
              .getTableCellRendererComponent(table, value, false, false, rowIndex, columnIndex);
        if (JLabel.class.isInstance(component)) {
            return ((JLabel)component).getText();
        }
        return value.toString();
    }


    private void toggleRows(final JTable table, int rowClicked, boolean multipleActivated) {
        int[] rowsAlreadySelected = table.getSelectedRows();
        table.clearSelection();

        if (!multipleActivated) {
            table.setRowSelectionInterval(rowClicked, rowClicked);
            return;
        }

        boolean wasAlreadyActivated = false;
        for (int aRowsAlreadySelected1 : rowsAlreadySelected) {
            if (aRowsAlreadySelected1 == rowClicked) {
                wasAlreadyActivated = true;
                break;
            }
        }
        if (!wasAlreadyActivated) {
            table.setRowSelectionInterval(rowClicked, rowClicked);
        }

        for (int aRowsAlreadySelected : rowsAlreadySelected) {
            if (aRowsAlreadySelected != rowClicked) {
                table.addRowSelectionInterval(aRowsAlreadySelected, aRowsAlreadySelected);
            }
        }
    }


    private void proceedList(net.codjo.test.release.task.gui.TestContext context, final JList list, T step) throws Exception {
        final int computedRow = findIndexToSelect(list, step);
        if (step.isMultiple()) {
            runAwtCode(context,
                       new Runnable() {
                           public void run() {
                               list.addSelectionInterval(computedRow, computedRow);
                           }
                       });
        }
        else {
            runAwtCode(context,
                       new Runnable() {
                           public void run() {
                               list.setSelectedIndex(computedRow);
                           }
                       });
        }
    }


    private int findIndexToSelect(JList list, T step) {
        if (step.getRow() != INITIAL_VALUE && step.getLabel() != null) {
            throw new GuiConfigurationException(step.computeIllegalUsageOfLabelAndRowMessage(step.getName()));
        }
        final int computedRow;
        if (step.getLabel() != null) {
            computedRow = convertLabelToRow(list, step);
        }
        else {
            computedRow = step.getRow();
        }
        if (computedRow < 0 || computedRow >= list.getModel().getSize()) {
            throw new GuiFindException(step.computeBadRowMessage(computedRow, step.getName()));
        }
        return computedRow;
    }


    private int convertLabelToRow(JList convList, T step) {
        int convertedRow = INITIAL_VALUE;
        for (int index = 0; index < convList.getModel().getSize(); index++) {
            String listValue = getListValue(convList, index, step);
            if (step.getLabel().equals(listValue)) {
                if (convertedRow != INITIAL_VALUE) {
                    throw new GuiFindException(step.computeDoubleLabelMessage(step.getLabel(), step.getName()));
                }
                convertedRow = index;
            }
        }
        if (convertedRow == INITIAL_VALUE) {
            throw new GuiFindException(step.computeUnknownLabelMessage(step.getLabel(), step.getName()));
        }
        return convertedRow;
    }

    private String getListValue(JList convList, int index, T step) {
        try {
            Thread.sleep(step.getListDelay());
        }
        catch (InterruptedException e) {
            ;
        }
        Object at = convList.getModel().getElementAt(index);

        Component renderedComponent =
              convList.getCellRenderer()
                    .getListCellRendererComponent(convList,
                                                  at,
                                                  index,
                                                  convList.isSelectedIndex(index),
                                                  false);

        if (JLabel.class.isInstance(renderedComponent)) {
            return ((JLabel)renderedComponent).getText();
        }
        throw new GuiConfigurationException(step.computeUnexpectedRendererMessage(renderedComponent.getClass().getName(),
                                                                             step.getName()));
    }


    private void proceedTree(net.codjo.test.release.task.gui.TestContext context, final JTree tree, final T step) throws Exception {
        if (step.getRow() != INITIAL_VALUE) {
            throw new GuiConfigurationException("L'attribut row n'est pas support�.");
        }
        if (step.getColumn() != INITIAL_VALUE) {
            throw new GuiConfigurationException("L'attribut column n'est pas support�.");
        }
        if (step.getPath() == null) {
            throw new GuiConfigurationException("Le path n'a pas �t� renseign�.");
        }

        final TreePath treePath = TreeUtils
              .convertIntoTreePath(tree, step.getPath(), TreeStepUtils.getConverter(step.getMode()));

        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           if (step.isMultiple()) {
                               tree.addSelectionPath(treePath);
                           }
                           else {
                               tree.setSelectionPath(treePath);
                           }
                       }
                   });
    }
}
