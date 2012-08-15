/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.AbstractGuiStep.AUTO_MODE;
import static net.codjo.test.release.task.gui.AbstractGuiStep.DISPLAY_MODE;
import static net.codjo.test.release.task.gui.AbstractGuiStep.MODEL_MODE;

import java.awt.Component;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import junit.extensions.jfcunit.finder.DialogFinder;
import junit.extensions.jfcunit.finder.Finder;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
import chrriis.dj.nativeswing.swtimpl.components.JHTMLEditor;
/**
 * Classe permettant de v�rifier la valeur d'un {@link javax.swing.JComponent}.
 */
public class AssertValueStep<T extends net.codjo.test.release.task.gui.AssertValueStep> extends AbstractMatchingStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        step.setExpected(context.replaceProperties(step.getExpected()));
        if (step.getMode() == null) {
        	step.setMode(AUTO_MODE);
        }

        Finder finder;
        if (step.getDialogTitle() == null) {
            finder = new NamedComponentFinder(JComponent.class, step.getName());
        }
        else {
            finder = new DialogFinder(step.getDialogTitle());
        }

        Component component = findOnlyOne(finder, context, step);
        if (component == null) {
            throw new GuiFindException("Le composant '" + step.getComponentName() + "' est introuvable.");
        }

        if (component instanceof JEditorPane) {
            proceed((JEditorPane)component, step);
        }
        else if (component instanceof JTextComponent) {
            proceed((JTextComponent)component, step);
        }
        else if (component instanceof JComboBox) {
            proceed((JComboBox)component, step);
        }
        else if (component instanceof JButton) {
            proceed((JButton)component, step);
        }
        else if (component instanceof AbstractButton) {
            proceed((AbstractButton)component, step);
        }
        else if (component instanceof JDialog) {
            proceed((JDialog)component, step);
        }
        else if (component instanceof JLabel) {
            proceed((JLabel)component, step);
        }
        else if (component instanceof JTabbedPane) {
            proceed((JTabbedPane)component, step);
        }
        else if (component instanceof JSpinner) {
            proceed((JSpinner)component, step);
        }
        else if (component instanceof JSlider) {
            proceed((JSlider)component, step);
        }
        else if (component instanceof JHTMLEditor) {
            proceed(context, (JHTMLEditor)component, step);
        }
        else {
            throw new GuiConfigurationException("Type de composant non support� : "
                                                + component.getClass().getName());
        }
    }


    private void proceed(TestContext context, final JHTMLEditor htmlEditor, T step) {
        final String[] actualValue = new String[1];

        try {
            runAwtCode(context, new Thread() {
                @Override
				public void run() {
                    actualValue[0] = String.valueOf(htmlEditor.getHTMLContent());
                }
            });
        }
        catch (Exception e) {
            throw new GuiAssertException("Impossible de r�cuperer le contenu html du htmlEditor", e);
        }
        assertExpected(actualValue[0], step);
    }


    private void proceed(JSlider slider, T step) {
        String actualValue = String.valueOf(slider.getValue());
        assertExpected(actualValue, step);
    }


    private void proceed(JSpinner spinner, T step) {
        String actualValue = String.valueOf(spinner.getValue());
        assertExpected(actualValue, step);
    }


    private void proceed(JTabbedPane tabbedPane, T step) {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String title = tabbedPane.getTitleAt(selectedIndex);
        assertExpected(title, step);
    }


    private void proceed(JEditorPane editorPane, T step) {
        String actualValue = editorPane.getText();

        if ("text/html".equals(editorPane.getContentType())) {
            if (AUTO_MODE.equals(step.getMode()) || DISPLAY_MODE.equals(step.getMode())) {
                actualValue = removeHtmlTags(actualValue);
            }
            else if (MODEL_MODE.equals(step.getMode())) {
                actualValue = extractHtmlBody(actualValue);
            }

            actualValue = removeLastNewLine(actualValue);
        }

        assertExpected(actualValue, step);
    }


    private void proceed(JTextComponent textComponent, T step) {
        String actualValue = textComponent.getText();
        assertExpected(actualValue, step);
    }


    private void proceed(JLabel label, T step) {
        String actualValue = label.getText();
        assertExpected(actualValue, step);
    }


    private void proceed(AbstractButton radioButton, T step) {
        String actualValue = radioButton.isSelected() ? "true" : "false";
        assertExpected(actualValue, step);
    }


    private void proceed(JButton button, T step) {
        String actualValue = button.getText();
        assertExpected(actualValue, step);
    }


    private void proceed(JComboBox comboBox, T step) {
        if (!AUTO_MODE.equals(step.getMode()) && !DISPLAY_MODE.equals(step.getMode()) && !MODEL_MODE
              .equals(step.getMode())) {
            throw new GuiAssertException("Invalid value of 'mode' attribute : must be in {\"" + AUTO_MODE
                                         + "\", \"" + DISPLAY_MODE + "\", \"" + MODEL_MODE + "\"}.");
        }

        String actualValue = "";
        if (comboBox.getSelectedItem() != null) {
            final ListCellRenderer renderer = comboBox.getRenderer();
            if (renderer != null) {
                final Component rendererComponent =
                      renderer.getListCellRendererComponent(new JList(), comboBox.getSelectedItem(),
                                                            comboBox.getSelectedIndex(), false, false);
                if (rendererComponent instanceof JLabel) {
                    actualValue = ((JLabel)rendererComponent).getText();
                }
                else {
                    throw new GuiAssertException("Unexpected renderer type for ComboBox");
                }
            }

            if (!step.getExpected().equals(actualValue)
                && !DISPLAY_MODE.equals(step.getMode())
                || MODEL_MODE.equals(step.getMode())
                || "".equals(actualValue)) {
                actualValue = comboBox.getSelectedItem().toString();
            }
        }

        assertExpected(actualValue, step);
    }


    private void proceed(JDialog dialog, T step) {
        Component[] components = dialog.getContentPane().getComponents();
        String message = null;
        for (Component component : components) {
            if (component instanceof JOptionPane) {
                message = (String)((JOptionPane)component).getMessage();
                break;
            }
        }
        assertExpected(message, step);
    }

    private String removeHtmlTags(String text) {
        StringBuilder textWithoutTags = new StringBuilder();
        parseHtmlText(text, new RemoveHtmlTagsParserCallback(textWithoutTags));
        return textWithoutTags.toString();
    }


    private String extractHtmlBody(String text) {
        StringBuilder htmlBody = new StringBuilder();
        parseHtmlText(text, new ExtractHtmlBodyParserCallback(htmlBody));
        return htmlBody.toString();
    }


    private void parseHtmlText(String text, ParserCallback callback) {
        try {
            new ParserDelegator().parse(new StringReader(text), callback, false);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String removeLastNewLine(String actualValue) {
        if (actualValue.length() > 0) {
            char lastChar = actualValue.charAt(actualValue.length() - 1);
            if (lastChar == '\n') {
                actualValue = actualValue.substring(0, actualValue.length() - 1);
            }
        }
        return actualValue;
    }


    private static class RemoveHtmlTagsParserCallback extends ParserCallback {
        private final StringBuilder textWithoutTags;


        private RemoveHtmlTagsParserCallback(StringBuilder textWithoutTags) {
            this.textWithoutTags = textWithoutTags;
        }


        @Override
        public void handleText(char[] data, int pos) {
            textWithoutTags.append(data);
        }


        @Override
        public void handleSimpleTag(Tag tag, MutableAttributeSet attributeSet, int pos) {
            if (Tag.BR.equals(tag)) {
                textWithoutTags.append("\n");
            }
        }


        @Override
        public void handleEndTag(Tag tag, int pos) {
            if (Tag.P.equals(tag)) {
                textWithoutTags.append("\n");
            }
        }
    }

    private static class ExtractHtmlBodyParserCallback extends ParserCallback {
        private final StringBuilder htmlBody;
        private boolean insideBodyTag = false;


        private ExtractHtmlBodyParserCallback(StringBuilder htmlBody) {
            this.htmlBody = htmlBody;
        }


        @Override
        public void handleText(char[] data, int pos) {
            if (insideBodyTag) {
                htmlBody.append(data);
            }
        }


        @Override
        public void handleStartTag(Tag tag, MutableAttributeSet attributeSet, int pos) {
            if (Tag.BODY.equals(tag)) {
                insideBodyTag = true;
            }
            else if (insideBodyTag) {
                htmlBody.append("<").append(tag).append(">");
            }
        }


        @Override
        public void handleEndTag(Tag tag, int pos) {
            if (Tag.BODY.equals(tag)) {
                insideBodyTag = false;
            }
            else if (insideBodyTag) {
                htmlBody.append("</").append(tag).append(">");
            }
        }
    }
}
