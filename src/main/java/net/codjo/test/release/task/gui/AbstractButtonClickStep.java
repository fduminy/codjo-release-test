package net.codjo.test.release.task.gui;

import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;


/**
 *
 */
public abstract class AbstractButtonClickStep extends AbstractClickStep {
	public static final String MATCHER_EQUALS = "equals";
    public static final String MATCHER_CONTAINS = "contains";

    private String menu;
    private String label;
    private String matcher = MATCHER_EQUALS;
    private int row = 0;
    private String path;
    private String mode;


    public String getMenu() {
        return menu;
    }


    public void setMenu(String menu) {
        this.menu = menu;
    }


    public void setRow(int row) {
        this.row = row;
    }


    public int getRow() {
        return row;
    }


    public String getLabel() {
        return label;
    }


    public void setLabel(String label) {
        this.label = label;
    }


    public String getMatcher() {
        return matcher;
    }


    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }


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
