/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
import net.codjo.test.release.task.Util;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

/**
 * Permet de simuler des �ditions dans des cellules de JTable.
 *
 * @version $Revision: 1.8 $
 */
public class EditCellStep extends StepList {
    public static final String NO_NAME_HAS_BEEN_SET = "Aucun nom de composant n'a �t� sp�cifi�.";
    public static final int INITIAL_ROW_VALUE = -1;

    private int row = INITIAL_ROW_VALUE;
    private String column;
    private String path;
    private String mode;
    private AbstractTableEditionStep editionStep;


    public AbstractTableEditionStep getEditionStep() {
		return editionStep;
	}
    
    public int getRow() {
        return row;
    }


    public void setRow(int row) {
        this.row = row;
    }


    public String getColumn() {
        return column;
    }


    public void setColumn(String column) {
        this.column = column;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getMode() {
        return mode;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public void addAssertEnabled(AssertEnabledStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertFrame(AssertFrameStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertList(AssertListStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertListSize(AssertListSizeStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertMenu(AssertMenuStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertSelected(AssertSelectedStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertTable(AssertTableStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertTableExcel(AssertTableExcelStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertTree(AssertTreeStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertValue(AssertValueStep step) {
        checkOrderAndAddStep(step);
    }


    public void addAssertVisible(AssertVisibleStep step) {
        checkOrderAndAddStep(step);
    }


    public void addClick(ClickStep step) {
        checkOrderAndAddStep(step);
    }


    public void addClickRight(ClickRightStep step) {
        checkOrderAndAddStep(step);
    }


    public void addCloseFrame(CloseFrameStep step) {
        checkOrderAndAddStep(step);
    }


    public void addPause(PauseStep step) {
        checkOrderAndAddStep(step);
    }


    public void addSelect(SelectStep step) {
        checkOrderAndAddStep(step);
    }


    public void addSelectTab(SelectTabStep step) {
        checkOrderAndAddStep(step);
    }


    public void addSetValue(SetValueStep step) {
        checkOrderAndAddStep(step);
    }


    public void addSleep(SleepStep step) {
        checkOrderAndAddStep(step);
    }


    public void addCancel(CancelTableEditionStep step) {
        addTableEditionStep(step);
    }


    public void addValidate(ValidateTableEditionStep step) {
        addTableEditionStep(step);
    }


    public void addEditCell(EditCellStep step) {
        checkOrderAndAddStep(step);
    }


    public void addPressKey(PressKeyStep step) {
        addStep(step);
    }


    @Override
    public void proceed(TestContext context) {
    	GUIToolkitManager.getGUIToolkit().proceed(this, context);
    }

    static String computeNotEditableCellMessage(String columnName) {
        return "La colonne '" + columnName + "' n'est pas �ditable.";
    }


    static public String computeStepAlreadyDefinedMessage(AbstractTableEditionStep step) {
        return "Une step '" + Util.computeClassName(step.getClass()) + "' est d�j� d�finie.";
    }


    static public String computeBadTagOrderMessage(AbstractTableEditionStep step) {
        return "La step '" + Util.computeClassName(step.getClass())
               + "' doit �tre la derni�re balise du bloc.";
    }


    private void checkOrderAndAddStep(GuiStep step) {
        checkCancelOrValidateIsTheLast();
        addStep(step);
    }


    private void checkCancelOrValidateIsTheLast() {
        if (editionStep != null) {
            throw new GuiConfigurationException(computeBadTagOrderMessage(editionStep));
        }
    }


    private void addTableEditionStep(AbstractTableEditionStep step) {
        if (editionStep != null) {
            throw new GuiConfigurationException(computeStepAlreadyDefinedMessage(editionStep));
        }
        editionStep = step;
        addStep(step);
    }
}
