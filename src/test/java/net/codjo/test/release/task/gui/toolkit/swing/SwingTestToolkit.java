package net.codjo.test.release.task.gui.toolkit.swing;

import net.codjo.test.release.task.gui.toolkit.GuiStep;

public class SwingTestToolkit extends SwingGUIToolkit {
	private GuiStep latestStep;
	
	@Override
	GuiStep createStep(net.codjo.test.release.task.gui.GuiStep step) {
		latestStep = null;
		latestStep = super.createStep(step);
		return latestStep;
	}
	
	public GuiStep getLatestStep() {
		return latestStep;
	}
}
