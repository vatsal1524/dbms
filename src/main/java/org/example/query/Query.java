package org.example.query;

import org.example.authentication.Authentication;
import org.example.log.Log;
import org.example.controller.User;
import org.example.query.handlers.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Query {

    /**
     * This method is used to prompt the user to execute a query, perform a transaction or logout.
     *
     * @param user The currently logged-in user details.
     */
    public static void promptForQuery(User user) {
        Scanner in = new Scanner(System.in);
        int n;
        do {
            System.out.println("\n1. Execute a query");
            System.out.println("2. Perform a transaction");
            System.out.println("3. Logout\n");
            System.out.print("Enter you choice: ");
            n = in.nextInt();

            switch (n) {
                case 1 -> {
                    System.out.print("\nSQL Query: ");
                    in.nextLine();
                    String query = in.nextLine().trim();
                    processQuery(user, query);
                }
                case 2 -> {
                    System.out.println("Transaction started! Enter 'commit' to end the transaction and 'rollback' to abort.");
                    List<String> queries = new ArrayList<>();
                    in.nextLine();
                    while (true) {
                        String query = in.nextLine();
                        if (query.equals("commit;")) {

                            String rootDir = System.getProperty("user.dir");
                            File originalDirectory = new File(rootDir + File.separator + "db" + File.separator + user.getEmailAddress());
                            File copiedDirectory = new File(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + "-copy");

                            try {
                                copyDirectory(originalDirectory, copiedDirectory);
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }

                            boolean success = true;
                            for (String str : queries) {
                                if (!processQuery(user, str)) {
                                    success = false;
                                    abort(user);
                                    break;
                                }
                            }
                            if (success) copiedDirectory.delete();
                            break;
                        } else if (query.equals("rollback;")) {
                            break;
                        }
                        queries.add(query);
                    }
                }
                case 3 -> {
                    user = Authentication.authenticate();
                    if (user != null)
                        Query.promptForQuery(user);
                }
                default -> System.out.println("\nPlease enter a valid input!\n");
            }
        } while(n != 3);
    }

    /**
     * This method is used to copy all the files from one directory to another.
     *
     * @param originalDirectory The original directory.
     * @param copiedDirectory The copied directory.
     */
    private static void copyDirectory(File originalDirectory, File copiedDirectory) {
        if (!copiedDirectory.exists())
            copiedDirectory.mkdirs();

        for (File originalFile : originalDirectory.listFiles()) {
            File copiedFile = new File(copiedDirectory.getAbsolutePath() + File.separator + originalFile.getName());
            try {
                copiedFile.createNewFile();
                copyFile(originalFile, copiedFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method is used to copy the content of one file to another.
     *
     * @param originalFile The original file.
     * @param copiedFile The copied file.
     */
    private static void copyFile(File originalFile, File copiedFile) {
        try {
            FileReader fileReader = new FileReader(originalFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FileWriter fileWriter = new FileWriter(copiedFile);

            String line;

            while((line = bufferedReader.readLine()) != null) {
                fileWriter.write(line + "\n");
            }

            fileWriter.close();
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to abort a failed transaction.
     *
     * @param user The currently logged-in user details.
     */
    private static void abort(User user) {
        String rootDir = System.getProperty("user.dir");
        File originalDirectory = new File(rootDir + File.separator + "db" + File.separator + user.getEmailAddress());
        File copiedDirectory = new File(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + "-copy");

        deleteFilesInFolder(originalDirectory);

        originalDirectory.delete();
        copiedDirectory.renameTo(originalDirectory);
    }

    /**
     * This method is used to delete all the files in a directory.
     *
     * @param file The directory from which files are to be deleted.
     */
    private static void deleteFilesInFolder(File file) {
        for (File node : file.listFiles()) {
            node.delete();
        }
    }

    /**
     * This method is used to process a query for a given user.
     *
     * @param query The query to be parsed.
     * @param user The currently logged-in user.
     * @return returns a boolean.
     */
    private static boolean processQuery(User user, String query) {
        QueryHandler queryHandler;

        query = query.trim();
        switch (query.split(" ")[0].toLowerCase()) {
            case "create" -> {
                queryHandler = new CreateQueryHandler();
                if (queryHandler.validateQuery(query)) {
                    if (queryHandler.executeQuery(query, user)) {
                        Log.writeLog(query, user);
                        System.out.println("Query executed successfully!");
                        return true;
                    } else System.out.println("Invalid query!");
                } else {
                    System.out.println("Invalid syntax!");
                }
            }
            case "insert" -> {
                queryHandler = new InsertQueryHandler();
                if (queryHandler.validateQuery(query)) {
                    if (queryHandler.executeQuery(query, user)) {
                        Log.writeLog(query, user);
                        System.out.println("Query executed successfully!");
                        return true;
                    } else System.out.println("Invalid query!");
                } else {
                    System.out.println("Invalid syntax!");
                }
            }
            case "select" -> {
                queryHandler = new SelectQueryHandler();
                if (queryHandler.validateQuery(query)) {
                    if (queryHandler.executeQuery(query, user)) {
                        Log.writeLog(query, user);
                        System.out.println("Query executed successfully!");
                        return true;
                    } else System.out.println("Invalid query!");
                } else {
                    System.out.println("Invalid syntax!");
                }
            }
            case "delete" -> {
                queryHandler = new DeleteQueryHandler();
                if (queryHandler.validateQuery(query)) {
                    if (queryHandler.executeQuery(query, user)) {
                        Log.writeLog(query, user);
                        System.out.println("Query executed successfully!");
                        return true;
                    } else System.out.println("Invalid query!");
                } else {
                    System.out.println("Invalid syntax!");
                }
            }
            case "update" -> {
                queryHandler = new UpdateQueryHandler();
                if (queryHandler.validateQuery(query)) {
                    if (queryHandler.executeQuery(query, user)) {
                        Log.writeLog(query, user);
                        System.out.println("Query executed successfully!");
                        return true;
                    } else System.out.println("Invalid query!");
                } else {
                    System.out.println("Invalid syntax!");
                }
            }
            case "drop" -> {
                queryHandler = new DropQueryHandler();
                if (queryHandler.validateQuery(query)) {
                    if (queryHandler.executeQuery(query, user)) {
                        System.out.println("Query executed successfully!");
                        return true;
                    } else System.out.println("Invalid query!");
                } else {
                    System.out.println("Invalid syntax!");
                }
            }
            case "show" -> {
                queryHandler = new ShowDatabaseQueryHandler();
                if (queryHandler.validateQuery(query)) {
                    if (queryHandler.executeQuery(query, user)) {
                        Log.writeLog(query, user);
                        System.out.println("Query executed successfully!");
                        return true;
                    } else System.out.println("Invalid query!");
                } else {
                    System.out.println("Invalid syntax!");
                }
            }
            default -> System.out.println("Invalid query!");
        }
        return false;
    }
}
