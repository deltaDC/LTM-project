package com.n19.ltmproject.server.controller;

import com.n19.ltmproject.server.service.GameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{

    private final Socket clientSocket;
    private GameManager gameManager;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientHandler(Socket socket, GameManager gameManager) {
        this.clientSocket = socket;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());

            while (true) {
                String request = (String) input.readObject();
                if (request.equalsIgnoreCase("exit")) {
                    break;
                }

                // Process the request and respond
                String response = handleRequest(request);
                output.writeObject(response);
                output.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String handleRequest(String request) {
        return "Invalid request";
    }
}
