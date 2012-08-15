/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;

import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;

import org.apache.log4j.Logger;

/**
 * Classe m�re de tous les AssertSteps : elle permet d'utiliser un m�canisme de retry et de timeout.
 *
 * @version $Revision: 1.6 $
 */
public abstract class AbstractAssertStep<T extends net.codjo.test.release.task.gui.AbstractAssertStep> extends AbstractGuiStep<T> {
    private final Logger logger = Logger.getLogger(getClass());

    /**
     * Appelle de mani�re r�p�t�e {@link #proceedOnce(TestContext)} tant qu'une exception survient.
     *
     * @param context le contexte du test
     */
	@SuppressWarnings("unchecked")
	public final void proceed(TestContext context, GuiStep s) {
		T step = (T) s;
		
        GuiException exception;
        long begin = System.currentTimeMillis();
        int attemptIndex = 0;
        do {
            attemptIndex++;
            try {
                proceedOnce(context, step);
                beforeStop();
                return;
            }
            catch (GuiException ex) {
                exception = ex;
                logger.debug(String.format(
                      "La tentative num�ro %d de l'assert a �chou�e. Mise en attente pour laisser le thread AWT travailler...",
                      attemptIndex));
                sleep(step);
            }
        }
        while (System.currentTimeMillis() - begin < step.getTimeout() * step.getWaitingNumber());
        beforeStop();
        throw exception;
    }

    protected void beforeStop() {
    }

    /**
     * Effectue l'assert une fois. Renvoie une exception si l'assert est faux. Cela peut arriver si l'IHM n'a
     * pas eu le temps de se mettre � jour.
     *
     * @param context le contexte du test.
     * @param step Le step a executer.
     */
    protected abstract void proceedOnce(TestContext context, T step);


    protected void sleep(T step) {
        try {
            Thread.sleep(step.getDelay());
        }
        catch (InterruptedException e) {
            ;
        }
    }
}
