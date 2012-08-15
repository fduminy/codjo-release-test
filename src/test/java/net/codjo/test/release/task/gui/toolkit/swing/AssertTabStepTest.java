package net.codjo.test.release.task.gui.toolkit.swing;

import static net.codjo.test.release.task.gui.toolkit.swing.SwingGuiTestUtils.proceedOnce;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.codjo.test.release.task.gui.AssertTabStep;
import net.codjo.test.release.task.gui.GuiAssertException;
import net.codjo.test.release.task.gui.GuiFindException;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.TestHelper;

public class AssertTabStepTest extends JFCTestCase {
    private AssertTabStep step;
    private JTabbedPane tabbedPane;


    @Override
    public void setUp() {
        step = new AssertTabStep();
        step.setTimeout(1);
        step.setDelay(5);
        step.setWaitingNumber(10);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        TestHelper.cleanUp(this);
    }


    private void showFrame(Component tab) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        panel.add(tab);

        frame.pack();
        frame.setVisible(true);
        flushAWT();
    }


    private JTabbedPane buildTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setName("MyTabbedPane");
        tabbedPane.add("Onglet0", new JPanel());
        tabbedPane.add("Onglet1", new JPanel());
        tabbedPane.add("Onglet2", new JPanel());
        return tabbedPane;
    }


    public void testInvalidComponent() {
        step.setName("TabbedPaneBidon");
        step.setTabLabel("Onglet0");
        step.setTabIndex(1);

        showFrame(new JButton("TabbedPane1"));
        try {
            proceedOnce(new TestContext(this), step);            
            fail();
        }
        catch (GuiFindException e) {
            assertEquals(
                  "Le conteneur d'onglets (JTabbedPane) portant le nom TabbedPaneBidon est introuvable",
                  e.getMessage());
        }
    }


    public void testAssertByNameKO() {
        showFrame(buildTabbedPane());

        step.setName("TabbedPaneBidon");
        step.setTabLabel("Onglet0");
        step.setTabIndex(1);
        try {
            proceedOnce(new TestContext(this), step);
            fail();
        }
        catch (GuiFindException e) {
            assertEquals(
                  "Le conteneur d'onglets (JTabbedPane) portant le nom TabbedPaneBidon est introuvable",
                  e.getMessage());
        }
    }


    public void testAssertByLabelOK() {
        showFrame(buildTabbedPane());

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet0");
        proceedOnce(new TestContext(this), step);
    }


    public void testAssertByLabelKO() {
        showFrame(buildTabbedPane());

        step.setName("MyTabbedPane");
        step.setTabLabel("OngletBidon");
        try {
            proceedOnce(new TestContext(this), step);
            fail();
        }
        catch (GuiFindException e) {
            assertEquals("L'onglet portant le nom OngletBidon est introuvable", e.getMessage());
        }
    }


    public void testAssertByLabelAndIndexOK() {
        showFrame(buildTabbedPane());

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet2");
        step.setTabIndex(2);
        proceedOnce(new TestContext(this), step);
    }


    public void testAssertByLabelAndIndexKO() {
        showFrame(buildTabbedPane());

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet2");
        step.setTabIndex(1);
        try {
            proceedOnce(new TestContext(this), step);
            fail();
        }
        catch (GuiFindException e) {
            assertEquals("L'onglet Onglet2 en position 2 ne se trouve pas en position 1", e.getMessage());
        }
    }


    public void testAssertSelectedTrueOk() throws Exception {
        showFrame(buildTabbedPane());

        tabbedPane.setSelectedIndex(0);

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet0");
        step.setSelected(true);
        proceedOnce(new TestContext(this), step);
    }


    public void testAssertSelectedTrueKo() throws Exception {
        showFrame(buildTabbedPane());

        tabbedPane.setSelectedIndex(1);

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet0");
        step.setSelected(true);
        try {
            proceedOnce(new TestContext(this), step);
            fail();
        }
        catch (GuiAssertException e) {
            assertEquals("L'onglet 'Onglet0' n'est pas s�lectionn�.", e.getMessage());
        }
    }


    public void testAssertSelectedFalseOk() throws Exception {
        showFrame(buildTabbedPane());

        tabbedPane.setSelectedIndex(1);

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet0");
        step.setSelected(false);
        proceedOnce(new TestContext(this), step);
    }


    public void testAssertSelectedFalseKo() throws Exception {
        showFrame(buildTabbedPane());

        tabbedPane.setSelectedIndex(0);

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet0");
        step.setSelected(false);
        try {
            proceedOnce(new TestContext(this), step);
            fail();
        }
        catch (GuiAssertException e) {
            assertEquals("L'onglet 'Onglet0' est s�lectionn�.", e.getMessage());
        }
    }


    public void testAssertEnabledTabOK() throws Exception {
        showFrame(buildTabbedPane());
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, true);

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet1");
        step.setEnabled(true);
        proceedOnce(new TestContext(this), step);

        tabbedPane.setEnabledAt(1, false);
        step.setEnabled(false);
        proceedOnce(new TestContext(this), step);
    }


    public void testAssertEnabledTabKO() throws Exception {
        showFrame(buildTabbedPane());
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, false);

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet1");
        step.setEnabled(true);
        try {
            proceedOnce(new TestContext(this), step);
            fail();
        }
        catch (GuiAssertException guiAssertException) {
            assertEquals(guiAssertException.getMessage(),
                         "L'onglet 'Onglet1' est ou n'est pas actif contrairement � ce qui a �t� sp�cifi�");
        }
    }


    public void testAssertEnabledTabOKselectedKO() throws Exception {
        showFrame(buildTabbedPane());
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, true);
        tabbedPane.setSelectedIndex(1);

        step.setName("MyTabbedPane");
        step.setTabLabel("Onglet1");
        step.setEnabled(true);
        proceedOnce(new TestContext(this), step);

        tabbedPane.setEnabledAt(1, false);
        step.setEnabled(false);
        try {
            proceedOnce(new TestContext(this), step);
        }
        catch (GuiAssertException exception) {
            assertEquals(exception.getMessage(), "L'onglet 'Onglet0' n'est pas s�lectionn�.");
        }
    }
}
