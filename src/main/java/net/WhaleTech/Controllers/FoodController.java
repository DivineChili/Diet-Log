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
        String[] globalSymptoms = JsonHandler.getGlobalSymptoms(UnsavedJsonSource);

        if(!MainController.searchText.isEmpty())
            nameField.setText(MainController.searchText);

        try {
            //if(MainController.staticTreeView.getSelectionModel().getSelectedItem().getValue().isCategory())
            categories.getSelectionModel().select(MainController.staticTreeView.getSelectionModel().getSelectedItem().getValue().getTitle());
        }catch (NullPointerException e)
        {System.out.println("No categories available or selected!");}
        if(MainController.categoryTitles.isEmpty()) {
            isCategory.setSelected(true);
            isCategory.setDisable(true);
            categories.setDisable(true);
            stateChoice.setDisable(true);
            selectedSymptoms.setDisable(true);
        } // If no categories exist in tree

        for(String symptom : globalSymptoms) {

            CheckBox cb = new CheckBox(symptom);

            // workaround: the color of the labels is wrong (white text on white background), we have to set it explicitly
            cb.setStyle("-fx-text-fill: -fx-text-base-color");

            CustomMenuItem cmi = new CustomMenuItem(cb);
            cmi.setHideOnClick(false);

            selectedSymptoms.getItems().add(cmi);

        } // Add Symptoms
        MenuItem addSymptom = new MenuItem("Legg Til Symptom");
        addSymptom.setDisable(true);
        selectedSymptoms.getItems().add(addSymptom);

        for(String str : MainController.categoryTitles)  {
            categories.getItems().add(str);
        } // Add Categories

        stateChoice.getItems().addAll("Bra", "Ok", "Ikke Spis!", "Utestet"); // Add States

        nameField.textProperty().addListener((e, oldValue, newValue) ->
        {
            // First letter in name
            if(oldValue.isEmpty() && !CharBlacklist.contains(newValue))
                nameField.setText(newValue.toUpperCase());

            if(!oldValue.isEmpty())

                // All letters after space to uppercase
                if(oldValue.charAt(oldValue.length()-1) == ' ' && newValue.charAt(newValue.length()-1) != ' ')
                    nameField.setText(newValue.substring(0,newValue.length()-1) + newValue.substring(newValue.length()-1).toUpperCase());
        });

        isCategory.setOnAction(e -> {
            Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
            if(isCategory.isSelected())
            {
                categories.setDisable(true);
                stateChoice.setDisable(true);
                selectedSymptoms.setDisable(true);
                window.setTitle("Legg til kategori!");
            }else
            {
                categories.setDisable(false);
                stateChoice.setDisable(false);
                selectedSymptoms.setDisable(false);
                window.setTitle("Legg til mat!");
            }
        }); // IsCategory ActionListener

        closeBtn.setOnAction(e -> {
            Node source = (Node)  e.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
        }); // Close-Button ActionListener

        addBtn.setOnAction(e ->
        { ObservableList<Symptoms> selSyms = FXCollections.observableArrayList();

            System.out.println(selectedSymptoms.getItems().size() - 1 + " symptoms detected!");
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
                } // Get Array of selected Symptoms

            Symptoms[] allSyms = new Symptoms[selSyms.size()];
            for (int j = 0; j < selSyms.size(); j++) {
                allSyms[j] = selSyms.get(j);
            }

            if(        // Basic Form Validation
                    !nameField.getText().isEmpty()) {

                if (!isCategory.isSelected())
                    MainController.makeLeaf(
                            MainController.categoryTitles.indexOf(categories.getValue()),
                            new Food(
                                    nameField.getText(),
                                    stateChoice.getSelectionModel().getSelectedIndex(),
                                    allSyms, // Symptoms Here
                                    "",
                                    false,
                                    new Tag(categories.getValue(),nameField.getText())
                                    )
                    );
                else
                    MainController.makeCategory(MainController.root, nameField.getText());

                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
            // Form not accepted handlers. Achieved by elseif statements
            else if (nameField.getText().isEmpty()) {nameField.setText("Dette feltet mangler tekst!");}
        }); // Add Button ActionListener
    }

    public static void display(String FXMLtoLoad) // Display Function
    {
        Stage window = new Stage();

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

        if(MainController.categoryTitles.isEmpty())
            window.setTitle("Legg til kategori!");
        else
            window.setTitle("Legg til mat!");

        window.setScene(new Scene(root));
        window.showAndWait();
    }
}
