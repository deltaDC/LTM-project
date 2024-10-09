package com.n19.ltmproject.server;

import com.n19.ltmproject.server.controller.ClientHandler;
import com.n19.ltmproject.server.service.GameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


//public class Server {
//
//    private ServerSocket serverSocket;
//    private GameManager gameManager;
//
//    public Server(int port) throws IOException {
//        serverSocket = new ServerSocket(port);
//        gameManager = new GameManager();
//    }
//
//    public void start() throws IOException {
//        System.out.println("Server is running...");
//        while (true) {
//            Socket clientSocket = serverSocket.accept();
//            new ClientHandler(clientSocket, gameManager).start();
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        Server server = new Server(12345);
//        server.start();
//    }
//}

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening on port 1234");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);  // Thêm client vào danh sách
                clientHandler.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }
    }

    public static void invitePlayer(String invitedPlayerName, String message) {
        System.out.println("Inviting player: " + invitedPlayerName);
        for (ClientHandler client : clients) {
            if (client.getUsername().trim().equals(invitedPlayerName.trim())) {
                client.sendMessage(message + " Invite You Game");
                System.out.println("Invite sent to " + invitedPlayerName);
                break;
            } else {
                System.out.println("No match for: " + client.getUsername());
            }
        }
    }
}



