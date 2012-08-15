package net.codjo.test.release.task.gui;

/**
 *
 */
public class AssertTabCountStep extends AbstractAssertStep {

    private String name;
    private int tabCount;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

   public int getTabCount() {
        return tabCount;
    }


    public void setTabCount(int tabCount) {
        this.tabCount = tabCount;
    }
}
