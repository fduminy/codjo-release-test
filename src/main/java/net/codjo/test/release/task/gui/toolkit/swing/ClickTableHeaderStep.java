/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.InputEvent;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import junit.extensions.jfcunit.eventdata.EventDataConstants;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.metainfo.ClickTableHeaderDescriptor;
import net.codjo.test.release.task.gui.metainfo.Introspector;
/**
 *
 */
public class ClickTableHeaderStep<T extends net.codjo.test.release.task.gui.ClickTableHeaderStep> extends AbstractClickStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(TestContext context, GuiStep s) {
		T step = (T) s;
        if (step.getName() != null) {
            proceedComponent((net.codjo.test.release.task.gui.toolkit.swing.TestContext) context, step.getName(), step, new NamedComponentFinder(JComponent.class, step.getName()));
        }
    }


    @Override
    protected MouseEventData createMouseEventData(TestContext context, Component comp, T step) {
        return new MouseEventData(((net.codjo.test.release.task.gui.toolkit.swing.TestContext) context).getTestCase(), comp);
    }

    @Override
    protected int getMouseModifiers(T step) {
        return getModifierFromName(step.getModifier()) | InputEvent.BUTTON1_MASK;
    }


    @Override
    protected void initDescriptor(Component comp, T step) {
        TableCellRenderer renderer = getTableHeaderRenderer(comp, step);
        if (renderer != null) {
            descriptor = Introspector.getTestBehavior(getTableHeaderRenderer(comp, step).getClass(),
                                                      ClickTableHeaderDescriptor.class);
        }
    }

    @Override
    protected void setReferencePointIfNeeded(MouseEventData eventData, Component comp, T step) {
        if (descriptor != null) {
            descriptor.setReferencePointIfNeeded(eventData, comp, step);
        }
        else {
            if (JTable.class.isInstance(comp)) {
                JTable table = (JTable)comp;
                int columnNumber = TableTools.searchColumn(table, step.getColumn());

                Rectangle cellRect = table.getTableHeader().getHeaderRect(columnNumber);

                cellRect.x = cellRect.x + cellRect.width / 2;
                cellRect.y = cellRect.y - cellRect.height / 2;

                eventData.setPosition(EventDataConstants.CUSTOM);
                eventData.setReferencePoint(cellRect.getLocation());
            }
        }
    }


    public Component getTableHeaderComponent(Component comp, T step) {
        if (step.getComponent()) {
            JTable table = (JTable)comp;
            int columnNumber = TableTools.searchColumn(table, step.getColumn());
            TableTools.checkTableHeaderExists(table, columnNumber);
            TableColumn tableColumn = table.getColumnModel().getColumn(columnNumber);
            TableCellRenderer renderer = tableColumn.getHeaderRenderer();
            return renderer.getTableCellRendererComponent(table, "", false, false, 0, columnNumber);
        }
        return comp;
    }


    public TableCellRenderer getTableHeaderRenderer(Component comp, T step) {
        JTable table = (JTable)comp;
        int columnNumber = TableTools.searchColumn(table, step.getColumn());
        TableTools.checkTableHeaderExists(table, columnNumber);
        TableColumn tableColumn = table.getColumnModel().getColumn(columnNumber);
        return tableColumn.getHeaderRenderer();
    }
}
