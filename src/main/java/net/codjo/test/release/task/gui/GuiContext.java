package net.codjo.test.release.task.gui;
public class GuiContext {
    private final StepPlayer stepPlayer;

    protected GuiContext(StepPlayer stepPlayer) {
    	this.stepPlayer = stepPlayer;
    }
    
    public StepPlayer getStepPlayer() {
        return stepPlayer;
    }
}
