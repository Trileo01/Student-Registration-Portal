package Login.Controls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactUs implements Initializable {

    @FXML
    private TextField name_tf;

    @FXML
    private TextField mail_tf;

    @FXML
    private TextField message_tf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void showalert(ActionEvent event) {
        if (name_tf.getText().trim().isEmpty() || mail_tf.getText().isEmpty() || message_tf.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Some Fields missing!!");
            alert.setContentText("Please fill all the fields to continue.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Submit Query");
            alert.setContentText("Are you to submit your query?");
            alert.showAndWait();
        }
    }
}
