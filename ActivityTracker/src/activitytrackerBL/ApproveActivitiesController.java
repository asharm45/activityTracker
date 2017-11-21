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
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author home
 */
public class ApproveActivitiesController implements Initializable {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Statement s;
    DbHandler handler;

    FXMLLoader loder;
    Parent root;
    Stage stage;
    ActivityClass ac;

    @FXML
    private TableView<Data> DataTable;
    @FXML
    private TableColumn<Data, String> userID_Col;
    @FXML
    private TableColumn<Data, String> taskCol;
    @FXML
    private TableColumn<Data, String> taskDescriptionCol;
    @FXML
    private TableColumn<Data, String> ActionCol;
    @FXML
    private TableColumn<Data, String> fromDateCol;
    @FXML
    private TableColumn<Data, String> toDateCol;

    ObservableList taskList = FXCollections.observableArrayList();
    FilteredList userFilter = new FilteredList(taskList, e -> true);

    @FXML
    private JFXButton loadData;
    @FXML
    private Label closeBtn;
    @FXML
    private StackPane rootStack;
    @FXML
    private JFXButton approveAll_Activity;
    @FXML
    private JFXButton rejectAll_Activity;
    @FXML
    private JFXButton approveSingleActovity;
    @FXML
    private JFXButton rejectSingleActovity;
    @FXML
    private Label backBtn;
    @FXML
    private JFXTextField FilterTxt;

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
        DataTable.setTableMenuButtonVisible(true);
    }

    public void setCellTable() {
        userID_Col.setCellValueFactory(new PropertyValueFactory<>("userId"));
        taskCol.setCellValueFactory(new PropertyValueFactory<>("task"));
        fromDateCol.setCellValueFactory(new PropertyValueFactory<>("fromDate"));
        toDateCol.setCellValueFactory(new PropertyValueFactory<>("toDate"));
        taskDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        ActionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
    }

    private void getUsersID() throws SQLException {
        try {
            conn = handler.getConnection();

            //MainController.uName
            String query = "select * from activitiestracker where Name IN (select loginId from usersinfo where ManagerId='" + MainController.ucObj().getLoginId() + "')";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                ac.setLoginId(rs.getString("Name"));
                ac.setFromDate(rs.getString("fromDate"));
                ac.setToDate(rs.getString("toDate"));
                ac.setTask(rs.getString("task"));
                ac.setTaskDescription(rs.getString("taskDescription"));
                ac.setAction(rs.getString("action"));
                taskList.add(new Data(ac.getLoginId(), ac.getFromDate(), ac.getToDate(), ac.getTask(), ac.getTaskDescription(), ac.getAction()));
            }
        } catch (SQLException e) {
        } finally {
            conn.close();
            ps.close();
            rs.close();
        }
        DataTable.setItems(taskList);
    }

    public void refreshtableData() {
        try {
            taskList.clear();
            setCellTable();
            getUsersID();
        } catch (SQLException ex) {
            Logger.getLogger(ApproveActivitiesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadDataAction(MouseEvent event) {
        refreshtableData();
        loadData.setText("Re-Load Data");
    }

    @FXML
    private void colourChangeBack(MouseEvent event) {
        closeBtn.setStyle("-fx-background-color:transprent;");
    }

    @FXML
    private void colourChange(MouseEvent event) {
        closeBtn.setStyle("-fx-background-color:RED;-fx-text-fill: black;");
    }

    @FXML
    private void closeBtnAction(MouseEvent event) {
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
    private void approveAll_ActivityAction(MouseEvent event) throws SQLException {
        try {

            conn = handler.getConnection();
            Data ts = DataTable.getItems().get(DataTable.getSelectionModel().getSelectedIndex());
            String updateQuery = "update activitiestracker set action='Approved' where Name='" + ts.getUserId() + "'";
            s = conn.createStatement();
            s.executeUpdate(updateQuery);
            addPopUp(ts.getUserId() + " Congratulations your task/tasks have been approved :)");
            refreshtableData();

        } catch (ArrayIndexOutOfBoundsException e) {
            addPopUp("You must select a record");
        } finally {
            conn.close();
            ps.close();
            rs.close();
        }
    }

    @FXML
    private void rejectAll_ActivityAction(MouseEvent event) throws SQLException {
        try {
            conn = handler.getConnection();
            Data ts = DataTable.getItems().get(DataTable.getSelectionModel().getSelectedIndex());
            String updateQuery = "update activitiestracker set action='Rejected' where Name='" + ts.getUserId() + "'";
            s = conn.createStatement();
            s.executeUpdate(updateQuery);
            addPopUp(ts.getUserId() + " Oops!!! your task/tasks have been rejected :(");
            refreshtableData();
        } catch (ArrayIndexOutOfBoundsException e) {
            addPopUp("You must select a record");
        } finally {
            conn.close();
            ps.close();
            rs.close();
        }
    }

    @FXML
    private void approveSingleActovityAction(MouseEvent event) throws SQLException {
        try {

            conn = handler.getConnection();
            Data ts = DataTable.getItems().get(DataTable.getSelectionModel().getSelectedIndex());
            String updateQuery = "update activitiestracker set action='Approved' where Name='" + ts.getUserId() + "' AND task='" + ts.getTask() + "' AND taskDescription='" + ts.getTaskDescription() + "'";
            s = conn.createStatement();
            int i = s.executeUpdate(updateQuery);
            if (i == 1) {
                addPopUp(ts.getUserId() + " Congratulations your task/tasks have been approved :)");
                refreshtableData();
            } else {
                addPopUp("Oops!!! Something went wrong");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            addPopUp("You must select a record");
        } finally {
            conn.close();
            ps.close();
            rs.close();
        }
    }

    @FXML
    private void rejectSingleActovityAction(MouseEvent event) throws SQLException {
        try {
            conn = handler.getConnection();
            Data ts = DataTable.getItems().get(DataTable.getSelectionModel().getSelectedIndex());
            String updateQuery = "update activitiestracker set action='Rejected' where Name='" + ts.getUserId() + "' AND fromDate='" + ts.getFromDate() + "' AND toDate='" + ts.getToDate() + "' AND task='" + ts.getTask() + "' AND taskDescription='" + ts.getTaskDescription() + "'";
            s = conn.createStatement();
            int i = s.executeUpdate(updateQuery);
            if (i == 1) {
                addPopUp(ts.getUserId() + " Oops!!! your task/tasks have been rejected :(");
                refreshtableData();
            } else {
                addPopUp("Oops!!! Something went wrong");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            addPopUp("You must select a record");
        } finally {
            conn.close();
            ps.close();
            rs.close();
        }
    }

    @FXML
    private void backBtnAction(MouseEvent event) throws IOException {
        loder = new FXMLLoader(getClass().getResource("/activitytracker/Managers.fxml"));
        root = loder.load();
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        //stage.setOpacity(0.9);
        stage.show();

        DataTable.getScene().getWindow().hide();
    }

    @FXML
    private void search(KeyEvent event) {
        FilterTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            userFilter.setPredicate(((Predicate<? super Data>) list -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (list.getUserId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (list.getTask().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (list.getTaskDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (list.getAction().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            }));
        });

        SortedList<Data> sortedData = new SortedList<Data>(userFilter);
        sortedData.comparatorProperty().bind(DataTable.comparatorProperty());
        DataTable.setItems(sortedData);
    }

}
