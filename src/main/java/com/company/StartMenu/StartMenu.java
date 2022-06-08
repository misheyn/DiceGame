package com.company.StartMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenu {
    public StartMenu() {
        menuController = fxmlLoader.getController();
    }

    public static void showMenu() throws IOException {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        fxmlLoader = new FXMLLoader(StartMenu.class.getResource("startMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        window.setScene(scene);
        window.setTitle("Start Menu");
        window.showAndWait();
    }

    public static StartMenu getInstance() {
        StartMenu localInstance = instance;
        if (localInstance == null) {
            synchronized (StartMenu.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new StartMenu();
                }
            }
        }
        return localInstance;
    }

    public Stage getStage() {
        return window;
    }

    private static volatile StartMenu instance;
    private static Stage window;
    public StartMenuController menuController;
    private static FXMLLoader fxmlLoader;
}
