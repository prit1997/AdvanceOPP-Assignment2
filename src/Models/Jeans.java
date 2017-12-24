package Models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author prit
 */
public class Jeans {
    // initializing variables
    private static int nextitemNum = 101;
    private String brandName, types, colour;
    private Double price, soldPrice;
    private int size, itemNumber;
    private Image image;
  
    /**
     * Constructor for image 
     * @param brandName
     * @param types
     * @param colour
     * @param price
     * @param soldPrice
     * @param size
     * @param itemNumber
     * @param image 
     */
    public Jeans(String brandName, String types, String colour, Double price, Double soldPrice, int size, int itemNumber, Image image)
    {
      
        this.image = image;
        
         try
        {
            BufferedImage bufferedImage = ImageIO.read(new File("./src/images/defaultJeans.png"));
            image = SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    } // ending of method to get Image
    /**
     * Starting Constructor 
     * @param brandName
     * @param types
     * @param colour
     * @param price
     * @param soldPrice
     * @param size
     * @param itemNumber 
     */
    public Jeans(String brandName, String types, String colour, Double price, Double soldPrice, int size, int itemNumber ) {
        this.brandName = brandName;
        this.types = types;
        this.colour = colour;
        this.size = size;
        this.price = price;
        this.soldPrice = soldPrice;
        this.itemNumber = itemNumber;
    } // end of Constructor

    /** 
     * Getter and Setter Method
     * @return 
     */
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(Double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }    //end of getter and setter method
    
    
    /**
     * This method will write the instance of the User into the database
     */
    public void insertIntoDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jeansMarket", "root", "prit1997");
            
            //2. Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO jeans (brandName, jeanstypes, colour ,price, soldPrice, size, itemNumber)"
                    + "VALUES (?,?,?,?,?,?,?)";
                    
            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);
                   
            //5. Bind the values to the parameters
            preparedStatement.setString(1, brandName);
            preparedStatement.setString(2, types);
            preparedStatement.setString(3, colour);
            preparedStatement.setDouble(4, price);
            preparedStatement.setDouble(5, soldPrice);
            preparedStatement.setInt(6, size);
            preparedStatement.setInt(7, itemNumber);
            
           
            
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }

    public boolean isAdmin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
} // end of class
