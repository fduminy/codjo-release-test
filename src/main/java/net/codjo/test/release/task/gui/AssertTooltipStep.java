package net.codjo.test.release.task.gui;

/**
 *
 */
public class AssertTooltipStep extends AbstractMatchingStep {
    private String name;
    private int row;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getRow() {
        return row;
    }


    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public String getComponentName() {
        return "Component '" + getName() + "'";
    }
}
