package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.awt.event.InputEvent;

import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.finder.Finder;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.metainfo.ClickDescriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public abstract class AbstractClickStep<T extends net.codjo.test.release.task.gui.AbstractClickStep> extends AbstractClickPopupMenuStep<T> {
    private static final Logger LOGGER = Logger.getLogger(AbstractClickStep.class);
    private static final String CTRL_MODIFIER = "ctrl";
    private static final String SHIFT_MODIFIER = "shift";
    protected ClickDescriptor<T> descriptor = null;


    protected void proceedComponent(TestContext context,
                                    String searchCriteria,
                                    T step, Finder... finders) {
        Component comp = findComponent(context, searchCriteria, step, finders);
        long begin = System.currentTimeMillis();
        initDescriptor(comp, step);

        while (!getComponentToClick(comp, step).isEnabled() && (System.currentTimeMillis() - begin
                                                          < step.getTimeout() * step.getWaitingNumber())) {
            try {
                LOGGER.debug(
                      "La tentative de l'assert a �chou�. Mise en attente pour laisser le thread AWT travailler...");
                Thread.sleep(step.getWaitingNumber() / 2);
            }
            catch (InterruptedException e) {
                ;
            }
        }

        if (!getComponentToClick(comp, step).isEnabled()) {
            throw new GuiConfigurationException("Le composant '" + searchCriteria + "' est inactif.");
        }

        MouseEventData eventData = createMouseEventData(context, comp, step);
        setReferencePointIfNeeded(eventData, comp, step);

        eventData.setNumberOfClicks(step.getCount());
        eventData.setModifiers(this.getMouseModifiers(step));

        if ((step.getSelect() != null) || (step.getStepList().size() > 0)) {
            showPopupAndSelectItem(comp, context, eventData, step);
        }
        else {
            context.getHelper().enterClickAndLeave(eventData);
        }
    }


    protected MouseEventData createMouseEventData(net.codjo.test.release.task.gui.TestContext context, Component comp, T step) {
        ClickMouseEventData clickData = new ClickMouseEventData((TestContext) context, comp);
        clickData.setModifiers(getMouseModifiers(step));
        return clickData;
    }

    abstract protected int getMouseModifiers(T step);

    protected abstract void initDescriptor(Component comp, T step);


    private Component findComponent(TestContext context, String searchCriteria, T step, Finder... finders) {
        Component comp = null;
        for (int index = 0; index < finders.length && comp == null; index++) {
            Finder finder = finders[index];
            comp = findOnlyOne(finder, context, step);
        }
        if (comp == null) {
            throw new GuiFindException("Le composant '" + searchCriteria + "' est introuvable.");
        }
        return comp;
    }


    protected Component getComponentToClick(Component comp, T step) {
        if (descriptor != null) {
            return descriptor.getComponentToClick(comp, step);
        }
        return comp;
    }


    protected void setReferencePointIfNeeded(MouseEventData eventData, Component comp, T step) {
        if (descriptor != null) {
            descriptor.setReferencePointIfNeeded(eventData, comp, step);
        }
    }

    protected static int getModifierFromName(String modifierName) throws IllegalArgumentException {
        if (CTRL_MODIFIER.equals(modifierName)) {
            return InputEvent.CTRL_MASK;
        }
        else if (SHIFT_MODIFIER.equals(modifierName)) {
            return InputEvent.SHIFT_MASK;
        }
        else if (StringUtils.isEmpty(modifierName)) {
            return 0;
        }
        else {
            throw new IllegalArgumentException(java.lang.String.format("Pas de modifier standard correspondant � %s",
                                                      modifierName));
        }
    }
}
