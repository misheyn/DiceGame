package com.company.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    static ExecutorService executeIt = Executors.newFixedThreadPool(2);


    public static void main(String[] args) {
        clientCount = 0;
        numbers = new ArrayList<>();
        try (ServerSocket server = new ServerSocket(3345)) {
            System.out.println("Server socket created, command console reader for listen to server commands");
            while (!server.isClosed()) {
                Socket client = server.accept();
                MonoThreadClientHandler clientThread = new MonoThreadClientHandler(client);
                clientCount++;
                clientThread.start();
                System.out.println("Connection accepted." + client.getInetAddress());
            }
            executeIt.shutdown();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static Integer clientCount;
    public static String bankerName = null;
    public static String ponterName = null;
    public static int bet = 0;
    public static ArrayList<Integer> numbers;
}
