package com.company;

import com.company.Main.Game;
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
                if (!closeFlag/* && Game.getInstance().initFlag && !Game.getInstance().winFlag*/) {
                    try {
                        if (clientFlag) getClientCount();
                        if (getBankerMoveFlag) getBankerMove();
                        if (getPonterMoveFlag) getPonterMove();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        e.printStackTrace();
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
            System.out.println("Closing connections & channels on clientSide - DONE.");
        }
    }

    public String getPartnerName() throws IOException {
        String name = null;
        if (!closeFlag) {
            oos.writeUTF("getPartnerName");
            oos.reset();
            name = ois.readUTF();
        }
        return name;
    }

    public void getClientCount() throws IOException {
        if (!closeFlag) {
            oos.writeUTF("getClientCount");
            oos.reset();
            int clientCount = Integer.parseInt(ois.readUTF());
//            System.out.println("client count " + clientCount);
            if (clientCount == 2) {
                clientFlag = false;
                StartMenu.getInstance().menuController.getStartButton().setDisable(false);
                clientType = getClientType();
                String partnerName = getPartnerName();
                if (clientType.equals("Ponter")) {
//                    System.out.println("Ponter");
                    Game.getInstance().clientType = PlayerType.Ponter;
                    Game.getInstance().addPlayer(StartMenu.getInstance().menuController.getLoginText(), PlayerType.Ponter);
                    Game.getInstance().addPlayer(partnerName, PlayerType.Banker);
                }
                else {
                    Game.getInstance().clientType = PlayerType.Banker;
                    Game.getInstance().addPlayer(StartMenu.getInstance().menuController.getLoginText(), PlayerType.Banker);
                    Game.getInstance().addPlayer(partnerName, PlayerType.Ponter);
                }
                Game.getInstance().mainController.initType();
                System.out.println(clientType);
            }
        }
    }

    public void sendPonterMove(int bet) throws IOException {
        if (!closeFlag) {
            System.out.println("sendPonterMove");
            oos.writeUTF("sendPonterMove");
            oos.writeUTF(Integer.toString(bet));
            oos.reset();
        }
    }

    public void getPonterMove() throws IOException {
        if (!closeFlag) {
            System.out.println("getPonterMove");
            oos.writeUTF("getPonterMove");
            oos.reset();
            int bet = Integer.parseInt(ois.readUTF());
            if (bet != 0) {
                getPonterMoveFlag = false;
                System.out.println("get ponter right move");
                Game.getInstance().mainController.ponterAction(bet);
            }
        }
    }

    public void sendBankerMove() throws IOException {
        if (!closeFlag) {
            System.out.println("sendBankerMove");
            oos.writeUTF("sendBankerMove");
            oos.writeUTF(Integer.toString(Game.getInstance().num1));
            oos.writeUTF(Integer.toString(Game.getInstance().num2));
            oos.writeUTF(Integer.toString(Game.getInstance().num3));
            oos.reset();
        }
    }

    public void getBankerMove() throws Exception {
        int[] num = new int[3];
        if (!closeFlag) {
            System.out.println("getBankerMove");
            oos.writeUTF("getBankerMove");
            oos.reset();
            for (int i = 0; i < 3; ++i) {
                num[i] = Integer.parseInt(ois.readUTF());
            }
            if (num[0] != 0 && num[1] != 0 && num[2] != 0) {
                getBankerMoveFlag = false;
                System.out.println("get banker right move");
                Game.getInstance().num1 = num[0];
                Game.getInstance().num2 = num[1];
                Game.getInstance().num3 = num[2];
                Game.getInstance().mainController.bankerAction();
            }
        }
    }

    public String getClientType() throws IOException {
        String type = null;
        if (!closeFlag) {
            oos.writeUTF("getClientType");
            oos.reset();
            type = ois.readUTF();
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
    public boolean getPonterMoveFlag = false;
    public boolean getBankerMoveFlag = false;
}
