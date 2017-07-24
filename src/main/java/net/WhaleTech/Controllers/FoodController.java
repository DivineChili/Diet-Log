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
import net.WhaleTech.Symptoms;
import net.WhaleTech.Tag;
import net.WhaleTech.Windows.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.ClassLoader.getSystemClassLoader;
import static net.WhaleTech.Controllers.MainController.*;
import static net.WhaleTech.Main.bundle;
import static net.WhaleTech.Main.db_controller;

/**
 * The food controller which controlls the addFood GUI
 */
 /*
    TODO [IMPORTANCE 2]; Internationalize Add-Food GUI
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


    public static boolean bIsChanging;
    private String CharBlacklist = "/\\_^";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO Get all global symptoms from db_controller
        String[] globalSymptoms = {"1","2","3"};

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

            if(bIsChanging && sSelectedFoodItem.getValue().serializeSymptoms().contains(symptom)) {
                cb.setSelected(true);
                System.out.println("Selecting " + symptom);
            }

            // workaround: the color of the labels is wrong (white text on white background), we have to set it explicitly
            cb.setStyle("-fx-text-fill: -fx-text-base-color");

            CustomMenuItem cmi = new CustomMenuItem(cb);
            cmi.setHideOnClick(false);

            // Add the CustomMenuItem to the selection box
            selectedSymptoms.getItems().add(cmi);

        }
        // Add the last item for the selection box. This is a non-custom item.
        MenuItem addSymptom = new MenuItem("Legg Til Symptom"); // TODO Localize
        addSymptom.setDisable(true); // Disable this item, as it is not yet implemented!
        selectedSymptoms.getItems().add(addSymptom);

        // Add the categories to the category selector
        for(String str : MainController.categoryTitles)  {
            categories.getItems().add(str);
        }

        // Add the state options to the state choicebox
        stateChoice.getItems().addAll(bundle.getString("gui.Food.state.good"),
                bundle.getString("gui.Food.state.ok"),
                bundle.getString("gui.Food.state.bad"),
                bundle.getString("gui.Food.state.untested")
        ); // Add States

        stateChoice.getSelectionModel().selectLast();
        categories.getSelectionModel().selectFirst();

        // If Changing a food and not adding
        if(!bIsChanging) {
            // Add the search content to the name bar if the search field in the main controller is not empty
            if (!MainController.searchText.isEmpty())
                nameField.setText(MainController.searchText);

            try {
                // Auto select the category which is selected in the main controller
                if(!MainController.sSelectedFoodItem.getValue().isCategory()) // Select the parent of the selected Leaf
                    categories.getSelectionModel().select(MainController.sSelectedFoodItem.getParent().getValue().getTitle());
                else
                    categories.getSelectionModel().select(MainController.sSelectedFoodItem.getValue().getTitle());

            } catch (NullPointerException e) {
                System.out.println("Selected item is not a category, or there is no selected item!");
            } // No categories selected

        }else
        { // Set all the options to in the GUI
            nameField.setText(MainController.sSelectedFoodItem.getValue().getTitle());
            stateChoice.getSelectionModel().select( sSelectedFoodItem.getParent().getValue().getTitle());

            categories.getSelectionModel().select(sSelectedFoodItem.getParent().getValue().getTitle());

            //Debug
            System.out.println("NameField = " + MainController.sSelectedFoodItem.getValue().getTitle());
            System.out.println("State = " + sSelectedFoodItem.getValue().getState());
            System.out.println("Symptoms = " + sSelectedFoodItem.getValue().serializeSymptoms());
            System.out.println("Category = " + sSelectedFoodItem.getParent().getValue().getTitle());
        }

        // Add a special listener on the nameField's property so that we can get manipulate the user input.
        nameField.textProperty().addListener((e, oldValue, newValue) ->
        {
            // First letter in name to uppercase
            if(oldValue.isEmpty() && !CharBlacklist.contains(newValue))
                nameField.setText(newValue.toUpperCase());

            if(!oldValue.isEmpty()) {

                // All letters after space to uppercase
                if (oldValue.charAt(oldValue.length() - 1) == ' ' && newValue.charAt(newValue.length() - 1) != ' ' && newValue.length() > oldValue.length())
                    nameField.setText(newValue.substring(0, newValue.length() - 1) + newValue.substring(newValue.length() - 1).toUpperCase());

                if (newValue.contains("$") ||
                        newValue.contains("&") ||
                        newValue.contains(":")){
                    if(!oldValue.isEmpty())
                        nameField.setText(oldValue);
                    else
                        nameField.setText("");
                }
            }
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
        {
            System.out.println("Adding food!");
            ObservableList<Symptoms> selSyms = FXCollections.observableArrayList();
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

            if(selSyms.size() == 0) {allSyms = null;}

            if(     // Basic form Validation
                    !nameField.getText().isEmpty()
                            && !foodIndex.contains(nameField.getText())
                            && !bIsChanging
                    ){

                // If food is not a food category
                if (!isCategory.isSelected())
                    try {
                        Food made_food = new Food(
                                nameField.getText(),
                                stateChoice.getSelectionModel().getSelectedIndex(),
                                allSyms,
                                "",
                                false,

                                // If you make 2 foods with equal name, one of them will contain a number on the end to signify that it's not the same.
                                // However, if you then make more of the same name, the 'duplicates' will all be deleted if one is deleted.
                                new Tag(categories.getValue(), (foodIndex.contains(nameField.getText())) ? nameField.getText() + "1" : nameField.getText())
                        );
                        MainController.makeLeaf(MainController.categoryTitles.indexOf(categories.getValue()), made_food);
                        db_controller.appendFood(MainController.categoryTitles.indexOf(categories.getValue()), made_food);
                    }catch  (Exception e1) {e1.printStackTrace();}
                else
                    // If food is a category
                    try{
                        MainController.makeCategory(MainController.root, nameField.getText());
                        db_controller.createCategory(nameField.getText(), MainController.categoryTitles.size()-1, tagRegistry.get(tagRegistry.size()-1).toString());
                } catch (Exception e1) {e1.printStackTrace();}

                // Close the stage
                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
            // Form not accepted handlers. Achieved by elseif statements
            // TODO Internationalize

            else if (nameField.getText().isEmpty()) {
                CustomAlert.display("ERROR!", "Mangler tekst i navnfeltet!");
            }
        });
    }

    public static void display(String FXMLtoLoad, boolean bChangingFood) // Display Function
    /**
     * I use the {@link String}, FXMLtoLoad, to be able to change the GUIs
     * FXML and still use the same controller.
     */
    {
        Stage window = new Stage();

        // Set modality so that only this window can be accessed while this window is open. Not any other windows opened by the app.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);

        bIsChanging = bChangingFood;

        Parent root = null;
        try
        {
            if(FXMLtoLoad.equals("food"))
                root = FXMLLoader.load(getSystemClassLoader().getResource("assets/foodEdit.fxml"));
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        // Set title of the stage.
        if(MainController.categoryTitles.isEmpty())
            window.setTitle("Legg til kategori!");
            // TODO Internationalize
        else
            window.setTitle("Legg til mat!");
            // TODO Internationalize

        window.setScene(new Scene(root));
        window.showAndWait();
    }
}
