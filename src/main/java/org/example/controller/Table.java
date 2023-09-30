package org.example.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private String tableName;
    private List<Attribute> attributes;

    /**
     * This constructor is used to create an empty Table object.
     *
     */
    public Table() {
    }

    /**
     * This constructor is used to create a Table object.
     *
     * @param tableName The name of the table.
     * @param attributes The list of attributes of the table.
     */
    public Table(String tableName, List<Attribute> attributes) {
        this.tableName = tableName;
        this.attributes = attributes;
    }

    /**
     * This method is used to retrieve rows to print given the name of attributes and constraints.
     *
     * @param tableName The name of the table.
     * @param attributeNames The list of name of the attributes.
     * @param user The logged-in user details.
     * @param constraints The constraints for the SELECT query.
     * @return returns a List<String> object.
     */
    public static List<String[]> retrieveRowsToPrint(String tableName, List<String> attributeNames, User user, List<Data> constraints) {
        List<Integer> order = new ArrayList<>();

        List<String[]> rowsToPrint = new ArrayList<>();

        try
        {
            String rootDir = System.getProperty("user.dir");
            FileReader fileReader = new FileReader(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + tableName + "-config.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int index;
            int count = 0;

            while((line = bufferedReader.readLine()) != null)
            {
                index = attributeNames.indexOf(line.split(",")[0]);
                if (index != -1) {
                    order.add(count);
                }

                if (constraints.size() > 0) {
                    for (int i = 0; i < constraints.size(); i++) {
                        if (constraints.get(i).getKey().equals(line.split(",")[0])) {
                            constraints.get(i).setId(count);
                        }
                    }
                }

                count++;
            }
            fileReader.close();

            fileReader = new FileReader(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + tableName + ".txt");
            bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null)
            {
                String[] arr = new String[order.size()];
                String[] data = line.split(",");
                for (int i = 0; i < order.size(); i++) {
                    arr[i] = data[order.get(i)];
                }

                boolean add = true;
                for (Data constraint : constraints) {
                    if (!data[constraint.getId()].equals(constraint.getValue())) {
                        add = false;
                        break;
                    }
                }
                if (add) rowsToPrint.add(arr);
            }
            fileReader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return rowsToPrint;
    }

    /**
     * This method is used to delete rows of the table based on the given constraints.
     *
     * @param tableName The name of the table.
     * @param user The logged-in user details.
     * @param constraints The constraints for the DELETE query.
     * @return returns whether the DELETE query was executed successfully or not.
     */
    public static boolean deleteRows(String tableName, User user, List<Data> constraints) {

        try {
            String rootDir = System.getProperty("user.dir");
            FileReader fileReader = new FileReader(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + tableName + "-config.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int count = 0;

            while ((line = bufferedReader.readLine()) != null) {
                if (constraints.size() > 0) {
                    for (Data constraint : constraints) {
                        if (constraint.getKey().equals(line.split(",")[0])) {
                            constraint.setId(count);
                        }
                    }
                }
                count++;
            }
            fileReader.close();

            fileReader = new FileReader(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + tableName + ".txt");
            bufferedReader = new BufferedReader(fileReader);

            try {
                new File("db" + File.separator + user.getEmailAddress()).mkdir();

                File originalFile = new File("db" + File.separator + user.getEmailAddress() + File.separator + tableName + ".txt");
                File newFile = new File("db" + File.separator + user.getEmailAddress() + File.separator + tableName + "-copy.txt");
                newFile.createNewFile();
                FileWriter fileWriter = new FileWriter(newFile, true);

                BufferedWriter writer = new BufferedWriter(fileWriter);

                StringBuilder lines = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    String[] data = line.split(",");

                    boolean delete = true;
                    for (Data constraint : constraints) {
                        if (!data[constraint.getId()].equals(constraint.getValue())) {
                            delete = false;
                            break;
                        }
                    }
                    if (!delete) lines.append(line.trim() + "\n");
                }
                fileReader.close();

                writer.write(lines.toString());
                writer.close();

                return originalFile.delete() && newFile.renameTo(originalFile);
            } catch (Exception e) {
                e.getStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method is used to update rows of the table based on the given constraints and values to be updated.
     *
     * @param tableName The name of the table.
     * @param user The logged-in user details.
     * @param constraints The constraints for the UPDATE query.
     * @param updatedData The data to be updated in the UPDATE query.
     * @return returns whether the UPDATE query was executed successfully or not.
     */
    public static boolean updateRows(String tableName, User user, List<Data> constraints, List<Data> updatedData) {

        try {
            String rootDir = System.getProperty("user.dir");
            FileReader fileReader = new FileReader(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + tableName + "-config.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int count = 0;

            while ((line = bufferedReader.readLine()) != null) {
                if (constraints.size() > 0) {
                    for (Data constraint : constraints) {
                        if (constraint.getKey().equals(line.split(",")[0])) {
                            constraint.setId(count);
                        }
                    }
                }
                for (Data data : updatedData) {
                    if (data.getKey().equals(line.split(",")[0])) {
                        data.setId(count);
                    }
                }
                count++;
            }
            fileReader.close();

            fileReader = new FileReader(rootDir + File.separator + "db" + File.separator + user.getEmailAddress() + File.separator + tableName + ".txt");
            bufferedReader = new BufferedReader(fileReader);

            try {
                new File("db" + File.separator + user.getEmailAddress()).mkdir();

                File originalFile = new File("db" + File.separator + user.getEmailAddress() + File.separator + tableName + ".txt");
                File newFile = new File("db" + File.separator + user.getEmailAddress() + File.separator + tableName + "-copy.txt");
                newFile.createNewFile();
                FileWriter fileWriter = new FileWriter(newFile, true);

                BufferedWriter writer = new BufferedWriter(fileWriter);

                StringBuilder data = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    boolean update = true;

                    for (Data constraint : constraints) {
                        if (!line.split(",")[constraint.getId()].equals(constraint.getValue())) {
                            update = false;
                        }
                    }

                    if (update) {
                        String[] row = line.split(",");
                        for (Data field : updatedData) {
                            row[field.getId()] = field.getValue();
                        }

                        for (int i = 0; i < row.length; i++) {
                            if (i == row.length - 1) {
                                data.append(row[i].trim()).append("\n");
                            } else {
                                data.append(row[i].trim()).append(",");
                            }
                        }
                    } else {
                        data.append(line.trim()).append("\n");
                    }

                }
                fileReader.close();

                writer.write(data.toString());
                writer.close();
                return originalFile.delete() && newFile.renameTo(originalFile);
            } catch (Exception e) {
                e.getStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method is used to get the name of the table.
     *
     * @return returns a String object.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * This method is used to delete rows of the table based on the given constraints.
     *
     * @param table Table object.
     * @param user The logged-in user details.
     * @return returns whether the table has been created successfully or not.
     */
    public static boolean storeTable(Table table, User user) {

        StringBuilder data = new StringBuilder();
        for (Attribute attribute : table.getAttributes()) {
            data.append(attribute.getAttributeName()).append(",").append(attribute.getDataType()).append(",").append(attribute.isPrimaryKey()).append(",").append(attribute.isNotNull()).append("\n");
        }
        try {
            new File("db" + File.separator + user.getEmailAddress()).mkdir();

            File file = new File( "db" + File.separator + user.getEmailAddress() + File.separator + table.getTableName() + "-config.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, true);

            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(data.toString());

            writer.close();
        }

        catch (Exception e) {
            e.getStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method is used to delete rows of the table based on the given constraints.
     *
     * @param tableName The name of the table.
     * @param user The logged-in user details.
     * @param row Array of String with the data to be stored.
     * @return returns whether the row has been added successfully to the table or not.
     */
    public static boolean storeRow(String tableName, User user, String[] row) {
        List<Attribute> attributes = Attribute.retrieveAttributes(tableName, user.getEmailAddress());

        if (attributes == null)
            return false;

        if (attributes.size() != row.length)
            return false;

        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).isNotNull()) {
                if (row[i] == null)
                    return false;
            }
            if (attributes.get(i).getDataType().equals("int")) {
                try {
                    Integer.parseInt(row[i]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
            if (attributes.get(i).isNotNull())
                if (row[i].equals("null") || row[i].equals(""))
                    return false;
        }

        String data = "";

        for (int i = 0; i < row.length; i++) {
            if (i == row.length - 1) {
                data += row[i].trim() + "\n";
            } else {
                data += row[i].trim() + ",";
            }
        }

        try {
            new File("db" + File.separator + user.getEmailAddress()).mkdir();
            File file = new File( "db" + File.separator + user.getEmailAddress() + File.separator + tableName + ".txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(data);
            writer.close();
        }

        catch (Exception e) {
            e.getStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method is used to get the list of attributes of the Table object.
     *
     * @return returns List object.
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
}
