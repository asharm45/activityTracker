/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activitytrackerBL;

import classes.DbHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author home
 */
public class ForgotPasswordController implements Initializable {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Statement s;
    public DbHandler handler;
    Set uLoginIdList = new HashSet();

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField newPassword;
    @FXML
    private JFXPasswordField confirmPassword;
    @FXML
    private JFXButton resetBtn;
    @FXML
    private Label closeBtn;
    @FXML
    private StackPane rootStack;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new DbHandler();
    }

    @FXML
    private void resetBtnAction(MouseEvent event) {

        try {
            int flag = -1;
            conn = handler.getConnection();

            String uName = this.username.getText();
            String nPassword = this.newPassword.getText();
            String cPassword = this.confirmPassword.getText();

            String projectName = "select * from usersinfo";
            ps = conn.prepareStatement(projectName);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (uName.isEmpty()) {
                    flag = 0;
                    break;
                }
                if (nPassword.isEmpty()) {
                    flag = 3;
                    break;
                }
                if (cPassword.isEmpty()) {
                    flag = 4;
                    break;
                }
                if (!nPassword.equals(cPassword)) {
                    flag = 1;
                    break;
                }
                if (nPassword.equals(cPassword)) {
                    flag = 2;
                    break;
                }
            }
            switch (flag) {
                case 0:
                    addPopUp("User name field can't be empty");
                    break;
                case 1:
                    addPopUp("Password doesn't match");
                    break;
                case 2:
                    String updateQuery = "update usersinfo set password ='" + nPassword + "' where loginId='" + uName + "'";
                    s = conn.createStatement();
                    int i = s.executeUpdate(updateQuery);
                    if (i == 1) {
                        addPopUp("Password updated successfuly");
                    } else {
                        addPopUp("Username does not exist in database");
                    }
                    break;
                case 3:
                    addPopUp("New password field can't be empty");
                    break;
                case 4:
                    addPopUp("Confirm password field can't be empty");
                    break;
                default:
                    addPopUp("Something is wrong with the application");
                    break;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                conn.close();
                ps.close();
                rs.close();
            } catch (SQLException e) {

            }
        }

    }

    @FXML
    private void loginBtnAction(MouseEvent event) throws IOException {
        FXMLLoader loder = new FXMLLoader(getClass().getResource("/activitytracker/Main.fxml"));
        Parent root = loder.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        //stage.setOpacity(0.9);
        stage.show();
        resetBtn.getScene().getWindow().hide();
    }

    @FXML
    private void closeWin(MouseEvent event) {
        resetBtn.getScene().getWindow().hide();
    }

    @FXML
    private void changeColorBack(MouseEvent event) {
        closeBtn.setStyle("-fx-background-color:transprent;");
    }

    @FXML
    private void changeColor(MouseEvent event) {
        closeBtn.setStyle("-fx-background-color:RED;-fx-text-fill: black;");
    }

    public void addPopUp(String text) {
        // Heading text
        Text t = new Text(text);
        t.setStyle("-fx-font-size:14px;");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(t);

        JFXDialog dialog = new JFXDialog(rootStack, dialogLayout, JFXDialog.DialogTransition.CENTER);
        // close button
        JFXButton closeButton = new JFXButton("OK");
        closeButton.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;");

        closeButton.setOnAction((ActionEvent event1) -> {
            dialog.close();
        });

        HBox box = new HBox();
        box.setSpacing(20);
        box.setPrefSize(200, 50);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().addAll(closeButton);
        dialogLayout.setActions(box);
        dialog.show();
    }
}
