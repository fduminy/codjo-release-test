package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import net.codjo.test.release.task.gui.GuiContext;
class SwingGuiContext extends GuiContext {
    private final List<Window> windows = new ArrayList<Window>();

    SwingGuiContext(net.codjo.test.release.task.gui.toolkit.swing.StepPlayer stepPlayer) {
        super(stepPlayer);
    }

    public List<Window> getWindows() {
        return windows;
    }
}
