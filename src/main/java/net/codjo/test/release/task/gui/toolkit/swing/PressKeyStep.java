package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.eventdata.KeyEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PressKeyStep<T extends net.codjo.test.release.task.gui.PressKeyStep> extends AbstractGuiStep<T> {
    private static final Logger LOG = Logger.getLogger(PressKeyStep.class);

	@SuppressWarnings("unchecked")
	public void proceed(net.codjo.test.release.task.gui.TestContext context, GuiStep s) {
		T step = (T) s;
        KeyStroke keyStroke = KeyStroke.getKeyStroke(step.getValue());
        if (keyStroke == null) {
            throw new IllegalStateException("La value '" + step.getValue() + "' est incorrecte.\n"
                                            + "Utilisation : modifiers (<typedID> | <pressedReleasedID>)\n"
                                            + "     *\n"
                                            + "     *    modifiers := shift | control | ctrl | meta | alt | altGraph \n"
                                            + "     *    typedID := typed <typedKey>\n"
                                            + "     *    typedKey := string of length 1 giving Unicode character.\n"
                                            + "     *    pressedReleasedID := (pressed | released) key\n"
                                            + "     *    key := KeyEvent key code name, i.e. the name following \"VK_\".\n"
                                            + "Exemples :\n"
                                            + "\"ENTER\", \"INSERT\", \"control DELETE\", \"alt shift X\", \"alt shift released X\", \"typed a\"");
        }

        if (StringUtils.isEmpty(step.getName())) {
            pressKeyWithRobot(keyStroke);
        }
        else {
            pressKeyWithSendKeyAction(context, keyStroke, step);
        }
    }


    private void pressKeyWithSendKeyAction(net.codjo.test.release.task.gui.TestContext context, KeyStroke keyStroke, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        final Component component = findOnlyOne(finder, context, step.getTimeout(), step);
        if (component == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (!component.isEnabled()) {
            throw new GuiConfigurationException(computeUneditableComponent(step.getName()));
        }

        JFCTestCase testCase = ((TestContext) context).getTestCase();
        KeyEventData keyEventData = new KeyEventData(testCase,
                                                     component,
                                                     keyStroke.getKeyCode());
        keyEventData.setModifiers(keyStroke.getModifiers());

        testCase.getHelper().sendKeyAction(keyEventData);
    }


    private void pressKeyWithRobot(KeyStroke keyStroke) {
        try {
            Robot robot = new Robot();
            int modifiers = keyStroke.getModifiers();
            pressModifierKeys(modifiers, robot);

            robot.keyPress(keyStroke.getKeyCode());
            robot.keyRelease(keyStroke.getKeyCode());

            releaseModifierKeys(modifiers, robot);

            robot.delay(10);
        }
        catch (AWTException e) {
            LOG.error("Impossible d'instancier Robot", e);
        }
    }


    private void releaseModifierKeys(int modifiers, Robot robot) {
        if ((modifiers & InputEvent.SHIFT_MASK) != 0) {
            robot.keyRelease(KeyEvent.VK_SHIFT);
        }
        if ((modifiers & InputEvent.CTRL_MASK) != 0) {
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }
        if ((modifiers & InputEvent.ALT_MASK) != 0) {
            robot.keyRelease(KeyEvent.VK_ALT);
        }
        if ((modifiers & InputEvent.META_MASK) != 0) {
            robot.keyRelease(KeyEvent.VK_META);
        }
        if ((modifiers & InputEvent.ALT_GRAPH_MASK) != 0) {
            robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
        }
    }


    private void pressModifierKeys(int modifiers, Robot robot) {
        if ((modifiers & InputEvent.SHIFT_MASK) != 0) {
            robot.keyPress(KeyEvent.VK_SHIFT);
        }
        if ((modifiers & InputEvent.CTRL_MASK) != 0) {
            robot.keyPress(KeyEvent.VK_CONTROL);
        }
        if ((modifiers & InputEvent.ALT_MASK) != 0) {
            robot.keyPress(KeyEvent.VK_ALT);
        }
        if ((modifiers & InputEvent.META_MASK) != 0) {
            robot.keyPress(KeyEvent.VK_META);
        }
        if ((modifiers & InputEvent.ALT_GRAPH_MASK) != 0) {
            robot.keyPress(KeyEvent.VK_ALT_GRAPH);
        }
    }


    static String computeUneditableComponent(String componentName) {
        return "Le composant '" + componentName + "' n'est pas �ditable.";
    }
}
