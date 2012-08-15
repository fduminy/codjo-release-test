package net.codjo.test.release.task.gui.toolkit.swing;

import java.awt.event.InputEvent;

public class ClickMiddleStep<T extends net.codjo.test.release.task.gui.ClickMiddleStep> extends AbstractButtonClickStep<T> {
    @Override
    protected int getMouseModifiers(T step) {
        return InputEvent.BUTTON2_MASK;
    }
}
