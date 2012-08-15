/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 * Classe permettant de v�rifier la s�lection d'une ligne dans une {@link javax.swing.JList}, une {@link
 * javax.swing.JTable} ou un {@link javax.swing.JTree}.
 */
public class AssertSelectedStep<T extends net.codjo.test.release.task.gui.AssertSelectedStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(JComponent.class, step.getName());
        Component comp = findOnlyOne(finder, context, 0, step);
        if (comp == null) {
            throw new GuiFindException("Le composant '" + step.getName() + "' est introuvable.");
        }

        if (comp instanceof JTable) {
            proceedTable((JTable)comp, step);
        }
        else if (comp instanceof JList) {
            proceedList((JList)comp, step);
        }
        else if (comp instanceof JTree) {
            proceedTree((JTree)comp, step);
        }
        else {
            throw new GuiConfigurationException("Type de composant non support� : "
                                                + comp.getClass().getName());
        }
    }


    private void proceedTree(JTree tree, T step) {

        TreePath selectionPath = tree.getSelectionPath();
        if (step.getPath() == null) {

            if (step.isExpected()) {
                if (selectionPath == null) {
                    throw new GuiAssertException("Au moins une s�lection dans l'arbre est attendue.");
                }
                return;
            } else {
                if (selectionPath != null) {
                    throw new GuiAssertException("Aucune s�lection dans l'arbre est attendue.");
                }
                return;
            }
        }

        if ((selectionPath == null) && step.isExpected()) {
            if (!"".equals(step.getPath())) {
                throw new GuiAssertException("Aucun noeud de l'arbre n'est s�lectionn�.");
            }
            return;
        }

        if ((selectionPath != null) && !step.isExpected()) {
            if ("".equals(step.getPath())) {
                return;
            }
        }

        TreePath expectedPath = TreeUtils
              .convertIntoTreePath(tree, step.getPath(), TreeStepUtils.getConverter(step.getMode()));

        if (!expectedPath.equals(selectionPath) && step.isExpected()) {
            throw new GuiAssertException(
                  "Le noeud s�lectionn� ne correspond pas : attendu = '" + expectedPath.toString()
                  + "' obtenu = '" + selectionPath.toString() + "'");
        }

        if (expectedPath.equals(selectionPath) && !step.isExpected()) {
            throw new GuiAssertException(
                  "Noeud '" + expectedPath.toString()
                  + "' : attendu = 'non s�lectionn�' obtenu = 's�lectionn�'");
        }
    }


    private void proceedTable(JTable table, T step) {
        if (step.getRow() < 0 || step.getRow() >= table.getRowCount()) {
            throw new GuiFindException("La ligne " + step.getRow() + " n'existe pas dans la table " + step.getName());
        }

        boolean actual;
        if (step.getColumn() < 0) {
            actual = table.isRowSelected(step.getRow());
        }
        else {
            actual = table.isCellSelected(step.getRow(), step.getColumn());
        }
        assertExpected(actual, step);
    }


    private void proceedList(JList list, T step) {
        if (step.getRow() < 0 || step.getRow() >= list.getModel().getSize()) {
            throw new GuiFindException("La ligne " + step.getRow() + " n'existe pas dans la liste " + step.getName());
        }
        boolean actual = list.isSelectedIndex(step.getRow());
        assertExpected(actual, step);
    }


    private void assertExpected(boolean actual, T step) {
        if (step.isExpected() != actual) {
            throw new GuiAssertException("Composant '" + step.getName() + "' : attendu='" + step.isExpected()
                                         + "' obtenu='" + actual + "'");
        }
    }
}
