package net.codjo.test.release.task.gui.toolkit.swing;
import javax.swing.JList;

import net.codjo.test.release.task.gui.GuiFindException;
/**
 *
 */
public class ListTools {
    private ListTools() {
    }


    public static void checkRowExists(JList list, int row) {
        if (row < 0 || row >= list.getModel().getSize()) {
            throw new GuiFindException("La ligne [row=" + row + "] n'existe pas dans la liste");
        }
    }
}
