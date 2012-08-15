/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

/**
 *
 */
public abstract class AbstractTableEditionStep extends AbstractGuiStep {
    private String name;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
		return name;
	}

	public final void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
