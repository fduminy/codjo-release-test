/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;
import java.awt.Container;
import java.util.List;

import javax.swing.SwingUtilities;

import junit.extensions.jfcunit.finder.Finder;
import junit.extensions.jfcunit.finder.JPopupMenuFinder;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.toolkit.GuiStep;
/**
 * Classe de base des steps de test IHM.
 */
public abstract class AbstractGuiStep<T extends net.codjo.test.release.task.gui.AbstractGuiStep> implements GuiStep {


    protected Component findOnlyOne(Finder finder, net.codjo.test.release.task.gui.TestContext context, T step) {
        return findOnlyOne(finder, context, step.getTimeout(), step);
    }


    /**
     * Cherche un composant donn�. Dans le cas des NamedComponentFinder, renvoie une exception lorsque
     * plusieurs composants sont trouv�s avec le m�me nom.
     *
     * @param finder le finder � utiliser
     *
     * @return le composant trouv�, ou bien <code>null</code>.
     */
    protected Component findOnlyOne(Finder finder, net.codjo.test.release.task.gui.TestContext context, int finderTimeoutInSeconds, T step) {
        finder.setWait(finderTimeoutInSeconds);
        Component currentComponent = ((TestContext) context).getCurrentComponent();
        finder.setOperation(getFinderOperation(step));
        if (finder instanceof NamedComponentFinder) {
            String name = ((NamedComponentFinder)finder).getName();
            if (name == null && currentComponent != null) {
                return currentComponent;
            }
            return findOnlyOneNamedComponent((NamedComponentFinder)finder, currentComponent);
        }
        else if (finder instanceof JPopupMenuFinder) {
            return finder.find();
        }
        else if (currentComponent != null) {
            return currentComponent;
        }
        else {
            int index = 0;
            Component componentFound;
            do {
                componentFound = finder.find(index++);
                if (componentFound != null &&
                    (componentFound.isShowing() ||
                     SwingUtilities.windowForComponent(componentFound).isShowing())) {
                    return componentFound;
                }
            }
            while (componentFound != null);
        }
        return null;
    }


    protected int getFinderOperation(T step) {
        return Finder.OP_EQUALS;
    }


    private Component findOnlyOneNamedComponent(NamedComponentFinder finder, Component currentComponent) {
        List components;
        if (currentComponent != null && currentComponent instanceof Container) {
            // cas EditCell et ClickRight:
            // plusieurs composants portent le meme nom mais dans des containers differents de l'IHM.
            // On fait alors la recherche a partir du composant courant issu du TestContext.
            components = finder.findAll((Container)currentComponent);
        }
        else {
            components = finder.findAll();
        }
        if (components.isEmpty()) {
            // Patch : il arrive que la m�thode findAll ne renvoie rien
            // alors que find renvoie bien un composant. Allez savoir pourquoi.
            return finder.find();
        }
        else if (components.size() == 1) {
            return (Component)components.get(0);
        }
        else {
            StringBuilder message = new StringBuilder();
            message.append("Il existe ");
            message.append(components.size());
            message.append(" composants portant le nom ");
            message.append(finder.getName());
            message.append(" : ");
            for (Object componentAsObject : components) {
                Component component = (Component)componentAsObject;
                message.append("[");
                message.append(buildComponentHierarchy(component));
                message.append(" <");
                message.append(component.getClass().getName());
                message.append(">]");
            }

            throw new GuiFindException(message.toString());
        }
    }


    private String buildComponentHierarchy(Component component) {
        Component parent = component.getParent();
        if (parent != null) {
            return buildComponentHierarchy(parent) + "/" + component.getName();
        }
        else {
            return component.getName();
        }
    }


    public static void runAwtCode(net.codjo.test.release.task.gui.TestContext context, Runnable runnable)
          throws Exception {
    	((TestContext) context).getTestCase().flushAWT();
        try {
            SwingUtilities.invokeAndWait(runnable);
        }
        finally {
        	((TestContext) context).getTestCase().flushAWT();
        }
    }


    public static void runAwtCodeLater(net.codjo.test.release.task.gui.TestContext context, Runnable runnable)
          throws Exception {
        ((TestContext) context).getTestCase().flushAWT();
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            }
            else {
                SwingUtilities.invokeLater(runnable);
            }
        }
        finally {
        	((TestContext) context).getTestCase().flushAWT();
        }
    }
}
