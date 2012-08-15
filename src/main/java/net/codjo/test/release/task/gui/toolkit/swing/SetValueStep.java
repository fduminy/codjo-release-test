/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.SetValueStep.BAD_BOOLEAN_VALUE_MESSAGE;
import static net.codjo.test.release.task.gui.SetValueStep.MODE_KEYBOARD;
import static net.codjo.test.release.task.gui.SetValueStep.MODE_SETTER;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.eventdata.KeyEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiActionException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.metainfo.Introspector;
import net.codjo.test.release.task.gui.metainfo.SetValueDescriptor;
import chrriis.dj.nativeswing.swtimpl.components.JHTMLEditor;


/**
 * Classe permettant d'affecter la valeur d'un composant
 */
public class SetValueStep<T extends net.codjo.test.release.task.gui.SetValueStep> extends AbstractGuiStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(net.codjo.test.release.task.gui.TestContext context, GuiStep s) {
		final T step = (T) s;
        step.setValue(context.replaceProperties(step.getValue()));
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        final Component component = findOnlyOne(finder, context, step.getTimeout(), step);
        if (component == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (!component.isEnabled()) {
            throw new GuiConfigurationException(T.computeUneditableComponent(step.getName()));
        }

        try {
            final SetValueDescriptor descriptor =
                  Introspector.getTestBehavior(component.getClass(), SetValueDescriptor.class);
            if (descriptor != null) {
                runAwtCode(context,
                           new Runnable() {
                               public void run() {
                                   descriptor.setValue(component, step);
                               }
                           });
            }
            else {
                if (component instanceof JTextComponent) {
                    proceed(context, (JTextComponent)component, step);
                }
                else if (component instanceof JComboBox) {
                    proceed(context, (JComboBox)component, step);
                }
                else if (component instanceof JCheckBox) {
                    proceed(context, (JCheckBox)component, step);
                }
                else if (component instanceof JTable) {
                    proceed(context, (JTable)component, step);
                }
                else if (component instanceof JSpinner) {
                    proceed(context, (JSpinner)component, step);
                }
                else if (component instanceof JSlider) {
                    proceed(context, (JSlider)component, step);
                }
                else if (component instanceof JHTMLEditor) {
                    proceed(context, (JHTMLEditor)component, step);
                }
                else {
                    throw new GuiConfigurationException("Type de composant non support� : "
                                                        + component.getClass().getName());
                }
            }
        }
        catch (GuiException e) {
            throw e;
        }
        catch (Exception e) {
            throw new GuiActionException("Impossible de fixer la valeur.", e);
        }
    }
    
