package net.codjo.test.release.task.gui.toolkit.swing;

import static net.codjo.test.release.task.gui.AbstractButtonClickStep.MATCHER_CONTAINS;

import java.awt.Component;
import java.awt.Rectangle;
import java.security.InvalidParameterException;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import junit.extensions.jfcunit.eventdata.EventDataConstants;
import junit.extensions.jfcunit.eventdata.JMenuMouseEventData;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.eventdata.PathData;
import junit.extensions.jfcunit.finder.Finder;
import junit.extensions.jfcunit.finder.JMenuItemFinder;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.GuiStep;
import net.codjo.test.release.task.gui.TestContext;
import net.codjo.test.release.task.gui.finder.LabeledAndNamedFinder;
import net.codjo.test.release.task.gui.finder.LabeledJComponentFinder;
import net.codjo.test.release.task.gui.metainfo.ClickDescriptor;
import net.codjo.test.release.task.gui.metainfo.Introspector;

/**
 *
 */
public abstract class AbstractButtonClickStep<T extends net.codjo.test.release.task.gui.AbstractButtonClickStep> extends AbstractClickStep<T> {

	@SuppressWarnings("unchecked")
	public void proceed(TestContext c, GuiStep s) {
		T step = (T) s;
		net.codjo.test.release.task.gui.toolkit.swing.TestContext context = (net.codjo.test.release.task.gui.toolkit.swing.TestContext) c;
        if (step.getMenu() != null) {
            proceedMenu(context, step);
        }
        else if (step.getName() != null && step.getLabel() != null) {
            proceedComponent(context, step.getLabel(), step, new LabeledAndNamedFinder(step.getName(), step.getLabel()));
        }
        else if (step.getName() != null) {
            proceedComponent(context, step.getName(), step, new NamedComponentFinder(JComponent.class, step.getName()));
        }
        else {
            proceedComponent(context, step.getLabel(), step, new LabeledJComponentFinder(step.getLabel()));
        }
    }


    @Override
    protected int getFinderOperation(T step) {
        return MATCHER_CONTAINS.equals(step.getMatcher()) ? Finder.OP_CONTAINS : Finder.OP_EQUALS;
    }


    private void proceedMenu(TestContext context, T step) {
        StringTokenizer tok = new StringTokenizer(step.getMenu(), ":");
        if (tok.countTokens() < 2) {
            throw new InvalidParameterException("L'identifiant '" + step.getMenu()
                                                + "' ne respecte pas la syntaxe 'menu:sous-menu:element'.");
        }

        String[] labels = new String[tok.countTokens()];

        for (int i = 0; tok.hasMoreTokens(); i++) {
            labels[i] = tok.nextToken();
        }

        PathData path = new PathData(labels);

        String menuName = labels[0];
        JMenuItemFinder finder = new JMenuItemFinder(menuName);
        JMenu currentMenu = (JMenu)findOnlyOne(finder, context, step);

        if (currentMenu == null) {
            throw new GuiFindException("Menu non trouv� : " + menuName);
        }

        JMenuBar menuBar = (JMenuBar)path.getRoot(currentMenu);
        int[] indexes = getPathIndexes(menuBar, labels);
        JMenuMouseEventData eventData = new JMenuMouseEventData(((net.codjo.test.release.task.gui.toolkit.swing.TestContext) context).getTestCase(), menuBar, indexes,
                                                                EventDataConstants.DEFAULT_NUMBEROFCLICKS,
                                                                getMouseModifiers(step),
                                                                EventDataConstants.DEFAULT_ISPOPUPTRIGGER,
                                                                EventDataConstants.DEFAULT_SLEEPTIME);
        eventData.setSleepTime(5000);
        ((net.codjo.test.release.task.gui.toolkit.swing.TestContext) context).getHelper().enterClickAndLeave(eventData);
    }


