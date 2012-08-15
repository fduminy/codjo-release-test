package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.toolkit.swing.AssertProgressDisplayStep.DisplayStep.hidden;
import static net.codjo.test.release.task.gui.toolkit.swing.AssertProgressDisplayStep.DisplayStep.neverShown;
import static net.codjo.test.release.task.gui.toolkit.swing.AssertProgressDisplayStep.DisplayStep.shown;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.finder.FastGlassPaneComponentFinder;

public class AssertProgressDisplayStep<T extends net.codjo.test.release.task.gui.AssertProgressDisplayStep> extends AbstractAssertStep<T> implements ComponentListener {
    private Component glassPane;
    enum DisplayStep {
        neverShown,
        shown,
        hidden
    }
    private DisplayStep state = neverShown;

	protected void proceedOnce(TestContext context, final T step) {
        try {
            runAwtCode(context, new Runnable() {
                public void run() {
                    if (state == neverShown) {
                        throw new GuiFindException("WaitingPanel '" + step.getName() + "' ne s'est pas affich�.");
                    }
                    if (state == shown) {
                        throw new GuiFindException("WaitingPanel '" + step.getName() + "' n'a pas disparu.");
                    }
                    // state = hidden : OK.
                }
            });
        }
        catch (Exception e) {
            throw new GuiFindException(e.getCause().getMessage(), e.getCause());
        }
    }


    @SuppressWarnings("rawtypes")
	public net.codjo.test.release.task.gui.toolkit.GuiStep getPreparationStep() {
        return new ProgressDisplayPreparationStep();
    }


    public void componentResized(ComponentEvent event) {
    }


    public void componentMoved(ComponentEvent event) {
    }


    public void componentShown(ComponentEvent event) {
        if (state == neverShown) {
            state = shown;
        }
    }


    public void componentHidden(ComponentEvent event) {
        if (state == shown) {
            state = hidden;
        }
    }


    @Override
    protected void beforeStop() {
        glassPane.removeComponentListener(this);
    }


    class ProgressDisplayPreparationStep<TPS extends SwingGUIToolkit.ProgressDisplayPreparationStep> extends AbstractGuiStep<TPS> {
    	@SuppressWarnings("unchecked")
		public void proceed(TestContext context, GuiStep s) {
    		final TPS step = (TPS) s;
            try {
                runAwtCode(context, new Runnable() {
                    public void run() {
                    	String name = step.getAssertProgressDisplayStep().getName();
                        glassPane = new FastGlassPaneComponentFinder(name).findOnlyOne();
                        if (glassPane == null) {
                            throw new GuiFindException("WaitingPanel '" + name + "' non trouv�.");
                        }

                        glassPane.addComponentListener(AssertProgressDisplayStep.this);
                    }
                });
            }
            catch (Exception e) {
                throw new GuiFindException(e.getCause().getMessage(), e.getCause());
            }
        }
    }
}
