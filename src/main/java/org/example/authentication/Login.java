package org.example.authentication;

import org.example.controller.User;

import java.util.Scanner;

public class Login {

    /**
     * This method is used to prompt the user to enter his login details and then verify the user.
     *
     * @return returns the User0 object of the logged-in user.
     */
    public static User login() {
        Scanner in = new Scanner(System.in);
        System.out.print("\nEmail address: ");
        String emailAddress = in.next();
        System.out.print("Password: ");
        String password = in.next();
        return User.verifyUser(emailAddress, password);
    }
}
