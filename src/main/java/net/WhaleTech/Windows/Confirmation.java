package net.WhaleTech.Windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.WhaleTech.Main;

public class Confirmation
{
    static boolean answer;

    // Declare some common Confirmation text properties.
    public static final String[] ConfirmExitProperty = new String[]{
            Main.bundle.getString("gui.Dia.confirm.func.exit.title"),
            Main.bundle.getString("gui.Dia.confirm.func.exit.text1"),
            Main.bundle.getString("gui.Dia.confirm.func.exit.text2")
    };

    /**
     * Used to get a confirmation from the user. Displays a message on the screen
     * @param title
     *              the title of the confirmation box
     * @param msg
     *              the message of the confirmation box
     * @return
     *              the boolean value of the confirmation
     */
    public static boolean confirm(String title, String msg)
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label(msg);
        label.setFont(new Font("Verdana", 14));
        label.setTextAlignment(TextAlignment.CENTER);

        // Create 2 buttons

        Button btnYes = new Button(Main.bundle.getString("gui.Dia.confirm.control.btn1"));
        Button btnNo = new Button(Main.bundle.getString("gui.Dia.confirm.control.btn2"));

        btnYes.setOnAction(e -> {   // "Yes"-button event
            answer = true;
            window.close();
        });

        btnNo.setOnAction(e -> {    // "No"-button event
            answer = false;
            window.close();
        });

        VBox layout = new VBox(20);
        HBox buttons = new HBox(10);

        buttons.getChildren().addAll(btnYes,btnNo);
        buttons.setAlignment(Pos.CENTER);

        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(label,buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);

        btnNo.requestFocus();

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
