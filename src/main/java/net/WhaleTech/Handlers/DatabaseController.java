package net.WhaleTech.Handlers;

import net.WhaleTech.Controllers.MainController;
import net.WhaleTech.Food;
import net.WhaleTech.Symptoms;
import net.WhaleTech.Tag;

import java.sql.*;
import java.util.ArrayList;

import static net.WhaleTech.Controllers.MainController.categoryTitles;

public class DatabaseController
{
    private Statement stat;
    private Connection conn;

    public DatabaseController() throws Exception{
        Class.forName("org.sqlite.JDBC");
        this.conn = DriverManager.getConnection("jdbc:sqlite:food.db");
        this.stat = conn.createStatement();
    }

    public void rebuildDatabase() throws Exception{
        // Delete the current tables
        this.stat.executeUpdate("DROP TABLE IF EXISTS categories;");
        this.stat.executeUpdate("DROP TABLE IF EXISTS symptoms");
        this.stat.executeUpdate("DROP TABLE IF EXISTS food");

        // Re-create deleted tables
        this.stat.executeUpdate("CREATE TABLE categories(ID INTEGER PRIMARY KEY AUTOINCREMENT, label string NOT NULL, indexvalue INTEGER NOT NULL,tag string NOT NULL);");
        this.stat.executeUpdate("CREATE TABLE symptoms(ID INTEGER PRIMARY KEY AUTOINCREMENT, name string NOT NULL);");
        this.stat.executeUpdate("CREATE TABLE food(ID INTEGER PRIMARY KEY AUTOINCREMENT,name string NOT NULL, state INTEGER NOT NULL, symptoms string, comment string, categoryindex INTEGER, tag string, FOREIGN KEY(categoryindex) REFERENCES categories(indexvalue));");
        this.stat.executeBatch();
    }

    /*
        This function removes all the items from the ResultSet provided, and needs to be removed!
     */
    public static int getNumberOfFetchedRows(ResultSet rs) throws Exception{ // TODO !!IMPORTANCE HIGH!! Remove this function!
        int count = 0;
        while(rs.next()){ ++count; }
        return count;
    }

    public String[] getGlobalSymptoms() throws Exception{
        ResultSet result = this.stat.executeQuery("SELECT name AS Symptom FROM symptoms");
        String[] symptoms = new String[getNumberOfFetchedRows(result)]; // TODO Remove getNumberOfFetchedRows!
        int count = 0;
        while(result.next()){
            symptoms[count] = result.getString("Symptom");
            count++;
        }
        result.close();
        return symptoms;
    }

    public void addGlobalSymptom(String symptom) throws Exception {
        this.stat.executeUpdate("INSERT INTO symptoms (name) VALUES (\""+symptom+"\");");
    }

    public String getCategoryTitle(int categoryIndex) throws Exception {
        ResultSet rs = this.stat.executeQuery("SELECT label FROM categories WHERE indexvalue="+categoryIndex+";");
        String name;
        if(rs.next())
            name = rs.getString("label");
        else
            name = "ERROR!";
        return name;
    }

    public int getCategoryAmount() throws Exception{
        ResultSet result =  this.stat.executeQuery("SELECT COUNT(*) AS NumberOfCategories FROM categories;");
        int amount = result.getInt("NumberOfCategories");
        result.close();
        return amount;
    }

    public int[] getCategoryIDList() throws Exception {
        ResultSet rs = this.stat.executeQuery("SELECT indexvalue FROM categories ORDER BY indexvalue ASC;");
        //System.out.println("Fetched: " + getNumberOfFetchedRows(rs));

        ArrayList<Integer> list = new ArrayList<>();

        while (rs.next()) {
            list.add(rs.getInt("indexvalue"));
        }
        System.out.println("List Length: " + list.size() + "");
        rs.close();
        int[] new_list = new int[list.size()];
        for(int i = 0; i < list.size(); i++) { new_list[i] = list.get(i);}
        return new_list;
    }

