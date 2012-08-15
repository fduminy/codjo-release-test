/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.EditCellStep.INITIAL_ROW_VALUE;
import static net.codjo.test.release.task.gui.EditCellStep.NO_NAME_HAS_BEEN_SET;
import static net.codjo.test.release.task.gui.toolkit.swing.TreeUtils.convertIntoTreePath;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiActionException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
/**
 * Permet de simuler des �ditions dans des cellules de JTable.
 *
 * @version $Revision: 1.8 $
 */
public class EditCellStep<T extends net.codjo.test.release.task.gui.EditCellStep> extends AbstractGuiStep<T> {
	@SuppressWarnings("unchecked")
	public void proceed(TestContext context, GuiStep s) {
		T step = (T) s;
        Component comp = findComponent(context, JTable.class, step);
        if (comp == null) {
            comp = findComponent(context, JTree.class, step);
        }

        if (comp == null) {
            throw new GuiFindException(computeComponentNotFoundMessage(step));
        }

        if (!comp.isEnabled()) {
            throw new GuiConfigurationException("Le composant '" + step.getName() + "' n'est pas actif.");
        }

        Component editorComponent;
        try {
            if (comp instanceof JTable) {
                editorComponent = proceedTable((JTable)comp, context, step);
            }
            else if (comp instanceof JTree) {
                editorComponent = proceedTree((JTree)comp, step);
            }
            else {
                throw new GuiConfigurationException("Type de composant non support� : "
                                                    + comp.getClass().getName());
            }
        }
        catch (GuiException e) {
            throw e;
        }
        catch (Exception e) {
            throw new GuiActionException("Impossible d'�diter le composant.", e);
        }

        ((net.codjo.test.release.task.gui.toolkit.swing.TestContext) context).setCurrentComponent(editorComponent);
        if (step.getEditionStep() != null) {
        	step.getEditionStep().setName(step.getName());
        }
        step.proceedStepList(context);
        ((net.codjo.test.release.task.gui.toolkit.swing.TestContext) context).setCurrentComponent(null);
    }

    private String computeComponentNotFoundMessage(T step) {
        if (step.getName() == null) {
            return NO_NAME_HAS_BEEN_SET;
        }
        else {
            return "Le composant '" + step.getName() + "' est introuvable.";
        }
    }


    private Component findComponent(TestContext context, Class aClass, T step) {
        NamedComponentFinder finder = new NamedComponentFinder(aClass, step.getName());
        return findOnlyOne(finder, context, step);
    }


    private Component proceedTree(JTree tree, T step) throws InterruptedException {
        if (step.getRow() != INITIAL_ROW_VALUE) {
            throw new GuiConfigurationException("L'attribut row n'est pas support� sur les arbres.");
        }
        if (step.getColumn() != null) {
            throw new GuiConfigurationException("L'attribut column n'est pas support� sur les arbres.");
        }
        if (step.getPath() == null) {
            throw new GuiConfigurationException("Le path n'a pas �t� renseign�.");
        }

        if (!tree.isEditable()) {
            throw new GuiConfigurationException("L'arbre n'est pas �ditable.");
        }
        final TreePath treePath = convertIntoTreePath(tree, step.getPath(), TreeStepUtils.getConverter(step.getMode()));
        tree.startEditingAtPath(treePath);

        Object node = treePath.getLastPathComponent();

        boolean isLeaf = tree.getModel().isLeaf(node);
        boolean expanded = tree.isExpanded(treePath);
        int pathRow = tree.getRowForPath(treePath);

        return tree.getCellEditor().getTreeCellEditorComponent(tree, node, true, expanded, isLeaf, pathRow);
    }


    private Component proceedTable(final JTable table, TestContext context, final T step) {
        if (step.getPath() != null) {
            throw new GuiConfigurationException("L'attribut path n'est pas support� sur les tables.");
        }
        if (step.getMode() != null) {
            throw new GuiConfigurationException("L'attribut mode n'est pas support� sur les tables.");
        }

        final int realColumn = TableTools.searchColumn(table, step.getColumn());
        TableTools.checkTableCellExists(table, step.getRow(), realColumn);
        if (!table.isCellEditable(step.getRow(), realColumn)) {
            throw new GuiConfigurationException(computeNotEditableCellMessage(step.getColumn()));
        }

        final boolean[] editionIsPossible = new boolean[]{false};
        try {
            runAwtCode(context, new Runnable() {
                public void run() {
                    editionIsPossible[0] = table.editCellAt(step.getRow(), realColumn);
                    if (!editionIsPossible[0]) {
                        throw new GuiConfigurationException(computeNotEditableCellMessage(step.getColumn()));
                    }
                }
            });
        }
        catch (Exception error) {
            throw new GuiException("Impossible d'editer la table '" + table.getName() + "'", error);
        }

        return table.getEditorComponent();
    }


    static String computeNotEditableCellMessage(String columnName) {
        return "La colonne '" + columnName + "' n'est pas �ditable.";
    }
}
