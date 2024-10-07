package com.n19.ltmproject.server.controller;

import com.n19.ltmproject.server.Server;
import com.n19.ltmproject.server.service.GameManager;

import java.io.*;
import java.net.Socket;

//public class ClientHandler extends Thread{
//
//    private final Socket clientSocket;
//    private GameManager gameManager;
//    private ObjectInputStream input;
//    private ObjectOutputStream output;
//
//    public ClientHandler(Socket socket, GameManager gameManager) {
//        this.clientSocket = socket;
//        this.gameManager = gameManager;
//    }
//
//    @Override
//    public void run() {
//        try {
//            input = new ObjectInputStream(clientSocket.getInputStream());
//            output = new ObjectOutputStream(clientSocket.getOutputStream());
//
//            while (true) {
//                String request = (String) input.readObject();
//                if (request.equalsIgnoreCase("exit")) {
//                    break;
//                }
//
//                // Process the request and respond
//                String response = handleRequest(request);
//                output.writeObject(response);
//                output.flush();
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String handleRequest(String request) {
//        return "Invalid request";
//    }
//}
public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Nhận tên đăng nhập từ client

            username = in.readLine();
            System.out.println(username + " has connected.");

            // Xác nhận đăng nhập thành công
            sendMessage("Login successful: " + username);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);

                if (message.startsWith("Invite:")) {
                    String invitedPlayer = message.split(":")[1];
                    Server.invitePlayer(invitedPlayer, username);
                }
            }
        } catch (IOException ex) {
            System.out.println("ClientHandler exception: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Socket closure exception: " + ex.getMessage());
            }
        }
    }
}
