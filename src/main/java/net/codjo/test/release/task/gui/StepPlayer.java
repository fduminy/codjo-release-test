/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
/**
 * Interface permettant d'ex�cuter une {@link GuiStep}.
 *
 */
public interface StepPlayer {
    void play(GuiStep guiStep);
    
    void cleanUp();
}
