package net.codjo.test.release.task.gui;

public abstract class AbstractClickStep extends AbstractClickPopupMenuStep {
    protected int count = 1;
    protected String modifier;

    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }


    public String getModifier() {
        return modifier;
    }


    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
