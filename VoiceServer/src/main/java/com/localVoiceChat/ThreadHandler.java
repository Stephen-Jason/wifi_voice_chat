package com.localVoiceChat;

public class ThreadHandler {

    public static void startNewClientThread(ClientHandler clientHandler){
        Thread thread = new Thread(clientHandler);
        thread.start();
    }
}
