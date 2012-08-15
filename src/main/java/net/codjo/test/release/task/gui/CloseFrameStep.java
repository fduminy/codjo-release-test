/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

/**
 * Step permettant de fermer une frame.
 */
public class CloseFrameStep extends AbstractGuiStep {
    private String title;


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
