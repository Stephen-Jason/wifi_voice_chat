package com.localVoiceChat;

import java.io.IOException;
import java.net.Socket;

public class ServerHandler {
    private Socket socket;
    private String IP;
    private int PORT;

    public void setPort(int newPort){
        this.PORT = newPort;
    }

    public void setIP(String newIP){
        this.IP = newIP;
    }

    public boolean connect(){
        try {
            this.socket = new Socket(IP, PORT);
            return true;
        } catch (IOException e) {
            System.out.println("Something went wrong connecting to " + IP + " : " + PORT);
            return false;
        }
    }
}
