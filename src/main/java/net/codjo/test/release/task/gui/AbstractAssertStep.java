/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

import org.apache.log4j.Logger;
/**
 * Classe m�re de tous les AssertSteps : elle permet d'utiliser un m�canisme de retry et de timeout.
 *
 * @version $Revision: 1.6 $
 */
public abstract class AbstractAssertStep extends AbstractGuiStep {
    /**
     * D�lai d'attente en millisecondes entre deux retries.
     */
    private long delay = 300;

    private long waitingNumber = 1000L;


    /**
     * Appelle de mani�re r�p�t�e {@link #proceedOnce(TestContext)} tant qu'une exception survient.
     *
     * @param context le contexte du test
     */
    public void proceed(TestContext context) {
    	GUIToolkitManager.getGUIToolkit().proceed(this, context);
    }


    public long getDelay() {
        return delay;
    }


    public void setDelay(long delay) {
        this.delay = delay;
    }


    public long getWaitingNumber() {
        return waitingNumber;
    }


    public void setWaitingNumber(long waitingNumber) {
        this.waitingNumber = waitingNumber;
    }
}
