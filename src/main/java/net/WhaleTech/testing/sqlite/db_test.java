package net.WhaleTech.testing.sqlite;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class db_test {
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement stat = conn.createStatement();

        stat.executeUpdate("DROP TABLE IF EXISTS categories;");
        stat.executeUpdate("CREATE TABLE categories(ID INTEGER PRIMARY KEY AUTOINCREMENT, label string NOT NULL, state , symptoms string, comment string, categoryindex , tag string);");

        PreparedStatement prep = conn.prepareStatement(
                "INSERT INTO categories VALUES (?, ?, ?, ?, ?, ?, ?);");

        prep.setString(2, "Test1");
        prep.addBatch();
        prep.setString(2, "Test2");
        prep.addBatch();
        prep.setString(2, "Test3");
        prep.addBatch();
        prep.setString(2, "Test4");
        prep.addBatch();
        /*
        stat.executeUpdate("drop table if exists people;");
        stat.executeUpdate("create table people (id, name, occupation);");
        PreparedStatement prep = conn.prepareStatement(
                "insert into people values (?, ?, ?);");

        prep.setInt(1, 0);
        prep.setString(2, "Gandhi");
        prep.setString(3, "politics");
        prep.addBatch();
        prep.setInt(1, 1);
        prep.setString(2, "Turing");
        prep.setString(3, "computers");
        prep.addBatch();
        prep.setInt(1, 2);
        prep.setString(2, "Wittgenstein");
        prep.setString(3, "smartypants");
        prep.addBatch();
        */


        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        ResultSet rs = stat.executeQuery("SELECT * FROM categories;");
        while (rs.next()) {
            System.out.println("id : " + rs.getInt("ID"));
            System.out.println("label = " + rs.getString("label"));
        }
        rs.close();
        conn.close();
    }
}