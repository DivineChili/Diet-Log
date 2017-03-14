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
}
