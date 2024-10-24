package com.n19.ltmproject.server.handler;

import com.google.gson.Gson;
import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.command.CommandFactory;
import com.n19.ltmproject.server.command.auth.LoginCommand;
import com.n19.ltmproject.server.manager.ClientManager;
import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import lombok.Getter;

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
    @Getter
    private String username;

    private final String clientAddress;

    /**
     * Constructor to initialize the ClientHandler with a socket and a reference to the ClientManager.
     *
     * @param socket the socket connected to the client
     * @param clientManager the manager that handles all connected clients
     */
    public ClientHandler(Socket socket, ClientManager clientManager) {
        this.socket = socket;
        this.clientManager = clientManager;
        this.clientAddress = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

        System.out.println("Client connected from: " + clientAddress);
    }
    // Gọi đến client manager .
    public void handleMessage(String message) {
        if (message.contains("Invite:")) {
            String invitedPlayerName = message.split(":")[1];
            // Gọi phương thức invitePlayer từ ClientManager
            clientManager.invitePlayer(invitedPlayerName, message+" Đã gửi thành công");
        }
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

            String jsonRequest;
            // Nhảy vào hàm handleMessage.
            while ((jsonRequest = input.readLine()) != null) {
                if(jsonRequest.contains("Invite:")){
                    handleMessage(jsonRequest);
                }
                else if(jsonRequest.startsWith("NGATLISTENING")){
                        output.println(gson.toJson(null));
                }
                else{

                    Request request = gson.fromJson(jsonRequest, Request.class);

                    System.out.println("---------------");
                    System.out.println("Received request from client: " + clientAddress);
                    if(!request.getAction().isEmpty() && !request.getParams().isEmpty()) {
                        System.out.println("Action: " + request.getAction());
                        System.out.println("Data: " + request.getParams().toString());
                    }
                    System.out.println("---------------");
                    // Gọi handleMessage để xử lý tin nhắn


                    Command command = CommandFactory.getCommand(request.getAction(),this);
                    Response response = command.execute(request);

                    output.println(gson.toJson(response));
                }
            }
        } catch (IOException e) {
            System.out.println("[Error]: " + e.getMessage());
        } finally {
            closeConnection();
        }
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
        output.println(message); // output là PrintWriter kết nối với client
    }
}