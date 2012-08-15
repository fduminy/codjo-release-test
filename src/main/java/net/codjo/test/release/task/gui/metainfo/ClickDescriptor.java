package net.codjo.test.release.task.gui.metainfo;
import java.awt.Component;

import net.codjo.test.release.task.gui.toolkit.swing.AbstractClickStep;
import junit.extensions.jfcunit.eventdata.MouseEventData;
/**
 *
 */
public interface ClickDescriptor<T extends net.codjo.test.release.task.gui.AbstractClickStep> {
    public Component getComponentToClick(Component comp, T step);


    public void setReferencePointIfNeeded(MouseEventData eventData, Component component, T step);
}
