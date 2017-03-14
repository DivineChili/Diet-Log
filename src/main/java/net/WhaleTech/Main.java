package net.WhaleTech;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.WhaleTech.Handlers.DatabaseController;
import net.WhaleTech.Handlers.FileHandler;
import net.WhaleTech.Handlers.JsonHandler;
import net.WhaleTech.Windows.Confirmation;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.util.ResourceBundle;

public class Main extends Application {


    // The datafile where all the specific data is stored
    private static final File user_settings = new File("settings.json");

    // The variables that will contain the JsonSource from the file,
    // and the JsonSource that will be used to contain temp changes from the user
    public static String JsonSource;
    public static DatabaseController db_controller;

    // The main window
    public static Stage primWindow;

    // ResourceBundle which hold Internationalization and localization file
    public static ResourceBundle bundle;


    /**
     * Start method which initializes the Main window
     * @param primaryStage
     *          the main window
     *
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Connect to Database
        db_controller = new DatabaseController();

        // First time setup
        // Runs if the data file does not exist

        if(!user_settings.exists()) {

            System.out.println("Running First-time setup!");
            db_controller.rebuildDatabase();

            try {
                // Creates the .data directory if it does not exist.
                if(!new File("./.data/").exists()) new File("./.data/").mkdirs();

                // Initializes a filewriter on the datafile
                FileWriter writer = new FileWriter(user_settings.getPath());

                // Simply writes the basic JSON file and closes the writer
                writer.write(FileHandler.readFile("assets/baseJson.json"));
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Exits the program if the filewriter does not work.
                System.exit(1);
            }

            Thread.sleep(750);
            System.out.println("First-time setup complete!");
        }
        else
        {
            System.out.println("Data file detected!");
            System.out.println("Skipping first-time setup!");
        }

        // Loads the data file and puts it in JsonSource. It then takes a copy of JsonSource to UnsavedJsonSource
        // I do this to compare for changes later on.
        JsonSource = JsonHandler.getJSON(user_settings);
        //UnsavedJsonSource = FileHandler.readFile("assets/baseJson.json");

        // TODO Remove db_rebuild when done debugging!
        //db_controller.rebuildDatabase();

        // Loads up the Localization file from the resources.
        bundle = ResourceBundle.getBundle("assets/lang/GUI",JsonHandler.getLocale(JsonSource));

        // Creates a scene "root" which contains the gui.fxml
        Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("assets/gui.fxml"));

        primWindow = primaryStage;

        // Sets the title
        primaryStage.setTitle(bundle.getString("title"));

        // The method to use when you close the window
        primaryStage.setOnCloseRequest(e->{
            // Consumes the event so the window only can be closed remotly
            e.consume();

            // Runs the closeApp() method
            closeApp();
        });

        // Sets the scene to root
        Scene scene = new Scene(root);

        // Applies the scene to the root and shows the window
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // Main start method. Just launches the program...
    public static void main(String[] args)
    {
        launch(args);
    }

    public static void setLoading(boolean loading) {

    }

    /*
     * Method to use when the user sends a shutdown signal to the thread
     */
    public void closeApp ()
    {
        // Sends a confirmation box to check if the user really want to exit.
        if(Confirmation.confirm(Confirmation.ConfirmExitProperty[0], Confirmation.ConfirmExitProperty[1]))
        {
            // Exits
            System.exit(0);
            Platform.exit();
        }
    }
}
