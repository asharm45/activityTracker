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
import java.util.ResourceBundle;
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
public class ResetPasswordController implements Initializable {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Statement s;
    public DbHandler handler;

    @FXML
    private JFXPasswordField oldPassword;
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
    @FXML
    private JFXButton loginPage;
    @FXML
    private JFXTextField username;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new DbHandler();
        username.setText(MainController.ucObj().getLoginId());
    }

    @FXML
    private void closeWin(MouseEvent event) {
        closeBtn.getScene().getWindow().hide();
    }

    @FXML
    private void resetBtnAction(MouseEvent event) throws IOException {
        resetPassword();
    }

    public void resetPassword() {
        try {
            int flag = -1;
            conn = handler.getConnection();

            String oPassword = this.oldPassword.getText();
            String nPassword = this.newPassword.getText();
            String cPassword = this.confirmPassword.getText();


            String projectName = "select * from usersinfo where loginId='" + MainController.ucObj().getLoginId() + "'";
            ps = conn.prepareStatement(projectName);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (oPassword.isEmpty()) {
                    flag = 5;
                    break;
                }
                if (!oPassword.equals(rs.getString("password"))) {
                    flag = 0;
                    break;
                }
                if (nPassword.isEmpty()) {
                    flag = 1;
                    break;

                }
                if (cPassword.isEmpty()) {
                    flag = 2;
                    break;
                }
                if (!nPassword.equals(cPassword)) {
                    flag = 3;
                    break;
                }
                if (nPassword.equals(cPassword)) {
                    flag = 4;
                    break;
                }
            }

            switch (flag) {
                case 0:
                    addPopUp("Old password is invalid");
                    break;
                case 1:
                    addPopUp("New Password field can't be empty");
                    break;
                case 2:
                    addPopUp("Confirm Password field can't be empty");
                    break;
                case 3:
                    addPopUp("Password doesn't match");
                    break;
                case 4:
                    String updateQuery = "update usersinfo set password='" + nPassword + "' where loginId='" + MainController.ucObj().getLoginId() + "'";
                    s = conn.createStatement();
                    int i = s.executeUpdate(updateQuery);
                    if (i == 1) {
                        addPopUp("Password updated successfuly");
                        clearTextFields();
                    } else {
                        addPopUp("User does not exist in database");
                    }
                    break;
                case 5:
                    addPopUp("Old Password field can't be empty");
                    break;
                default:
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

    @FXML
    private void loginPageAction(MouseEvent event) throws IOException {
        FXMLLoader loder = new FXMLLoader(getClass().getResource("/activitytracker/Main.fxml"));
        Parent root = loder.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.show();
        resetBtn.getScene().getWindow().hide();
    }

    public void clearTextFields() {
        oldPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }

}
