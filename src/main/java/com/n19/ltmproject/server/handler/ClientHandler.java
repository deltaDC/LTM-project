package com.n19.ltmproject.server.handler;

import com.n19.ltmproject.server.manager.ClientManager;

import java.io.*;
import java.net.Socket;


/**
 * Handles communication with a single client.
 * This class is responsible for managing the connection, reading input from the client,
 * and sending output to the client. It also handles client disconnection and notifies
 * the ClientManager when a client disconnects.
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String username;
    private final ClientManager clientManager;

    /**
     * Constructor to initialize the ClientHandler with a socket and a reference to the ClientManager.
     *
     * @param socket the socket connected to the client
     * @param clientManager the manager that handles all connected clients
     */
    public ClientHandler(Socket socket, ClientManager clientManager) {
        this.socket = socket;
        this.clientManager = clientManager;
    }

    /**
     * The main method of the thread that handles the client's communication.
     * It reads the client's credentials, processes them, and sends appropriate responses.
     */
    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            String credentials = input.readLine();
            System.out.println("[Server] Received credentials: " + credentials);

            String[] parts = credentials.split(" ");
            if (parts.length == 2) {
                //TODO check the credentials in the database -> write a service
                this.username = parts[0];
                String password = parts[1];
                System.out.println("[Server] " + username + " has connected");

                output.println("Login successful");

                // This is to keep user not closeConnection() until close the app
                String message;
                while ((message = input.readLine()) != null) {
                    System.out.println("[Server] Received message from " + username + ": " + message);
                }
            } else {
                output.println("Invalid credentials format.");
            }
        } catch (IOException e) {
            System.out.println("Error with client " + username + ": " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public String getUsername() {
        return username;
    }

    /**
     * Interrupts the thread and closes the connection.
     */
    @Override
    public void interrupt() {
        closeConnection();
        super.interrupt();
    }

    /**
     * Closes the connection with the client and notifies the ClientManager to remove this client.
     */
    private void closeConnection() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            System.out.println("Client " + username + " disconnected.");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            clientManager.removeClient(this);
        }
    }
}
