package com.localVoiceChat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;
    private static List<ClientHandler> clientHandlerList = new ArrayList<>();
    private int clientID;
    public int num = 0;

    ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedOutputStream = new BufferedOutputStream(this.socket.getOutputStream());
        this.bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
        this.clientID = clientHandlerList.size() + 1;
    }

    public int getClientID(){
        return this.clientID;
    }

    private static void addNewClient(ClientHandler clientHandler){
        clientHandlerList.add(clientHandler);
    }

    public List<ClientHandler> getClientHandlerList(){
        return clientHandlerList;
    }

    public void sendAudioToClient(AudioInputStream audioInputStream) {
        try{
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, this.bufferedOutputStream);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public void receiveAudioFromClient() throws IOException {
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        while(!this.socket.isInputShutdown()){
            try(
                    AudioInputStream audioInputStream = new AudioInputStream(this.bufferedInputStream, audioFormat, 1000);
                    ){

                clientHandlerList.stream().forEach(client -> {
                    if(!client.equals(this)){
                        client.sendAudioToClient(audioInputStream);
                    }
                });
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof ClientHandler)){
            return false;
        }

        ClientHandler otherClient = (ClientHandler) o;
        return this.clientID == otherClient.clientID;
    }


    @Override
    public void run() {

    }
}
