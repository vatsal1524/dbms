package org.example.authentication;

import org.example.controller.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;

import static org.example.authentication.Registration.register;

public class Authentication {
    /**
     * This method is used to allow the user to ogin or register.
     *
     * @return returns the User object.
     */
    public static User authenticate() {
        Scanner in = new Scanner(System.in);
        int n;
        User user;
        do {
            System.out.println("\n1. Log in");
            System.out.println("2. Register as a new user");
            System.out.println("3. Exit\n");
            System.out.print("Enter you choice: ");
            n = in.nextInt();

            switch(n) {
                case 1:
                    user = Login.login();
                    if (user != null) {
                        return user;
                    } else
                        System.out.println("\nIncorrect email address or password! Please try again");
                    break;

                case 2:
                    register();
                    System.out.println("you have been registered. Please log in now!");
                    break;

                case 3:
                    break;

                default:
                    System.out.println("\nPlease enter a valid input!\n");
            }
        } while(n != 3);
        return null;
    }

    /**
     * This method is used to generate an MD5 hashcode for a given string of characters.
     *
     * @param input The string of characters for which the hashcode is to be generated.
     * @return returns the String object containing te hashcode for the given input.
     */
    public static String generateMD5Hash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            byte[] digest = messageDigest.digest(input.getBytes());
            BigInteger hashNumber = new BigInteger(1, digest);

            String hashCode = hashNumber.toString();
            while (hashCode.length() < 32) {
                hashCode = "0" + hashCode;
            }
            return hashCode;
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
