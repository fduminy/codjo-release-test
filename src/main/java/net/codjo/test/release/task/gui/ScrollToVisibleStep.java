package net.codjo.test.release.task.gui;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

public class ScrollToVisibleStep extends AbstractGuiStep {
    private String name;
    private String scrollPane;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getScrollPane() {
        return scrollPane;
    }


    public void setScrollPane(String scrollPane) {
        this.scrollPane = scrollPane;
    }


	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
