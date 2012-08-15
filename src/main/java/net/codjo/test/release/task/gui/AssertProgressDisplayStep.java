package net.codjo.test.release.task.gui;

import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager.GUIToolkit;

public class AssertProgressDisplayStep extends AbstractAssertStep {
    private String name;
    
    private net.codjo.test.release.task.gui.toolkit.GuiStep step;
    
    public AssertProgressDisplayStep() {
        setTimeout(50);
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public GuiStep getPreparationStep() {
    	if (step != null) {
    		throw new IllegalStateException("steps are already defined");
    	}
    	
    	Object[] steps = GUIToolkitManager.getGUIToolkit().prepareSteps(this);
    	step = (net.codjo.test.release.task.gui.toolkit.GuiStep) steps[1];
    	return (GuiStep) steps[0];
    }
    
    @Override
    public void proceed(TestContext context) {
    	step.proceed(context, this);
    }
}
