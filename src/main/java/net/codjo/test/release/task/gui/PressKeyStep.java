package net.codjo.test.release.task.gui;

import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

public class PressKeyStep extends AbstractGuiStep {
    private String value;
    private String name;

    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
