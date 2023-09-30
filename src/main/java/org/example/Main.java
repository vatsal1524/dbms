package org.example;

import org.example.authentication.Authentication;
import org.example.controller.User;
import org.example.query.Query;

public class Main {
    public static void main(String[] args) {
        System.out.println("************Welcome to the DBMS************");

        User user = Authentication.authenticate();
        if (user != null)
            Query.promptForQuery(user);
    }
}