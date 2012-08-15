/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import static net.codjo.test.release.task.gui.toolkit.swing.TreeStepUtils.getAssertConverter;
import static net.codjo.test.release.task.gui.toolkit.swing.TreeUtils.convertIntoTreePath;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.AgfTask;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TestContext;
/**
 * Classe permettant de v�rifier le contenu d'une {@link javax.swing.JTree}.
 */
public class AssertTreeStep<T extends net.codjo.test.release.task.gui.AssertTreeStep> extends AbstractAssertStep<T> {
	protected void proceedOnce(TestContext context, T step) {
        if (step.getPath() == null) {
            throw new GuiConfigurationException("Le path n'a pas �t� renseign�.");
        }

        NamedComponentFinder finder = new NamedComponentFinder(JTree.class, step.getName());
        JTree tree = (JTree)findOnlyOne(finder, context, 0, step);

        if (tree == null) {
            throw new GuiFindException("L'arbre '" + step.getName() + "' est introuvable.");
        }

        step.setPath(context.replaceProperties(step.getPath()));

        try {
            TreePath foundPath = convertIntoTreePath(tree, step.getPath(), getAssertConverter(step.getMode()));
            if (!step.isExists()) {
                throw new GuiAssertException("Le noeud '" + step.getPath() + "' existe.");
            }
            if (step.getRow() != -1) {
                int rowForPath = tree.getRowForPath(foundPath);
                if (step.getRow() != rowForPath) {
                    throw new GuiAssertException("Le noeud '" + step.getPath() + "' ne se situe pas � la position '"
                                                 + step.getRow() + "' mais � la position '" + rowForPath + "'");
                }
            }
            if (step.getForeground() != null) {
                assertForeground(getRendererComponent(tree, foundPath, step), step);
            }
            if (step.getIcon() != null) {
                assertIcon(context, getRendererComponent(tree, foundPath, step), step);
            }
        }
        catch (GuiFindException e) {
            if (step.isExists()) {
                throw new GuiAssertException(e.getMessage());
            }
        }
    }


    private Component getRendererComponent(JTree tree, TreePath foundPath, T step) {
        boolean expanded = tree.isExpanded(foundPath);
        boolean selected = tree.getSelectionModel().isPathSelected(foundPath);
        Object value = foundPath.getLastPathComponent();
        boolean leaf = tree.getModel().isLeaf(value);
        boolean focus = tree.hasFocus();
        return tree.getCellRenderer().getTreeCellRendererComponent(tree, value, selected, expanded, leaf, step.getRow(), focus);
    }


    private void assertIcon(TestContext context, Component rendererComponent, T step) {
        try {
            Icon actualIcon = ((JLabel)rendererComponent).getIcon();
            if (!(rendererComponent instanceof JLabel)) {
                throw new GuiAssertException(
                      "Le noeud '" + step.getPath() + "' n'est pas rendu sous forme d'un type g�r�.");
            }
            String parentPath = context.getProperty(AgfTask.TEST_DIRECTORY);
            ImageIcon actualImageIcon = (ImageIcon)actualIcon;

            String expectedIconName = new File(parentPath, step.getIcon()).getName();
            String actualIconName = new File(new URL(actualImageIcon.getDescription()).getPath()).getName();
            if (!expectedIconName.equals(actualIconName)) {
                throw new GuiAssertException("Erreur de l'icone sur '" + step.getName() + "' au niveau de '" + step.getPath() +
                                             "' : attendu='" + expectedIconName + "' obtenu='" + actualIconName + "'");
            }
        }
        catch (IOException e) {
            throw new GuiAssertException(e.getMessage());
        }
    }


    private void assertForeground(Component rendererComponent, T step) {
        Color actualForeground = rendererComponent.getForeground();
        if (step.getForeground() == null) {
            return;
        }
        boolean equals = actualForeground.getRed() == step.getForeground().getRed()
                         && actualForeground.getGreen() == step.getForeground().getGreen()
                         && actualForeground.getBlue() == step.getForeground().getBlue();

        if (!equals) {
            throw new GuiAssertException("Couleur de police du composant '" + step.getName() + "' au niveau de '" + step.getPath() +
                                         "' : attendu='" + step.getForeground() + "' obtenu='" + actualForeground + "'");
        }
    }
}
