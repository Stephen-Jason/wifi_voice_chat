package com.localVoiceChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private static List<ClientHandler> clientHandlerList;

    Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void start() throws IOException {
        while(!this.serverSocket.isClosed()){
            System.out.println("The server has started!");
            System.out.println("The server is ready and waiting for a new connection to be received. . . ");
            Socket socket = this.serverSocket.accept();
            System.out.println("A new connection has been received from the ip: " + socket.getLocalAddress());
            ClientHandler newClient = new ClientHandler(socket);
            ThreadHandler.startNewClientThread(newClient);
        }
    }
}
