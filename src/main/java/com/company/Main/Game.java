package com.company.Main;

import com.company.Client;
import com.company.Player;
import com.company.PlayerType;
import com.company.StartMenu.StartMenu;
import com.company.ResultWindow.WinWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Game extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("mainWindow.fxml"));
        scene = new Scene(fxmlLoader.load());
        mainController = fxmlLoader.getController();
        Image img = new Image(new FileInputStream("src/image/background.png"));
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        mainController.getBackgroundPane().setBackground(bGround);
        stage.setTitle("Dice Game 'Bank on the bones'");
        stage.getIcons().add(new Image(new FileInputStream("src/image/dice_icon.png")));
        stage.setScene(scene);
        initialize();
        stage.show();
    }

    public void addPlayer(String name, PlayerType type) {
        Player player = new Player(name, type);
        playerMap.put(player.type.toString(), player);
        System.out.println("Put " + player.type);
        mainController.getPlayersTextArea().appendText(name + player.type + "\n");
        mainController.getScoreTextArea().appendText(player.score + "\n");
    }

    private int generateNumber(ImageView dice) throws FileNotFoundException {
        Random random = new Random();
        int score = random.nextInt(7 - 1) + 1;

        return score;
    }

    private void checkWin() {
        if (currentRound == 3) {
            if (playerMap.get("Ponter").score > playerMap.get("Banker").score)
                playerMap.get("Ponter").winStatus = true;
            else if (playerMap.get("Ponter").score < playerMap.get("Banker").score)
                playerMap.get("Banker").winStatus = true;
            winFlag = true;
        }
    }


    public void diceRoll() throws Exception {
        mainController.getDiceGif1().setImage(new Image(new FileInputStream("src/image/dice.gif")));
        mainController.getDiceGif2().setImage(new Image(new FileInputStream("src/image/dice.gif")));
        mainController.getDiceGif3().setImage(new Image(new FileInputStream("src/image/dice.gif")));
        mainController.getDiceGif1().setVisible(true);
        mainController.getDiceGif2().setVisible(true);
        mainController.getDiceGif3().setVisible(true);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> {
                try {
                    mainController.showResultImage();
                    checkScore();
                    checkWin();
                    if (!winFlag) {
                        currentRound++;
                        mainController.getRoundTextField().setText("Round " + currentRound);
                        mainController.getTurnTextField().setText("Ponter turn");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> {
                try {
                    if (winFlag)
                        Game.getInstance().showWindow();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
        t.start();
    }

    public void cubeRandomMove() throws FileNotFoundException {
        num1 = generateNumber(mainController.getDiceGif1());
        num2 = generateNumber(mainController.getDiceGif2());
        num3 = generateNumber(mainController.getDiceGif3());
    }

    private void showWindow() {
        try {
            WinWindow.showResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkScore() {
        boolean flag1, flag2, flag3;
        flag1 = checkNum(num1);
        flag2 = checkNum(num2);
        flag3 = checkNum(num3);
        if (!flag1 && !flag2 && !flag3) {
            playerMap.get("Banker").score += currentBet;
            mainController.getScoreTextArea().setText(playerMap.get("Ponter").score + "\n");
            mainController.getScoreTextArea().appendText(playerMap.get("Banker").score + "\n");
        }
    }

    private boolean checkNum(int num) {
        boolean flag = false;
        if (num == currentBet) {
            flag = true;
            playerMap.get("Ponter").score += currentBet;
            mainController.getScoreTextArea().setText("");
            mainController.getScoreTextArea().setText(playerMap.get("Ponter").score + "\n");
            mainController.getScoreTextArea().appendText(playerMap.get("Banker").score + "\n");
        }
        return flag;
    }

    public void initialize() throws Exception {
        playerMap = new HashMap<>();
        StartMenu.showMenu();
        if (!StartMenu.getInstance().menuController.connectionFlag) {
            addPlayer("Ponter", PlayerType.Ponter);
            addPlayer("Banker", PlayerType.Banker);
            mainController.getTurnTextField().setText("Ponter turn");
        } else {
            mainController.getTurnTextField().setText(playerMap.get(PlayerType.Ponter.toString()).name + " [Ponter] turn");
        }
        currentRound = 1;
        currentBet = 1;
        winFlag = false;
        initFlag = true;
        mainController.restartView();
    }

    public static Game getInstance() {
        Game localInstance = instance;
        if (localInstance == null) {
            synchronized (Game.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Game();
                }
            }
        }
        return localInstance;
    }

    public static void main(String[] args) throws IOException {
        launch();
        if (Client.getInstance() != null) Client.getInstance().quit();
    }

    private static volatile Game instance;
    public Scene scene;
    public GameController mainController;
    public static HashMap<String, Player> playerMap;
    public int currentRound;
    public int currentBet;
    public int num1;
    public int num2;
    public int num3;
    public boolean winFlag = false;
    public PlayerType clientType;
    public boolean initFlag = false;
}
