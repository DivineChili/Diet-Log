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

    public static Locale getLocale(String source)
    {
        JSONObject root = new JSONObject(source);
        String[] locale = root.getString("locale").split("_");
        return new Locale(locale[0],locale[1]);
    }

    public static String getFoodTitle(String source, int catIndex, int foodIndex)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        JSONArray categories = data.getJSONArray(catIndex);
        JSONObject food1 = categories.getJSONObject(foodIndex);
        return food1.getString("title");
    }

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

    public static String addGlobalSymptom(String source, String symptom)
    {
        JSONObject root = new JSONObject(source);
        JSONArray globalSymptoms = root.getJSONArray("globalSymptoms");

        globalSymptoms.put(globalSymptoms.length(), symptom);

        return root.toString();
    }

    public static String getCategoryTitle(String source, int catIndex)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        JSONArray category = data.getJSONArray(catIndex);

        return category.getString(0);
    }

    public static int getCategoryAmount(String source)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        return data.length();
    }

    public static JSONArray getCategory(String source, int index)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        return data.getJSONArray(index);
    }

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

    public static String removeCategory(String source, int catIndex)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        data.remove(catIndex);

        return root.toString();

    }

    public static String removeFood(String source, int catIndex, int food)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        JSONArray categories = data.getJSONArray(catIndex);

        categories.remove(food);

        return root.toString();
    }

    public static String appendFood(String source, int catIndex, Food food)
    {
        JSONObject root = new JSONObject(source);

        JSONArray data = root.getJSONArray("data");
        JSONArray categoriy = data.getJSONArray(catIndex);

        JSONObject jFood = new JSONObject(food.toJSON());
        categoriy.put(categoriy.length(), jFood);

        return root.toString();
    }

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

    public static Food getFood(String source, int catIndex, int food)
    {
        JSONObject root = new JSONObject(source);
        JSONArray data = root.getJSONArray("data");
        JSONArray category  = data.getJSONArray(catIndex);
        JSONObject jFood = category.getJSONObject(food);

        return new Food(jFood.getString("title"),jFood.getInt("state"),getFoodSymptoms(jFood),jFood.getString("comment"),jFood.getBoolean("isCategory"), new Tag(jFood.getString("tag")));
    }

    public static Object getFoodKey(String source, String key, int category, int food)
    {
        JSONObject root = new JSONObject();
        JSONArray data = root.getJSONArray("data");
        JSONArray categories = data.getJSONArray(category);
        JSONObject foodObj = categories.getJSONObject(food);
        return foodObj.get(key);
    }

    public static void createJsonTest()
    {
        JSONObject root = new JSONObject("{}");
        JSONArray data = new JSONArray();
        JSONArray categories = new JSONArray();
        JSONObject food1 = new JSONObject();
        JSONArray symptoms = new JSONArray();


        JSONArray globalSymptoms = new JSONArray();

        globalSymptoms.put(0, "Energi");
        globalSymptoms.put(1, "Søvn");
        globalSymptoms.put(2, "Vekt");
        globalSymptoms.put(3, "Ødem");
        globalSymptoms.put(4, "Hud - Utslett");
        globalSymptoms.put(5, "Hud - Kløe");
        globalSymptoms.put(6, "Mage - Smerter");
        globalSymptoms.put(7, "Mage - Oppblåst");
        globalSymptoms.put(8, "Avføring");
        globalSymptoms.put(9, "Hodepine");

        symptoms.put(0, new JSONObject("{\"name\": \"Energi\", \"comment\": \"Wow!\"}"));

        root.put("data", data);
        root.put("globalSymptoms", globalSymptoms);

        data.put(0,categories);

        categories.put(0,"Korn");
        categories.put(1, food1);

        food1.put("title","Hvete");
        food1.put("state",2);
        food1.put("symptoms",symptoms);

        food1.put("comment","Ikke spis dette!");
        food1.put("isCategory", false);

        try
        {
            FileWriter writer = new FileWriter("./.data/test.json");
            writer.write(root.toString());
            writer.flush();
            writer.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
