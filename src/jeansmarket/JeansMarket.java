/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeansmarket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author prit
 */
public class JeansMarket extends Application {
    //main method to launch Jeans Market
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("JeansMarket.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Arvind Jeans Market");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * main method
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}//end of class
