package Login.Controls;

import Login.Helpers.DBConnection;
import Login.Helpers.SHAEncryption;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Login.Helpers.SHAEncryption.getSHA;

public class LoginController implements Initializable {

    @FXML
    public VBox loginbox;

    @FXML
    public VBox whiteslider;

    @FXML
    public VBox redslider;

    @FXML
    public HBox titlebar;

    @FXML
    private TextField username_tf;

    @FXML
    private PasswordField password_tf;

    @FXML
    private Button loginbutton;

    @FXML
    void login(MouseEvent event) {

        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT Username FROM student WHERE Username = ?")) {

            statement.setString(1, username_tf.getText());
            System.out.println(statement.toString());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                PreparedStatement statement1 = connection.prepareStatement("SELECT Password FROM student WHERE Username = ?");
                statement1.setString(1, username_tf.getText());
                System.out.println(statement1.toString());

                ResultSet resultSet = statement1.executeQuery();
                if (resultSet.next()) {
                    String password = SHAEncryption.toHexString(getSHA(password_tf.getText()));
                    System.out.println("Converted " + password_tf.getText() + " -> " + password);
                    System.out.println(resultSet.getString(1));
                    if (resultSet.getString(1).equalsIgnoreCase(password)) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Views/DashBoard.fxml"));
                        Parent root = loader.load();
                        DashBoard application = loader.getController();
                        application.username_la.setText(username_tf.getText());
                        application.setStuUser(username_tf.getText());
                        application.loadBasicdata();

                        Node node = (Node) event.getSource();
                        Stage primaryStage = (Stage) node.getScene().getWindow();
                        primaryStage.close();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setMaximized(true);
                        stage.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Wrong Credentials!!");
                        alert.setContentText("Please enter the correct password for your account!!");
                        alert.showAndWait();
                    }
                }
                resultSet.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User doesn't exist!!");
                alert.setContentText("Please Sign Up to continue!!");
                alert.showAndWait();
            }
            result.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Some error occurred while searching!!");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginbutton.disableProperty().bind(Bindings.isEmpty(username_tf.textProperty()).or(Bindings.isEmpty(password_tf.textProperty())));
    }

    @FXML
    public void tosignup(MouseEvent mouseEvent) {
        loginbox.setVisible(false);
        titlebar.setVisible(false);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), redslider);
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(1), whiteslider);
        transition.setToX(whiteslider.getLayoutX() + 13);
        transition1.setToX(-382);
        transition.play();
        transition1.play();
        transition.setOnFinished(t -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Login/Views/Sign_Up.fxml"));
                Node node = (Node) mouseEvent.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(new Scene(root, 814.4000244140625, 588.0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
