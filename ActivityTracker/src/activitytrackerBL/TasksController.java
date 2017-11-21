/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activitytrackerBL;

import classes.DbHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author home
 */
public class TasksController implements Initializable {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Statement s;
    DbHandler handler;

    @FXML
    private Label closeBtn;
    @FXML
    public JFXTextField T_UserId;
    @FXML
    public JFXTextField T_Task;
    @FXML
    public JFXTextField T_TaskDescription;
    @FXML
    private StackPane rootStack;
    @FXML
    private JFXDatePicker T_FromSelectData;
    @FXML
    private JFXDatePicker T_ToSelectData;
    @FXML
    private JFXButton saveBtn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new DbHandler();
        T_UserId.setText(MainController.ucObj().getLoginId());
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
        closeBtn.getScene().getWindow().hide();
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
    private void SaveTaskAction(MouseEvent event) {
        loadDataIntoDatabase();
    }

    public void loadDataIntoDatabase() {

        try {
            conn = handler.getConnection();
            String insertData = "insert into activitiestracker(Name,fromDate,toDate,task,taskDescription) values(?,?,?,?,?)";
            ps = conn.prepareStatement(insertData);
            ps.setString(1, T_UserId.getText());
            ps.setString(2, ((JFXTextField) T_FromSelectData.getEditor()).getText());
            ps.setString(3, ((JFXTextField) T_ToSelectData.getEditor()).getText());
            ps.setString(4, T_Task.getText());
            ps.setString(5, T_TaskDescription.getText());

            if (T_UserId.getText().isEmpty()
                    || T_Task.getText().isEmpty()
                    || T_TaskDescription.getText().isEmpty()
                    || ((JFXTextField) T_FromSelectData.getEditor()).getText().isEmpty()
                    || ((JFXTextField) T_ToSelectData.getEditor()).getText().isEmpty()) {
                addPopUp("insufficent informations");
            } else {
                ps.execute();
                addPopUp("Data Added successfully");
                T_Task.getScene().getWindow().hide();

            }
        } catch (SQLException e) {
            System.out.println("Oops!!! Something went wrong");
        } finally {
            try {
                conn.close();
                ps.close();
            } catch (SQLException e) {

            }
        }
    }

    public void clearTestSuiteFieldsData() {
        T_UserId.setText("");
        T_FromSelectData.setValue(null);
        T_ToSelectData.setValue(null);
        T_Task.setText("");
        T_TaskDescription.setText("");
    }
}
