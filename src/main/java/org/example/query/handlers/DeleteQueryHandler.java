package org.example.query.handlers;

import org.example.controller.Data;
import org.example.controller.Table;
import org.example.controller.User;
import org.example.query.QueryHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteQueryHandler implements QueryHandler {

    /**
     * This method is used to validate the syntax of the query.
     *
     * @param query The query to be validated.
     * @return returns a boolean.
     */
    @Override
    public boolean validateQuery(String query) {
        Matcher matcher1 = Pattern.compile("delete\\sfrom\\s[A-Za-z0-9]+;").matcher(query);
        Matcher matcher2 = Pattern.compile("delete\\sfrom\\s[A-Za-z0-9]+\\swhere\\s[A-Za-z0-9]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher3 = Pattern.compile("delete\\sfrom\\s[A-Za-z0-9]+\\swhere\\s[A-Za-z0-9]+=[A-Za-z0-9]+\\sand\\s[A-Za-z0-9]+=[A-Za-z0-9]+;").matcher(query);

        return (matcher1.matches() || matcher2.matches() || matcher3.matches());
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

        List<Data> constraints = new ArrayList<>();
        if (query.split(" ").length > 3) {
            if (query.split(" ")[3].equals("where")){
                String[] data = new String[2];
                if (query.split(" ").length == 7) {
                    data = new String[] {query.split(" ")[4], query.split(" ")[6]};
                } else if (query.split(" ").length == 5) {
                    data = new String[] {query.split(" ")[4]};
                }
                for (String datum : data) {
                    constraints.add(new Data(datum.split("=")[0], datum.split("=")[1].split(";")[0]));
                }
            }
        }

        try {
            return Table.deleteRows(tableName, user, constraints);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
