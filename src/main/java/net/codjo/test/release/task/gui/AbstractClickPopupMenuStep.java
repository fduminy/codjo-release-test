package net.codjo.test.release.task.gui;

public abstract class AbstractClickPopupMenuStep extends StepList {
    public static final int INITIAL_ROW_VALUE = -1;
    protected String column = "0";
    protected String select;
    protected int finderTimeout = 2;
    protected int waitingNumber = 10;
    protected int disappearTryingNumber = 10;
    private boolean popupVisible = true;


    public String getColumn() {
        return column;
    }


    public void setColumn(String column) {
        this.column = column;
    }


    public String getSelect() {
        return select;
    }


    public void setSelect(String select) {
        this.select = select;
    }


    public boolean isPopupVisible() {
        return popupVisible;
    }


    public void setPopupVisible(boolean popupVisible) {
        this.popupVisible = popupVisible;
    }


    public int getFinderTimeout() {
        return finderTimeout;
    }


    public void setFinderTimeout(int finderTimeout) {
        this.finderTimeout = finderTimeout;
    }


    public void setWaitingNumber(int waitingNumber) {
        this.waitingNumber = waitingNumber;
    }


    public int getWaitingNumber() {
        return waitingNumber;
    }


    public void setDisappearTryingNumber(int disappearTryingNumber) {
        this.disappearTryingNumber = disappearTryingNumber;
    }

    public int getDisappearTryingNumber() {
		return disappearTryingNumber;
	}

    public void addSelect(SelectStep step) {
        addStep(step);
    }


    public void addAssertListSize(AssertListSizeStep step) {
        addStep(step);
    }


    public void addAssertList(AssertListStep step) {
        addStep(step);
    }


    public void addAssertEnabled(AssertEnabledStep step) {
        addStep(step);
    }


    public void addPause(PauseStep step) {
        addStep(step);
    }
}
