/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
/**
 *
 */
public class ClickRightStep extends AbstractClickButtonStep {
    private int row = -1;


    public ClickRightStep() {
        setTimeout(500);
    }


    public int getRow() {
        return row;
    }


    public void setRow(int row) {
        this.row = row;
    }
}
