module Login.UI {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires de.jensd.fx.fontawesomefx.fontawesome;
    requires de.jensd.fx.fontawesomefx.commons;
    requires java.sql;
    requires mysql.connector.java;

    exports Login to javafx.graphics;
    exports Login.Controls to javafx.fxml;
    opens Login.Views to javafx.controls;
    opens Login.Controls to javafx.fxml;
}