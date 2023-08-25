package com.localVoiceChat;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private Socket socket;
    private String IP;
    private int PORT;
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;
    private AudioFormat audioFormat;
    private AudioInputStream audioInputStream;
    private TargetDataLine targetDataLine;
    private int num;

    ServerHandler(){
        this.audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        this.targetDataLine = this.openMicrophone();
    }

    public void setPort(int newPort){
        this.PORT = newPort;
    }

    public void setIP(String newIP){
        this.IP = newIP;
    }

    public boolean connect(){
        try {
            this.socket = new Socket(IP, PORT);
            this.bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
            this.bufferedOutputStream = new BufferedOutputStream(this.socket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.out.println("Something went wrong connecting to " + IP + " : " + PORT);
            return false;
        }
    }

    public void startRecordingAudio(){
        this.targetDataLine.start();
        this.audioInputStream = new AudioInputStream(this.targetDataLine);
    }

    public void stopRecordingAudio(){
        this.targetDataLine.stop();
    }

    private TargetDataLine openMicrophone(){
        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, this.audioFormat);
        try (
                TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
        ){
            targetDataLine.open();
            return targetDataLine;

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeMicrophone(){
        this.targetDataLine.close();
    }

    private void sendAudioToServer(){
        try {
            AudioSystem.write(this.audioInputStream, AudioFileFormat.Type.WAVE, this.bufferedOutputStream);
        } catch (IOException e) {
            System.out.println("Something went wrong sending audio to the server");
        }
    }

    public void receiveAudioFromServer(){

                while(!this.socket.isClosed()){
                    try(
                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.bufferedInputStream);
                    ){
                        SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(this.audioFormat);
                        sourceDataLine.open();
                        while(sourceDataLine.isActive()){
                            sourceDataLine.write(audioInputStream.readAllBytes(), 0, 0);
                            System.out.println("something is happening! " + ++this.num);
                        }
                    } catch (IOException e) {
                        System.out.println("something went wrong listening to the server!");
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    } catch (UnsupportedAudioFileException e) {
                        throw new RuntimeException(e);
                    }
                }


    }

    @Override
    public void run() {
        this.receiveAudioFromServer();
    }
}
