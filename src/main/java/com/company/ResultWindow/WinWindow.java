package com.company.ResultWindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WinWindow {

    public WinWindow() {
    }

    public static void showResult() throws IOException {
        getInstance().exitFlag = false;
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(WinWindow.class.getResource("winWindow.fxml"));
        fxmlLoader.getRoot();
        Scene scene = new Scene(fxmlLoader.load());
        window.setScene(scene);
        window.setTitle("Congratulations to the winner");
        window.showAndWait();
    }

    public Stage getStage() {
        return window;
    }

    public static WinWindow getInstance() {
        WinWindow localInstance = instance;
        if (localInstance == null) {
            synchronized (WinWindow.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new WinWindow();
                }
            }
        }
        return localInstance;
    }

    public boolean exitFlag;
    private static volatile WinWindow instance;
    private static Stage window;
}
