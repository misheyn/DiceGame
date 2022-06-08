package com.company.StartMenu;

import com.company.Client;
import com.company.Main.Game;
import com.company.PlayerType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartMenuController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane startAnchorPane;
    @FXML
    private Button startButton;
    @FXML
    private TextField IPTextField;
    @FXML
    private Button connectButton;
    @FXML
    private TextField loginTextField;

    public Button getStartButton() {
        return startButton;
    }

    @FXML
    void clickStartButton(ActionEvent event) {
        StartMenu.getInstance().getStage().close();
    }

    @FXML
    void clickConnectButton(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION, "Wrong input!\nThink about it ☺", ButtonType.YES);
        if (Client.getInstance() == null) {
            String host;
            int port;
            if (IPTextField.getText().equals("localhost")) {
                host = IPTextField.getText();
                port = 3345;
            } else {
                Pattern p = Pattern.compile("^\\s*(.*?):(\\d+)\\s*$");
                Matcher m = p.matcher(IPTextField.getText());
                if (m.matches() && !loginTextField.getText().isEmpty()) {
                    host = m.group(1);
                    port = Integer.parseInt(m.group(2));
                } else {
                    IPTextField.setText("localhost");
                    loginTextField.setText("");
                    alert.showAndWait();
                    return;
                }
            }
            try {
                Client client = new Client(loginTextField.getText(), host, port);
                startButton.setDisable(true);
                client.clientFlag = true;
                client.start();
                if (Client.getInstance().clientType.equals("Ponter")) {
                    System.out.println("Ponter");
                    Game.getInstance().addPlayer(loginTextField.getText(), PlayerType.Ponter);
                }
                else Game.getInstance().addPlayer(loginTextField.getText(), PlayerType.Banker);
                System.out.println(client.clientType);
//                Game.getInstance().mainController.getPlayersTextArea().setText(loginTextField.getText());
//                Game.getInstance().mainController.addPlayer(loginTextField.getText(), PlayerType.valueOf(typePlayer));
                if (client.closeFlag) {
                    IPTextField.setText("localhost");
                    loginTextField.setText("");
                    alert.showAndWait();
                    return;
                }
                Thread.sleep(100);
                if (Client.getInstance().closeFlag) {
                    IPTextField.setText("localhost");
                    loginTextField.setText("");
                    alert.showAndWait();
                    return;
                }
                connectionFlag = true;
            } catch (IOException | InterruptedException e) {
                IPTextField.setText("localhost");
                loginTextField.setText("");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void initialize() {
        IPTextField.setText("localhost");
        Game.getInstance().playerMap = new HashMap<>();
    }

    public Alert alert;
    public boolean connectionFlag = false;
}
