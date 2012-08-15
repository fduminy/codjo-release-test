/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;
/**
 * Classe permettant d'affecter la valeur d'un composant
 */
public class SetValueStep extends AbstractGuiStep {
    public static final String MODE_KEYBOARD = "keyboard";
    public static final String MODE_SETTER = "setter";
    public static final String BAD_BOOLEAN_VALUE_MESSAGE =
          "Seules les valeurs 'true' et 'false' sont autoris�es pour ce composant.";
    public static final String BAD_NUMBER_VALUE_MESSAGE
          = "Seules les entiers positifs sont autoris�es pour ce composant.";
    public static final String CHECKBOX_DISABLED_MESSAGE = "La checkBox est gris�e.";
    private String value;
    private String mode = MODE_SETTER;
    private String name;
    private int row = -1;
    private String column = "-1";


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public String getMode() {
        return mode;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public int getRow() {
        return row;
    }


    public void setRow(int row) {
        this.row = row;
    }


    public String getColumn() {
        return column;
    }


    public void setColumn(String column) {
        this.column = column;
    }


    static public String computeUneditableComponent(String componentName) {
        return "Le composant '" + componentName + "' n'est pas �ditable.";
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
