package com.example.homework_chatsentfile.server;

import javax.net.ssl.HandshakeCompletedEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerIo {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8189);
        System.out.println("Server started...");
        while (true) {
            try {
                Socket socket = server.accept();
                System.out.println("Client connected...");
                new Thread(new Handler(socket)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
