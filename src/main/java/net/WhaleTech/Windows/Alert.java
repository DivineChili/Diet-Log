package net.WhaleTech.Windows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.WhaleTech.Main;

public class Alert
{

    public static final String[] CommingSoonResourceProperty = new String[]{Main.bundle.getString("gui.Dia.alert.func.CommingSoon.title"),Main.bundle.getString("gui.Dia.alert.func.CommingSoon.text")};
    public static final String[] SaveResourceProperty = new String[]{Main.bundle.getString("gui.Dia.alert.func.Saved.title"),Main.bundle.getString("gui.Dia.alert.func.Saved.text")};


    public static void display(String title, String msg)
    {
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
    }
}
