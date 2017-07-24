package net.WhaleTech.Windows;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import net.WhaleTech.Main;

public class CustomAlert
{
    // Declare some default text properties
    public static final String[] CommingSoonResourceProperty = new String[]{Main.bundle.getString("gui.Dia.alert.func.CommingSoon.title"),Main.bundle.getString("gui.Dia.alert.func.CommingSoon.text")};
    public static final String[] SaveResourceProperty = new String[]{Main.bundle.getString("gui.Dia.alert.func.Saved.title"),Main.bundle.getString("gui.Dia.alert.func.Saved.text")};


    /**
     * Displays the alert box
     * @param title
     *              the title of the alert
     * @param msg
     *              the alert message
     */
    public static void display(String title, String msg)
    {

        Alert box = new Alert(Alert.AlertType.WARNING);
        box.setTitle(title);
        box.setHeaderText(msg);

        box.initOwner(Main.primWindow);
        box.initModality(Modality.APPLICATION_MODAL);

        box.showAndWait();
        /*
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label(msg);
        label.setFont(new Font("Verdana", 14));

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> window.close());

        VBox layout = new VBox(20);

        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
        */
    }
}
