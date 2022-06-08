module com.company.dicegamechicago {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens com.company to javafx.fxml;
    exports com.company;
    exports com.company.Main;
    opens com.company.Main to javafx.fxml;
    exports com.company.StartMenu;
    opens com.company.StartMenu to javafx.fxml;
    exports com.company.ResultWindow;
    opens com.company.ResultWindow to javafx.fxml;
}