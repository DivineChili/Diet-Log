package net.WhaleTech;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
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
import java.util.ResourceBundle;

public class Main extends Application {


    // The datafile where all the specific data is stored
    private static final File userDataFile = new File("./.data/data.json");

    // The variables that will contain the JsonSource from the file,
    // and the JsonSource that will be used to contain temp changes from the user
    public static String JsonSource;
    public static String UnsavedJsonSource;

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
    public void start(Stage primaryStage) throws Exception{

        // First time setup
        // Runs if the data file does not exist
        if(!userDataFile.exists()) {
            System.out.println("Running First-time setup!");
            try {
                // Creates the .data directory if it does not exist.
                if(!new File("./.data/").exists()) new File("./.data/").mkdirs();

                // Initializes a filewriter on the datafile
                FileWriter writer = new FileWriter(userDataFile.getPath());

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
            System.out.println("Skipping first-time run!");
        }

        // Loads the data file and puts it in JsonSource. It then takes a copy of JsonSource to UnsavedJsonSource
        // I do this to compare for changes later on.
        JsonSource = JsonHandler.getJSON(userDataFile);
        UnsavedJsonSource = JsonSource;

        // Loads up the Localization file from the resources.
        bundle = ResourceBundle.getBundle("assets/lang/GUI",JsonHandler.getLocale(JsonSource));

        // Creates a scene "root" which contains the gui.fxml
        Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("assets/gui.fxml"));

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

    /*
     * Method to use when the user sends a shutdown signal to the thread
     */
    public void closeApp ()
    {
        // Sends a confirmation box to check if the user really want to exit.
        if(Confirmation.confirm(Confirmation.ConfirmExitProperty[0], Confirmation.ConfirmExitProperty[1]))
        {
            // Then scans for any unsaved changes.
            if(!UnsavedJsonSource.equals(JsonSource))
            {
                //If there are differences, send confirmation to check if user want to save.
                if(Confirmation.confirm(Confirmation.ConfirmExitProperty[0],Confirmation.ConfirmExitProperty[2]))
                {
                    // Saves the code and exits the program
                    save(UnsavedJsonSource);
                    System.exit(0);
                    Platform.exit();
                }
                else
                {
                    // Just exits without saving
                    System.exit(0);
                    Platform.exit();
                }
            }
            else {
                // Exits without asking for saving
                System.exit(0);
                Platform.exit();
            }
        }
    }

    /**
     * Function used to save the {@link String}{@param unsaved} to the user JSON file
     *
     * @param unsaved
     *          the unsaved Json to be saved to user file.
     */
    public static void save(String unsaved)
    {
        System.out.println("Saving UnsavedJsonSource!");
        try {
            // Deletes old datafile and creates a fresh, empty file
            new File(userDataFile.getPath()).delete();
            FileWriter writer = new FileWriter(userDataFile.getPath());

            // Writes the unsaved text and closes the writer
            writer.write(unsaved);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            System.out.println("Done!");
            // When done, the JsonSource is now equal to the Unsaved text, but the change is only in the file. Not memory
            // So we have to declare the JsonSource again.
            JsonSource = unsaved;
        }
    }
}
