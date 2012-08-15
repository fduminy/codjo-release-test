/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.test.release.task.gui;
import net.codjo.test.release.task.gui.matcher.MatcherFactory;

/**
 * Classe permettant de v�rifier l'�tat d'une JInternalFrame.
 */
public class AssertFrameStep extends AbstractAssertStep {
    private String title;
    private boolean closed = false;
    private String matching = MatcherFactory.EQUALS_MATCHING;


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public boolean getClosed() {
        return closed;
    }


    public void setClosed(boolean closed) {
        this.closed = closed;
    }


    public void setMatching(String matching) {
        this.matching = matching;
    }
    
    public String getMatching() {
		return matching;
	}
}
