/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.EditCellStep.NO_NAME_HAS_BEEN_SET;

import javax.swing.JTable;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;

/**
 *
 */
public abstract class AbstractTableEditionStep<T extends net.codjo.test.release.task.gui.AbstractTableEditionStep> extends AbstractGuiStep<T> {
    protected abstract void finishEditing(JTable table);

    @SuppressWarnings("unchecked")
	public final void proceed(TestContext context, net.codjo.test.release.task.gui.GuiStep s) {
		T step = (T) s;
    	String name = step.getName(); 
        if (name == null) {
            throw new GuiFindException(NO_NAME_HAS_BEEN_SET);
        }
        NamedComponentFinder finder = new NamedComponentFinder(JTable.class, name);
        final JTable table = (JTable)findOnlyOne(finder, context, step);
        if (!table.isEditing()) {
            throw new GuiConfigurationException("La table '" + name + "' n'est plus en �dition.");
        }
        try {
            runAwtCodeLater(context, new Runnable() {
                public void run() {
                    finishEditing(table);
                }
            });
        }
        catch (Exception e) {
            throw new GuiException("Impossible de " + getClass().getSimpleName() + " sur '" + name + "'", e);
        }
    }
}
