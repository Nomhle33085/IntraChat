package com.IntraChat;

import java.util.Scanner;

import com.IntraChat.network.Client;
import com.IntraChat.network.Server;

public class NetworkBackendTest {

    public static void main(String[] args) {
        System.out.println("Network test..");
        System.out.println("Are you a Server? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        String userInp = scanner.nextLine();

        if (userInp.equalsIgnoreCase("y")) {
            System.out.println("Starting Server...");

            int defaultPort = 5000;
            Server server = new Server(defaultPort);
            server.start();

        } else if (userInp.equalsIgnoreCase("n")) {
            System.out.println("Starting Client");

            System.out.print("Enter Username: ");
            String username = scanner.nextLine();

            Client client = new Client("localhost", 5000);
            if (client.connect(username)) {
                System.out.println("Connected! Type your messages below (or '/quit' to leave):");

                while (true) {
                    String msg = scanner.nextLine();
                    if (msg.equalsIgnoreCase("/quit")) {
                        client.sendMessage("/quit");
                        client.disconnect();
                        break;
                    }
                    client.sendMessage(msg);
                }
                scanner.close();
            }

        } else {
            System.out.println("Invalid Input. Quiting");
        }

        scanner.close();
    }
}