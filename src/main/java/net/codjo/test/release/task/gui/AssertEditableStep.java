/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
/**
 *
 */
public class AssertEditableStep extends AbstractAssertStep {
    private String name;
    private String expected;
    private String row = "-1";
    private String column = "-1";


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getExpected() {
        return expected;
    }


    public void setExpected(String expected) {
        this.expected = expected;
    }
    
    public void setRow(String rowIndex) {
        this.row = rowIndex;
    }


    public void setColumn(String columnNameOrIndex) {
        this.column = columnNameOrIndex;
    }
    
    public String getRow() {
		return row;
	}
    
    public String getColumn() {
		return column;
	}
}
