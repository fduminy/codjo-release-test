/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import javax.swing.JTable;
/**
 * 
 */
public class ValidateTableEditionStep<T extends net.codjo.test.release.task.gui.ValidateTableEditionStep> extends AbstractTableEditionStep<T> {
    @Override
    protected void finishEditing(JTable table) {
        table.getCellEditor().stopCellEditing();
    }
}
