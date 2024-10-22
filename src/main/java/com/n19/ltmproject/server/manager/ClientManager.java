package com.n19.ltmproject.server.manager;

import com.n19.ltmproject.server.handler.ClientHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for managing the clients that are connected to the server.
 */
public class ClientManager {

    private final List<ClientHandler> clients = new ArrayList<>();

    public ClientManager() {}

    public synchronized void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        System.out.println("There are " + clients.size() + " clients have connected");
    }
    public synchronized List<ClientHandler> getClients() {
        return new ArrayList<>(clients); // Trả về một bản sao của danh sách để tránh các vấn đề đồng bộ
    }

    // Mời người chơi được mời
    public void invitePlayer(String invitedPlayerName, String message) {
        System.out.println("Inviting player: " + invitedPlayerName);

        for (ClientHandler client : getClients()) {
            System.out.println("Client username: " + client.getUsername());
            if (client.getUsername().trim().equals(invitedPlayerName.trim())) {
                client.sendMessage(message + " Invite You Game");
                System.out.println("Invite sent to " + invitedPlayerName);
                break;
            } else {
                System.out.println("No match for: " + client.getUsername());
            }
        }
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}