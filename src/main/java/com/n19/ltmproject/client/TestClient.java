package com.n19.ltmproject.client;

import com.google.gson.Gson;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * A simple client that connects to the server and sends a test request.
 */
public class TestClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            // Create a test request
            Request request = new Request();
            request.setAction("getAllGameData");
            request.setData(null);

            // Send the request to the server
            output.println(gson.toJson(request));

            // Read the response from the server
            String jsonResponse = input.readLine();
            Response response = gson.fromJson(jsonResponse, Response.class);

            // Print the response
            System.out.println("Response: " + response.getStatus() + " - " + response.getMessage());
            System.out.println(response.getData().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
