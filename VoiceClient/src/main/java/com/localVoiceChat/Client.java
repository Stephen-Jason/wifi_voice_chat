package com.localVoiceChat;

import java.util.Scanner;

public class Client {



    private Scanner scanner;
    private ServerHandler serverHandler;
    Client(){
        this.scanner = new Scanner(System.in);
    }

    public void askForIP(){
        System.out.println("Please enter the IP Address of the host that exists on your local network");
        String userIP;
        while(!(userIP = this.scanner.nextLine()).matches("(0|[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})")){
            System.out.println("Please enter an IP Address that exists on your local network");
        }

        if(!userIP.equals("0")){
            this.serverHandler.setIP(userIP);
        }

    }

    public void askForPort(){
        System.out.println("Please enter the port number of the host");
        String userPort;
        while(!(userPort = this.scanner.nextLine()).matches("(0|[5-9][0-9]{3}|[1-6][0-9]{4})")){
            System.out.println("Please enter the port number of the host");
        }
        if(!userPort.equals("0")){
            this.serverHandler.setPort(Integer.parseInt(userPort));
        }
    }

    public void start(){
        this.serverHandler = new ServerHandler();
        this.askForIP();
        this.askForPort();
        boolean connected = this.serverHandler.connect();

        if(!connected){
            System.out.println("Do you want to try connect to a different server?(y/n)");

            String userInput;
            while(!(userInput = this.scanner.nextLine()).matches("y|n")){
                System.out.println("Do you want to try connect to a different server?(y/n)");
            }

            if(userInput.equals("y")){
                this.serverHandler.connect();
            }

            System.out.println("Goodbye!");
        }
    }


}
