/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
import static java.lang.Integer.valueOf;

import java.awt.Color;
/**
 * Classe permettant de v�rifier le contenu d'une {@link javax.swing.JTree}.
 */
public class AssertTreeStep extends AbstractAssertStep {
    private String name;
    private String path;
    private int row = -1;
    private Color foreground;
    private String icon;
    private boolean exists = true;
    private String mode = DISPLAY_MODE;


    public boolean isExists() {
        return exists;
    }


    public void setExists(boolean exists) {
        this.exists = exists;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public String getMode() {
        return mode;
    }


    public int getRow() {
        return row;
    }


    public void setRow(int row) {
        this.row = row;
    }


    public Color getForeground() {
        return foreground;
    }


    public void setForeground(String rgb) {
        String[] rgbArray = rgb.split(",");
        try {
            foreground = new Color(valueOf(rgbArray[0]), valueOf(rgbArray[1]), valueOf(rgbArray[2]));
        }
        catch (NumberFormatException e) {
            throw new GuiException("Invalid rgb format : " + rgb, e);
        }
    }


    public String getIcon() {
        return icon;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }
}
