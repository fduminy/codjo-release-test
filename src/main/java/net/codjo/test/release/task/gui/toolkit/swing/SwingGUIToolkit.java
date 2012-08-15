package net.codjo.test.release.task.gui.toolkit.swing;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.WindowMonitor;
import net.codjo.test.release.task.gui.AbstractGuiStep;
import net.codjo.test.release.task.gui.GuiContext;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager.GUIToolkit;

import org.apache.tools.ant.Project;

public class SwingGUIToolkit implements GUIToolkit<StepPlayer, SwingGuiContext> {
	public void proceed(GuiStep step, net.codjo.test.release.task.gui.TestContext context) {
		net.codjo.test.release.task.gui.toolkit.GuiStep gs = createStep(step);
		gs.proceed(context, step);
	}
	
	@SuppressWarnings("unchecked")
	net.codjo.test.release.task.gui.toolkit.GuiStep createStep(GuiStep step) {
		String className = step.getClass().getName();
		className = className.replace("net.codjo.test.release.task.gui.", "net.codjo.test.release.task.gui.toolkit.swing.");
		try {
			Class<? extends net.codjo.test.release.task.gui.toolkit.GuiStep> clazz = (Class<? extends net.codjo.test.release.task.gui.toolkit.GuiStep>) Class.forName(className);
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void setUpConfiguration() {
        TestHelper.setKeyMapping(new FrenchKeyMapping());
	}

	public void showClient(GuiContext guiContext) {
		SwingGuiContext swingContext = (SwingGuiContext) guiContext;
        for (Window window : swingContext.getWindows()) {
            window.setVisible(true);
        }
	}

	public void hideClient(GuiContext guiContext) {
		SwingGuiContext swingContext = (SwingGuiContext) guiContext;
        List<Window> windows = new ArrayList<Window>();
        for (Window window : WindowMonitor.getWindows()) {
            if (window.isVisible()) {
                window.setVisible(false);
                windows.add(window);
            }
        }
        swingContext.getWindows().clear();
        swingContext.getWindows().addAll(windows);
	}

	public SwingGuiContext createGuiContext(Project project, String testDirectory) {
        return new SwingGuiContext(createStepPlayer(project, testDirectory));
	}

	public StepPlayer createStepPlayer(Project project, String testDirectory) {
        return new StepPlayer(this, project, testDirectory);
	}

	public TestContext createTestContext(
			StepPlayer stepPlayer, Project project) {
        return new net.codjo.test.release.task.gui.toolkit.swing.TestContext(stepPlayer, project);
	}

	@SuppressWarnings("unchecked")
	public Object[] prepareSteps(
			net.codjo.test.release.task.gui.AssertProgressDisplayStep assertProgressDisplayStep) {
		AssertProgressDisplayStep<net.codjo.test.release.task.gui.AssertProgressDisplayStep> step = (AssertProgressDisplayStep<net.codjo.test.release.task.gui.AssertProgressDisplayStep>) createStep(assertProgressDisplayStep);				
		ProgressDisplayPreparationStep preparationStep = new ProgressDisplayPreparationStep(assertProgressDisplayStep, step.getPreparationStep());
		return new Object[] {preparationStep, step};
	}

	public static class ProgressDisplayPreparationStep extends AbstractGuiStep {
		private final net.codjo.test.release.task.gui.toolkit.GuiStep guiStep;
		private final net.codjo.test.release.task.gui.AssertProgressDisplayStep assertProgressDisplayStep;
		private ProgressDisplayPreparationStep(net.codjo.test.release.task.gui.AssertProgressDisplayStep assertProgressDisplayStep, net.codjo.test.release.task.gui.toolkit.GuiStep guiStep) {
			this.guiStep = guiStep;
			this.assertProgressDisplayStep = assertProgressDisplayStep;
		}
		
		public net.codjo.test.release.task.gui.AssertProgressDisplayStep getAssertProgressDisplayStep() {
			return assertProgressDisplayStep;
		}
		
	    public void proceed(TestContext context) {
	    	guiStep.proceed(context, this);
	    }
	}
}
