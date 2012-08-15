package net.codjo.test.release.task.gui.metainfo;
import java.awt.Component;

import net.codjo.test.common.LogString;
import net.codjo.test.release.task.gui.SetValueStep;
/**
 *
 */
public class AnotherComponentMockTestBehavior implements SetValueDescriptor{
        static public LogString LOG;

        public void setValue(Component comp, SetValueStep step) {
            LOG.call("AnotherComponentMockTestBehavior.setValue", comp, step);
            ((AnotherComponentMock)comp).specificMethod(step.getValue());
        }

}
