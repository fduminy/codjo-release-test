package net.codjo.test.release.task.gui.toolkit.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.codjo.test.release.task.gui.GuiStep;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Classe permettant de faire une pause durant l'execution d'un test IHM.
 */
public class PauseStep<T extends net.codjo.test.release.task.gui.PauseStep> extends AbstractGuiStep<T> {
    private static final Logger LOGGER = LogManager.getLogger(PauseStep.class);

	public void proceed(net.codjo.test.release.task.gui.TestContext context, GuiStep s) {
        try {
            pause();
        }
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    protected void pause() throws InterruptedException {
        final Lock lock = new ReentrantLock();
        final Condition continueCondition = lock.newCondition();
        final JFrame frame = new JFrame("Test IHM en attente !!");
        JButton button = new JButton("Continue");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                lock.lock();
                try {
                    continueCondition.signal();
                }
                finally {
                    lock.unlock();
                }
                frame.dispose();
            }
        });
        frame.add(button);
        frame.pack();

        lock.lock();
        try {
            frame.setVisible(true);
            continueCondition.await();
        }
        finally {
            lock.unlock();
        }
    }
}
