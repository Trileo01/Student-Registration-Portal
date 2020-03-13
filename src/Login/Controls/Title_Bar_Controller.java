package Login.Controls;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Title_Bar_Controller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void close(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void max(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        System.out.println(stage.isFullScreen());
        if (!stage.isFullScreen()) {
            stage.setFullScreen(true);
        } else {
            stage.setFullScreen(false);
        }
    }

    @FXML
    public void min(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setIconified(true);
    }
}
