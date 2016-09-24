package net.WhaleTech.Controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import net.WhaleTech.*;
import net.WhaleTech.Handlers.JsonHandler;
import net.WhaleTech.Windows.Alert;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static net.WhaleTech.Main.JsonSource;
import static net.WhaleTech.Main.UnsavedJsonSource;

public class MainController implements Initializable
{

    @FXML
    private TreeView<Food> treeView;

    @FXML
    private TextField searchField;

    @FXML
    private TextArea comField;

    @FXML
    private Label food;

    @FXML
    private SplitMenuButton modify;
    @FXML
    private MenuItem modSave;
    @FXML
    private MenuItem modDelete;

    @FXML
    private ImageView statIcon;

    @FXML
    private TableColumn<Symptoms, String> colSym;
    @FXML
    private TableColumn<Symptoms, String> colCom;
    @FXML
    private TableView<Symptoms> table;

    @FXML
    private Button btnAdd;

    @FXML
    private Label symText;

    @FXML
    private TextField symCom;

    @FXML
    private Button symChange;
    @FXML
    private Button symDel;


    private static ArrayList<FilterableTreeItem<Food>> categories = new ArrayList<>();
    public static ArrayList<String> categoryTitles = new ArrayList<>();

    public static ArrayList<String> foodIndex = new ArrayList<>();
    public static ArrayList<Tag> tagRegistry = new ArrayList<>();

    public static String searchText;
    public static TreeView<Food> staticTreeView;

    public static FilterableTreeItem<Food> root = new FilterableTreeItem<>(new Food("", true, new Tag("root")));

    private static Food selectedFood;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        System.out.println("Localizing...");
        LocalizeGUI(Main.bundle);

        System.out.println("Creating Root!");

        root = getTreeModel(JsonSource);
        treeView.setCellFactory(param -> new FoodFormatCell());

        colSym.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCom.setCellValueFactory(new PropertyValueFactory<>("comment"));

