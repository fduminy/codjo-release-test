/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;

import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

/**
 * Classe permettant de Selectionner un onglet dans une {@link javax.swing.JTabbedPane}
 */
public class SelectTabStep extends AbstractGuiStep {
    public static final int INITIAL_INDEX_VALUE = -1;
    private String name;
    private String tabLabel;
    private int tabIndex = INITIAL_INDEX_VALUE;

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public String getTabLabel() {
        return tabLabel;
    }


    public void setTabLabel(String tabLabel) {
        this.tabLabel = tabLabel;
    }


    public int getTabIndex() {
        return tabIndex;
    }


    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    static public String computeIllegalUsageOfAttributes(String name) {
        return "Les attributs 'tabIndex' et 'tabLabel' du composant '" + name
        + "' ne peuvent pas �tre utilis�s en m�me temps.";
    }

	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
