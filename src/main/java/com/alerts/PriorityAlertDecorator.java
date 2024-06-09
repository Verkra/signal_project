package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {
    private String priorityLevel;

    public PriorityAlertDecorator(Alert decoratedAlert, String priorityLevel) {
        super(decoratedAlert);
        this.priorityLevel = priorityLevel;
    }

    @Override
    public String getDetails() {
        return decoratedAlert.getDetails() + String.format(", Priority Level: %s", priorityLevel);
    }
}
