package org.example.query.handlers;

import org.example.controller.Attribute;
import org.example.controller.Table;
import org.example.controller.User;
import org.example.query.QueryHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class CreateQueryHandler implements QueryHandler {

    /**
     * This method is used to validate the syntax of the query.
     *
     * @param query The query to be validated.
     * @return returns a boolean.
     */
    @Override
    public boolean validateQuery(String query) {
        Pattern pattern = Pattern.compile("create\\s+table\\s+[A-Za-z]+ \\([^)]*\\);");
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

        String[] rows = query.split("\\(", 2)[1].split("\\)")[0].split(",");

        List<Attribute> attributes = new ArrayList<>();
        for (String row : rows) {
            String[] data = row.trim().split(" ");
            boolean primaryKey = false;
            boolean notNull = false;
            if (data.length == 3 && data[2].equals("primary_key"))
                primaryKey = true;
            if (data.length == 3 && data[2].equals("not_null"))
                notNull = true;
            if (data.length == 4) {
                primaryKey = true;
                notNull = true;
            }
            attributes.add(new Attribute(data[0], data[1], primaryKey, notNull));
        }

        Table table = new Table(tableName, attributes);
        return Table.storeTable(table, user);
    }
}