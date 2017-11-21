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
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author home
 */
public class MainController implements Initializable {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Statement s;
    static UsersClass uc;

    FXMLLoader loder;
    Parent root;
    Stage stage;
    public DbHandler handler;
    Set uLoginIdList = new HashSet();

    @FXML
    public JFXButton LoginBtn;
    @FXML
    public JFXPasswordField password;
    @FXML
    public Label forgotPassword;
    @FXML
    public Label closeBtn;
    @FXML
    public JFXTextField username;
    @FXML
    public StackPane rootStack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        uc = new UsersClass();
        handler = new DbHandler();
        try {
            Set<String> list = dataBaseUsername();
            TextFields.bindAutoCompletion(username, list);
        } catch (SQLException ex) {
        }
    }

    public static UsersClass ucObj() {
        return uc;
    }

    @FXML
    private void LoginApp(MouseEvent event) throws IOException, Exception {
        if (username.getText().isEmpty()) {
            addPopUp("Please enter Username");
        } else if (password.getText().isEmpty()) {
            addPopUp("Please enter Password");
        } else if (password.getText().equals("tcoe123")) {
            firstTimePasswordChange();
        } else {
            loginApplication();
        }
    }

    private void forgotPasswordAction(MouseEvent event) throws IOException {
        loder = new FXMLLoader(getClass().getResource("/activitytracker/ResetPassword.fxml"));
        root = loder.load();
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        //stage.setOpacity(0.9);
        stage.show();

        LoginBtn.getScene().getWindow().hide();
    }

    public void firstTimePasswordChange() {
        try {
            int flag = -1;
            conn = handler.getConnection();
            String projectName = "select * from usersinfo";
            ps = conn.prepareStatement(projectName);
            rs = ps.executeQuery();

            while (rs.next()) {
                uc.setLoginId(rs.getString("loginId"));
                uc.setPassword(rs.getString("password"));
                if (uc.getLoginId().equalsIgnoreCase(username.getText()) && (uc.getPassword().equals("tcoe123"))) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 0) {

                loder = new FXMLLoader(getClass().getResource("/activitytracker/ResetPassword.fxml"));
                root = loder.load();
                stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));
                //stage.setOpacity(0.9);
                stage.show();
                this.addPopUp("Please change your password");
                LoginBtn.getScene().getWindow().hide();
            }

        } catch (IOException | SQLException ex) {
            System.out.println(ex);
        } finally {
            try {
                conn.close();
                ps.close();
                rs.close();
            } catch (SQLException e) {
            }
        }
    }

    public void loginApplication() throws Exception {
        try {
            int flag = -1;
            conn = handler.getConnection();

            String projectName = "select * from usersinfo";
            ps = conn.prepareStatement(projectName);
            rs = ps.executeQuery();

            while (rs.next()) {

                uc.setLoginId(rs.getString("loginId"));
                uc.setPassword(rs.getString("password"));
                uc.setRole(rs.getString("access"));
                uc.setReviewOfficer(rs.getString("ManagerId"));

                if (uc.getLoginId().equals(username.getText())
                        && uc.getPassword().equals(password.getText())
                        && uc.getRole().equals("User")) {
                    flag = 1;
                    break;
                }
                if (uc.getLoginId().equals(username.getText())
                        && uc.getPassword().equals(password.getText())
                        && uc.getRole().equals("Manager")) {
                    flag = 2;
                    break;
                }
                if (uc.getLoginId().equals(username.getText())
                        && uc.getPassword().equals(password.getText())
                        && uc.getRole().equals("Admin")) {
                    flag = 3;
                    break;
                }
            }

            switch (flag) {
                case 1:
                    loder = new FXMLLoader(getClass().getResource("/activitytracker/User.fxml"));
                    root = loder.load();
                    stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root));
                    stage.show();
                    LoginBtn.getScene().getWindow().hide();
                    break;
                case 2:
                    loder = new FXMLLoader(getClass().getResource("/activitytracker/Managers.fxml"));
                    root = loder.load();
                    stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root));
                    stage.show();
                    LoginBtn.getScene().getWindow().hide();
                    break;
                case 3:
                    loder = new FXMLLoader(getClass().getResource("/activitytracker/Admin.fxml"));
                    root = loder.load();
                    stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root));
                    stage.show();
                    LoginBtn.getScene().getWindow().hide();
                    break;
                default:
                    addPopUp("Username or Password is invalid");
                    break;

            }
        } catch (IOException | SQLException ex) {
            System.out.println(ex);
        } finally {
            try {
                conn.close();
                ps.close();
                rs.close();
            } catch (SQLException e) {
            }
        }
    }

    public Set<String> dataBaseUsername() throws SQLException {
        try {
            conn = handler.getConnection();
            String uLoginID = "select loginId from usersinfo";
            ps = conn.prepareStatement(uLoginID);
            rs = ps.executeQuery();
            while (rs.next()) {
                uc.setLoginId(rs.getString("loginId"));
                String loginID = uc.getLoginId();
                uLoginIdList.add(loginID);
            }
        } catch (SQLException e) {
        } finally {
            rs.close();
            ps.close();
            conn.close();
        }
        return uLoginIdList;
    }

    @FXML
    private void changeCursor(MouseEvent event) {
    }

    @FXML
    private void moveToResetPassword(MouseEvent event) throws Exception {
        loder = new FXMLLoader(getClass().getResource("/activitytracker/ForgotPassword.fxml"));
        root = loder.load();
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.show();
        LoginBtn.getScene().getWindow().hide();
    }

    @FXML
    private void changeColorBack(MouseEvent event) {
        closeBtn.setStyle("-fx-background-color:transprent;");
    }

    @FXML
    private void changeColor(MouseEvent event) {
        closeBtn.setStyle("-fx-background-color:RED;-fx-text-fill: black;");

    }

    @FXML
    private void closeWin(MouseEvent event) {
        LoginBtn.getScene().getWindow().hide();
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
