package net.codjo.test.release.task.gui;

/**
 *
 */
public class AssertTabStep extends AbstractAssertStep {
    private String name;
    private String tabLabel;
    private int tabIndex = -1;
    private boolean selected = false;
    private boolean selectedAttributeIsSet = false;
    private Boolean enabled;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getTabLabel() {
        return tabLabel;
    }


    public void setTabLabel(String tabLabel) {
        this.tabLabel = tabLabel;
    }


    public int getTabIndex() {
        return tabIndex;
    }


    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
        selectedAttributeIsSet = true;
    }


    public void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;
    }


    public boolean isSelected() {
		return selected;
	}


    public boolean isSelectedAttributeIsSet() {
		return selectedAttributeIsSet;
	}


    public Boolean getEnabled() {
		return enabled;
	}
    
    
}
