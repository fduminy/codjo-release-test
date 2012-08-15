/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;


/**
 * Classe de base des steps de test IHM.
 */
public abstract class AbstractGuiStep implements GuiStep {
    private int timeout = 15;
    public static final String MODEL_MODE = "model";
    public static final String DISPLAY_MODE = "display";
    public static final String AUTO_MODE = "auto";


    public int getTimeout() {
        return timeout;
    }


    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
