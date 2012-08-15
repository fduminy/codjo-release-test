package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.AbstractGuiStep.DISPLAY_MODE;
import static net.codjo.test.release.task.gui.AbstractGuiStep.MODEL_MODE;
import static net.codjo.test.release.task.gui.toolkit.swing.TreeUtils.MODEL_CONVERTER;
import static net.codjo.test.release.task.gui.toolkit.swing.TreeUtils.RENDERER_ASSERT_CONVERTER;
import static net.codjo.test.release.task.gui.toolkit.swing.TreeUtils.RENDERER_CONVERTER;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.toolkit.swing.converter.TreeNodeConverter;

/**
 *
 */
public class TreeStepUtils {
    private static final String MESSAGE = "Invalid value of 'mode' attribute : must be in {\"" + DISPLAY_MODE
                                          + "\", \"" + MODEL_MODE + "\"}.";


    private TreeStepUtils() {
    }


    public static TreeNodeConverter getConverter(String mode) {
        chekModeValidity(mode);
        return selectConverter(mode, RENDERER_CONVERTER);
    }


    public static TreeNodeConverter getAssertConverter(String mode) {
        chekModeValidity(mode);
        return selectConverter(mode, RENDERER_ASSERT_CONVERTER);
    }


    private static TreeNodeConverter selectConverter(String mode, TreeNodeConverter rendererConverter) {
        if (mode == null || DISPLAY_MODE.equals(mode)) {
            return rendererConverter;
        }
        else {
            return MODEL_CONVERTER;
        }
    }


    private static void chekModeValidity(String mode) {
        if (mode != null && !DISPLAY_MODE.equals(mode) && !MODEL_MODE.equals(mode)) {
            throw new GuiAssertException(MESSAGE);
        }
    }
}
