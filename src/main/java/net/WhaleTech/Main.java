package net.WhaleTech;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
import com.sun.org.apache.regexp.internal.RE;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.WhaleTech.Handlers.FileHandler;
import net.WhaleTech.Handlers.JsonHandler;
import net.WhaleTech.Windows.Confirmation;

import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    private static final File userDataFile = new File("./.data/data.json");
    public static String JsonSource;
    public static String UnsavedJsonSource;
    private static boolean firstTime = false;
    public static ResourceBundle bundle;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // First time setup
        if(!userDataFile.exists()) {
            System.out.println("Running First-time setup!");
            try {
                if(!new File("./.data/").exists()) new File("./.data/").mkdirs();
                firstTime = true;
                FileWriter writer = new FileWriter(userDataFile.getPath());

                writer.write(FileHandler.readFile("assets/baseJson.json"));
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            Thread.sleep(2000);

            System.out.println("First-time setup complete!");
        }
        else
        {
            System.out.println("Data file detected!");
            System.out.println("Skipping first-time run!");
        }

        JsonSource = JsonHandler.getJSON(userDataFile);
        UnsavedJsonSource = FileHandler.readFile("assets/baseJson.json");

        bundle = ResourceBundle.getBundle("assets/lang/GUI",JsonHandler.getLocale(JsonSource));

        Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("assets/gui.fxml"));
        primaryStage.setTitle(bundle.getString("title"));
        primaryStage.setOnCloseRequest(e->{
            e.consume();
            closeApp();
        });

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }

    public void closeApp ()
    {
        if(Confirmation.confirm(Confirmation.ConfirmExitProperty[0], Confirmation.ConfirmExitProperty[1]))
        {
            if(!UnsavedJsonSource.equals(JsonSource))
            {
                System.out.println(JsonSource);
                System.out.println(UnsavedJsonSource);
                if(Confirmation.confirm(Confirmation.ConfirmExitProperty[0],Confirmation.ConfirmExitProperty[2]))
                {
                    save(UnsavedJsonSource);
                    System.exit(0);
                    Platform.exit();
                }
                else
                {
                    System.exit(0);
                    Platform.exit();
                }
            }
            else {
                System.exit(0);
                Platform.exit();
            }
        }
    }

    public static void save(String unsaved)
    {
        System.out.println("Saving UnsavedJsonSource!");
        try {
            new File(userDataFile.getPath()).delete();
            FileWriter writer = new FileWriter(userDataFile.getPath());

            writer.write(unsaved);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            System.out.println("Done!");
            JsonSource = unsaved;
        }
    }
}
