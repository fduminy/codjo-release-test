package net.codjo.test.release.task.gui.toolkit.swing;

import junit.extensions.jfcunit.JFCTestCase;
import net.codjo.test.release.task.gui.GuiStep;

public class SwingGuiTestUtils {
	private static final SwingGUIToolkit SWING_GUI_TOOLKIT = new SwingGUIToolkit();
	
	public static SwingGUIToolkit getSwingGUIToolkit() {
		return SWING_GUI_TOOLKIT;
	}
	
	public static net.codjo.test.release.task.gui.toolkit.GuiStep createGuiStep(GuiStep step) {
		return SWING_GUI_TOOLKIT.createStep(step);
	}
	
    @SuppressWarnings("unchecked")
	public static <T extends net.codjo.test.release.task.gui.AbstractAssertStep> void proceedOnce(TestContext context, T step) {
    	net.codjo.test.release.task.gui.toolkit.swing.AbstractAssertStep<T> s =
    			(net.codjo.test.release.task.gui.toolkit.swing.AbstractAssertStep<T>)
    			SwingGuiTestUtils.createGuiStep(step);
    	s.proceedOnce(context, step);
    }	
    
	public static TestContext createTestContext(JFCTestCase testCase) {
		return new TestContext(testCase);
	}
}
