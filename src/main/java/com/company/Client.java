package com.company;

import com.company.StartMenu.StartMenu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends Thread {

    public Client(String _login, String _ip, int _port) throws IOException {
        instance = this;
        login = _login;
        ip = _ip;
        port = _port;
        timer = new Timer();
    }
    

    public void update() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (/*!sendFlag && */!closeFlag /*&& VirusApplication.getInstance().initFlag && !VirusApplication.getInstance().finishGameFlag && VirusApplication.getInstance().moveStatus != VirusApplication.getInstance().clientKind*/) {
//                    System.out.println("get move action");
                    try {
                        if (clientFlag) {
//                            System.out.println("clientflag true");
                            getClientCount();
                        }
//                        getMove();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 400);
    }

    public void quit() throws IOException {
        if (!closeFlag) {
            oos.writeUTF("quit");
            oos.close();
            closeFlag = true;
//            readyFlag = false;
            System.out.println("Closing connections & channels on clientSide - DONE.");
        }
    }

    public void getClientCount() throws IOException {
        if (!closeFlag) {
            oos.writeUTF("getClientCount");
            oos.reset();
            int clientCount = Integer.parseInt(ois.readUTF());
            System.out.println("client count " + clientCount);
            if (clientCount == 2) {
                clientFlag = false;
                StartMenu.getInstance().menuController.getStartButton().setDisable(false);
            }
        }
    }

    public String getClientType() throws IOException {
        String type = null;
        if (!closeFlag) {
//            oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.reset();
//            System.out.println("getClientType");
            oos.writeUTF("getClientType");
            oos.reset();
            type = ois.readUTF();
//            System.out.println(type);
//            System.out.println("client count " + clientCount);
        }
        return type;
    }

    public void run() {
        System.out.println("client thread " + getId());
        try {
            socket = new Socket(ip, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.reset();
            ois = new ObjectInputStream(socket.getInputStream());
            if (oos != null) {
                System.out.println("Client connected to socket.");
                oos.writeUTF(login);
                oos.reset();
                clientType = getClientType();
//                readyFlag = true;
                update();
                while (!socket.isClosed() && !closeFlag) {
                    Thread.sleep(1000); //todo timerTask
                }
                System.out.println("Client disconnected" + closeFlag);
                ois.close();
                timer.cancel();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Connection refused");
        }
        closeFlag = true;
    }

    public static Client getInstance() {
        Client localInstance = instance;
        if (localInstance == null) {
            synchronized (Client.class) {
                localInstance = instance;
            }
        }
        return localInstance;
    }
    private static volatile Client instance;
    private final String login;
    private final String ip;
    private final int port;
    private final Timer timer;
    Socket socket = null;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    public boolean closeFlag = false;
    public boolean clientFlag = false;
    public String clientType = "";
}
