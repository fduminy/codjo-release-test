/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
/**
 * Classe permettant de v�rifier qu'un bouton {@link javax.swing.JButton} est activ� ou pas.
 */
public class AssertEnabledStep extends AbstractAssertStep {
    private String name;
    private String menu;
    private String expected;
    private String row = "-1";
    private String column = "-1";


    public String getMenu() {
        return menu;
    }


    public void setMenu(String menu) {
        this.menu = menu;
    }


    public String getName() {
        if (name == null && menu != null) {
            return menu;
        }
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
