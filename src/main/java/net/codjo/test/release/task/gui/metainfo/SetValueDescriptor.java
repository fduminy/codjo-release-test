package net.codjo.test.release.task.gui.metainfo;
import java.awt.Component;

import net.codjo.test.release.task.gui.SetValueStep;
/**
 *
 */
public interface SetValueDescriptor {
     public abstract void setValue(Component comp, SetValueStep step);
}
