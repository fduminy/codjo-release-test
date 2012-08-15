package net.codjo.test.release.task.gui.toolkit;

import net.codjo.test.release.task.gui.AssertProgressDisplayStep;
import net.codjo.test.release.task.gui.GuiContext;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.StepPlayer;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.toolkit.swing.SwingGUIToolkit;

import org.apache.tools.ant.Project;

public class GUIToolkitManager {
	private static GUIToolkit<?, ?> INSTANCE = new SwingGUIToolkit();
	
	public static GUIToolkit<?, ?> getGUIToolkit() {
		return INSTANCE;
	}
	
	static void setGUIToolkit(GUIToolkit<?, ?> toolkit) {
		INSTANCE = toolkit;
	}
	
	public static interface GUIToolkit<SP extends StepPlayer, GC extends GuiContext> {

		void proceed(GuiStep step, TestContext context);

		void setUpConfiguration();

		void showClient(GuiContext guiContext);

		void hideClient(GuiContext guiContext);

		GC createGuiContext(Project project, String testDirectory);

		SP createStepPlayer(Project project, String testDirectory);
		public Object[] prepareSteps(
				AssertProgressDisplayStep assertProgressDisplayStep);
	}	
}
