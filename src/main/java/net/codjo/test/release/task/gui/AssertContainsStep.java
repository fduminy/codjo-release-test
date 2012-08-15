/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;


/**
 * 
 */
public class AssertContainsStep extends AbstractAssertStep {
    private String name;
    private String expected;
    private String mode;

    public void setName(String name) {
        this.name = name;
    }


    public void setExpected(String expected) {
        this.expected = expected;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public String getMode() {
		return mode;
	}
    
    public String getExpected() {
		return expected;
	}
    
    public String getName() {
		return name;
	}
}
