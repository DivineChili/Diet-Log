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
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static net.WhaleTech.Main.JsonSource;
import static net.WhaleTech.Main.db_controller;

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

    @FXML
    private Menu OptionMenu;


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
        // Localize the GUI
        LocalizeGUI(Main.bundle);

        System.out.println("Creating Root!");

        // Cretes a new root from the DB.
        try{
            root = getTreeModel();
        }catch (Exception e) {
            e.printStackTrace();
        }

        // Set CellFactory class for treeview to be the FoodFormatCell() class.
        treeView.setCellFactory(param -> new FoodFormatCell());

        // Sets the cellfactory for the tableview columns
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
                selectedFood = null;
            }  // When branch is selected

            // When leaf is selected
            if(newValue != null)
            {
                selectedFood = newValue.getValue();

                // If leaf is food
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
                }
                else
                // If leaf is category
                {
                    food.setFont(new Font(14D));
                    food.setText("Kategori kan ikke vises!");
                    comField.setText(null);
                    statIcon.setImage(null);
                    table.setItems(null);
                }

            }

        }); // TreeView Action Listener

        // Table View Action Listener
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
        });


        /*
            Some simple action listeners for the different controllers in the GUI
         */

        btnAdd.setOnAction(e -> {searchText = searchField.getText(); staticTreeView = treeView; FoodController.display("food");});

        symChange.setOnAction(e-> Alert.display(Alert.CommingSoonResourceProperty[0],Alert.CommingSoonResourceProperty[1]));
        symDel.setOnAction(e -> Alert.display(Alert.CommingSoonResourceProperty[0],Alert.CommingSoonResourceProperty[1]));

        modify.setOnAction(e-> Alert.display(Alert.CommingSoonResourceProperty[0],Alert.CommingSoonResourceProperty[1]));
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

    /**
     * Gets all the text from the loaded property file and sets the text and prompt text
     * for all the objects in the main GUI
     * @param bundle
     *          the {@link ResourceBundle} which contains all the localizations
     */
    public void LocalizeGUI(ResourceBundle bundle)
    {
        searchField.setPromptText(bundle.getString("gui.Main.searchField.prompt"));
        btnAdd.setText(bundle.getString("gui.Main.btnAdd"));
        modify.setText(bundle.getString("gui.Main.modifyMenu"));
        modDelete.setText(bundle.getString("gui.Main.modDelete"));

        colSym.setText(bundle.getString("gui.Main.table.columnSymptoms"));
        colCom.setText(bundle.getString("gui.Main.table.columnComment"));
        symText.setText(bundle.getString("gui.Main.table.symptomText.default"));
        symCom.setPromptText(bundle.getString("gui.Main.table.symptomComment.prompt"));
        symChange.setText(bundle.getString("gui.Main.table.symptomChange"));
        symDel.setText(bundle.getString("gui.Main.table.symptomDelete"));

        comField.setPromptText(bundle.getString("gui.Main.foodComment.prompt"));

        OptionMenu.setText(bundle.getString("gui.Main.menu"));

    }


    /*
            Tree Methods
      */

    /**
     *  Creates the basic tree model
     *
     * @return
     *          the {@link TreeItem} root which contains all the sub categories and foods
     */
    private FilterableTreeItem<Food> getTreeModel() throws Exception {
        root = new FilterableTreeItem<>(new Food("",true, new Tag("root")));

        // Create tree here

        makeTree(root);
        return root;
    }

    /**
     * Creates a food category.
     * @param parent
     *          which {@link FilterableTreeItem} is the parent
     * @param title
     *          the name of the category
     */
    public static void makeCategory(FilterableTreeItem<Food> parent, String title) throws Exception
    {
        tagRegistry.add(new Tag(title));
        FilterableTreeItem<Food> category = new FilterableTreeItem<>(new Food(title,true,tagRegistry.get(tagRegistry.size()-1)));
        category.setExpanded(true);
        categories.add(category);
        categoryTitles.add(title);
        parent.getInternalChildren().add(category);
    }



    /**
     * Populates a {@link FilterableTreeItem} category.
     * @param catIndex
     *          the index of the category to populate
     * @param food
     *          the {@link Food} which is to be created in the corresponding slot
     */
    public static void makeLeaf(int catIndex, Food food) throws Exception
    {
        FilterableTreeItem<Food> leaf = new FilterableTreeItem<>(food);
        categories.get(catIndex).getInternalChildren().add(leaf);
        foodIndex.add(food.getTitle());
    }

    /**
     * Populates the root with categories, sub-categories and foods.
     *
     * @param root
     *      the {@link FilterableTreeItem} root to put the hole tree inside.
     */
    private void makeTree(FilterableTreeItem<Food> root) throws Exception
    {

        System.out.println("Category Iterations: " + db_controller.getCategoryAmount());
        int[] IDlist = db_controller.getCategoryIDList();
        for(int i : IDlist)
        {
            System.out.println("MakeTree@i: " + i);

            // Loop through all categories
            System.out.println("MakeTree@i#category: " + db_controller.getCategoryTitle(i));
            makeCategory(root,db_controller.getCategoryTitle(i));
            // Make a category each iteration

            for(Food food : db_controller.getAllFoodsFromCategory(i)) // Loops through all the food in a category with index i
            {
                System.out.println("MakeTree@j: " + food);

                //    Every time a category is created,
                //    make all of it's children

                makeLeaf(i, food);
            }
        }

    }

    /*
            Table Methods
      */

    /**
     * Creates an {@link ObservableList} of symptoms from each {@link Food}
     * @param food
     *          the food to get the symptoms from
     * @return
     *          an {@link ObservableList} of all the symptoms in the {@link Food} object
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
        {}
        return symptoms;
    }

    /**
     * Class which renders each cell.
     */
    private class FoodFormatCell extends TreeCell<Food>
    {
        // Empty constructor.
        public FoodFormatCell() {    }

        /**
         * Called each time a {@link TreeCell} needs to be updated.
         * @param item
         *          the value of the {@link TreeCell}
         * @param empty
         *          is the {@link TreeCell} empty or not
         */
        @Override protected void updateItem(Food item, boolean empty)
        {
            super.updateItem(item, empty);
            // Sets item of TreeCell
            setItem(item);

            if (empty && isSelected()) {
                setText(null);
                updateSelected(false);
            }

            if(empty)
                setText(null);

            // If cell is not empty and is not null, then render it with the title of the item.
            if(!empty && item != null)
            {
                setText(item.getTitle());
            }
        }
    }
}


