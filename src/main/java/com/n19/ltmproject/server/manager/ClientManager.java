package com.n19.ltmproject.server.manager;

import com.n19.ltmproject.server.handler.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {

    private final List<ClientHandler> clients = new ArrayList<>();

    public ClientManager() {}

    public synchronized void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        System.out.println("There are " + clients.size() + " clients have connected");
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}
