package net.WhaleTech.Handlers;

import net.WhaleTech.Food;
import net.WhaleTech.Symptoms;
import net.WhaleTech.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class JsonHandler
{
    /**
     * Utilizes the {@link FileHandler} to get the json from a file.
     *
     * @param file
     *          file to read
     * @return
     *          json string
     */
    public static String getJSON(File file)
    {
        try
        {
            return FileHandler.readFile(file);

        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the {@link Locale} from the users json file.
     * @param source
     *              the {@link JSONObject} which is to be read
     * @return
     *              the {@link Locale} found in the {@param source}
     */
    public static Locale getLocale(String source)
    {
        JSONObject root = new JSONObject(source);
        String[] locale = root.getString("locale").split("_");
        return new Locale(locale[0],locale[1]);
    }

    /**
     * Gets all the saved global symptoms which the user can choose from when creating foods
     * @param source
     *              the {@link JSONObject} which is to be read
     * @return
     *              an array of {@link String}s which contains the names of all the {@link Symptoms}
     */
    public static String[] getGlobalSymptoms(String source)
    {
        JSONObject root = new JSONObject(source);

        JSONArray globalSymptoms = root.getJSONArray("globalSymptoms");
        String[] globalSymptomsStr = new String[globalSymptoms.length()];
        for(int i = 0; i<globalSymptoms.length();i++)
        {
            globalSymptomsStr[i] = globalSymptoms.getString(i);
        }

        return globalSymptomsStr;
    }

    /**
     * Adds a global symptom to the {@link JSONObject}
     * @param source
     *              the {@link JSONObject} which is to be modified
     * @param symptom
     *              the name of the symptom to be added.
     * @return
     *              the new {@link JSONObject} in string format
     */
    public static String addGlobalSymptom(String source, String symptom)
    {
        JSONObject root = new JSONObject(source);
        JSONArray globalSymptoms = root.getJSONArray("globalSymptoms");

        globalSymptoms.put(globalSymptoms.length(), symptom);

        return root.toString();
    }

    /**
     * Gets the title of the category in the desired index
     * @param source
     *              the {@link JSONObject} which is to be read
     * @param catIndex
     *              the index of the category
     * @return
     *              the title of the category
     */
    public static String getCategoryTitle(String source, int catIndex)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        JSONArray category = data.getJSONArray(catIndex);

        return category.getString(0);
    }

    /**
     * Gets the amount of categories stored in {@param source}
     * @param source
     *              the {@link JSONObject} which is to be read
     * @return
     *              the amount of categories
     */
    public static int getCategoryAmount(String source)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        return data.length();
    }

    /**
     * Used to get each individual category from the source
     * @param source
     *              the {@link JSONObject} which is to be read
     * @param index
     *              the category index
     * @return
     *              the category in the form of a {@link JSONArray}
     */
    public static JSONArray getCategory(String source, int index)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        return data.getJSONArray(index);
    }

    /**
     * Adds a category to the source
     * @param source
     *              the {@link JSONObject} which will be modified
     * @param name
     *              the name of the new category
     * @return
     *              the new {@link JSONObject} in String form
     */
    public static String createCategory(String source, String name)
    {
        JSONObject root = new JSONObject(source);
        System.out.println(root.toString());
        JSONArray data = root.getJSONArray("data");
        JSONArray category = new JSONArray();

        category.put(0, name);

        data.put(data.length(), category);
        System.out.println(root.toString());
        return root.toString();
    }

    /**
     * Removes a category from the source
     * @param source
     *              the {@link JSONObject} which is to be modified
     * @param catIndex
     *              the category to remove
     * @return
     *              the modified source
     */
    public static String removeCategory(String source, int catIndex)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        data.remove(catIndex);

        return root.toString();

    }

    /**
     * Removes a food from desired category in source
     * @param source
     *              the {@link JSONObject} which is to be modified
     * @param catIndex
     *              the index of the category to remove from
     * @param food
     *              the index of the food
     * @return
     *              the modified source
     */
    public static String removeFood(String source, int catIndex, int food)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        JSONArray categories = data.getJSONArray(catIndex);

        categories.remove(food);

        return root.toString();
    }

    /**
     * Gets al the symptoms from the source (Not commonly used!)
     * @param source
     *              the {@link JSONObject} which is to be read
     * @param catIndex
     *              the index of the category within the food exists
     * @param foodIndex
     *              the food te get the {@link Symptoms} from
     * @return
     *              the modified source
     */
    public static Symptoms[] getFoodSymptoms(String source, int catIndex, int foodIndex)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        JSONArray category = data.getJSONArray(catIndex);
        JSONObject food = category.getJSONObject(foodIndex);
        JSONArray syms = food.getJSONArray("symptoms");

        Symptoms[] allSymptoms = new Symptoms[syms.length()];

        for(int i = 0; i<syms.length(); i++)
        {
            JSONObject curSym = syms.getJSONObject(i);
            allSymptoms[i] = new Symptoms(curSym.getString("name"),curSym.getString("comment"));
        }

        return allSymptoms;
    }

    /**
     * Used to get all the {@link Symptoms} out of a desired food in JSON format.
     * More commonly used that the one above...
     * @param food
     *          the {@link JSONObject} to read
     * @return
     *          all the {@link Symptoms} which is contained within the {@link JSONObject}
     */
    public static Symptoms[] getFoodSymptoms(JSONObject food)
    {
        JSONArray syms = food.getJSONArray("symptoms");

        Symptoms[] allSymptoms = new Symptoms[syms.length()];

        for(int i = 0; i<syms.length(); i++)
        {
            JSONObject curSym = syms.getJSONObject(i);
            allSymptoms[i] = new Symptoms(curSym.getString("name"),curSym.getString("comment"));
        }

        return allSymptoms;
    }

    /**
     * Gets a {@link Food} object from source, provided
     * you give the category index and the food index
     * @param source
     *              the {@link JSONObject} which is to be read
     * @param catIndex
     *              the index of the category to read
     * @param food
     *              the index of the food to get
     * @return
     *              the {@link Food} object which is fetched from the {@param food} index
     */
    public static Food getFood(String source, int catIndex, int food)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        JSONArray category  = data.getJSONArray(catIndex);
        JSONObject jFood = category.getJSONObject(food);

        return new Food(jFood.getString("title"),jFood.getInt("state"),getFoodSymptoms(jFood),jFood.getString("comment"),jFood.getBoolean("isCategory"), new Tag(jFood.getString("tag")));
    }
}
