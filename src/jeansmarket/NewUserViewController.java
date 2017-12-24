package jeansmarket;

import Models.User;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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


public class NewUserViewController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    } 
    }
    
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField phoneTextField;
    @FXML private DatePicker birthday;
    @FXML private Label errMsgLabel;
    @FXML private Label headerLabel;
    @FXML private ImageView imageView;
    
    // used for controlling whether or not the user is an administrator
    @FXML private CheckBox adminCheckBox;
    @FXML private Label adminLabel;
    
    
    private File imageFile;
    private boolean imageFileChanged;
    private User users;
    
    //used for the passwords
    @FXML private PasswordField pwField;
    @FXML private PasswordField confirmPwField;
    
    
    
    /**
     * This method will change back to the TableView of users without adding
     * a user.  All data in the form will be lost
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
       SceneChanger sc = new SceneChanger();
        
       //check if it is an admin user and go to the table view
            if (SceneChanger.getLoggedInUser().isAdmin())
            sc.changeScenes(event, "AllUsers.fxml", "All Users");
        else
        {
            sc.changeScenes(event, "LoginView.fxml", "Login View");
        }
    }
    
    /**
     * When this button is pushed, a FileChooser object is launched to allow the user
     * to browse for a new image file.  When that is complete, it will update the 
     * view with a new image
     */
    public void chooseImageButtonPushed(ActionEvent event) throws IOException
    {
        //get the Stage to open a new window (or Stage in JavaFX)
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //Instantiate a FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //filter for .jpg and .png
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        //Set to the user's picture directory or user directory if not available
        String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
        File userDirectory = new File(userDirectoryString);
        
        //if you cannot navigate to the pictures directory, go to the user home
        if (!userDirectory.canRead())
            userDirectory = new File(System.getProperty("user.home"));
        
        fileChooser.setInitialDirectory(userDirectory);
        
        //open the file dialog window
        File tmpImageFile = fileChooser.showOpenDialog(stage);
        
        if (tmpImageFile != null)
        {
            imageFile = tmpImageFile;
        
            //update the ImageView with the new image
            if (imageFile.isFile())
            {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView.setImage(img);
                imageFileChanged = true;
            }
        }
        
    }
    
    public void saveUserButtonPushed(ActionEvent event)
    {
        if (validPassword() || users != null)
        {
            try
            {
                if (users != null) //we need to edit/update an existing user
                {
                    //update the user information
                    updateUser();
                    users.updateUserInDB();
                    
                    //update the password if it changed
                    if (!pwField.getText().isEmpty())
                    {
                        if (validPassword())
                        {
                            users.changePassword(pwField.getText());
                        }
                    }
                }
                else    //we need to create a new user
                {
                    
                    if (imageFileChanged) //create a user with a custom image
                    {
                        System.out.printf("FUCK OFF");
                        users = new User(firstNameTextField.getText(),lastNameTextField.getText(),
                                phoneTextField.getText(), birthday.getValue(), imageFile,
                                adminCheckBox.isSelected(),pwField.getText());
                    }
                    else  //create a user with a default image
                    {
                        users = new User(firstNameTextField.getText(),lastNameTextField.getText(),
                                phoneTextField.getText(), birthday.getValue(),
                                pwField.getText(),
                                adminCheckBox.isSelected());
                    }
                    errMsgLabel.setText("");    //do not show errors if creating user was successful
                    users.insertIntoDB();    
                }

                SceneChanger sc = new SceneChanger();
                sc.changeScenes(event, "AllUses.fxml", "All Users");
            }
            catch (Exception e)
            {
                errMsgLabel.setText(e.getMessage());
            }
        }
    }
         public boolean validPassword()
    {
        if (pwField.getText().length() < 5)
        {
            errMsgLabel.setText("Passwords must be greater than 5 characters in length");
            return false;
        }
        
        if (pwField.getText().equals(confirmPwField.getText()))
            return true;
        else
            return false;
    }


/**
     * This method will update the view with a user object pre loaded for an edit
     * @param users
     */
    public void preloadData(User usersData) {
        this.users = usersData;
        this.firstNameTextField.setText(usersData.getFirstName());
        this.lastNameTextField.setText(usersData.getLastName());
        this.birthday.setValue(usersData.getBirthday());
        this.phoneTextField.setText(usersData.getPhoneNumber());
        this.headerLabel.setText("Edit User");
        
        if (usersData.isAdmin())
            adminCheckBox.setSelected(true);
        
        //load the image 
        try{
            String imgLocation = ".\\src\\images\\" + usersData.getImageFile().getName();
            imageFile = new File(imgLocation);
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(img);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * This method will read from the GUI fields and update the User object
     */
    public void updateUser() throws IOException
    {
        users.setFirstName(firstNameTextField.getText());
        users.setLastName(lastNameTextField.getText());
        users.setPhoneNumber(phoneTextField.getText());
        users.setBirthday(birthday.getValue());
        users.setImageFile(imageFile);
        users.setAdmin(adminCheckBox.isSelected());
        
        if (imageFileChanged)
            users.copyImageFile();
    }
    
    
    /**
     * This method will log the user out of the application and return them to the
     * LoginView scene
     * @param user
     */
    public void logoutButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger.setLoggedInUser(null);
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "LoginView.fxml", "Login View");
    }

    
       
}

   
