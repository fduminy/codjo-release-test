/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui.toolkit.swing;
import java.awt.Component;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;

import org.apache.tools.ant.Project;
/**
 * Contexte d'ex�cution des tests pass� entre les steps.
 */
class TestContext extends net.codjo.test.release.task.gui.TestContext {
    private static final String CURRENT_COMPONENT = "currentComponent";
	
    private JFCTestCase testCase = null;
    private JFCTestHelper helper = new JFCTestHelper();

    TestContext(JFCTestCase testCase) {
        this(testCase, new Project());
    }


    TestContext(JFCTestCase testCase, Project project) {
    	super(project);
        testCase.setHelper(helper);
        this.testCase = testCase;
    }

    public void setCurrentComponent(Component component) {
        putObject(CURRENT_COMPONENT, component);
    }


    public Component getCurrentComponent() {
        return (Component)getObject(CURRENT_COMPONENT);
    }

    public JFCTestHelper getHelper() {
        return helper;
    }


    public JFCTestCase getTestCase() {
        return testCase;
    }
}
