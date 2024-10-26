package com.n19.ltmproject.server.handler;

import com.google.gson.Gson;
import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.command.CommandFactory;
import com.n19.ltmproject.server.manager.ClientManager;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import lombok.Getter;
import lombok.Setter;

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
    private final ClientManager clientManager;
    private final Gson gson = new Gson();
    @Setter
    @Getter
    private String username;

    private final String clientAddress;

    /**
     * Constructor to initialize the ClientHandler with a socket and a reference to the ClientManager.
     *
     * @param socket        the socket connected to the client
     * @param clientManager the manager that handles all connected clients
     */
    public ClientHandler(Socket socket, ClientManager clientManager) {
        this.socket = socket;
        this.clientManager = clientManager;
        this.clientAddress = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

        System.out.println("Client connected from: " + clientAddress);
    }

    /**
     * The main method of the thread that handles the client's communication.
     * It reads the client's credentials, processes them, and sends appropriate responses.
     */
    @Override
    public void run() {
        try {
            setupStreams();
            listenForRequests();
        } catch (IOException e) {
            System.out.println("[Error]: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void setupStreams() throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    private void listenForRequests() throws IOException {
        String jsonRequest;
        while ((jsonRequest = input.readLine()) != null) {
            if (jsonRequest.contains("Invite:")) {
                handleGameInvitation(jsonRequest);
            } else if (jsonRequest.startsWith("STOP_LISTENING")) {
                output.println(gson.toJson(null));
            } else {
                processRequest(jsonRequest);
            }
        }
    }

    // Gọi đến client manager .
    public void handleGameInvitation(String message) {
        if (message.contains("Invite:")) {
            String invitedPlayerName = message.split(":")[1];
            clientManager.invitePlayer(invitedPlayerName, message + " Đã gửi thành công");
        }
    }

    private void processRequest(String jsonRequest) {
        Request request = gson.fromJson(jsonRequest, Request.class);
        logRequest(request);

        Command command = CommandFactory.getCommand(request.getAction(), this);
        Response response = command.execute(request);
        output.println(gson.toJson(response));
    }

    private void logRequest(Request request) {
        System.out.println("---------------");
        System.out.println("Received request from client: " + clientAddress);
        if (!request.getAction().isEmpty() && !request.getParams().isEmpty()) {
            System.out.println("Action: " + request.getAction());
            System.out.println("Data: " + request.getParams());
        }
        System.out.println("---------------");
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
            System.out.println("Client disconnected.");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            clientManager.removeClient(this);
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }
}