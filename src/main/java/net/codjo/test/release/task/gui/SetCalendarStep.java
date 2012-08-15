package net.codjo.test.release.task.gui;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;
/**
 *
 */
public class SetCalendarStep extends AbstractGuiStep {
    private String name;
    private String value;


    static public String computeUneditableComponent(String componentName) {
        return "Le composant '" + componentName + "' n'est pas �ditable.";
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
