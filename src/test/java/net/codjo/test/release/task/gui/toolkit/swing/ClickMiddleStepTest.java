package net.codjo.test.release.task.gui.toolkit.swing;

import java.awt.event.MouseEvent;

import net.codjo.test.release.task.gui.AbstractButtonClickStep;
import net.codjo.test.release.task.gui.ClickMiddleStep;


public class ClickMiddleStepTest extends AbstractClickStepTest {

    @Override
    protected AbstractButtonClickStep createClickStep() {
        return new ClickMiddleStep();
    }

    @Override
    protected boolean isCorrectClickEvent(MouseEvent event) {
        return (event.getModifiers() & MouseEvent.BUTTON2_MASK) != 0;
    }
}
