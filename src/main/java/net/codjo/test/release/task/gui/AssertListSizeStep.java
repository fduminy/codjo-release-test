/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;


/**
 * Classe de base pour l'assertSize d'une : {@link javax.swing.JTable} ou une {@link javax.swing.JList} ou une
 * {@link javax.swing.JComboBox}
 *
 * @version $Revision: 1.12 $
 */
public class AssertListSizeStep extends AbstractAssertStep {
    private String name;
    private int expected = -1;


    public int getExpected() {
        return expected;
    }


    public void setExpected(int expected) {
        this.expected = expected;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }    
}
