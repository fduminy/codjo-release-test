package net.codjo.test.release.task.gui.toolkit.swing;

import static junit.framework.Assert.fail;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.AgfTask;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.util.comparisonstrategy.BufferedImageComparisonStrategy;

/**
 *
 */
public class AssertComponentImageStep<T extends net.codjo.test.release.task.gui.AssertComponentImageStep> extends AbstractAssertStep<T> {

	protected void proceedOnce(TestContext context, T step) {
        JComponent component = getComponentFromName(context, step);
        try {
            BufferedImage componentBufferedImage = exportImageFromComponent(component);
            BufferedImage expectedBufferedImage;
            String parentPath = context.getProperty(AgfTask.TEST_DIRECTORY);
			File expectedFile = new File(parentPath, ((T) step).getExpected());
            if (!expectedFile.exists()) {
                expectedBufferedImage = exportImageFromComponent(component);
                ImageIO.write(expectedBufferedImage, "bmp", expectedFile);
            }
            else {
                expectedBufferedImage = ImageIO.read(expectedFile);
            }

            proceedComparison(expectedBufferedImage, componentBufferedImage);
        }
        catch (IOException e) {
            fail("Impossible d'ouvrir le fichier sp�cifi�.");
        }
    }


    private void proceedComparison(BufferedImage expectedFile, BufferedImage actualFile) {
        try {
            BufferedImageComparisonStrategy comparisonStrategy =
                  new BufferedImageComparisonStrategy(expectedFile, actualFile);
            if (!comparisonStrategy.compare()) {
                throw new GuiAssertException("");
            }
        }
        catch (FileNotFoundException ex) {
            throw new GuiAssertException(
                  "Unable to compare : " + expectedFile + " and " + actualFile, ex);
        }
    }


    private BufferedImage exportImageFromComponent(JComponent component) {
       BufferedImage image = new BufferedImage(component.getWidth(),
                                              component.getHeight(),
                                              BufferedImage.TYPE_INT_RGB);
        Graphics gx = image.getGraphics();
        component.paint(gx);
        gx.dispose();

        return image;
    }


	@SuppressWarnings("unchecked")
    private JComponent getComponentFromName(TestContext context, GuiStep s) {
		T step = (T) s;
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        JComponent component = (JComponent)findOnlyOne(finder, context, 0, step);
        if (component == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }
        return component;
    }
}
