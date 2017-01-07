package net.WhaleTech;

/**
 * Symptom class used in the {@link Food} objects
 */
public class  Symptoms
{
    // The local name and comment of the symptom
    private String name;
    private String comment;

    /**
     * Constructor for the symptom. Called by the controllers when each {@link Food} object is created.
     * @param name
     *          the name of the symptom.
     * @param comment
     *          the comment of the symptom.
     */
    public Symptoms(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    /**
     * Overload constructor to create a symptom from a serialized string.
     *
     * @param serializedString
     *          the serialized string
     *
     * @apiNote Syntax: name:some_name,comment:some_comment
     */
    public Symptoms(String serializedString) {

        System.out.println("Full string: " +serializedString);
        String[] segments = serializedString.split("\\$");

        String[] name_segment = segments[0].split(":");
        System.out.println(name_segment[0]);

        String[] comment_segment = segments[1].split(":");
        this.name = name_segment[1];
        this.comment = comment_segment[1];
    }

    /**
     * toString() override.
     * @return The Name of the symptom, followd by the comment.
     * @apiNote This string is not recommended to use in code, as it is meant to be used for debug purposes.
     */
    @Override
    public String toString() {
        String finalString = "";

        if(!comment.equals(""))
            finalString += "name:" + name + "$comment:" + comment;
        else
            finalString += "name:" + name + "$comment:No comment";

        return finalString;
    }

    public static String arrayToSerialized() {
        return "";
    }

    /*
     * Getters and setters... nothing big here
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
