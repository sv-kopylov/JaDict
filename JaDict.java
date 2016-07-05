package jadict;

import java.io.File;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Сергей
 */
public class JaDict extends Application {

    Dictonary dictonary = null;
    ObservableList<String> wordList;

    @Override
    public void start(Stage primaryStage) {

        String message = "";
        String path;
        Settings settings = Settings.getInstance();
        Label dictName = new Label();
//        обработчик выбора файла
        FileChooser fileChoser = new FileChooser();
        fileChoser.setInitialDirectory(new File(settings.dictsFolderPath));
        fileChoser.setTitle("Выбор словаря");
        fileChoser.getExtensionFilters().add(new FileChooser.ExtensionFilter("zd, d", "*.zd", "*.d"));
        
        
        
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openMI = new MenuItem("open");
        openMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
           String openedFilePath = fileChoser.showOpenDialog(primaryStage).getAbsolutePath();
           dictonary = Dictonary.getInstance(openedFilePath);
           dictName.setText(dictonary.name);
           
            }
        });
        MenuItem exitMI = new MenuItem("exit");
        
        fileMenu.getItems().addAll(openMI, exitMI);
        menuBar.getMenus().add(fileMenu);
        
        
        
        WebView articleField = new WebView();


        ListView<String> promptWordsBox = new ListView<>();
        promptWordsBox.setMaxWidth(150);
        promptWordsBox.setMinWidth(150);
        promptWordsBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
        articleField.getEngine().loadContent(dictonary.getArticle(promptWordsBox.getSelectionModel().getSelectedItem()));
            }
        });
        
        promptWordsBox.getSelectionModel().getSelectedItem();
        
        
        
        TextField enterWord = new TextField();
        enterWord.setMaxWidth(150);
        enterWord.setMinWidth(150);

        enterWord.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String entered = enterWord.getText();
                if (dictonary != null) {
                    wordList = FXCollections.observableArrayList(dictonary.getList(entered, 50));
                    promptWordsBox.setItems(wordList);
                }
            }
        });

        enterWord.setOnAction((ActionEvent event) -> {
            String entered = enterWord.getText();
            if (dictonary != null) {
                wordList = FXCollections.observableArrayList(dictonary.getList(entered, 50));
                promptWordsBox.setItems(wordList);
            }

        }
        );

//-d
        settings.scanUnconvertedDicts(settings.dictsFolderPath);

        path = settings.getLastDictFilePath();
        if (path != null) {
            dictonary = Dictonary.getInstance(path);
        } else {
            message = "There no avalabel dicts, would you to scan ?";
        }


        HBox root = new HBox();
        VBox vbox = new VBox();
        
        vbox.getChildren().add(dictName);
        vbox.getChildren().add(enterWord);

        vbox.getChildren().add(promptWordsBox);
        
        root.getChildren().add(menuBar);
        root.getChildren().add(vbox);
        root.getChildren().add(articleField);
        

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("JaDict");
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
    public void stop() {
        Settings.getInstance().save();

    }

}
