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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Login.Helpers.SHAEncryption.getSHA;

public class SignUpController implements Initializable {

    @FXML
    public AnchorPane anchorpane;

    @FXML
    public StackPane stackpane;

    @FXML
    private VBox whiteslider;

    @FXML
    private VBox redslider;

    @FXML
    private HBox titlebar;

    @FXML
    private VBox signupbox;

    @FXML
    private TextField username_tf;

    @FXML
    private TextField mail_tf;

    @FXML
    private PasswordField password_tf;

    @FXML
    private Button signupbutton;

    private boolean nameValid;


    @FXML
    void tologin(MouseEvent event) {
        signupbox.setVisible(false);
        titlebar.setVisible(false);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), redslider);
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(1), whiteslider);
        transition.setToX(-382);
        transition1.setToX(redslider.getLayoutX() + 13);
        transition.play();
        transition1.play();
        transition.setOnFinished(t -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Login/Views/Login.fxml"));
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(new Scene(root, 814.4000244140625, 588.0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void signup(MouseEvent event) {
        Connection connection = DBConnection.getInstance().getConnection();

        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO student (Username,Password,Mail) VALUES (?,?,?)")) {

            String user = username_tf.getText();
            String password = SHAEncryption.toHexString(getSHA(password_tf.getText()));
            String mail = mail_tf.getText();

            statement.setString(1,user);
            statement.setString(2,password);
            statement.setString(3,mail);

            int status = statement.executeUpdate();

            if (status > 0) {
                nameValid = true;
                System.out.println("User Registered!!");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Views/Application.fxml"));
                    Parent root = loader.load();
                    Application application = loader.getController();
                    application.setStuUser(username_tf.getText());
                    Node node = (Node) event.getSource();
                    Stage primaryStage = (Stage) node.getScene().getWindow();
                    primaryStage.close();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setMaximized(true);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("IOException error");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Username already exists");
            alert.setContentText("Please enter a unique Username!!!");
            alert.showAndWait();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signupbutton.disableProperty().bind(Bindings.isEmpty(username_tf.textProperty()).or(Bindings.isEmpty(mail_tf.textProperty()))
                .or(Bindings.isEmpty(password_tf.textProperty())));
    }
}
