package com.n19.ltmproject.server;

import com.n19.ltmproject.server.controller.ClientHandler;
import com.n19.ltmproject.server.service.GameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private GameManager gameManager;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        gameManager = new GameManager();
    }

    public void start() throws IOException {
        System.out.println("Server is running...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket, gameManager).start();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(12345);
        server.start();
    }
}
