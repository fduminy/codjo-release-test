/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

/**
 * Classe permettant de v�rifier le contenu d'une {@link javax.swing.JTable}.
 */
public class AssertListStep extends AbstractAssertStep {
    private String name;
    private String expected;
    private int row = -1;
    private String mode;


    public String getName() {
        return name;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public String getMode() {
        return mode;
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


    public int getRow() {
        return row;
    }


    public void setRow(int row) {
        this.row = row;
    }
}
