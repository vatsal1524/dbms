package org.example.log;

import org.example.controller.User;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    /**
     * This method is used to write logs for the successfully executed queries.
     *
     * @param query The query that is executed.
     * @param user The User object for the logged-in user.
     */
    public static void writeLog(String query, User user) {
        String rootDir = System.getProperty("user.dir");
        File file = new File( rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + "logs.txt");

        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write("----Query: " + query + "\tExecuted at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
