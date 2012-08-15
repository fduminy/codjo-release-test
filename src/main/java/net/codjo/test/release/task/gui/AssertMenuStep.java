/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

/**
 * Classe permettant de verifier le contenu d'un menu (radio check ) et la taille d'un menu
 */
public class AssertMenuStep extends AbstractAssertStep {
    private boolean checked;
    private String name;
    private String label;

    public String getLabel() {
        return label;
    }


    public void setLabel(String label) {
        this.label = label;
    }


    public boolean isChecked() {
        return checked;
    }


    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
