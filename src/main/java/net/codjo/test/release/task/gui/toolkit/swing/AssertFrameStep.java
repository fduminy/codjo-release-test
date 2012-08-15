/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import junit.extensions.jfcunit.finder.Finder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.finder.FrameFinder;
import net.codjo.test.release.task.gui.matcher.MatcherFactory;
/**
 * Classe permettant de v�rifier l'�tat d'une JInternalFrame.
 */
public class AssertFrameStep<T extends net.codjo.test.release.task.gui.AssertFrameStep> extends AbstractAssertStep<T> {
    @Override
    protected int getFinderOperation(T step) {
        return MatcherFactory.getFinderOperationFromMatching(step.getMatching());
    }

	protected void proceedOnce(TestContext context, T step) {
        String parsedTitle = context.replaceProperties(step.getTitle());
        Finder finder = new FrameFinder(parsedTitle);
        Component frame = findOnlyOne(finder, context, 0, step);
        if (step.getClosed()) {
            if (frame != null) {
                throw new GuiAssertException("La fen�tre '" + parsedTitle + "' n'est pas ferm�e");
            }
        }
        else {
            if (frame == null) {
                throw new GuiAssertException("La fen�tre '" + parsedTitle + "' n'est pas ouverte");
            }
        }
    }
}
