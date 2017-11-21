/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activitytrackerBL;

import classes.DbHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
public class AddNewEmployeeController implements Initializable {
    
    FXMLLoader loder;
    Parent root;
    Stage stage;
    DbHandler handler;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Statement s;
    
    @FXML
    private Label closeBtn;
    @FXML
    private Label BackBtn;
    @FXML
    private JFXButton addBtn;
    @FXML
    private StackPane rootStack;
    @FXML
    private JFXTextField userId;
    @FXML
    private JFXPasswordField password;
    ObservableList<String> accessTypeList = FXCollections.observableArrayList("User", "Manager", "Admin");
    //final ObservableList accessTypes = FXCollections.observableArrayList();
    @FXML
    private JFXComboBox<String> accessType;
    @FXML
    private JFXTextField managerId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new DbHandler();
        password.setText("tcoe123");
        accessType.setItems(accessTypeList);
    }
    
    @FXML
    private void closeWin(MouseEvent event) {
        closeBtn.getScene().getWindow().hide();
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
    private void BackBtnAction(MouseEvent event) {
        loder = new FXMLLoader(getClass().getResource("/activitytracker/Admin.fxml"));
        root = null;
        try {
            root = loder.load();
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        //stage.setOpacity(0.9);
        stage.show();
        closeBtn.getScene().getWindow().hide();
        
    }
    
    @FXML
    private void addBtnAction(MouseEvent event) {
        try {
            conn = handler.getConnection();
            String insertData = "insert into usersinfo(loginId,password,access,ManagerId) values(?,?,?,?)";
            ps = conn.prepareStatement(insertData);
            ps.setString(1, userId.getText());
            ps.setString(2, password.getText());
            ps.setString(3, accessType.getSelectionModel().getSelectedItem());
            ps.setString(4, managerId.getText());
            if (!userId.getText().equals("")) {
                ps.execute();
                addPopUp("Data Added successfully");
                clearTextFields();
            } else {
                addPopUp("insufficent informations");
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
    
    public void clearTextFields(){
        userId.setText("");
        managerId.setText("");
        accessType.setValue("");
    }
    
}
