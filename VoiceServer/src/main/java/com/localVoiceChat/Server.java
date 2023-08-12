package com.localVoiceChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private int PORT;
    private static List<ClientHandler> clientHandlerList;

    Server(ServerSocket serverSocket, int port){
        this.serverSocket = serverSocket;
        this.PORT = port;
    }

    public void start() throws IOException {
        while(!this.serverSocket.isClosed()){
            System.out.println("The server has started!");
            Socket socket = this.serverSocket.accept();
            ClientHandler newClient = new ClientHandler(socket);
            ThreadHandler.startNewClientThread(newClient);
        }
    }
}
