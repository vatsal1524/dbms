package org.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Attribute {
    private String attributeName;
    private String dataType = "";
    private boolean primaryKey = false;
    private boolean notNull = false;

    /**
     * This constructor is used to create an Attribute object.
     *
     * @param attributeName Name of the attribute.
     * @param dataType Datatype of the attribute ("int" or "varchar").
     * @param primaryKey Stores whether the attribute is a primary key or not.
     * @param notNull Stores whether the attribute has a not null constraint or not.
     */

    public Attribute(String attributeName, String dataType, boolean primaryKey, boolean notNull) {
        this.attributeName = attributeName;
        this.dataType = dataType;
        this.primaryKey = primaryKey;
        this.notNull = notNull;
    }

    /**
     * This method is used to retrieve the list of attributes for a given table.
     *
     * @param tableName Name of the table.
     * @param emailAddress Email Address of the logged-in user.
     * @return returns the List<Attribute> object.
     */
    public static List<Attribute> retrieveAttributes(String tableName, String emailAddress) {
        List<Attribute> attributes = new ArrayList<>();
        try
        {
            String rootDir = System.getProperty("user.dir");
            FileReader fileReader = new FileReader(rootDir + File.separator + "db"  + File.separator + emailAddress + File.separator + tableName + "-config.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                String[] row = line.split(",");
                attributes.add(new Attribute(row[0], row[1], row[2].equals("true"), row[3].equals("true")));
            }
            fileReader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return attributes;
    }

    /**
     * This method is used to get attributeName of the Attribute object.
     *
     * @return returns the String object.
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * This method is used to get datatype of the Attribute object.
     *
     * @return returns the String object.
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * This method is used to check whether the Attribute is a primary key or not.
     *
     * @return returns a boolean.
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * This method is used to check whether the Attribute has not null constraint or not.
     *
     * @return returns a boolean.
     */
    public boolean isNotNull() {
        return notNull;
    }

    /**
     * This method is used to override the toString() method of the Object class.
     *
     * @return returns a String object.
     */
    @Override
    public String toString() {
        return "Attribute{" +
                "attributeName='" + attributeName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", primaryKey=" + primaryKey +
                ", notNull=" + notNull +
                '}';
    }
}
