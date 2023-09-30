package org.example.controller;

import org.example.authentication.Authentication;

import java.io.*;
import java.util.Scanner;

public class User {
    private String emailAddress = "";
    private String password = "";
    private String firstName = "";
    private String lastName = "";
    private String securityQuestion = "";
    private String securityAnswer = "";

    /**
     * This method is used to get the security question for the user.
     *
     * @return returns a String.
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * This method is used to get the security answer for the user.
     *
     * @return returns a String.
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * This constructor is used to create a User object.
     *
     * @param emailAddress The email address of the user.
     * @param password The password of the user.
     * @param firstName The first name address of the user.
     * @param lastName The last name of the user.
     * @param securityQuestion The security question of the user.
     * @param securityAnswer The security answer of the user.
     */
    public User(String emailAddress, String password, String firstName, String lastName, String securityQuestion, String securityAnswer) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    /**
     * This constructor is used to create an empty User object.
     *
     */
    public User() {

    }

    /**
     * This method is used to verify a user.
     *
     * @param emailAddress The email address of the user.
     * @param password The password of the user.
     * @return returns a User object
     */
    public static User verifyUser(String emailAddress, String password) {
        Scanner in = new Scanner(System.in);
        try
        {
            String rootDir = System.getProperty("user.dir");
            FileReader fileReader = new FileReader(rootDir + File.separator + "db" + File.separator + "users-info.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(emailAddress) && userInfo[1].equals(Authentication.generateMD5Hash(password))) {
                    System.out.print("\n" + userInfo[4] + " ");
                    String answer = in.nextLine().trim();
                    if (userInfo[5].equals(answer))
                        return new User(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4], userInfo[5]);
                }
            }
            fileReader.close();
            return null;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is used to store a user after registration.
     *
     * @param emailAddress The email address of the user.
     * @param password The password of the user.
     * @param firstName The first name address of the user.
     * @param lastName The last name of the user.
     * @param securityQuestion The security question of the user.
     * @param securityAnswer The security answer of the user.
     */
    public static void storeUser(String emailAddress, String password, String firstName, String lastName, String securityQuestion, String securityAnswer) {
        User user = new User(emailAddress, password, firstName, lastName, securityQuestion, securityAnswer);
        String data = user.getEmailAddress() + "," + Authentication.generateMD5Hash(user.getPassword()) + "," + user.getFirstName()
                + "," + user.getLastName() + "," + user.getSecurityQuestion() + "," + user.getSecurityAnswer() + "\n";

        try {
            new File("db").mkdir();
            File file = new File( "db" + File.separator + "users-info.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(data);
            System.out.print("\n" + firstName + " " + lastName + ", ");

            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * This method is used to get the email address of the user.
     *
     * @return returns a String object.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * This method is used to get the password of the user.
     *
     * @return returns a String object.
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method is used to get the first name of the user.
     *
     * @return returns a String object.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This method is used to get the last name of the user.
     *
     * @return returns a String object.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This method is used to override the toString() method of the Object class.
     *
     * @return returns a String object.
     */
    @Override
    public String toString() {
        return "User{" +
                "emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
