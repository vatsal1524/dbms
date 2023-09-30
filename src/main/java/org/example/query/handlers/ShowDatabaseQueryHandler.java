package org.example.query.handlers;

import org.example.controller.User;
import org.example.query.QueryHandler;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowDatabaseQueryHandler implements QueryHandler {

    /**
     * This method is used to validate the syntax of the query.
     *
     * @param query The query to be validated.
     * @return returns a boolean.
     */
    @Override
    public boolean validateQuery(String query) {
        Matcher matcher = Pattern.compile("show\\sdatabase;").matcher(query);
        return matcher.matches();
    }

    /**
     * This method is used to execute the query for a given user.
     *
     * @param query The query to be executed.
     * @param user The currently logged-in user details.
     * @return returns a boolean.
     */
    @Override
    public boolean executeQuery(String query, User user) {

        String rootDir = System.getProperty("user.dir");
        File[] files = new File(rootDir + File.separator + "db" + File.separator + user.getEmailAddress()).listFiles();

        return printTableConfig(files);
    }

    /**
     * This method is used to print table configurations.
     *
     * @param files The list of files.
     * @return returns a boolean.
     */
    private boolean printTableConfig(File[] files) {
        for (File file : files) {
            if (file.getName().contains("-config.txt")) {
                try {
                    FileReader fileReader = new FileReader(file);

                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;

                    System.out.println("\nTable: " + file.getName().split("-")[0]);
                    System.out.println("\nAttribute\tDatatype\tPrimary Key\tNot Null");
                    System.out.println("--------------------------------------------");
                    while ((line = bufferedReader.readLine()) != null) {
                        for (String str : line.split(",")) {
                            System.out.print(str + "\t\t");
                        }
                        System.out.println();
                    }
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        System.out.println();
        return true;
    }
}
