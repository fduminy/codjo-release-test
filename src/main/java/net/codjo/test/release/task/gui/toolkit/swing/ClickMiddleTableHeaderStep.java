package net.codjo.test.release.task.gui.toolkit.swing;

import java.awt.event.InputEvent;

import javax.swing.JTable;

import net.codjo.test.release.task.gui.GuiFindException;

public class ClickMiddleTableHeaderStep<T extends net.codjo.test.release.task.gui.ClickMiddleTableHeaderStep> extends ClickButtonTableHeaderStep<T> {
    @Override
    protected int getButtonMask() {
        return InputEvent.BUTTON2_MASK;
    }


    @Override
    protected void proceedTableHeader(TestContext context, JTable table, T step) {
        int realColumn = 0;

        if (!"".equals(step.getColumn())) {
            realColumn = TableTools.searchColumn(table, step.getColumn());
        }
        if (realColumn >= table.getColumnCount()) {
            throw new GuiFindException("La colonne '" + step.getColumn() + "' est introuvable dans la table '"
                    + step.getName() + "'");
        }

        context.getHelper().enterClickAndLeave(getMouseEventData(context, table, realColumn, step));
    }
}
