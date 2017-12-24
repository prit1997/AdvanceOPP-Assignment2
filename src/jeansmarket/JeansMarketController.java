/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeansmarket;

import Models.Jeans;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author prit
 */
public class JeansMarketController implements Initializable {
    ObservableList<Jeans> jeans = FXCollections.observableArrayList();
    //for Coloumn in @FXML 
    //configure the table elements
    @FXML private TableView<Jeans> jeansTable;
    @FXML private TableColumn<Jeans, Integer> itemNumberColumn;
    @FXML private TableColumn<Jeans, String> brandNameColumn;
    @FXML private TableColumn<Jeans, String> typeColumn;
    @FXML private TableColumn<Jeans, String> colourColumn;
    @FXML private TableColumn<Jeans, Integer> sizeColumn;
    @FXML private TableColumn<Jeans, Double> priceColumn;
    @FXML private TableColumn<Jeans, Double> soldPriceColumn;
    //for Button in @FXML
    @FXML private Button createJeans;
    @FXML private Button editJeans;
    @FXML private Button sellJeans;
  
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        itemNumberColumn.setCellValueFactory(new PropertyValueFactory<Jeans, Integer>("itemNumber"));
        brandNameColumn.setCellValueFactory(new PropertyValueFactory<Jeans, String>("brandName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Jeans, String>("types"));
        colourColumn.setCellValueFactory(new PropertyValueFactory<Jeans, String>("colour"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Jeans, Integer>("size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Jeans, Double>("price"));
        soldPriceColumn.setCellValueFactory(new PropertyValueFactory<Jeans, Double>("soldPrice"));
        
        try {
            // loading dummy data
             loadJeans();
           
        }  catch (SQLException ex) {
            Logger.getLogger(JeansMarketController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
   
    
    /**
     * This method will load the users from the database and load them into 
     * the TableView object
     */
    public void loadJeans() throws SQLException
    {
        ObservableList<Jeans> jeans = FXCollections.observableArrayList();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jeansMarket?useSSL=false", "root", "prit1997");
            //2.  create a statement object
            statement = conn.createStatement();
            
            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM jeans");
            //4.  create user objects from each record
            while (resultSet.next())
            {
                 Jeans newJean = new Jeans(resultSet.getString("brandName"),
                                                       resultSet.getString("jeanstypes"),
                                                       resultSet.getString("colour"),
                                                       resultSet.getDouble("price"),
                                                       resultSet.getDouble("price"),
                                                       resultSet.getInt("size"),
                                                       resultSet.getInt("itemNumber"));
        
                System.out.println(newJean);
                jeans.add(newJean); 
            }
            
            
            jeansTable.getItems().addAll(jeans);
            
        } catch (Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }
    
    
    
    /**
     * this method will create Jeans and Redirect to CreateJeansView.
     * @param event
     * @throws IOException 
     */
    public void createNewJeansButtonPushed(ActionEvent event) throws IOException
    {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CreateJeansView.fxml"));
        Parent parent = loader.load();
        Scene newJeansScene = new Scene(parent);
        
        //access the controller of the newJeansScene and send over
        //the current list of jeans
        CreateJeansViewController controller = loader.getController();
        controller.initialData(jeansTable.getItems());
        
        //Get the current "stage" (aka window) 
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //change the scene to the new scene
        stage.setTitle("Add New Jeans");
        stage.setScene(newJeansScene);
        stage.show();
    }
    /**
     * this method will sell the selected jeans from the table 
    */
    public void sellbuttonPushed(ActionEvent event){
      Jeans currentJeans = jeansTable.getSelectionModel().getSelectedItem();
        jeans.remove(currentJeans);
        
    }
    
    /**
     * This method will populate the list of Jeans
     * @return 
     */
    public void loadJeans(ObservableList<Jeans> newList)
    {
        this.jeansTable.setItems(newList);
    }
    
    /**
     * this method has all the dummy data and it will return data when you launch views.
     * @return
     * @throws IOException 
     */
    public ObservableList<Jeans> getJeans() throws IOException, SQLException
    {
        //add jeans to the list
       
        System.out.print(jeans);
        //return the list
        return jeans;
    }
    
    /**
     * method for default Image
     * @return
     * @throws IOException 
     */
    public Image getImagegDenim() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File("./src/images/defaultJeans.png"));
        Image denim = SwingFXUtils.toFXImage(bufferedImage, null);
        return denim;
    }
}// end of class
