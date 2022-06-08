package com.company.ResultWindow;

import com.company.Main.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WinWindowController {
    @FXML
    private Button exitButton;
    @FXML
    private Button restartButton;
    @FXML
    private TextArea winTextArea;
    @FXML
    private ImageView greenFireImage;
    @FXML
    private ImageView lizardImage;
    @FXML
    private ImageView purpleFireImage;
    @FXML
    private ImageView redFireImage;

    @FXML
    void clickExitButton(ActionEvent event) {
        WinWindow.getInstance().getStage().close();
        WinWindow.getInstance().exitFlag = true;
    }

    @FXML
    void clickRestartButton(ActionEvent event) throws Exception {
        WinWindow.getInstance().getStage().close();
        Game.getInstance().initialize();
    }

    @FXML
    void initialize() throws FileNotFoundException {
        lizardImage.setImage(new Image(new FileInputStream("src/image/lizard.gif")));
        redFireImage.setImage(new Image(new FileInputStream("src/image/firework1.gif")));
        greenFireImage.setImage(new Image(new FileInputStream("src/image/firework2.gif")));
        purpleFireImage.setImage(new Image(new FileInputStream("src/image/firework3.gif")));
        if (Game.getInstance().playerMap.get("Ponter").winStatus)
            winTextArea.setText("Ponter wins!\nCongratulations, you are incredibly lucky");
        else if (Game.getInstance().playerMap.get("Banker").winStatus)
            winTextArea.setText("Banker wins!\nCongratulations, you are incredibly lucky");
        else winTextArea.setText("Draw(\nGood luck next time");
    }
}
