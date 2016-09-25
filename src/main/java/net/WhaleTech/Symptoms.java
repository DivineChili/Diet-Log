package net.WhaleTech;

/**
 * Symptom class used in the {@link Food} objects
 */
public class Symptoms
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
     * toString() override.
     * @return The Name of the symptom, followd by the comment.
     * @apiNote This string is not recommended to use in code, as it is meant to be used for debug purposes.
     */
    @Override
    public String toString()
    {
        String finalString = "";

        finalString += "Navn: " + name + ", Kommentar: " + comment + '\n';

        return finalString;
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


    /**
     * Similar to the toString() function, but is used to return the JSON form of the symptom.
     *
     * @return {@link org.json.JSONObject} version of this symptom
     */
    public String toJSON()
    {
        return "{\"name\":\""+ name +"\",\"comment\":\"" + comment + "\"}";
    }
}
