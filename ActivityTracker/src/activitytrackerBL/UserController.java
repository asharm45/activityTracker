/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activitytrackerBL;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author home
 */
public class UserController implements Initializable {

    FXMLLoader loder;
    Parent root;
    Stage stage;

    @FXML
    private JFXHamburger Hamburger;
    @FXML
    private JFXDrawer drawer;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            VBox box = FXMLLoader.load(getClass().getResource("/activitytracker/UserActions.fxml"));

            drawer.setSidePane(box);

            for (Node node : box.getChildren()) {
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        switch (node.getAccessibleText()) {
                            case "Material_LogOut":
                                loder = new FXMLLoader(getClass().getResource("/activitytracker/Main.fxml"));
                                root = null;
                                try {
                                    root = loder.load();
                                } catch (IOException ex) {
                                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                stage = new Stage();
                                stage.initStyle(StageStyle.UNDECORATED);
                                stage.setScene(new Scene(root));
                                //stage.setOpacity(0.9);
                                stage.show();
                                Hamburger.getScene().getWindow().hide();
                                break;

                            case "Material_Activity":
                                loder = new FXMLLoader(getClass().getResource("/activitytracker/TaskSecond.fxml"));
                                root = null;
                                try {
                                    root = loder.load();
                                } catch (IOException ex) {
                                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                stage = new Stage();
                                stage.initStyle(StageStyle.UNDECORATED);
                                stage.setScene(new Scene(root));
                                //stage.setOpacity(0.9);
                                stage.show();
                                Hamburger.getScene().getWindow().hide();
                                break;
                        }
                    });
                }
            }

            HamburgerBackArrowBasicTransition burgerTask = new HamburgerBackArrowBasicTransition(Hamburger);
            burgerTask.setRate(-1);
            Hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();

                if (drawer.isShown()) {
                    drawer.close();
                } else {
                    drawer.open();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
