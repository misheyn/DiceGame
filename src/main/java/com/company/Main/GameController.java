package com.company.Main;

import com.company.Client;
import com.company.Main.Game;
import com.company.Player;
import com.company.PlayerType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Pane controlPane;
    @FXML
    private Pane backgroundPane;
    @FXML
    private Pane headerPane;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button playButton;
    @FXML
    private Button placeBetButton;
    @FXML
    private ComboBox<Integer> betComboBox;
    @FXML
    private TextArea playersTextArea;
    @FXML
    private TextArea scoreTextArea;
    @FXML
    private TextField roundTextField;
    @FXML
    private TextField turnTextField;
    @FXML
    private ImageView diceGif1;
    @FXML
    private ImageView diceGif2;
    @FXML
    private ImageView diceGif3;

    @FXML

    public TextArea getPlayersTextArea() {
        return playersTextArea;
    }

    public TextArea getScoreTextArea() {
        return scoreTextArea;
    }

    public ImageView getDiceGif1() {
        return diceGif1;
    }

    public ImageView getDiceGif2() {
        return diceGif2;
    }

    public ImageView getDiceGif3() {
        return diceGif3;
    }

    public TextField getRoundTextField() {
        return roundTextField;
    }

    public TextField getTurnTextField() {
        return turnTextField;
    }

    public Pane getBackgroundPane() {
        return backgroundPane;
    }

    public ComboBox<Integer> getBetComboBox() {
        return betComboBox;
    }

    private void subShowResultImage(int num, ImageView dice) throws FileNotFoundException {
        String path = "src/image/dice_%d.png".formatted(num);
        System.out.println("Path: " + path);
        dice.setImage(new Image(new FileInputStream(path)));
    }

    public void showResultImage() throws FileNotFoundException {
        subShowResultImage(Game.getInstance().num1, diceGif1);
        subShowResultImage(Game.getInstance().num2, diceGif2);
        subShowResultImage(Game.getInstance().num3, diceGif3);
    }

    @FXML
    void clickPlayButton(ActionEvent event) throws Exception {
        Game.getInstance().cubeRandomMove();
        bankerAction();
        Client.getInstance().sendBankerMove();
        Client.getInstance().getPonterMoveFlag = true;
    }

    public void bankerAction() throws Exception {
        Game.getInstance().diceRoll();
        placeBetButton.setDisable(false);
        betComboBox.setDisable(false);
        playButton.setDisable(true);
    }

    @FXML
    void clickPlaceBetButton(ActionEvent event) throws IOException {
        ponterAction(betComboBox.getValue());
        Client.getInstance().sendPonterMove(betComboBox.getValue());
        Client.getInstance().getBankerMoveFlag = true;
    }

    public void ponterAction(int bet) {
        Game.getInstance().currentBet = bet;
        placeBetButton.setDisable(true);
        betComboBox.setDisable(true);
        playButton.setDisable(false);
        turnTextField.setText("Banker turn");
    }

    public void restartView() {
//        playersTextArea.setText("");
//        scoreTextArea.setText("");
        diceGif1.setVisible(false);
        diceGif2.setVisible(false);
        diceGif3.setVisible(false);
        betComboBox.getSelectionModel().select(0);
        roundTextField.setText("Round " + Game.getInstance().currentRound);
    }

    public void initType() {
        System.out.println("GameController: " + Game.getInstance().clientType);
        if (Game.getInstance().clientType == PlayerType.Banker) {
            betComboBox.setDisable(true);
            placeBetButton.setDisable(true);
            Client.getInstance().getPonterMoveFlag = true;
        }
    }

    @FXML
    void initialize() throws FileNotFoundException {
        ArrayList<Integer> comboBox = new ArrayList<>();
        for (int i = 1; i <= 6; i++)
            comboBox.add(i);
        betComboBox.getItems().addAll(comboBox);
        turnTextField.setText("Ponter turn");
        Image img = new Image(new FileInputStream("src/image/dice.gif"));
        diceGif1.setImage(img);
        diceGif2.setImage(img);
        diceGif3.setImage(img);
        playButton.setDisable(true);
    }
}