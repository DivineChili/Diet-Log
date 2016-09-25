package net.WhaleTech;

/**
 * A tag is basically the codename for the {@link Food} object.
 * It contains the names of all parent directories, so it will be shown if the
 * parent is searched for in the prediacte
 */
public class Tag
{

    //Fields which contains the name of the parent, and the holder
    private String parent;
    private String name;

    /**
     * The constructor for the tag
     *
     * @param parent
     *          string version of the tag which is held in the parent food.
     * @param name
     *          this tag-holder's name
     */
    public Tag(String parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    /**
     * This constructor is used if there is no parent to the holder. For example the root.
     *
     * @param name
     *          this tag-holder's name
     */
    public Tag(String name)
    {
        this.name = name;
    }

    /**
     * To string function.
     * @return {@code this.name}
     * @return this tag's parent # this tag's name [root#milk#butter]
     */
    @Override
    public String toString()
    {
        if(parent != null)
            return this.parent + "#" + this.name;
        else
            return this.name;
    }
}
