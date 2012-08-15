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
public class ClickTableHeaderStep extends AbstractClickStep {
    private boolean component;

	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
    
    public boolean getComponent() {
        return component;
    }


    public void setComponent(boolean component) {
        this.component = component;
    }
}
