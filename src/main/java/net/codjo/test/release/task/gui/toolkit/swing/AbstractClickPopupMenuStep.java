package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import junit.extensions.jfcunit.eventdata.AbstractMouseEventData;
import junit.extensions.jfcunit.eventdata.JMenuMouseEventData;
import junit.extensions.jfcunit.finder.JPopupMenuFinder;
import net.codjo.test.release.task.gui.GuiActionException;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;

import org.apache.log4j.Logger;

public abstract class AbstractClickPopupMenuStep<T extends net.codjo.test.release.task.gui.AbstractClickPopupMenuStep> extends AbstractGuiStep<T> {
    protected static final Logger LOG = Logger.getLogger(AbstractClickPopupMenuStep.class);

    protected void selectItemMenu(TestContext context, Component popup, JPopupMenuFinder popupFinder, T step) {
        if (step.getSelect() == null) {
            context.setCurrentComponent(popup);
            step.proceedStepList(context);
            context.setCurrentComponent(null);
            popup.setVisible(false);
        }
        else {
            StringTokenizer tok = new StringTokenizer(step.getSelect(), ":");
            String[] labels = new String[tok.countTokens()];

            for (int i = 0; tok.hasMoreTokens(); i++) {
                labels[i] = tok.nextToken();
            }

            int[] indexes = AbstractButtonClickStep.getPathIndexes((JPopupMenu)popup, labels);
            JMenuMouseEventData menuEventData = new JMenuMouseEventData(context.getTestCase(),
                                                                        (JComponent)popup, indexes);
            menuEventData.setSleepTime(step.getTimeout() * step.getWaitingNumber());
            context.getHelper().enterClickAndLeave(menuEventData);

            if (popup.isVisible()) {
                waitForPopupDisappear(popup, context, menuEventData, popupFinder, step);
            }
        }
    }


    protected void showPopupAndSelectItem(Component component,
                                          TestContext context,
                                          AbstractMouseEventData eventData, T step) {
        context.getHelper().enterClickAndLeave(eventData);

        JPopupMenuFinder popupFinder = new JPopupMenuFinder(component);
        Component popup = findOnlyOne(popupFinder, context, step.getFinderTimeout(), step);

        if (popup == null) {
            if (step.isPopupVisible()) {
                throw new GuiFindException("Le menu contextuel associ� au composant '" + step.getName()
                                           + "' est introuvable");
            }
            else {
                return;
            }
        }

        if (!step.isPopupVisible() && popup.isShowing()) {
            throw new GuiAssertException(
                  "Le menu contextuel est visible alors que l'attribut 'popup' est initialis� a 'false'");
        }

        selectItemMenu(context, popup, popupFinder, step);
    }


    private void waitForPopupDisappear(Component popup,
                                       TestContext context,
                                       JMenuMouseEventData menuEventData,
                                       JPopupMenuFinder popupFinder, T step) {
        int index = 0;
        while (index < step.getDisappearTryingNumber() && popup != null) {
            LOG.debug("La tentative num�ro " + index + " de l'assert a �chou�.");
            context.getHelper().enterClickAndLeave(menuEventData);
            popup = findOnlyOne(popupFinder, context, step.getFinderTimeout(), step);
            index++;
        }

        if (popup != null) {
            throw new GuiActionException("Le menu contextuel associ� au composant " + step.getName()
                                         + " est toujours pr�sent");
        }
    }
}
