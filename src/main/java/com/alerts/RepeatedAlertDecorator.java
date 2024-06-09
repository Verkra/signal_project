package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int repeatInterval; // Interval in seconds

    public RepeatedAlertDecorator(Alert decoratedAlert, int repeatInterval) {
        super(decoratedAlert);
        this.repeatInterval = repeatInterval;
    }

    @Override
    public String getDetails() {
        return decoratedAlert.getDetails() + String.format(", Recheck every %d seconds", repeatInterval);
    }

    // Method to simulate rechecking the alert condition
    public void recheckCondition() {
        // Implement the rechecking logic here
        System.out.println("Rechecking condition for alert: " + decoratedAlert.getCondition());
    }
}


