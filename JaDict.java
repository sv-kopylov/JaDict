package jadict;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Сергей
 */
public class JaDict extends Application {

    @Override
    public void start(Stage primaryStage) {
        ObservableList<String> wordList; 
        
        String message = "";
        String path;
        Settings settings = Settings.getInstance();
        Dictonary dictonary = null;
//-d
        settings.scanUnconvertedDicts(settings.dictsFolderPath);
        
        path = settings.getLastDictFilePath();
        if (path!= null) {
            dictonary = Dictonary.getInstance(path);
        } else {
            message = "There no avalabel dicts, would you to scan ?";
        }

        ChoiceBox<String> promptWordsBox = new ChoiceBox<>();
        promptWordsBox.setMinSize(200, 100);
        if (dictonary != null) {
            wordList = FXCollections.observableArrayList(dictonary.getList("в",10));
            promptWordsBox.setItems(wordList);
        }
        Label dictName = new Label(dictonary.name);
        
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });

        VBox root = new VBox();
         root.getChildren().add(dictName);
        root.getChildren().add(promptWordsBox);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello List!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop(){
        Settings.getInstance().save();
        
    }

}
