package net.codjo.test.release.task.gui.toolkit.swing;
import net.codjo.test.release.task.gui.AbstractMatchingStep;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.matcher.MatcherFactory;
import net.codjo.test.release.task.gui.toolkit.GuiStep;
import net.codjo.test.release.task.gui.toolkit.swing.SwingGUIToolkit;
import junit.framework.TestCase;
import static net.codjo.test.release.task.gui.toolkit.swing.AbstractMatchingStep.assertExpected;
import static net.codjo.test.release.task.gui.toolkit.swing.AbstractMatchingStep.compareWithExpectedValue;
/**
 *
 */
public class AbstractMatchingStepTest extends TestCase {

    public void test_compareWithExpectedValue() {
        MyMatchingStep step = new MyMatchingStep();
        String actualValue = "Salut toto";

        step.setExpected("toto");
        step.setMatching(MatcherFactory.EQUALS_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.CONTAINS_MATCHING);
        assertTrue(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.ENDS_WITH_MATCHING);
        assertTrue(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.STARTS_WITH_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));

        step.setExpected("Salut");
        step.setMatching(MatcherFactory.EQUALS_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.CONTAINS_MATCHING);
        assertTrue(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.ENDS_WITH_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.STARTS_WITH_MATCHING);
        assertTrue(compareWithExpectedValue(actualValue, step));

        step.setExpected("t t");
        step.setMatching(MatcherFactory.EQUALS_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.CONTAINS_MATCHING);
        assertTrue(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.ENDS_WITH_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.STARTS_WITH_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));

        step.setExpected("chat");
        step.setMatching(MatcherFactory.EQUALS_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.CONTAINS_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.ENDS_WITH_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
        step.setMatching(MatcherFactory.STARTS_WITH_MATCHING);
        assertFalse(compareWithExpectedValue(actualValue, step));
    }


    public void test_assertExpected() {
        MyMatchingStep step = new MyMatchingStep();
        String actualValue = "Salut toto";

        String expected = "toto";
        step.setExpected(expected);
        step.setMatching(MatcherFactory.EQUALS_MATCHING);
        checkException(step, actualValue, expected);
        step.setMatching(MatcherFactory.CONTAINS_MATCHING);
        assertExpected(actualValue, step);
        step.setMatching(MatcherFactory.ENDS_WITH_MATCHING);
        assertExpected(actualValue, step);
        step.setMatching(MatcherFactory.STARTS_WITH_MATCHING);
        checkException(step, actualValue, expected);

        expected = "Salut";
        step.setExpected(expected);
        step.setMatching(MatcherFactory.EQUALS_MATCHING);
        checkException(step, actualValue, expected);
        step.setMatching(MatcherFactory.CONTAINS_MATCHING);
        assertExpected(actualValue, step);
        step.setMatching(MatcherFactory.ENDS_WITH_MATCHING);
        checkException(step, actualValue, expected);
        step.setMatching(MatcherFactory.STARTS_WITH_MATCHING);
        assertTrue(compareWithExpectedValue(actualValue, step));

        expected = "t t";
        step.setExpected(expected);
        step.setMatching(MatcherFactory.EQUALS_MATCHING);
        checkException(step, actualValue, expected);
        step.setMatching(MatcherFactory.CONTAINS_MATCHING);
        assertExpected(actualValue, step);
        step.setMatching(MatcherFactory.ENDS_WITH_MATCHING);
        checkException(step, actualValue, expected);
        step.setMatching(MatcherFactory.STARTS_WITH_MATCHING);
        checkException(step, actualValue, expected);

        expected = "chat";
        step.setExpected(expected);
        step.setMatching(MatcherFactory.EQUALS_MATCHING);
        checkException(step, actualValue, expected);
        step.setMatching(MatcherFactory.CONTAINS_MATCHING);
        checkException(step, actualValue, expected);
        step.setMatching(MatcherFactory.ENDS_WITH_MATCHING);
        checkException(step, actualValue, expected);
        step.setMatching(MatcherFactory.STARTS_WITH_MATCHING);
        checkException(step, actualValue, expected);
    }


    private void checkException(MyMatchingStep step, String actualValue, String expected) {
        try {
            assertExpected(actualValue, step);
            fail("Exception attendue.");
        }
        catch (GuiAssertException ex) {
            assertEquals("Composant MyComponent : attendu='" + expected + "' obtenu='" + actualValue + "'",
                         ex.getMessage());
        }
    }


    private class MyMatchingStep extends AbstractMatchingStep {
    	
        @Override
        public String getComponentName() {
            return "MyComponent";
        }
    }
}
