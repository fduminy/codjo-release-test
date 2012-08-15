/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

/**
 * Classe permettant de Selectionner une ligne dans une {@link javax.swing.JList} ou {@link javax.swing.JTable} ou
 * {@link javax.swing.JPopupMenu}.
 */
public class SelectStep extends AbstractGuiStep {
    public static final String MULTIPLE_UNSUPPORTED_MESSAGE =
          "L'attribute 'multiple' n'est pas support� pour des composants de type JComboBox.";
    public static final int INITIAL_VALUE = -1;
    private String name;
    private int row = INITIAL_VALUE;
    private int column = INITIAL_VALUE;
    private String label;
    private boolean multiple = false;
    private String path;
    private String mode;
    private long popupDelay = 500;
    private long listDelay = 50;


    public boolean isMultiple() {
        return multiple;
    }


    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setRow(int row) {
        this.row = row;
    }


    public void setLabel(String label) {
        this.label = label;
    }


    public String getName() {
        return name;
    }


    public int getRow() {
        return row;
    }


    public String getLabel() {
        return label;
    }


    public int getColumn() {
        return column;
    }


    public void setColumn(int column) {
        this.column = column;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getPath() {
        return path;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public String getMode() {
        return mode;
    }


    public long getPopupDelay() {
        return popupDelay;
    }


    public void setPopupDelay(long popupDelay) {
        this.popupDelay = popupDelay;
    }

    static public String computeBadRowMessage(int computedRow, String component) {
        return "La ligne '" + computedRow + "' n'existe pas dans le composant '" + component + "'";
    }


    static public String computeUnknownLabelMessage(String label, String component) {
        return "Le composant '" + component + "' ne contient pas le label '" + label + "'";
    }


    static public String computeDoubleLabelMessage(String label, String component) {
        return "Le composant '" + component + "' contient plusieurs fois le label '" + label + "'";
    }


    static public String computeUnexpectedRendererMessage(String rendererClass, String component) {
        return "Le renderer associ� � la liste '" + component + "' est de type '" + rendererClass + "'. "
               + "Seuls les renderers de type JLabel sont support�s.";
    }


    static public String computeIllegalUsageOfLabelAndRowMessage(String componentName) {
        return "Les attributs 'row' et 'label' du composant '" + componentName
               + "' ne peuvent pas �tre utilis�s en m�me temps.";
    }


    static public String computeUneditableComponent(String componentName) {
        return "Le composant '" + componentName + "' n'est pas �ditable.";
    }

    public long getListDelay() {
        return listDelay;
    }


    public void setListDelay(long listDelay) {
        this.listDelay = listDelay;
    }


	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
