package org.example.query.handlers;

import org.example.controller.Data;
import org.example.controller.Table;
import org.example.controller.User;
import org.example.query.QueryHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateQueryHandler implements QueryHandler {

    /**
     * This method is used to validate the syntax of the query.
     *
     * @param query The query to be validated.
     * @return returns a boolean.
     */
    @Override
    public boolean validateQuery(String query) {
        Matcher matcher1 = Pattern.compile("update\\s[A-Za-z]+\\sset\\s([A-Za-z]+=([A-Za-z0-9]+( [A-Za-z0-9]+)+)(,[A-Za-z0-9]+=[A-Za-z0-9]+)+);").matcher(query);
        Matcher matcher2 = Pattern.compile("update\\s[A-Za-z]+\\sset\\s([A-Za-z]+=[A-Za-z0-9]+(,[A-Za-z]+=[A-Za-z0-9]+)+)\\swhere\\s[A-Za-z]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher3 = Pattern.compile("update\\s[A-Za-z]+\\sset\\s([A-Za-z]+=[A-Za-z0-9]+(,[A-Za-z]+=[A-Za-z0-9]+)+)\\swhere\\s[A-Za-z]+=[A-Za-z0-9]+\\sand\\s[A-Za-z]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher4 = Pattern.compile("update\\s[A-Za-z]+\\sset\\s[A-Za-z]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher5 = Pattern.compile("update\\s[A-Za-z]+\\sset\\s[A-Za-z]+=[A-Za-z0-9]+\\swhere\\s[A-Za-z]+=[A-Za-z0-9]+;").matcher(query);
        Matcher matcher6 = Pattern.compile("update\\s[A-Za-z]+\\sset\\s[A-Za-z]+=[A-Za-z0-9]+\\swhere\\s[A-Za-z]+=[A-Za-z0-9]+\\sand\\s[A-Za-z]+=[A-Za-z0-9]+;").matcher(query);

        return (matcher1.matches() || matcher2.matches() || matcher3.matches() || matcher4.matches() || matcher5.matches() ||
                matcher6.matches());
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
        String tableName = query.split(" ")[1].trim();

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

        List<Data> updatedData = new ArrayList<>();

        String[] arr = query.split(" ")[3].split(",");
        for (String str : arr) {
            updatedData.add(new Data(str.split("=")[0], str.split("=")[1]));
        }

        try {
            return Table.updateRows(tableName, user, constraints, updatedData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
