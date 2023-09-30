package org.example.query.handlers;

import org.example.controller.Table;
import org.example.controller.User;
import org.example.query.QueryHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQueryHandler implements QueryHandler {

    /**
     * This method is used to validate the syntax of the query.
     *
     * @param query The query to be validated.
     * @return returns a boolean.
     */
    @Override
    public boolean validateQuery(String query) {
        Pattern pattern = Pattern.compile("insert\\sinto\\s[A-Za-z]+\\svalues\\s\\([^)]*\\);");
        Matcher matcher = pattern.matcher(query);
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
        String tableName = query.split(" ")[2];

        String[] row = query.split("\\(", 2)[1].split("\\)")[0].split(",");

        return Table.storeRow(tableName, user, row);
    }
}
