package net.WhaleTech;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Food
{
    private int state;
    private Symptoms[] symptoms;
    private String comment;
    private String title;
    private Tag tag;
    private boolean isCategory;

    public Food(String title, int state, Symptoms[] symptoms, String comment, boolean isCategory,Tag tag) {
        this.state = state;
        this.symptoms = symptoms;
        this.comment = comment;
        this.title = title;
        this.isCategory = isCategory;
        this.tag = tag;
        System.out.println("New Created Food: " + title + ", " + toJSON());
    }

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


    public String symptomsToJSON()
    {
        String allSymptoms = "";

        if(getSymptoms() != null)
        {
            for (int i = 0; i < symptoms.length; i++) {
                if (i == 0)
                    allSymptoms += symptoms[i].toJSON();
                else
                    allSymptoms += "," + symptoms[i].toJSON();
            }
        }

        return allSymptoms;
    }

    public String toJSON()
    {
        return "{\"title\": \"" + title + "\", \"state\": "+ state + ", \"symptoms\":[" + symptomsToJSON() + "],\"comment\": \"" + comment +"\", \"isCategory\": " + isCategory + ", \"tag\": \"" + tag.toString()  + "\"}";
    }
}
