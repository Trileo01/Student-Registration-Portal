package Login.Controls;

import Login.Helpers.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class Application implements Initializable {

    private String stuUser;

    public void setStuUser(String stuUser) {
        this.stuUser = stuUser;
    }

    @FXML
    private TextField firstname_tf;

    @FXML
    private TextField lastname_tf;

    @FXML
    private TextField fathername_tf;

    @FXML
    private TextField mothername_tf;

    @FXML
    private DatePicker bday_date;

    @FXML
    private TextArea streetadd_tf;

    @FXML
    private TextField city_tf;

    @FXML
    private TextField state_tf;

    @FXML
    private ComboBox<String> country_tf;

    @FXML
    private TextField mobile_tf;

    @FXML
    private Button loadbutton;

    @FXML
    private Button continuebutton;

    private FileInputStream fileInputStream = null;

    @FXML
    void continuetoapp(ActionEvent event) {
        if (firstname_tf.getText().trim().isEmpty() || mobile_tf.getText().trim().equals(null) || lastname_tf.getText().trim().isEmpty() || fathername_tf.getText().trim().isEmpty() || mothername_tf.getText().trim().isEmpty() || bday_date.getValue() == null
                || streetadd_tf.getText().trim().isEmpty() || city_tf.getText().trim().isEmpty() || state_tf.getText().isEmpty() || country_tf.getValue() == null || fileInputStream == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Some Fields missing!!");
            alert.setContentText("Please fill all the fields to continue.");
            alert.showAndWait();
        } else {
            try (Connection connection = DBConnection.getInstance().getConnection();
                 PreparedStatement statement1 = connection.prepareStatement("UPDATE student SET First_Name = (?), Last_Name = (?), Father_Name = (?) , " +
                         "Mother_Name = (?), Street_Address = (?) , City = (?) , State = (?) , Country = (?) , Mobile_No = (?) , DOB = (?) , Picture = (?) WHERE Username = (?)")) {
                statement1.setString(12,stuUser);
                statement1.setString(1, firstname_tf.getText());
                statement1.setString(2, lastname_tf.getText());
                statement1.setString(3, fathername_tf.getText());
                statement1.setString(4, mothername_tf.getText());
                statement1.setString(5, streetadd_tf.getText());
                statement1.setString(6, city_tf.getText());
                statement1.setString(7, state_tf.getText());
                statement1.setString(8, country_tf.getValue());
                statement1.setLong(9, Long.parseLong(mobile_tf.getText()));
                statement1.setDate(10, Date.valueOf(bday_date.getValue()));
                statement1.setBinaryStream(11,fileInputStream,fileInputStream.available());

                System.out.println("Executing query : " + statement1.toString());

                int status = statement1.executeUpdate();

                if (status > 0) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Views/Application2.fxml"));
                        Parent root = loader.load();
                        Application2 application2 = loader.getController();
                        application2.setStuUser(stuUser);
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.getScene().setRoot(root);
                        stage.setMaximized(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error while loading pane!!!");
                    }
                }

            } catch (SQLException | IOException e) {
                e.printStackTrace();
                System.out.println("Some error occurred while creating or executing query!!");
            }
        }
    }

    @FXML
    void loadphoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(loadbutton.getScene().getWindow());
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> countries = FXCollections.observableArrayList();
        String[] locales1 = Locale.getISOCountries();
        for (String countrylist : locales1) {
            Locale obj = new Locale("", countrylist);
            String[] city = { obj.getDisplayCountry() };
            for (int x = 0; x < city.length; x++) {
                countries.add(obj.getDisplayCountry());
            }
        }
        country_tf.setItems(countries);
    }
}
