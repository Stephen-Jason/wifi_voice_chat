package com.localVoiceChat;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    private Server server;
    private ServerSocket serverSocket;
    public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(5000));
        server.start();
    }
}