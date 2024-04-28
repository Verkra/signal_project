package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Implements the OutputStrategy to send output data over a TCP connection.
 * This class manages a TCP server that sends formatted patient data to a connected TCP client.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Initializes a TCP server on the specified port.
     * The server runs in a separate thread and listens for incoming client connections.
     *
     * @param port the port number on which the server will listen for connections
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Outputs the formatted data to the connected TCP client.
     * If no client is connected, the data is not sent.
     *
     * @param patientId the identifier of the patient
     * @param timestamp the timestamp of the data
     * @param label a label describing the type of data
     * @param data the data to be sent
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
