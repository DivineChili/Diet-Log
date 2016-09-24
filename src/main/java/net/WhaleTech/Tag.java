package net.WhaleTech;

public class Tag
{
    private String parent;
    private String name;

    public Tag(String parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public Tag(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        if(parent != null)
            return this.parent + "#" + this.name;
        else
            return this.name;
    }
}