    private void proceed(net.codjo.test.release.task.gui.TestContext context, final JHTMLEditor htmlEditor, final T step) throws Exception {
        if (!htmlEditor.isEnabled()) {
            return;
        }
        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           htmlEditor.requestFocus();
                       }
                   });
        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           htmlEditor.setHTMLContent(step.getValue());
                       }
                   });
    }


    private void proceed(net.codjo.test.release.task.gui.TestContext context, final JSpinner jSpinner, final T step) throws Exception {
        if (!jSpinner.isEnabled()) {
            return;
        }

        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           jSpinner.requestFocus();
                       }
                   });

        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           jSpinner.setValue(step.getValue());
                       }
                   });
    }


    private void proceed(net.codjo.test.release.task.gui.TestContext context, final JSlider jSlider, final T step) throws Exception {
        if (!jSlider.isEnabled()) {
            return;
        }

        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           jSlider.requestFocus();
                       }
                   });

        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           Dictionary dictionary = jSlider.getLabelTable();
                           Enumeration enumeration = dictionary.keys();
                           while (enumeration.hasMoreElements()) {
                               Object key = enumeration.nextElement();
                               if (key instanceof Integer
                                   && dictionary.get(key) instanceof JLabel
                                   && ((JLabel)dictionary.get(key)).getText().equals(step.getValue())) {
                                   jSlider.setValue(Integer.valueOf(key.toString()));
                               }
                           }
                       }
                   });
    }


    private void proceed(net.codjo.test.release.task.gui.TestContext context, JTextComponent textComponent, T step) throws Exception {
        if (MODE_KEYBOARD.equals(step.getMode())) {
            proceedModeKeyboard(context, textComponent, step);
        }
        else if (MODE_SETTER.equals(step.getMode())) {
            proceedModeSetter(context, textComponent, step);
        }
        else {
            throw new GuiConfigurationException("Mode invalide : " + step.getMode() + ". Les modes valides sont "
                                                + MODE_KEYBOARD + " et " + MODE_SETTER + ".");
        }
    }


    private void proceedModeSetter(final net.codjo.test.release.task.gui.TestContext context, final JTextComponent textComponent, final T step)
          throws Exception {
        if (!textComponent.isEnabled()) {
            return;
        }

        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           textComponent.requestFocus();
                       }
                   });
        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           textComponent.setText(step.getValue());
                       }
                   });
    }


    private void proceedModeKeyboard(net.codjo.test.release.task.gui.TestContext context, final JComponent textComponent, T step)
          throws Exception {
        JFCTestCase testCase = ((TestContext) context).getTestCase();

        // Se positionner sur le composant
        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           textComponent.requestFocus();
                       }
                   });

        // S�lectionner tout l'ancien texte
        KeyEventData keyEventData = new KeyEventData(testCase, textComponent, KeyEvent.VK_A);
        keyEventData.setModifiers(InputEvent.CTRL_MASK);
        testCase.getHelper().sendKeyAction(keyEventData);

        // Ecraser l'ancien texte avec le nouveau
        if (step.getValue() == null || step.getValue().length() == 0) {
            keyEventData = new KeyEventData(testCase, textComponent, KeyEvent.VK_BACK_SPACE);
            testCase.getHelper().sendKeyAction(keyEventData);
        }
        else {
            testCase.getHelper().sendString(new StringEventData(testCase, textComponent, step.getValue()));
        }
    }


    private void proceed(net.codjo.test.release.task.gui.TestContext context, final JComboBox comboBox, final T step)
          throws Exception {
        checkComponentExistence(comboBox, step);
        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           comboBox.setSelectedItem(step.getValue());
                           if (!comboBox.getSelectedItem().equals(step.getValue())) {
                               selectItem(comboBox, step);
                           }
                       }
                   });

        if (MODE_KEYBOARD.equals(step.getMode()) && comboBox.isEditable()) {
            proceedModeKeyboard(context, comboBox, step);
        }
    }


    private void selectItem(JComboBox comboBox, T step) {
        for (int index = 0; index < comboBox.getItemCount(); index++) {
            if (comboBox.getItemAt(index).toString().equals(step.getValue())) {
                comboBox.setSelectedIndex(index);
            }
        }
    }


    private void checkComponentExistence(final JComboBox comboBox, T step) {
        if (comboBox.isEditable()) {
            return;
        }
        boolean exists = false;
        for (int index = 0; index < comboBox.getItemCount(); index++) {
            if (comboBox.getItemAt(index).toString().equals(step.getValue())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new GuiFindException("L'�l�ment '" + step.getValue() + "' n'existe pas dans le composant " + step.getName()
                                       + ".");
        }
    }


    private void proceed(net.codjo.test.release.task.gui.TestContext context, final JCheckBox checkbox, T step)
          throws Exception {
        if ("true".equalsIgnoreCase(step.getValue())) {
            selectCheckBox(context, checkbox);
        }
        else if ("false".equalsIgnoreCase(step.getValue())) {
            unselectCheckBox(context, checkbox);
        }
        else {
            throw new GuiConfigurationException(BAD_BOOLEAN_VALUE_MESSAGE);
        }
    }


    private void selectCheckBox(net.codjo.test.release.task.gui.TestContext context, final JCheckBox checkBox)
          throws Exception {
        if (!checkBox.isSelected()) {
            clickOnCheckBox(context, checkBox);
        }
    }


    private void unselectCheckBox(net.codjo.test.release.task.gui.TestContext context, JCheckBox checkBox)
          throws Exception {
        if (checkBox.isSelected()) {
            clickOnCheckBox(context, checkBox);
        }
    }


    private void clickOnCheckBox(net.codjo.test.release.task.gui.TestContext context, final JCheckBox checkBox)
          throws Exception {
        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           checkBox.doClick();
                       }
                   });
    }


    private void proceed(net.codjo.test.release.task.gui.TestContext context, final JTable table, final T step) throws Exception {
        final int realColumn = TableTools.searchColumn(table, step.getColumn());
        TableTools.checkTableCellExists(table, step.getRow(), realColumn);
        runAwtCode(context,
                   new Runnable() {
                       public void run() {
                           table.setValueAt(step.getValue(), step.getRow(), realColumn);
                       }
                   });
    }
}
