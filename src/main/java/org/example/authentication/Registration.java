package org.example.authentication;

import org.example.controller.User;

import java.util.Scanner;

public class Registration {

    /**
     * This method is used to prompt the user to enter his registration details and then add that information in the database.
     *
     */
    public static void register() {
        Scanner in = new Scanner(System.in);
        System.out.print("\nEmail address: ");
        String emailAddress = in.next();
        System.out.print("Password: ");
        String password = in.next();
        System.out.print("First Name: ");
        String firstName = in.next();
        System.out.print("Last Name: ");
        String lastName = in.next();
        in.nextLine();
        System.out.print("Enter a security question: ");
        String securityQuestion = in.nextLine();
        System.out.print("Enter your answer for the security question: ");
        String securityAnswer = in.nextLine();
        User.storeUser(emailAddress, password, firstName, lastName, securityQuestion, securityAnswer);
    }
}
