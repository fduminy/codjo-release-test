/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
import java.util.HashMap;
import java.util.Map;
import org.apache.tools.ant.Project;
/**
 * Contexte d'ex�cution des tests pass� entre les steps.
 */
public class TestContext {
    private static final String TEST_LOCATION = "testLocation";
    private Project project;
    private Map objects = new HashMap();


    public TestContext(Project project) {
        this.project = project;
        putObject(TEST_LOCATION, new TestLocation());
    }


    public String replaceProperties(String value) {
        return project.replaceProperties(value);
    }


    public String getProperty(String name) {
        return project.getProperty(name);
    }


    public void setProperty(String name, String value) {
        project.setProperty(name, value);
    }


    public TestLocation getTestLocation() {
        return (TestLocation)getObject(TEST_LOCATION);
    }


    protected void putObject(Object key, Object value) {
        objects.put(key, value);
    }


    protected Object getObject(Object key) {
        return objects.get(key);
    }
}
