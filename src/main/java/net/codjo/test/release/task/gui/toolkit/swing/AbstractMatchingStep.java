package net.codjo.test.release.task.gui.toolkit.swing;

import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.matcher.Matcher;

/**
 *
 */
public abstract class AbstractMatchingStep<T extends net.codjo.test.release.task.gui.AbstractMatchingStep> extends AbstractAssertStep<T> {

    protected static <T extends net.codjo.test.release.task.gui.AbstractMatchingStep> boolean compareWithExpectedValue(String actualValue, T step) {
        Matcher matcher = step.getMatcherFactory().get(step.getMatching(), actualValue);
        return matcher.match(step.getExpected());
    }

    protected static <T extends net.codjo.test.release.task.gui.AbstractMatchingStep> void assertExpected(String actualValue, T step) {
        if (!compareWithExpectedValue(actualValue, step)) {
            throw new GuiAssertException("Composant " + step.getComponentName()
                                         + " : attendu='" + step.getExpected()
                                         + "' obtenu='" + actualValue + "'");
        }
    }

}
