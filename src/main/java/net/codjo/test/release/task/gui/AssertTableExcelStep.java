/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

import net.codjo.test.common.excel.matchers.CellValueStringifier;

/**
 * Classe permettant de v�rifier le contenu d'une {@link javax.swing.JTable} via un fichier Excel.
 */
public class AssertTableExcelStep extends AbstractAssertStep {
    private String name;
    private String file;
    private String sheetName;
    private int expectedRowCount;
    private int expectedColumnCount = -1;
    private String excludedColumns;
    private CellValueStringifier cellValueStringifier = new CellValueStringifier();
    private boolean checkColumnOrder;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getFile() {
        return file;
    }


    public void setFile(String file) {
        this.file = file;
    }


    public String getSheetName() {
        return sheetName;
    }


    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }


    public void setExcludedColumns(String excludedColumns) {
        this.excludedColumns = excludedColumns;
    }


    public void setExpectedRowCount(int expectedRowCount) {
        this.expectedRowCount = expectedRowCount;
    }


    public void setCheckColumnOrder(boolean checkColumnOrder) {
        this.checkColumnOrder = checkColumnOrder;
    }


    public void setExpectedColumnCount(int expectedColumnCount) {
        this.expectedColumnCount = expectedColumnCount;
    }


    public int getExpectedRowCount() {
		return expectedRowCount;
	}


    public int getExpectedColumnCount() {
		return expectedColumnCount;
	}


    public String getExcludedColumns() {
		return excludedColumns;
	}


    public CellValueStringifier getCellValueStringifier() {
		return cellValueStringifier;
	}


    public boolean isCheckColumnOrder() {
		return checkColumnOrder;
	}
    
    
}
