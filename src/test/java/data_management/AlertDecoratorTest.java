package data_management;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.BasicAlert;
import com.alerts.PriorityAlertDecorator;
import com.alerts.RepeatedAlertDecorator;

import static org.junit.jupiter.api.Assertions.*;

public class AlertDecoratorTest {

    @Test
    public void testRepeatedAlertDecorator() {
        BasicAlert basicAlert = new BasicAlert("1", "High Blood Pressure", System.currentTimeMillis());
        RepeatedAlertDecorator repeatedAlert = new RepeatedAlertDecorator(basicAlert, 60);

        assertEquals("High Blood Pressure", repeatedAlert.getCondition());
        assertTrue(repeatedAlert.getDetails().contains("Recheck every 60 seconds"));

        repeatedAlert.recheckCondition();
    }

    @Test
    public void testPriorityAlertDecorator() {
        Alert basicAlert = new BasicAlert("1", "Low Oxygen Level", System.currentTimeMillis());
        PriorityAlertDecorator priorityAlert = new PriorityAlertDecorator(basicAlert, "High");

        assertEquals("Low Oxygen Level", priorityAlert.getCondition());
        assertTrue(priorityAlert.getDetails().contains("Priority Level: High"));
    }

    @Test
    public void testCombinedAlertDecorator() {
        Alert basicAlert = new BasicAlert("1", "Irregular Heart Rate", System.currentTimeMillis());
        RepeatedAlertDecorator repeatedAlert = new RepeatedAlertDecorator(basicAlert, 60);
        PriorityAlertDecorator priorityAlert = new PriorityAlertDecorator(repeatedAlert, "Critical");

        assertEquals("Irregular Heart Rate", priorityAlert.getCondition());
        assertTrue(priorityAlert.getDetails().contains("Recheck every 60 seconds"));
        assertTrue(priorityAlert.getDetails().contains("Priority Level: Critical"));

        repeatedAlert.recheckCondition();
    }
}
