package net.codjo.test.release.task.gui;

/**
 * Classe permettant d'asserter le titre d'une bordure.
 */
public class AssertTitleBorderStep extends AbstractMatchingStep {
    private String name;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getComponentName() {
        return "Component '" + getName() + "' TitleBorder";
    }
}
