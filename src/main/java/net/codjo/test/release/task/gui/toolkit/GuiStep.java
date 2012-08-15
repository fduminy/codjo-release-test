/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit;

import net.codjo.test.release.task.gui.TestContext;

/**
 * Interface executant une �tape de test IHM.
 */
public interface GuiStep {
    void proceed(TestContext context, net.codjo.test.release.task.gui.GuiStep step);
}
