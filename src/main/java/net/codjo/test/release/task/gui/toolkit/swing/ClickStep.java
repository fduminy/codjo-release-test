/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.event.InputEvent;

/**
 * Gestion du tag <code>click</code>. Ce tag permet de simuler un clic souris sur un composant graphique.
 */
public class ClickStep<T extends net.codjo.test.release.task.gui.ClickStep> extends AbstractButtonClickStep<T> {

    @Override
    protected int getMouseModifiers(T step) {
        return getModifierFromName(step.getModifier()) | InputEvent.BUTTON1_MASK;
    }
}
