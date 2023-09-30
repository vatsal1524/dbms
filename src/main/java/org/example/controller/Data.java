package org.example.controller;

public class Data {
    private int id;
    private String key;
    private String value;

    /**
     * This constructor is used to create a Data object.
     *
     * @param key The name of the Attribute.
     * @param value The value of the Attribute.
     */
    public Data(String key, String value) {
        this.id = -1;
        this.key = key;
        this.value = value;
    }

    /**
     * This method is used to get the id of the Data object.
     *
     * @return returns an int.
     */
    public int getId() {
        return id;
    }

    /**
     * This method is used to set the id of the Data object.
     *
     * @param id The id to be set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This method is used to get the key of the Data object.
     *
     * @return returns a String object.
     */
    public String getKey() {
        return key;
    }

    /**
     * This method is used to get the value of the Data object.
     *
     * @return returns a String object.
     */
    public String getValue() {
        return value;
    }

    /**
     * This method is used to override the toString() method of the Object class.
     *
     * @return returns a String object.
     */
    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
