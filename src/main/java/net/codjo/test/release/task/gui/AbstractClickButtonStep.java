package net.codjo.test.release.task.gui;

import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

/**
 *
 */
public abstract class AbstractClickButtonStep extends AbstractClickPopupMenuStep {
    private String path;
    private String mode;


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getMode() {
        return mode;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);    	
    }
}
