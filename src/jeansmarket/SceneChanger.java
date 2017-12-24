/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeansmarket;

import Models.Jeans;
import Models.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author prit
 */
public class SceneChanger {
    private static Jeans loggedInUser;

    
    public void changeScenes(ActionEvent event, String viewName, String title) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * This method will change scenes and pre load the next scene with a User object
     */
    public void changeScenes(ActionEvent event, String viewName, String title, Jeans jeans, ControllerClass controllerClass) throws IOException  
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //access the controller class and preloaded the User data
        controllerClass = loader.getController();
        controllerClass.preloadData(jeans);
        
        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    
    public static Jeans getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User users) {
        SceneChanger.loggedInUser = loggedInUser;
    }

    void changeScenes(ActionEvent event, String newUserViewfxml, String edit_User, User user, NewUserViewController npvc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
