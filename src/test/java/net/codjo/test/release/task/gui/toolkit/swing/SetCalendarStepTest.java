package net.codjo.test.release.task.gui.toolkit.swing;
import net.codjo.test.common.LogString;
import net.codjo.test.release.task.gui.SetCalendarStep;

import net.codjo.test.release.task.gui.metainfo.MyCalendarMock;
import net.codjo.test.release.task.gui.metainfo.MyCalendarMockTestBehavior;
import net.codjo.test.release.task.gui.toolkit.GuiStep;
import net.codjo.test.release.task.gui.toolkit.GuiToolkitManagerUtils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import junit.extensions.jfcunit.JFCTestCase;
/**
 *
 */
public class SetCalendarStepTest extends JFCTestCase {
    private SetCalendarStep step = new SetCalendarStep();
    private JTextField myComponent;


    private void showFrame() {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        frame.setContentPane(panel);

        myComponent = new MyCalendarMock();
        myComponent.setName("fred");
        panel.add(myComponent);

        frame.pack();
        frame.setVisible(true);
        flushAWT();
    }


    public void test_specificTestBehavior() throws Exception {
        showFrame();
        LogString logString = new LogString();
        MyCalendarMockTestBehavior.LOG = logString;
        step.setName("fred");
        step.setValue("Une autre valeur");
        
        GuiToolkitManagerUtils.proceedAndAssert(step, this, logString, "MyCalendarMockTestBehavior.setCalendarValue", myComponent);
    }
}
