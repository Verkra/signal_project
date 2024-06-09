package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.data_management.CustomWebSocketClient;
import com.data_management.DataStorage;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WebSocketClientErrorHandlingTest {
    private CustomWebSocketClient client;
    private DataStorage dataStorage;
    private static final Logger logger = Logger.getLogger(WebSocketClientErrorHandlingTest.class.getName());

    @BeforeEach
    public void setup() {
        dataStorage = new DataStorage();
        client = new CustomWebSocketClient(URI.create("ws://localhost:8080"), dataStorage);
    }



    @Test
    public void testDataTransmissionFailure() {
        // Add sample data to the storage
        client.onMessage("1,1623456789000,HeartRate,75.0");
        client.onMessage("1,1623456790000,BloodPressure,120.0");

        // Assert that the data is stored correctly
        assertEquals(1, dataStorage.getAllPatients().size());
        assertEquals(1, dataStorage.getRecords(1, 1623456789000L, 1623456790000L, "HeartRate").size());
        assertEquals(1, dataStorage.getRecords(1, 1623456789000L, 1623456790000L, "BloodPressure").size());

        // Simulate data transmission failure
        client.onError(new Exception("Data transmission failure"));

        // Assert that the data is still present after transmission failure
        assertEquals(1, dataStorage.getAllPatients().size());
        assertEquals(1, dataStorage.getRecords(1, 1623456789000L, 1623456790000L, "HeartRate").size());
        assertEquals(1, dataStorage.getRecords(1, 1623456789000L, 1623456790000L, "BloodPressure").size());
    }
}
