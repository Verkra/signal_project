package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.data_management.CustomWebSocketClient;
import com.data_management.DataStorage;

import java.net.URI;

import static org.mockito.Mockito.*;

public class CustomWebSocketClientTest {
    private CustomWebSocketClient client;
    private DataStorage dataStorage;

    @BeforeEach
    public void setup() {
        dataStorage = Mockito.mock(DataStorage.class);
        client = new CustomWebSocketClient(URI.create("ws://localhost:8080"), dataStorage);
    }

    @Test
    public void testOnMessage_ValidMessage() {
        String message = "1,1623456789000,HeartRate,75.0";
        client.onMessage(message);
        verify(dataStorage, times(1)).addPatientData(1, 75.0, "HeartRate", 1623456789000L);
    }

    @Test
    public void testOnMessage_InvalidMessage() {
        String message = "1,1623456789000,HeartRate,invalid_value";
        client.onMessage(message);
        verify(dataStorage, never()).addPatientData(anyInt(), anyDouble(), anyString(), anyLong());
    }

    @Test
    public void testOnClose() {
        client.onClose(1000, "Normal closure", true);
        // Add assertions for expected behavior
    }

    @Test
    public void testOnError() {
        Exception ex = new Exception("Test exception");
        client.onError(ex);
        // Add assertions for expected behavior
    }
}
