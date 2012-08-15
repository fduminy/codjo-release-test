/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
import static java.lang.Integer.valueOf;

import java.awt.Color;
/**
 * Classe permettant de v�rifier le contenu d'une {@link javax.swing.JTable}.
 */
public class AssertTableStep extends AbstractMatchingStep {
    private String name;
    private int row = -1;
    private String column = "-1";
    private Color background;
    private String mode = AUTO_MODE;
    private String expectedCellRenderer;


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


    public String getColumn() {
        return column;
    }


    public void setColumn(String column) {
        this.column = column;
    }


    public String getMode() {
        return mode;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public Color getBackground() {
        return background;
    }


    public void setBackground(String rgb) {
        String[] rgbArray = rgb.split(",");
        try {
            background = new Color(valueOf(rgbArray[0]), valueOf(rgbArray[1]), valueOf(rgbArray[2]));
        }
        catch (NumberFormatException e) {
            throw new GuiException("Invalid rgb format : " + rgb, e);
        }
    }

    @Override
    public String getComponentName() {
        return "Table '" + getName() + "' [ligne " + row + ", colonne " + column + ", mode " + getMode()
               + "]";
    }


    public String getExpectedCellRenderer() {
        return expectedCellRenderer;
    }


    public void setExpectedCellRenderer(String expectedCellRenderer) {
        this.expectedCellRenderer = expectedCellRenderer;
    }
}
