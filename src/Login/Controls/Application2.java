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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Application2 implements Initializable {

    private String stuUser;

    @FXML
    private ComboBox<String> branchchoice;

    @FXML
    private ComboBox<String> coursechoice;

    @FXML
    private ComboBox<String> subject1_cb;

    @FXML
    private TextField teacher1_t;

    @FXML
    private ComboBox<String> subject2_cb;

    @FXML
    private TextField teacher2_t;

    @FXML
    private ComboBox<String> subject3_cb;

    @FXML
    private TextField teacher3_t;

    public void setStuUser(String stuUser) {
        this.stuUser = stuUser;
    }

    @FXML
    private Button finishbutton;


    private ObservableList<String> subjects = FXCollections.observableArrayList();

    @FXML
    void showcourses(ActionEvent event) {
        ObservableList<String> courses = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT Course_Name FROM courses c,department d WHERE d.Dep_Name = (?) AND d.Dep_ID = c.Dep_ID")) {
            String branchoice = branchchoice.getValue();
            statement.setString(1, branchoice);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courses.add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while creating statement!!");
        }

        coursechoice.setItems(null);
        coursechoice.setItems(courses);
    }

    @FXML
    void showsubjects(ActionEvent event) {

        Connection connection = DBConnection.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT Subject_Name FROM subjects s,courses c WHERE c.Course_Name = (?) AND c.Course_ID = s.Course_ID " +
                "AND s.Optional = '1'")) {
            String course = coursechoice.getValue();
            statement.setString(1, course);

            System.out.println("Running Query" + statement.toString());

            ResultSet resultSet = statement.executeQuery();
            subjects.setAll((String) null);
            while (resultSet.next()) {
                subjects.add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Problem while executing statement!!");
        }

        subject1_cb.setItems(null);
        subject2_cb.setItems(null);
        subject3_cb.setItems(null);
        subject1_cb.setItems(subjects);
        subject2_cb.setItems(subjects);
        subject3_cb.setItems(subjects);
        teacher1_t.setText(null);
        teacher2_t.setText(null);
        teacher3_t.setText(null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> branches = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();

        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT Dep_Name FROM department");

            while (set.next()) {
                branches.add(set.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Some error occured while quering departments!!");
        }

        branchchoice.setItems(null);
        branchchoice.setItems(branches);
    }

    @FXML
    void subject1_chosen(ActionEvent event) {
        System.out.println(event.toString());
        ObservableList<String> subject2 = FXCollections.observableArrayList(subjects);
        ObservableList<String> subject3 = FXCollections.observableArrayList(subjects);

        subject3.remove(subject1_cb.getValue());
        subject2.remove(subject1_cb.getValue());

        if (!subject3_cb.getSelectionModel().isEmpty()) {
            subject2.remove(subject3_cb.getValue());
        }

        if (!subject2_cb.getSelectionModel().isEmpty()) {
            subject3.remove(subject2_cb.getValue());
        }

        subject2_cb.setItems(subject2);
        subject3_cb.setItems(subject3);

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Teacher_Name FROM subjects WHERE Subject_Name = (?)")) {
            statement.setString(1, subject1_cb.getValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                teacher1_t.setText(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while creating statement!!!");
        }

    }

    @FXML
    void subject2_chosen(ActionEvent event) {
        ObservableList<String> subject1 = FXCollections.observableArrayList(subjects);
        ObservableList<String> subject3 = FXCollections.observableArrayList(subjects);

        subject3.remove(subject2_cb.getValue());
        subject1.remove(subject2_cb.getValue());

        if (!subject1_cb.getSelectionModel().isEmpty()) {
            subject3.remove(subject1_cb.getValue());
        }

        if (!subject3_cb.getSelectionModel().isEmpty()) {
            subject1.remove(subject3_cb.getValue());
        }

        subject1_cb.setItems(subject1);
        subject3_cb.setItems(subject3);

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Teacher_Name FROM subjects WHERE Subject_Name = (?)")) {
            statement.setString(1, subject2_cb.getValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                teacher2_t.setText(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while creating statement!!!");
        }
    }

    @FXML
    void subject3_chosen(ActionEvent event) {

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Teacher_Name FROM subjects WHERE Subject_Name = (?)")) {
            statement.setString(1, subject3_cb.getValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                teacher3_t.setText(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while creating statement!!!");
        }
    }


    @FXML
    void finish(ActionEvent event) {
        if (subject1_cb.getSelectionModel().isEmpty() || subject2_cb.getSelectionModel().isEmpty() || subject3_cb.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Some Fields missing!!");
            alert.setContentText("Please fill all the fields to continue.");
            alert.showAndWait();
        } else {
            ObservableList<Integer> stuSubjects = FXCollections.observableArrayList();
            int stuID = 0;

            try (Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT Student_ID from student WHERE Username = (?)");
                 PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO taken VALUES(?,?)");
                 PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT Subject_ID FROM subjects WHERE Subject_Name = (?)");
                 PreparedStatement preparedStatement4 = connection.prepareStatement("SELECT Subject_ID FROM subjects WHERE Course_ID = (?) AND Optional = '0'");
                 PreparedStatement preparedStatement5 = connection.prepareStatement("SELECT Course_ID FROM courses WHERE Course_Name = (?)");
                 PreparedStatement preparedStatement6 = connection.prepareStatement("INSERT INTO enroll VALUES (?,?)")) {
                preparedStatement1.setString(1, stuUser);
                ResultSet ID = preparedStatement1.executeQuery();
                while (ID.next()) {
                    stuID = ID.getInt(1);
                }

                preparedStatement2.setInt(1, stuID);

                int course_id = 0;
                preparedStatement5.setString(1, coursechoice.getValue());
                ResultSet cou_ID = preparedStatement5.executeQuery();
                while (cou_ID.next()) {
                    course_id = cou_ID.getInt(1);
                }

                preparedStatement4.setInt(1, course_id);
                ResultSet com_Sub = preparedStatement4.executeQuery();
                while (com_Sub.next()) {
                    stuSubjects.add(com_Sub.getInt(1));
                }

                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        preparedStatement3.setString(1, subject1_cb.getValue());
                        ResultSet opt_Sub = preparedStatement3.executeQuery();
                        while (opt_Sub.next()) {
                            stuSubjects.add(opt_Sub.getInt(1));
                        }
                    } else if (i == 1) {
                        preparedStatement3.setString(1, subject2_cb.getValue());
                        ResultSet opt_Sub = preparedStatement3.executeQuery();
                        while (opt_Sub.next()) {
                            stuSubjects.add(opt_Sub.getInt(1));
                        }
                    } else {
                        preparedStatement3.setString(1, subject3_cb.getValue());
                        ResultSet opt_Sub = preparedStatement3.executeQuery();
                        while (opt_Sub.next()) {
                            stuSubjects.add(opt_Sub.getInt(1));
                        }
                    }
                }

                preparedStatement6.setInt(1,course_id);
                preparedStatement6.setInt(2,stuID);
                preparedStatement6.execute();

                for (int n : stuSubjects) {
                    preparedStatement2.setInt(2,n);
                    preparedStatement2.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Some error occurred!!");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Views/DashBoard.fxml"));
            try {
                Parent root = loader.load();
                DashBoard dashBoard = loader.getController();
                dashBoard.username_la.setText(stuUser);
                dashBoard.setStuUser(stuUser);
                dashBoard.setStuID(stuID);
                dashBoard.loadBasicdata();
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.getScene().setRoot(root);
                stage.setMaximized(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