    private static int[] getPathIndexes(JMenuBar menuBar, String[] labels) {
        int[] indexes = new int[labels.length];
        indexes[0] = findMenuIndex(menuBar, labels[0]);
        JPopupMenu popupMenu = menuBar.getMenu(indexes[0]).getPopupMenu();

        String[] labelsWithoutFirst = new String[labels.length - 1];
        System.arraycopy(labels, 1, labelsWithoutFirst, 0, labels.length - 1);
        int[] pathIndexes = getPathIndexes(popupMenu, labelsWithoutFirst);
        System.arraycopy(pathIndexes, 0, indexes, 1, pathIndexes.length);

        return indexes;
    }


    static int[] getPathIndexes(JPopupMenu popupMenu, String[] labels) {
        int[] indexes = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            indexes[i] = findMenuItemIndex(popupMenu, labels[i]);

            if (i < (labels.length - 1)) {
                Component child = popupMenu.getComponent(indexes[i]);
                if (!(child instanceof JMenu)) {
                    throw new GuiFindException(labels[i] + " n'est pas un sous-menu");
                }

                JMenu childMenu = (JMenu)child;
                popupMenu = childMenu.getPopupMenu();
            }
        }

        return indexes;
    }


    private static int findMenuIndex(JMenuBar menuBar, String label) {
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu currentMenu = menuBar.getMenu(i);
            if (currentMenu.getText().equals(label)) {
                if (!currentMenu.isEnabled()) {
                    throw new GuiFindException("Menu d�sactiv� : " + label);
                }

                return i;
            }
        }

        throw new GuiFindException("Menu non trouv� : " + label);
    }


    private static int findMenuItemIndex(JPopupMenu popupMenu, String label) {
        for (int i = 0; i < popupMenu.getComponentCount(); i++) {
            Component comp = popupMenu.getComponent(i);
            if (!(comp instanceof JMenuItem)) {
                continue;
            }

            JMenuItem menuItem = (JMenuItem)comp;
            if (menuItem.getText().equals(label)) {
                if (!menuItem.isEnabled()) {
                    throw new GuiFindException("Menu d�sactiv� : " + label);
                }

                return i;
            }
        }

        throw new GuiFindException("MenuItem non trouv� : " + label);
    }


    @Override
    protected void setReferencePointIfNeeded(MouseEventData eventData, Component component, T step) {
        if (descriptor != null) {
            super.setReferencePointIfNeeded(eventData, component, step);
        }
        else {
            Rectangle cellRect = null;
            if (JTable.class.isInstance(component)) {
                JTable table = (JTable)component;
                int columnNumber = TableTools.searchColumn(table, step.getColumn());
                TableTools.checkTableCellExists(table, step.getRow(), columnNumber);
                cellRect = table.getCellRect(step.getRow(), columnNumber, true);
            }
            else if (JList.class.isInstance(component)) {
                JList list = (JList)component;
                ListTools.checkRowExists(list, step.getRow());
                cellRect = list.getCellBounds(step.getRow(), step.getRow());
            }

            if (cellRect != null) {
                eventData.setPosition(EventDataConstants.CUSTOM);
                eventData.setReferencePoint(cellRect.getLocation());
            }
        }
    }


    @Override
    protected void initDescriptor(Component comp, T step) {
        if (step.getPath() != null) {
            descriptor = new ClickDescriptor<T>() {
                public Component getComponentToClick(Component comp, T step) {
                    return comp;
                }


                public void setReferencePointIfNeeded(MouseEventData eventData,
                                                      Component component,
                                                      T step) {
                    JTree tree = (JTree)component;
                    if (step.getPath() == null) {
                        throw new GuiConfigurationException("Le path n'a pas �t� renseign�.");
                    }

                    final TreePath treePath = TreeUtils
                          .convertIntoTreePath(tree, step.getPath(), TreeStepUtils.getConverter(step.getMode()));

                    Rectangle cellRect = tree.getPathBounds(treePath);
                    if (cellRect != null) {
                        cellRect.x += cellRect.width / 2;
                        cellRect.y += cellRect.height / 2;

                        eventData.setPosition(EventDataConstants.CUSTOM);
                        eventData.setReferencePoint(cellRect.getLocation());
                    }
                }
            };
        }
        else {
            descriptor = Introspector.getTestBehavior(comp.getClass(), ClickDescriptor.class);
        }
    }
}
