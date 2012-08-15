/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

/**
 * Classe permettant de v�rifier la s�lection d'une ligne dans une {@link javax.swing.JList}, une {@link
 * javax.swing.JTable} ou un {@link javax.swing.JTree}.
 */
public class AssertSelectedStep extends AbstractAssertStep {
    private String name;
    private int row = -1;
    private int column = -1;
    private boolean expected = true;
    private String path;
    private String mode = DISPLAY_MODE;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getRow() {
        return row;
    }


    public void setRow(int row) {
        this.row = row;
    }


    public int getColumn() {
        return column;
    }


    public void setColumn(int column) {
        this.column = column;
    }


    public boolean isExpected() {
        return expected;
    }


    public void setExpected(boolean expected) {
        this.expected = expected;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public String getPath() {
        return path;
    }


    public String getMode() {
        return mode;
    }
}
