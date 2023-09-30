package org.example.query;

import org.example.controller.User;

public interface QueryHandler {
    public boolean validateQuery(String query);
    public boolean executeQuery(String query, User user);
}
