/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

/**
 * Classe permettant de v�rifier la valeur d'un {@link javax.swing.JComponent}.
 */
public class AssertValueStep extends AbstractMatchingStep {
    private String name;
    private String mode;
    private String dialogTitle;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getMode() {
        return mode;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public String getDialogTitle() {
        return dialogTitle;
    }


    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    @Override
    public String getComponentName() {
        return "'" + (getDialogTitle() != null ? getDialogTitle() : getName()) + "'";
    }
}
