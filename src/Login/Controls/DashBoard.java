package Login.Controls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoard implements Initializable {

    private String StuUser;

    private int stuID;

    public void setStuID(int stuID) {
        this.stuID = stuID;
    }

    public void setStuUser(String stuUser) {
        StuUser = stuUser;
    }

    @FXML
    public Label username_la;

    @FXML
    private AnchorPane anchorpane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadBasicdata() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Views/Basic Data.fxml"));
            AnchorPane pane = loader.load();
            BasicData data = loader.getController();
            System.out.println("Dashboard -> " + StuUser);
            data.setUsername(StuUser);
            data.setStuID(stuID);
            data.loadBasicData1();
            anchorpane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showbasicdata(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Views/Basic Data.fxml"));
            AnchorPane pane = loader.load();
            BasicData data = loader.getController();
            System.out.println("Dashboard -> " + StuUser);
            data.setUsername(StuUser);
            data.setStuID(stuID);
            data.loadBasicData1();
            anchorpane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showcontactus(MouseEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Login/Views/ContactUs.fxml"));
            String style = "-fx-background-color: linear-gradient(to bottom,#FFC88F,#FF7372);";
            anchorpane.setStyle(style);
            anchorpane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Some error occured while loading Contact Us!!");
        }
    }

    @FXML
    void signout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login/Views/Login.fxml"));
            Stage primaryStage = (Stage) anchorpane.getScene().getWindow();
            primaryStage.close();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root,814.4000244140625, 588.0));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