        root.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            if (searchField.getText() == null || searchField.getText().isEmpty())
                return null;
            return TreeItemPredicate.create(actor -> actor.toString().toLowerCase().contains(searchField.getText().toLowerCase()));
        }, searchField.textProperty())); // Root Predicate Property

        treeView.getSelectionModel().selectedItemProperty().addListener ((c, oldValue, newValue) -> {
            if(newValue != null && !newValue.isLeaf())
            {
                //Platform.runLater(() -> treeView.getSelectionModel().clearSelection());
                selectedFood = null;
            }  // When branch is selected

            if(newValue != null)
            {
                selectedFood = newValue.getValue();

                if(!selectedFood.isCategory())
                {
                    food.setFont(new Font(20D));
                    food.setText(selectedFood.getTitle());
                    comField.setText(selectedFood.getComment());
                    if(selectedFood.getState() < 4 && selectedFood.getState() > -1)
                        statIcon.setImage(new Image(ClassLoader.getSystemClassLoader().getResource("assets/status/" + selectedFood.getState() +".png").toString()));
                    else
                        statIcon.setImage(new Image(ClassLoader.getSystemClassLoader().getResource("assets/status/no_icon.png").toString()));

                    table.setItems(getSymptoms(selectedFood));
                }   // If leaf is food
                else
                {
                    food.setFont(new Font(14D));
                    food.setText("Kategori kan ikke vises!");
                    comField.setText(null);
                    statIcon.setImage(null);
                    table.setItems(null);
                }   // If leaf is category

            } // When leaf is selected

        }); // TreeView Action Listener

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                symText.setText(newSelection.getName());
                symCom.setText(newSelection.getComment());
                symChange.setDisable(false);
                symDel.setDisable(false);
            }
            else
            {
                symText.setText("[Ingen Symptom Valgt!]");
                symCom.setText(null);
                symChange.setDisable(true);
                symDel.setDisable(true);
            }
        }); // Table View Action Listener

        btnAdd.setOnAction(e -> {searchText = searchField.getText(); staticTreeView = treeView; FoodController.display("food");});

        symChange.setOnAction(e-> Alert.display(Alert.CommingSoonResourceProperty[0],Alert.CommingSoonResourceProperty[1]));
        symDel.setOnAction(e -> Alert.display(Alert.CommingSoonResourceProperty[0],Alert.CommingSoonResourceProperty[1]));

        modify.setOnAction(e-> Alert.display(Alert.CommingSoonResourceProperty[0],Alert.CommingSoonResourceProperty[1]));
        modSave.setOnAction(e-> {Main.save(UnsavedJsonSource); Alert.display(Alert.SaveResourceProperty[0],Alert.SaveResourceProperty[1]);});
        modDelete.setOnAction(e-> { /*
            TreeItem<Food> child = treeView.getSelectionModel().getSelectedItem();
            if(!child.getValue().isCategory())
            {
                UnsavedJsonSource = JsonHandler.removeFood(UnsavedJsonSource, categoryTitles.indexOf((child.getParent().getValue()).getTitle()),foodIndex.indexOf(child.getValue().getTitle()));
                child.getParent().getChildren().remove(foodIndex.indexOf(child.getValue().getTitle()));
                foodIndex.remove(foodIndex.indexOf(child.getValue().getTitle()));
            }
            else
            {
                UnsavedJsonSource = JsonHandler.removeCategory(UnsavedJsonSource, categoryTitles.indexOf(child.getValue().getTitle()));
                child.getParent().getChildren().remove(categoryTitles.indexOf(child.getValue().getTitle()));
                categoryTitles.remove(categoryTitles.indexOf(child.getValue().getTitle()));
            }*/ Alert.display(Alert.CommingSoonResourceProperty[0],Alert.CommingSoonResourceProperty[1]);});

        treeView.setRoot(root);
        treeView.setShowRoot(false);

    }

    /*
     *
     *  Global Methods
     *
     */

    public void LocalizeGUI(ResourceBundle bundle)
    {
        searchField.setPromptText(bundle.getString("gui.Main.searchField.prompt"));
        btnAdd.setText(bundle.getString("gui.Main.btnAdd"));
        modify.setText(bundle.getString("gui.Main.modifyMenu"));
        modSave.setText(bundle.getString("gui.Main.modSave"));
        modDelete.setText(bundle.getString("gui.Main.modDelete"));

        colSym.setText(bundle.getString("gui.Main.table.columnSymptoms"));
        colCom.setText(bundle.getString("gui.Main.table.columnComment"));
        symText.setText(bundle.getString("gui.Main.table.symptomText.default"));
        symCom.setPromptText(bundle.getString("gui.Main.table.symptomComment.prompt"));
        symChange.setText(bundle.getString("gui.Main.table.symptomChange"));
        symDel.setText(bundle.getString("gui.Main.table.symptomDelete"));

        comField.setPromptText(bundle.getString("gui.Main.foodComment.prompt"));

    }


    /*
            Tree Methods
      */

    private FilterableTreeItem<Food> getTreeModel(String jSource) {
        root = new FilterableTreeItem<>(new Food("",true, new Tag("root")));

        // Create tree here

        makeTree(jSource, root);
        return root;
    }

    public static void makeCategory(FilterableTreeItem<Food> parent, String title)
    {
        tagRegistry.add(new Tag(title));
        FilterableTreeItem<Food> category = new FilterableTreeItem<>(new Food(title,true,tagRegistry.get(tagRegistry.size()-1)));
        UnsavedJsonSource = JsonHandler.createCategory(UnsavedJsonSource, title);
        category.setExpanded(true);
        categories.add(category);
        categoryTitles.add(title);
        parent.getInternalChildren().add(category);
    }

    public static void makeLeaf(int catIndex, Food food)
    {
        FilterableTreeItem<Food> leaf = new FilterableTreeItem<>(food);
        categories.get(catIndex).getInternalChildren().add(leaf);
        foodIndex.add(food.getTitle());

        System.out.println("New Food: " + food.toJSON());
        UnsavedJsonSource = JsonHandler.appendFood(UnsavedJsonSource, catIndex, food);
    }

    private void makeTree(String jSource, FilterableTreeItem<Food> root)
    {
        System.out.println("Category Iterations: " + JsonHandler.getCategoryAmount(jSource));
        for(int i = 0; i< JsonHandler.getCategoryAmount(jSource); i++)
        {
            System.out.println("MakeTree@i: " + i);

            // Loop through all categories
            System.out.println("MakeTree@i#category: " + JsonHandler.getCategoryTitle(jSource,i));
            makeCategory(root,JsonHandler.getCategoryTitle(jSource,i));
            // Make a category each iteration

            for(int j = 1; j < JsonHandler.getCategory(jSource,i).length(); j++)
            {
                System.out.println("MakeTree@j: " + j);
                /*
                    Every time a category is created,
                    make all of it's children
                 */

                makeLeaf(i, JsonHandler.getFood(jSource,i,j));
                System.out.println("MakeTree@j#" + JsonHandler.getFood(jSource,i,j) + ": " + JsonHandler.getFood(jSource,i,j).toString());
            }
        }
    }

    /*
            Table Methods
      */

    private ObservableList<Symptoms> getSymptoms(Food food)
    {
        ObservableList<Symptoms> symptoms = FXCollections.observableArrayList();
        try{
            for(Symptoms sym : food.getSymptoms())
            {
                symptoms.add(sym);
            }
        }catch (NullPointerException e)
        {e.printStackTrace();}
        return symptoms;
    }


    private class FoodFormatCell extends TreeCell<Food>
    {
        public FoodFormatCell() {    }

        @Override protected void updateItem(Food item, boolean empty)
        {
            super.updateItem(item, empty);
            setItem(item);
            if (empty && isSelected()) {
                setText(null);
                updateSelected(false);
            }
            if(empty)
                setText(null);
            if(!empty && item != null)
            {
                setText(item.getTitle());
            }
        }
    }

}


