package net.codjo.test.release.task.gui.metainfo;
import javax.swing.JTable;

import net.codjo.test.common.LogString;
import net.codjo.test.release.task.gui.toolkit.swing.AssertTableStep;
public class MyTableCellRendererTestBehavior implements AssertTableDescriptor {
    static public LogString LOG;


    @SuppressWarnings("rawtypes")
	public void assertTable(JTable table, AssertTableStep step) {
        LOG.call(getClass().getSimpleName() + ".assertTable", table, step);
    }
}
