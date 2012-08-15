package net.codjo.test.release.task.gui.toolkit;

import java.awt.Component;

import javax.swing.JTable;

import junit.extensions.jfcunit.JFCTestCase;
import net.codjo.test.common.LogString;
import net.codjo.test.release.task.gui.AssertTableStep;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager.GUIToolkit;
import net.codjo.test.release.task.gui.toolkit.swing.AssertTableStepTest;
import net.codjo.test.release.task.gui.toolkit.swing.SwingGuiTestUtils;
import net.codjo.test.release.task.gui.toolkit.swing.SwingTestToolkit;

public class GuiToolkitManagerUtils {
	public static void initTestToolkit() {
		GUIToolkit<?, ?> toolkit = GUIToolkitManager.getGUIToolkit();
		if (!(toolkit instanceof SwingTestToolkit)) {
			GUIToolkitManager.setGUIToolkit(new SwingTestToolkit());
		}
	}
	
	public static SwingTestToolkit getSwingTestToolkit() {
		return (SwingTestToolkit) GUIToolkitManager.getGUIToolkit();
	}

	public static void proceedAndAssert(net.codjo.test.release.task.gui.GuiStep step,
			JFCTestCase testCase, LogString logString, String string,
			Component myComponent) {
        initTestToolkit();
        step.proceed(SwingGuiTestUtils.createTestContext(testCase));
        GuiStep latestStep = getSwingTestToolkit().getLatestStep(); 
        logString.assertContent(
              string + "(" + myComponent + ", " + latestStep + ")");
        logString.clear();
	}
}
