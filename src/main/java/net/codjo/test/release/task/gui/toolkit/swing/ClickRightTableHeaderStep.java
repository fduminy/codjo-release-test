package net.codjo.test.release.task.gui.toolkit.swing;

import java.awt.event.InputEvent;

public class ClickRightTableHeaderStep<T extends net.codjo.test.release.task.gui.ClickRightTableHeaderStep> extends ClickButtonTableHeaderStep<T> {

    @Override
    protected int getButtonMask() {
        return InputEvent.BUTTON3_MASK;
    }
}
