/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

/**
 * Classe permettant de v�rifier qu'un composant est visible ou non.
 */
public class AssertVisibleStep extends AbstractAssertStep {
    private String name;
    private boolean expected = true;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public boolean isExpected() {
        return expected;
    }


    public void setExpected(boolean expected) {
        this.expected = expected;
    }
}
