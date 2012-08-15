package net.codjo.test.release.task.gui;

/**
 *
 */
public class AssertComponentImageStep extends AbstractAssertStep {

    private String name;
    private String expected;
    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getExpected() {
        return expected;
    }


    public void setExpected(String expected) {
        this.expected = expected;
    }
}
