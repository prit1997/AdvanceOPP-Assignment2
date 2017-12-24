/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Models.PasswordGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 *
 * @author prit
 */
public class PasswardTester {
     public static void main(String[] args) throws NoSuchAlgorithmException
    {
        String password = "simple";
        byte[] salt = PasswordGenerator.getSalt();
        
        System.out.printf("password: %s%n", PasswordGenerator.getSHA512Password(password, salt));
        System.out.printf("password: %s%n", PasswordGenerator.getSHA512Password(password, salt));
    }
}
