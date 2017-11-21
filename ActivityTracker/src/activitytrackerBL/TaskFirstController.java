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
import com.jfoenix.controls.JFXTextField;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class TaskFirstController implements Initializable {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Statement s;
    DbHandler handler;

    @FXML
    private Label closeBtn;
    @FXML
    private JFXButton addNewTaskBtn;
    @FXML
    private Label backToHome;
    @FXML
    public TableView<Record> ActivityTrackerTable;
    @FXML
    public TableColumn<Record, String> LoginIdCol;
    @FXML
    public TableColumn<Record, String> TaskCol;
    @FXML
    public TableColumn<Record, String> TaskDescriptionCol;

    ObservableList taskList = FXCollections.observableArrayList();
    TasksController taskObject;
    @FXML
    private JFXButton deleteBtn;
    @FXML
    private JFXButton refreshBtn;
    @FXML
    private JFXTextField userId;
    @FXML
    private JFXTextField task;
    @FXML
    private JFXTextField taskDescription;
    @FXML
    private StackPane rootStack;
    @FXML
    private TableColumn<Record, String> fromDateCol;
    @FXML
    private TableColumn<Record, String> toDateCol;

    ActivityClass ac;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new DbHandler();
        ac = new ActivityClass();
        refreshtableData();
        setCellValueFromTableToTextField();
        ActivityTrackerTable.setTableMenuButtonVisible(true);
    }

    public void setCellTable() {
        LoginIdCol.setCellValueFactory(new PropertyValueFactory<>("loginId"));
        fromDateCol.setCellValueFactory(new PropertyValueFactory<>("fromDate"));
        toDateCol.setCellValueFactory(new PropertyValueFactory<>("toDate"));
        TaskCol.setCellValueFactory(new PropertyValueFactory<>("Task"));
        TaskDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("TaskDescription"));

    }

    private void loadDataFromDB() throws SQLException {
        try {
            conn = handler.getConnection();
            ps = conn.prepareStatement("select * from activitiestracker where Name='" + MainController.ucObj().getLoginId() + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                ac.setLoginId(rs.getString("Name"));
                ac.setFromDate(rs.getString("fromDate"));
                ac.setToDate(rs.getString("toDate"));
                ac.setTask(rs.getString("task"));
                ac.setTaskDescription(rs.getString("taskDescription"));
                taskList.add(new Record(ac.getLoginId(), ac.getFromDate(), ac.getToDate(), ac.getTask(), ac.getTaskDescription()));
            }
        } catch (SQLException e) {

        }
        ActivityTrackerTable.setItems(taskList);
    }

    public void refreshtableData() {
        try {
            taskList.clear();
            setCellTable();
            loadDataFromDB();
        } catch (SQLException ex) {
            Logger.getLogger(TaskSecondController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void CloseWin(MouseEvent event) {
        addNewTaskBtn.getScene().getWindow().hide();
    }

    @FXML
    private void closeColorChnage(MouseEvent event) {
        closeBtn.setStyle("-fx-background-color:RED;-fx-text-fill: black;");
    }

    @FXML
    private void colorChangeNormal(MouseEvent event) {
        closeBtn.setStyle("-fx-background-color:transprent;");
    }

    @FXML
    private void newTaskAction(MouseEvent event) throws IOException {
        FXMLLoader loder = new FXMLLoader(getClass().getResource("/activitytracker/Tasks.fxml"));
        Parent root = loder.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        //stage.setOpacity(0.9);
        stage.show();
    }

    @FXML
    private void backToHomeAction(MouseEvent event) throws IOException {
        FXMLLoader loder = new FXMLLoader(getClass().getResource("/activitytracker/Managers.fxml"));
        Parent root = loder.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        //stage.setOpacity(0.9);
        stage.show();
        addNewTaskBtn.getScene().getWindow().hide();
    }

    @FXML
    private void deleteAction(MouseEvent event) {
        addPopUp("Do you really want to delete");
    }

    public void addPopUp(String text) {
        // Heading text
        Text t = new Text(text);
        t.setStyle("-fx-font-size:14px;");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(t);

        JFXDialog dialog = new JFXDialog(rootStack, dialogLayout, JFXDialog.DialogTransition.CENTER);
        // close button
        JFXButton closeButton = new JFXButton("Dismiss");
        closeButton.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;");
        //ok button
        JFXButton addBtn = new JFXButton("Yes");
        addBtn.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;"
                + "");
        closeButton.setOnAction((ActionEvent event1) -> {
            dialog.close();
        });
        addBtn.setOnAction((ActionEvent event1) -> {
            try {
                conn = handler.getConnection();
                String deleteQuery = "delete from activitiestracker where Name='" + userId.getText() + "' AND task = '" + task.getText() + "' AND taskDescription='" + taskDescription.getText() + "'";
                ps = conn.prepareStatement(deleteQuery);
                int i = ps.executeUpdate();
                if (i == 1) {
                    refreshtableData();
                    clearFieldsData();
                } else {
                    JOptionPane.showMessageDialog(null, "Couldn't delete");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Somthing went wrong while deleting the data from the table");
            } catch (HeadlessException ex) {
            } finally {
                try {
                    conn.close();
                    ps.close();
                } catch (SQLException e) {

                }
            }
            dialog.close();
        });

        HBox box = new HBox();
        box.setSpacing(20);
        box.setPrefSize(200, 50);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().addAll(addBtn, closeButton);
        dialogLayout.setActions(box);
        dialog.show();
    }

    @FXML
    private void refreshAction(MouseEvent event) {
        refreshtableData();
    }

    public void clearFieldsData() {
        userId.setText("");
        task.setText("");
        taskDescription.setText("");
    }

    public void setCellValueFromTableToTextField() {
        ActivityTrackerTable.setOnMouseClicked((MouseEvent event) -> {
            try {
                Record ts = ActivityTrackerTable.getItems().get(ActivityTrackerTable.getSelectionModel().getSelectedIndex());
                userId.setText(ts.getLoginId());
                task.setText(ts.getTask());
                taskDescription.setText(ts.getTaskDescription());
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        });
    }
}
