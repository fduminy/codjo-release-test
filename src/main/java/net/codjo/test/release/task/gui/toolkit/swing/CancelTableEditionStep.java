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
public class CancelTableEditionStep<T extends net.codjo.test.release.task.gui.CancelTableEditionStep> extends AbstractTableEditionStep<T> {
    @Override
    protected void finishEditing(JTable table) {
        table.getCellEditor().cancelCellEditing();
    }
}
