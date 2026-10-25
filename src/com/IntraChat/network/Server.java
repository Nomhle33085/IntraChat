package com.IntraChat.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

private final int port;
    private ServerSocket serverSocket;
    private boolean isRunning;
    
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        isRunning = true;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("(server) Started on port " + port + ". Waiting for clients...");

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("(server) New connection from: " + clientSocket.getRemoteSocketAddress());

                ClientHandler handler = new ClientHandler(clientSocket, this);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            if (isRunning) {
                System.err.println("(server error) " + e.getMessage());
            }
        } finally {
            stop();
        }
    }

    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("(server) Client disconnected. Total active: " + clients.size());
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ClientHandler client : clients) {
                client.closeConnection();
            }
            clients.clear();
            System.out.println("(server) Server stopped.");
        } catch (IOException e) {
            System.err.println("(server error) Error closing server: " + e.getMessage());
        }
    }

}