    public void createCategory(String name, int index, String tag) throws Exception {
        this.stat.executeUpdate("INSERT INTO categories (label, indexvalue, tag) VALUES (\""+name+"\",\""+index+"\",\""+tag+"\");");
    }

    public void removeCategory(int catIndex, boolean bDeleteFoods) throws Exception {
        if(!bDeleteFoods){
            this.stat.executeUpdate("DELETE FROM categories WHERE ID="+catIndex+";");
            if(MainController.categoryTitles.size() < 1) MainController.makeCategory(MainController.root,"Uncategorized");
            this.stat.executeUpdate("");
        }
        else {
            this.stat.executeUpdate("DELETE FROM food WHERE categoryindex="+catIndex+";");
            this.stat.executeUpdate("DELETE FROM categories WHERE ID="+catIndex+";");
        }

    }

    public int getCategorySize(int catIndex) throws Exception {
        ResultSet rs = this.stat.executeQuery("SELECT COUNT(categoryindex) AS AmountOfFood FROM food WHERE categoryindex="+catIndex+";");
        int size = rs.getInt("AmountOfFood");
        rs.close();
        return size;
    }

    public void removeFood(String foodTag) throws Exception {
        this.stat.executeUpdate("DELETE FROM food WHERE tag=\""+foodTag+"\";");
    }

    public void appendFood(int catIndex, Food food) throws Exception {
        PreparedStatement prep = conn.prepareStatement("INSERT INTO food VALUES (?, ?, ?, ?, ?, ?, ?);");
        prep.setString(2, food.getTitle());
        prep.setInt(3, food.getState());
        prep.setString(4, food.serializeSymptoms());
        prep.setString(5, food.getComment());
        prep.setInt(6,catIndex);
        prep.setString(7, food.toString());
        prep.addBatch();

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);
    }

    public void updateFood(int catIndex, Food food) throws Exception {
        this.stat.executeUpdate("UPDATE food SET " +
                "name = " + food.getTitle() + ", " +
                "state = " + food.getState() + ", " +
                "symptoms = " + food.serializeSymptoms() + ", " +
                "comment = " + food.getComment() + ", " +
                "categoryindex = " + catIndex + ", " +
                "tag = " + food.toString() + ", "
        );
    }

    public Symptoms[] getFoodSymptoms(int catIndex, int foodIndex) throws Exception {
        ResultSet rs = this.stat.executeQuery("SELECT symptoms FROM food WHERE category-index="+catIndex+",ID="+foodIndex+";");
        String serializedSymptoms = rs.getString("symptoms");
        rs.close();
        String[] serializedSymptom = serializedSymptoms.split("|");
        int size = serializedSymptom.length;
        Symptoms[] allSymptoms = new Symptoms[size];


        for(int i = 0; i<size; i++)
        {

            allSymptoms[i] = new Symptoms(serializedSymptom[i]);
        }

        return allSymptoms;
    }

    public String getSerializedFoodSymptoms(int catIndex, int foodIndex) throws Exception {
        ResultSet rs = this.stat.executeQuery("SELECT symptoms FROM food WHERE category-index="+catIndex+",ID="+foodIndex+";");
        String serializedSymptoms = rs.getString("symptoms");
        rs.close();

        return serializedSymptoms;
    }

    public Food[] getAllFoodsFromCategory(int catIndex) throws Exception {
        ResultSet rs = this.stat.executeQuery("SELECT * FROM food WHERE categoryindex="+catIndex+";");
        ArrayList<Food> allFood = new ArrayList<>();
        while(rs.next()) {
            allFood.add(new Food(
                    rs.getString("name"),
                    rs.getInt("state"),
                    Food.deserializeSymptomArray(rs.getString("symptoms")),
                    rs.getString("comment"),
                    false,
                    new Tag(rs.getString("tag"))
            ));
        }
        Food[] new_list = new Food[allFood.size()];
        for(int i = 0; i < allFood.size(); i++) {
            new_list[i] = allFood.get(i);
        }
        //rs.close();
        return new_list;
    }
}
