/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeansmarket;


import Models.PasswordGenerator;
import Models.User;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


/**
 * FXML Controller class
 *
 * @author prit
 */
public class LoginViewController implements Initializable {
    ObservableList<User> users = FXCollections.observableArrayList();
    
    //used for TextField
    @FXML private TextField login;
    //used for the passwords
    @FXML private PasswordField pwField;
    
    //used for error
    @FXML private Label errMsgLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errMsgLabel.setText("");
    }    
    
public void loginButtonPushed(ActionEvent event) throws IOException, NoSuchAlgorithmException
    {
        //query the database with the userId provided, get the salt
        //and encrypted password stored in the database
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        int userNum = Integer.parseInt(login.getText());
        
        try{
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jeansMarket?useSSL=false", "root", "prit1997");
            
            //2.  create a query string with ? used instead of the values given by the user
            String sql = "SELECT * FROM users WHERE userID = ?";
            
            //3.  prepare the statement
            ps = conn.prepareStatement(sql);
            
            //4.  bind the userID to the ?
            ps.setInt(1, userNum);
            
            //5. execute the query
            resultSet = ps.executeQuery();
            
            //6.  extract the password and salt from the resultSet
            String dbPassword=null;
            byte[] salt = null;
            boolean admin = false;
            User users = null;
            
            while (resultSet.next())
            {
                dbPassword = resultSet.getString("password");
                
                Blob blob = resultSet.getBlob("salt");
                
                //convert into a byte array
                int blobLength = (int) blob.length();
                salt = blob.getBytes(1, blobLength);
                
                admin = resultSet.getBoolean("admin");
                
                users = new User(resultSet.getString("firstName"),
                                                       resultSet.getString("lastName"),
                                                       resultSet.getString("phoneNumber"),
                                                       resultSet.getDate("birthday").toLocalDate(),
                                                       resultSet.getString("password"),
                                                       resultSet.getBoolean("admin"));
                users.setuserID(resultSet.getInt("userID"));
                users.setImageFile(new File(resultSet.getString("imageFile")));  
            }
            
            //convert the password given by the user into an encryted password
            //using the salt from the database
            String userPW = PasswordGenerator.getSHA512Password(pwField.getText(), salt);
            
            SceneChanger sc = new SceneChanger();
            
            if (userPW.equals(dbPassword))
                SceneChanger.setLoggedInUser(users);
            
            //if the passwords match - change to the Allusers Table View
            if (userPW.equals(dbPassword) && admin)
                sc.changeScenes(event, "AllUsers.fxml", "All Users");
            else if (userPW.equals(dbPassword))
            {
                //create an instance of the controller class for log hours view
                NewUserViewController controllerClass = new NewUserViewController();
                sc.changeScenes(event, "NewUserView.fxml", "Edit Users");
            }
            else
                //if the do not match, update the error message
                errMsgLabel.setText("The userID and password do not match");
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        
    }
}