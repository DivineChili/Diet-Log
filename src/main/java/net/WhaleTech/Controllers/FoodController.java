package net.WhaleTech.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.WhaleTech.Food;
import net.WhaleTech.Handlers.JsonHandler;
import net.WhaleTech.Symptoms;
import net.WhaleTech.Tag;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.ClassLoader.getSystemClassLoader;
import static net.WhaleTech.Main.UnsavedJsonSource;

/**
 * The food controller which controlls the addFood GUI
 */

/*
 * TODO: 25.09.2016 Internationalize the food gui!
 */

public class FoodController implements Initializable
{
    @FXML
    private MenuButton selectedSymptoms;

    @FXML
    private ChoiceBox<String> stateChoice;

    @FXML
    private Button closeBtn;
    @FXML
    private Button addBtn;

    @FXML
    private TextField nameField;

    @FXML
    private CheckBox isCategory;

    @FXML
    private ChoiceBox<String> categories;

    private String CharBlacklist = "/\\_^";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Get all symptoms from the Unsaved json-source
        String[] globalSymptoms = JsonHandler.getGlobalSymptoms(UnsavedJsonSource);

        // Add the search content to the name bar if the search field in the main controller is not empty
        if(!MainController.searchText.isEmpty())
            nameField.setText(MainController.searchText);

        try {
            // Auto select the category which is selected in the main controller
            categories.getSelectionModel().select(MainController.staticTreeView.getSelectionModel().getSelectedItem().getValue().getTitle());

        }catch (NullPointerException e)
                // If no categories is selected:
        {System.out.println("No categories selected!");}

        // If there dosn't exist any categories
        if(MainController.categoryTitles.isEmpty()) {
            // Disable all the controllers, except from the name field
            isCategory.setSelected(true);
            isCategory.setDisable(true);
            categories.setDisable(true);
            stateChoice.setDisable(true);
            selectedSymptoms.setDisable(true);
        }

        // Add the symptoms to the selection box, and create a custom menu item for each option, so you can
        // check each symptom without closing the dialog.
        for(String symptom : globalSymptoms) {

            CheckBox cb = new CheckBox(symptom);

            // workaround: the color of the labels is wrong (white text on white background), we have to set it explicitly
            cb.setStyle("-fx-text-fill: -fx-text-base-color");

            CustomMenuItem cmi = new CustomMenuItem(cb);
            cmi.setHideOnClick(false);

            // Add the CustomMenuItem to the selection box
            selectedSymptoms.getItems().add(cmi);

        }
        // Add the last item for the selection box. This is a non custom item.
        MenuItem addSymptom = new MenuItem("Legg Til Symptom");
        addSymptom.setDisable(true); // Disable this item, as it is not yet implemented!
        selectedSymptoms.getItems().add(addSymptom);

        // Add the categories to the category selector
        for(String str : MainController.categoryTitles)  {
            categories.getItems().add(str);
        }

        // Add the state options to the state choicebox
        stateChoice.getItems().addAll("Bra", "Ok", "Ikke Spis!", "Utestet"); // Add States

        // Add a special listener on the nameField's property so that we can get manipulate the user input.
        nameField.textProperty().addListener((e, oldValue, newValue) ->
        {
            // First letter in name to uppercase
            if(oldValue.isEmpty() && !CharBlacklist.contains(newValue))
                nameField.setText(newValue.toUpperCase());

            if(!oldValue.isEmpty())

                // All letters after space to uppercase
                if(oldValue.charAt(oldValue.length()-1) == ' ' && newValue.charAt(newValue.length()-1) != ' ')
                    nameField.setText(newValue.substring(0,newValue.length()-1) + newValue.substring(newValue.length()-1).toUpperCase());
        });

        // Change the gui controllers if the isCategory checkbox is changed.
        isCategory.setOnAction(e -> {
            // Get the stage this controller is inside
            Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
            if(isCategory.isSelected())
            {
                // Disable unnecessary controllers if checked.
                categories.setDisable(true);
                stateChoice.setDisable(true);
                selectedSymptoms.setDisable(true);
                // Change stage title to add category.
                window.setTitle("Legg til kategori!");
            }else
            {
                // Enable necessary controllers
                categories.setDisable(false);
                stateChoice.setDisable(false);
                selectedSymptoms.setDisable(false);
                // Change title of parent stage to add food...
                window.setTitle("Legg til mat!");
            }
        });

        // Close-Button ActionListener
        closeBtn.setOnAction(e -> {
            // Closes the stage if close button is pressed
            Node source = (Node)  e.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
        });

        // Adds the food with all selected values from the GUI
        addBtn.setOnAction(e ->
        { ObservableList<Symptoms> selSyms = FXCollections.observableArrayList();
            // Add all the symptoms selected in the select symptoms menu into a single Observable array list.
            // This will also sort them so no spaces is inputed in the table.
            for (int i = 0; i < selectedSymptoms.getItems().size() - 1; i++) {
                    CustomMenuItem curItem;
                    if (selectedSymptoms.getItems().get(i) instanceof CustomMenuItem) {
                        curItem = (CustomMenuItem) selectedSymptoms.getItems().get(i);
                        if (curItem.getContent() instanceof CheckBox) {
                            if (((CheckBox) curItem.getContent()).isSelected()) {
                                Symptoms tempSym = new Symptoms(((CheckBox) curItem.getContent()).getText(), "");
                                System.out.println("Symptom: " + i + ": " + tempSym.toString());
                                selSyms.add(tempSym);
                            }
                        }
                    }
                }

            // Create a pure Symptoms array which contains the symptoms from the observable array
            Symptoms[] allSyms = new Symptoms[selSyms.size()];
            for (int j = 0; j < selSyms.size(); j++) {
                allSyms[j] = selSyms.get(j);
            }

            if(        // Basic Form Validation
                    !nameField.getText().isEmpty()) {

                // If food is not a food category
                if (!isCategory.isSelected())
                    MainController.makeLeaf(
                            MainController.categoryTitles.indexOf(categories.getValue()),
                            new Food(
                                    nameField.getText(),
                                    stateChoice.getSelectionModel().getSelectedIndex(),
                                    allSyms,
                                    "",
                                    false,
                                    new Tag(categories.getValue(),nameField.getText())
                                    )
                    );
                else
                    // If food is a category
                    MainController.makeCategory(MainController.root, nameField.getText());

                // Close the stage
                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
            // Form not accepted handlers. Achieved by elseif statements
            else if (nameField.getText().isEmpty()) {nameField.setText("Dette feltet mangler tekst!");}
        });
    }

    public static void display(String FXMLtoLoad) // Display Function
    /**
     * The reason I use the {@link String} FXMLtoLoad is to be able to change the GUIs
     * FXML and still use the same controller.
     */
    {
        Stage window = new Stage();

        // Set modality so that only this window can be accessed while this window is open.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);

        Parent root = null;
        try
        {
            if(FXMLtoLoad == "food")
                root = FXMLLoader.load(getSystemClassLoader().getResource("assets/foodEdit.fxml"));
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        // Set title of the stage.
        if(MainController.categoryTitles.isEmpty())
            window.setTitle("Legg til kategori!");
        else
            window.setTitle("Legg til mat!");

        window.setScene(new Scene(root));
        window.showAndWait();
    }
}
