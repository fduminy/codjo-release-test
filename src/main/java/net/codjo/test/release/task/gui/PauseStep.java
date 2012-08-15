package net.codjo.test.release.task.gui;

import net.codjo.test.release.task.gui.toolkit.GUIToolkitManager;

/**
 * Classe permettant de faire une pause durant l'execution d'un test IHM.
 */
public class PauseStep extends AbstractGuiStep {

	public void proceed(TestContext context) {
		GUIToolkitManager.getGUIToolkit().proceed(this, context);
	}
}
