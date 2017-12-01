/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeansmarket;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author prit
 */
public class CreateJeansViewController implements Initializable {
    
    private ObservableList<Jeans> jeans;
    //configure the table elements
    @FXML private TextField itemNumberTextField;
    @FXML private TextField brandNameTextField;
    @FXML private TextField typeTextField;
    @FXML private TextField colourTextField;
    @FXML private TextField sizeTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField soldpriceTextField;
    //configure for Image
    @FXML private ImageView jeansImage;
    // configure the button elements 
    @FXML Button clearButton;
    @FXML Button addButton;
    @FXML Button addImage;
    // Used for chossing file 
    private FileChooser fileChooser;
    private File imageFile;
    @FXML private Label errorlabel;
    
    /*
    Initalizing for all data
    */
     public void initialData(ObservableList<Jeans> listOfJeans)
    {
        jeans = listOfJeans;
    }   
    
     /**
      * method to create New Jeans 
      * @param event
      * @throws IOException 
      */
     public void createNewJeansButtonPushed(ActionEvent event) throws IOException
    {
        // to get errors in the missing items
        if(itemNumberTextField.getText().isEmpty()) {
            errorlabel.setText("Please enter the item Number");
        }   else if (brandNameTextField.getText().isEmpty()) {
            errorlabel.setText("Please enter your Brand Name");
        }   else if(typeTextField.getText().isEmpty()) {
            errorlabel.setText("Please enter the type");
        }    else if(colourTextField.getText().isEmpty()) {
            errorlabel.setText("Please enter the colour");
        }    else if(sizeTextField.getText().isEmpty()) {
            errorlabel.setText("Please enter your Size");
        }    else if(priceTextField.getText().isEmpty()) {
            errorlabel.setText("Please enter the price");
        }    else if(soldpriceTextField.getText().isEmpty()) {
            errorlabel.setText("Please enter Sold Price");
        }    
        else{
            //try catch method and validations
              try
        {
            Jeans newJean = new Jeans(brandNameTextField.getText(), 
                                      typeTextField.getText(), 
                                      colourTextField.getText(), 
                                      (Double.parseDouble(priceTextField.getText())),
                                      (Double.parseDouble(soldpriceTextField.getText())),
                                      (Integer.parseInt(sizeTextField.getText())),
                                      (Integer.parseInt(itemNumberTextField.getText())));
                                      
            jeans.add(newJean);
            changeScene(event, "JeansMarket.fxml");
        }
        catch (IllegalArgumentException e)
        {
            errorlabel.setText("Please enter valid inputs and change ==>"+e.getMessage());
        }
    }
        }
    
     /**
      * This method launches a FileChooser and load the image if accessible
      * //// Reference   PROF. JARET WRIGHT
      * @param event 
      */
      public void chooseImageButtonPushed(ActionEvent event)   
    {
        //get the stage to open a new window
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //filter for only .jpg and .png files
        FileChooser.ExtensionFilter jpgFilter 
                = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter 
                = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        
        //Set to the user's picture directory or C drive if not available
        String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
        File userDirectory = new File(userDirectoryString);
        
        if (!userDirectory.canRead())
            userDirectory = new File("c:/");
        
        fileChooser.setInitialDirectory(userDirectory);
        
        //open the file dialog window
        imageFile = fileChooser.showOpenDialog(stage);
        
        //ensure the user selected a file
        if (imageFile.isFile())
        {
            try
            {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage,null);
                jeansImage.setImage(image);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }
   
      /** 
       * this method will help us to get back to main Scene
       * @param event
       * @throws IOException 
       */
     public void cancelButtonPushed(ActionEvent event) throws IOException
    {
        changeScene(event, "JeansMarket.fxml");
    }
    
    /**
     * this method will help us to change scene
     * @param event
     * @param fxmlFileName
     * @throws IOException 
     */
    public void changeScene(ActionEvent event, String fxmlFileName) throws IOException
    {
        //load a new scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFileName));
        Parent parent = loader.load();
        Scene newScene = new Scene(parent);
        
        //access the controller of the new Scene and send over
        //the current list of jeans
        JeansMarketController controller = loader.getController();
        controller.loadJeans(jeans);
        
        //Get the current "stage" (aka window) 
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //change the scene to the new scene
        stage.setTitle("Jeans Market");
        stage.setScene(newScene);
        stage.show();
    }
    
    // Initializing Data
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}// end of class
