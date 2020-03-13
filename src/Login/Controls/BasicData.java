package Login.Controls;

import Login.Helpers.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BasicData implements Initializable {

    private String username;

    private int stuID;

    public void setStuID(int stuID) {
        this.stuID = stuID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private Label name_lb;

    @FXML
    private Label fathername_lb;

    @FXML
    private Label date_lb;

    @FXML
    private Label mobile_lb;

    @FXML
    private TextArea add_area;

    @FXML
    private Label branch_lb;

    @FXML
    private Label course_lb;

    @FXML
    private TextArea subjects1;

    @FXML
    private ImageView imageView;

    private StringBuilder name = new StringBuilder();
    private String father_name;
    private Date date;
    private BigDecimal mobile;
    private StringBuilder address = new StringBuilder();
    private StringBuilder subjects = new StringBuilder();
    private int course_id = 0;
    private int dep_id = 0;
    private String course_name = null;
    private String dep_name = null;
    private Blob blob = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void loadBasicData1() {
        System.out.println(username);

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT First_Name,Last_Name,Father_Name,DOB,Mobile_No,Street_Address,City," +
                    "State,Country,Picture FROM student WHERE Username = (?)")) {
            statement.setString(1,username);
            System.out.println(statement.toString());
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                name.append(set.getString(1));
                name.append(" ");
                name.append(set.getString(2));
                father_name = set.getString(3);
                date = set.getDate(4);
                mobile = set.getBigDecimal(5);
                address.append(set.getString(6));
                address.append("\n");
                address.append(set.getString(7));
                address.append("\n");
                address.append(set.getString(8));
                address.append("\n");
                address.append(set.getString(9));
                blob = set.getBlob(10);
                if (blob != null) {
                    InputStream inputStream = blob.getBinaryStream();
                    Image image = new Image(inputStream);
                    imageView.setImage(image);
                    imageView.setVisible(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting connection!!");
        }

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement6 = connection.prepareStatement("SELECT  Student_ID FROM student WHERE Username = (?)");
            PreparedStatement statement1 = connection.prepareStatement("SELECT Course_ID FROM enroll WHERE Student_ID = (?)");
            PreparedStatement statement2 = connection.prepareStatement("SELECT Course_Name,Dep_ID FROM courses WHERE Course_ID = (?)");
            PreparedStatement statement3 = connection.prepareStatement("SELECT Dep_Name FROM department WHERE Dep_ID = (?)");
            PreparedStatement statement4 = connection.prepareStatement("SELECT Subject_ID FROM taken WHERE Student_ID = (?)");
            PreparedStatement statement5 = connection.prepareStatement("SELECT Subject_Name FROM subjects WHERE Subject_ID = (?)")) {

            statement6.setString(1,username);
            ResultSet set9 = statement6.executeQuery();
            while (set9.next()) {
                stuID = set9.getInt(1);
            }

            statement1.setInt(1,stuID);
            ResultSet set1 = statement1.executeQuery();
            while (set1.next()) {
                course_id = set1.getInt(1);
            }

            statement2.setInt(1,course_id);
            ResultSet set2 = statement2.executeQuery();
            while (set2.next()) {
                course_name = set2.getString(1);
                dep_id = set2.getInt(2);
            }

            statement3.setInt(1,dep_id);
            ResultSet set3 = statement3.executeQuery();
            while (set3.next()) {
                dep_name = set3.getString(1);
            }

            statement4.setInt(1,stuID);
            ResultSet set4 = statement4.executeQuery();
            while (set4.next()) {
                statement5.setInt(1,set4.getInt(1));
                ResultSet set = statement5.executeQuery();
                while (set.next()) {
                    subjects.append(set.getString(1));
                }
                subjects.append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Some error occured!!!");
        }
        name_lb.setText(name.toString());
        fathername_lb.setText(father_name);
        if (date != null) {
            date_lb.setText(date.toString());
        }
        mobile_lb.setText(mobile.toString());
        add_area.setText(address.toString());
        System.out.println(dep_name);
        branch_lb.setText(dep_name);
        course_lb.setText(course_name);
        subjects1.setText(subjects.toString());
    }

}

