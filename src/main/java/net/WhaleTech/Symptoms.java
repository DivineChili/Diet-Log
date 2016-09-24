package net.WhaleTech;

public class Symptoms
{
    private String name;
    private String comment;

    public Symptoms(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    @Override
    public String toString()
    {
        String finalString = "";

        finalString += "Navn: " + name + ", Kommentar: " + comment + '\n';

        return finalString;
    }

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

    public String toJSON()
    {
        return "{\"name\":\""+ name +"\",\"comment\":\"" + comment + "\"}";
    }
}
