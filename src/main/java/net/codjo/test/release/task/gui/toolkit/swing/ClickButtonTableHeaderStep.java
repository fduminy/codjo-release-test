package net.codjo.test.release.task.gui.toolkit.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import junit.extensions.jfcunit.eventdata.AbstractMouseEventData;
import junit.extensions.jfcunit.eventdata.EventDataConstants;
import junit.extensions.jfcunit.eventdata.JTableHeaderMouseEventData;
import net.codjo.test.release.task.gui.GuiFindException;

public abstract class ClickButtonTableHeaderStep<T extends net.codjo.test.release.task.gui.ClickButtonTableHeaderStep> extends AbstractClickButtonStep<T> {
    private static final int NUMBER_OF_CLICKS = 1;


    @Override
    protected boolean acceptAndProceed(net.codjo.test.release.task.gui.TestContext context, Component comp, T step) throws Exception {
        if (comp instanceof JTable) {
            proceedTableHeader((TestContext) context, (JTable) comp, step);
            return true;
        }
        return false;
    }


    protected void proceedTableHeader(TestContext context, JTable table, T step) {
        int realColumn = 0;

        if (!"".equals(step.getColumn())) {
            realColumn = TableTools.searchColumn(table, step.getColumn());
        }
        if (realColumn >= table.getColumnCount()) {
            throw new GuiFindException("La colonne '" + step.getColumn() + "' est introuvable dans la table '"
                    + step.getName() + "'");
        }

        showPopupAndSelectItem(table.getTableHeader(), context,
                getMouseEventData(context, table, realColumn, step), step);
    }


    @Override
    protected AbstractMouseEventData getMouseEventData(net.codjo.test.release.task.gui.TestContext context, JTable table, int realColumn, T step) {
        JTableHeader tableHeader = table.getTableHeader();

        return new JTableHeaderMouseEventData(((TestContext) context).getTestCase(),
                tableHeader,
                realColumn,
                NUMBER_OF_CLICKS,
                getButtonMask(),
                true,
                EventDataConstants.DEFAULT_SLEEPTIME);
    }

    protected abstract int getButtonMask();
}
