/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeansmarket;


import Models.User;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author prit
 */
public class AllUsersController implements Initializable {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> userIDColumn;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, LocalDate> birthdayColumn;
    
    @FXML private Button editUserButton;
    @FXML private Button createUserButton;
     @Override
    public void initialize(URL url, ResourceBundle rb) {
     //   disable the edit button until a user has been selected from the table
        editUserButton.setDisable(true);
        createUserButton.setDisable(true);
        
        
        // confgure the table columns
        userIDColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("userID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("birthday"));
        
        try{
            loadUsers();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }      
    
    
    
    /**
     * If the edit button is pushed, pass the selected User to the NewUserView 
     * and pre load it with the data
     */
    public void editButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        User users = this.userTable.getSelectionModel().getSelectedItem();
        NewUserViewController npvc = new NewUserViewController();
        sc.changeScenes(event, "NewUserView.fxml", "Edit User", users, npvc);
    }
        /**

     * This method will switch to the NewUserView scene when the button is pushed
     */
    public void newUserButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "NewUserView.fxml", "Create New User");
    }
    
    /**
     * This Method will take you to Store if their is some customer (LOL!!!)
     * @param event
     * @throws IOException 
     */
    public void storeButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "JeansMarket.fxml", "Mahakali Jeans");
    }
    
    
    /**
     * This method will load the users from the database and load them into 
     * the TableView object
     */
    public void loadUsers() throws SQLException
    {
        
        ObservableList<User> users = FXCollections.observableArrayList();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jeansMarket?useSSL=false", "root", "prit1997");
            //2.  create a statement object
            statement = conn.createStatement();
            
            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM users");
            
            //4.  create user objects from each record
            while (resultSet.next())
            {
                System.out.printf("FUCKOFF");
                User newUser = new User(resultSet.getString("firstName"),
                                                       resultSet.getString("lastName"),
                                                       resultSet.getString("phoneNumber"),
                                                       resultSet.getDate("birthday").toLocalDate(),
                                                       resultSet.getString("password"),
                                                       resultSet.getBoolean("admin"));
                newUser.setuserID(resultSet.getInt("userID"));
                newUser.setImageFile(new File(resultSet.getString("imageFile")));
                System.out.println(newUser);
                users.add(newUser);
            }
           
            userTable.getItems().addAll(users);
            
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
     * If a user has been selected in the table, enable the edit button
     */
    public void usersSelected()
    {
        editUserButton.setDisable(false);
        createUserButton.setDisable(false);
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
        sc.changeScenes(event, "LoginView.fxml", "Login");
    }
}
