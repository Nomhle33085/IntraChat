package com.IntraChat.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
private final String serverIp;
    private final int serverPort;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isConnected;

    public Client(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public boolean connect(String username) {
        try {
            socket = new Socket(serverIp, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            isConnected = true;

            out.println(username);

            new Thread(this::listenForMessages).start();

            return true;
        } catch (IOException e) {
            System.err.println("(error) Could not connect to server: " + e.getMessage());
            return false;
        }
    }

    private void listenForMessages() {
        try {
            String incoming;
            while (isConnected && (incoming = in.readLine()) != null) {
                // Note: Remember to handle the gui aspect when complete
                System.out.println(incoming);
            }
        } catch (IOException e) {
            if (isConnected) {
                System.out.println("(client) Disconnected from server.");
            }
        } finally {
            disconnect();
        }
    }
    public void sendMessage(String message) {
        if (out != null && isConnected) {
            out.println(message);
        }
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("(error) Error closing connection: " + e.getMessage());
        }
    }
}
