package net.codjo.test.release.task.gui.toolkit.swing.converter;
import javax.swing.JTree;
/**
 *
 */
public interface TreeNodeConverter {
    String getValue(JTree tree, Object node);
}
