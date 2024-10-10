package com.n19.ltmproject.client.handler;

import java.io.*;
import java.net.*;

public class ServerHandler {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);  // Enable auto-flush
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Successfully connected to the server!");  // Successful connection
        } catch (UnknownHostException e) {
            System.out.println("Cannot connect to host: " + host + ". Check the server address.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnect() throws IOException {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("Disconnected from the server.");
        } catch (IOException e) {
            System.out.println("Error while disconnecting: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            System.out.println("Message sent: " + message);
        }
    }

    public String receiveMessage() throws IOException {
        try {
            String message = in.readLine();
            if (message != null) {
                System.out.println("Received message: " + message);
            }
            return message;
        } catch (IOException e) {
            System.out.println("Error reading message from server: " + e.getMessage());
            return null;
        }
    }
}