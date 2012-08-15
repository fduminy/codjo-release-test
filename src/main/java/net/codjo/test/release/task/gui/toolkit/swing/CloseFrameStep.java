/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.awt.Window;

import javax.swing.JInternalFrame;

import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.finder.FrameFinder;
/**
 * Step permettant de fermer une frame.
 */
public class CloseFrameStep<T extends net.codjo.test.release.task.gui.CloseFrameStep> extends AbstractGuiStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(TestContext context, GuiStep s) {
		T step = (T) s;
        FrameFinder finder = new FrameFinder(step.getTitle());
        final Component frame = findOnlyOne(finder, context, step);

        if (frame == null) {
            throw new GuiFindException("La fen�tre '" + step.getTitle() + "' n'est pas ouverte");
        }

        try {
            runAwtCode(context,
                       new Runnable() {
                           public void run() {
                               dispose(frame);
                           }
                       });
        }
        catch (Exception error) {
            throw new GuiException("Impossible de fermer '" + step.getTitle() + "'", error);
        }
    }


    private void dispose(Component frame) {
        if (frame instanceof JInternalFrame) {
            ((JInternalFrame)frame).doDefaultCloseAction();
        }
        else if (frame instanceof Window) {
            Window window = (Window)frame;
            window.setVisible(false);
            window.dispose();
        }
        else {
            throw new GuiException("Type de composant non g�r� par la balise CloseFrameStep");
        }
    }
}
