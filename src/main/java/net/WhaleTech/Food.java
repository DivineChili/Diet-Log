package net.WhaleTech;


import javafx.collections.ObservableList;

/**
 * One of the most used objects in this program.
 * This object contains the core data of each food, which will be represented in the GUI.
 */
public class Food implements HierarchyData<Food>
{
    private int state;
    private Symptoms[] symptoms;
    private String comment;
    private String title;
    private Tag tag;
    private boolean isCategory;

    /**
     *  Constructor called by the main treeview function and the JSON Handler to get food objects out of user file.
     *
     * @param title
     *          the name of the food
     * @param state
     *          state of which you can eat or not. Value from 0-4
     * @param symptoms
     *          array of which symptoms the food will have as default. Only used when constructing food from JSON or manually added through GUI
     * @param comment
     *          the basic comment of the food. Is displayd in the general comment area of the main GUI
     * @param isCategory
     *          boolean value to check if this is a food category or not.
     * @param tag
     *          this foods unlocalized name. Only used by the predicate to filter correctly.
     */
    public Food(String title, int state, Symptoms[] symptoms, String comment, boolean isCategory,Tag tag) {
        this.state = state;
        this.symptoms = symptoms;
        this.comment = comment;
        this.title = title;
        this.isCategory = isCategory;
        this.tag = tag;
        System.out.println("New Created Food: " + title + ", " + toString());
    }

    /**
     * Constructor used to generate food categories.
     *
     * @param title
     *          title of the food
     * @param isCategory
     *          boolean to check if food is category
     * @param tag
     *          tag used with the predicate
     */
    public Food(String title, boolean isCategory, Tag tag)
    {
        this.title = title;
        this.isCategory = isCategory;
        this.tag = tag;
    }


    @Override
    public String toString()
    {
        return tag.toString();
    }

    /*
        Basic getters and setters
     */

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Symptoms[] getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Symptoms[] symptoms) {
        this.symptoms = symptoms;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCategory() {
        return isCategory;
    }

    public void setCategory(boolean category) {
        isCategory = category;
    }


    /**
     * Returns a serialized version of the
     * symptoms to store as a string
     *
     * @return
     *      The serialized string
     *
     */
    public String serializeSymptoms()
    {
        String allSymptoms = "";

        if(getSymptoms() != null)
        {
            for (int i = 0; i<symptoms.length; i++)
            {
                if(i == 0)
                    allSymptoms += symptoms[i].toString();
                else
                    allSymptoms += "&" + symptoms[i].toString();
            }
        }else
            allSymptoms = null;
        return allSymptoms;
    }

    public static Symptoms[] deserializeSymptomArray(String serializedString) {
        if(serializedString != null) {
            String[] serializedSymptom = serializedString.split("&");
            int size = serializedSymptom.length;
            Symptoms[] allSymptoms = new Symptoms[size];


            for (int i = 0; i < size; i++) {
                allSymptoms[i] = new Symptoms(serializedSymptom[i]);
                System.out.println(serializedSymptom[i]);
            }
            return allSymptoms;
        }
        else
            return null;
    }

    @Override
    public ObservableList<Food> getChildren() {
        return null;
    }
}
