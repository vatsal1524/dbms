package org.example.query.handlers;

import org.example.log.Log;
import org.example.controller.User;
import org.example.query.QueryHandler;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropQueryHandler implements QueryHandler {

    /**
     * This method is used to validate the syntax of the query.
     *
     * @param query The query to be validated.
     * @return returns a boolean.
     */
    @Override
    public boolean validateQuery(String query) {
        Matcher matcher1 = Pattern.compile("drop\\sdatabase;").matcher(query);
        Matcher matcher2 = Pattern.compile("drop\\stable\\s[A-Za-z]+;").matcher(query);

        return (matcher1.matches() || matcher2.matches());
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

        if (query.split(" ")[1].equals("database;")) {
            String rootDir = System.getProperty("user.dir");
            File database = new File(rootDir + File.separator + "db" + File.separator + user.getEmailAddress());
            deleteFilesInFolder(database);
            database.delete();
            return true;
        }

        if (query.split(" ")[1].equals("table")) {
            String tableName = query.split(" ")[2].split(";")[0];

            String rootDir = System.getProperty("user.dir");
            File table = new File(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + tableName + ".txt");
//            System.out.println(table.getAbsolutePath());
            File tableConfig = new File(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + tableName + "-config.txt");
//            System.out.println(tableConfig.getAbsolutePath());
            table.delete();
            tableConfig.delete();
            Log.writeLog(query, user);
            return true;
        }

        return false;
    }

    private void deleteFilesInFolder(File file) {
        for (File node : file.listFiles()) {
            node.delete();
        }
    }
}