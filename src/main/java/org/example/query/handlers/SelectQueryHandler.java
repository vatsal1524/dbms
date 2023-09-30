package org.example.query.handlers;

import org.example.controller.Attribute;
import org.example.controller.Data;
import org.example.controller.Table;
import org.example.controller.User;
import org.example.query.QueryHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectQueryHandler implements QueryHandler {

    /**
     * This method is used to validate the syntax of the query.
     *
     * @param query The query to be validated.
     * @return returns a boolean.
     */
    @Override
    public boolean validateQuery(String query) {
        Matcher matcher1 = Pattern.compile("select\\s\\*\\sfrom\\s[A-Za-z]+;").matcher(query);
        Matcher matcher2 = Pattern.compile("select\\s([A-Za-z]+(,[A-Za-z]+)+)\\sfrom\\s[A-Za-z]+;").matcher(query);
        Matcher matcher3 = Pattern.compile("select\\s[A-Za-z]+\\sfrom\\s[A-Za-z0-9]+;").matcher(query);
        Matcher matcher4 = Pattern.compile("select\\s([A-Za-z]+(,[A-Za-z]+)+)\\sfrom\\s[A-Za-z0-9]+\\swhere\\s[A-Za-z0-9]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher5 = Pattern.compile("select\\s\\*\\sfrom\\s[A-Za-z0-9]+\\swhere\\s[A-Za-z0-9]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher6 = Pattern.compile("select\\s[A-Za-z]+\\sfrom\\s[A-Za-z0-9]+\\swhere\\s[A-Za-z0-9]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher7 = Pattern.compile("select\\s[A-Za-z]+\\sfrom\\s[A-Za-z0-9]+\\swhere\\s[A-Za-z0-9]+=[A-Za-z0-9]+\\sand\\s[A-Za-z0-9]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher8 = Pattern.compile("select\\s\\*\\sfrom\\s[A-Za-z0-9]+\\swhere\\s[A-Za-z0-9]+=[A-Za-z0-9]+\\sand\\s[A-Za-z0-9]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher9 = Pattern.compile("select\\s([A-Za-z]+(,[A-Za-z]+)+)\\sfrom\\s[A-Za-z0-9]+\\swhere\\s[A-Za-z0-9]+=[A-Za-z0-9]+\\sand\\s[A-Za-z0-9]+=[A-Za-z0-9]+;").matcher(query);

        return (matcher1.matches() || matcher2.matches() || matcher3.matches() || matcher4.matches() || matcher5.matches() ||
                matcher6.matches() || matcher7.matches() || matcher8.matches() || matcher9.matches());
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
        String tableName = query.split("from")[1].split(";")[0].trim();

        if (tableName.contains("where")) tableName = tableName.split("where")[0].trim();

        List<String> attributeNames = new ArrayList<>();
        if (query.startsWith("*", 7)) {
            List<Attribute> attributeList = Attribute.retrieveAttributes(tableName, user.getEmailAddress());
            for (Attribute attribute : attributeList) {
                attributeNames.add(attribute.getAttributeName());
            }
        } else {
            if (query.contains(",")) {
                String[] data = query.split(" ")[1].split(",");
                attributeNames.addAll(Arrays.asList(data));
            } else {
                attributeNames.add(query.split(" ")[1].trim());
            }
        }

        List<Data> constraints = new ArrayList<>();
        if (query.split(" ").length > 4) {
            if (query.split(" ")[4].equals("where")){
                String[] data = new String[2];
                if (query.split(" ").length == 8) {
                    data = new String[] {query.split(" ")[5], query.split(" ")[7]};
                } else if (query.split(" ").length == 6) {
                    data = new String[] {query.split(" ")[5]};
                }
                for (String datum : data) {
                    constraints.add(new Data(datum.split("=")[0], datum.split("=")[1].split(";")[0]));
                }
            }
        }

        try {
            List<String[]> rowsToPrint = Table.retrieveRowsToPrint(tableName, attributeNames, user, constraints);
            if (rowsToPrint == null)
                return false;
            if (rowsToPrint.size() == 0)
                System.out.println("No data found!");
            else {
                System.out.println();
                for (String attribute : attributeNames) {
                    System.out.print(attribute + "\t");
                }
                System.out.println();

                for (String[] arr : rowsToPrint) {
                    for (String s : arr) {
                        System.out.print(s + "\t");
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
