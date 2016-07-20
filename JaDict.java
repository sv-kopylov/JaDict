package jadict;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 *
 * @author Сергей
 */
public class JaDict extends Application {

    Dictonary dictonary = null;
    ObservableList<String> wordList;
    Popup popupProgress = new Popup();
    
    ProgressBar progresBarOpening = new ProgressBar();
    BorderPane root;

    @Override
    public void start(Stage primaryStage) {

        String message = "";
        String path;
        Settings settings = Settings.getInstance();
        Label dictName = new Label();
        WebView articleField = new WebView();
//        dictName.setAlignment(Pos.CENTER);
        dictName.setStyle(settings.dTitleStyle);
      

//        получение словаря при загрузке
        path = settings.getLastDictFilePath();
        if (path != null) {
            dictonary = Dictonary.getInstance(path);
            if (dictonary != null) {
                dictName.setText(dictonary.name);
                }
        } else {
            message = "There no avalabel dicts, would you to scan ?";
        }
//        группа кнопок выбора кодировки
        RadioMenuItem utfRadio = new RadioMenuItem("UTF-8");
        RadioMenuItem w1251Radio = new RadioMenuItem("windows-1251");
        ToggleGroup tg = new ToggleGroup();
        utfRadio.setToggleGroup(tg);
        w1251Radio.setToggleGroup(tg);

//        установка начальных значений при запуске программы
        if (settings.encoding.equals(settings.UTF)) {
            utfRadio.setSelected(true);
        } else if (settings.encoding.equals(settings.W1251)) {
            w1251Radio.setSelected(true);
        }

//        обработчик группы кнопок
        EventHandler<ActionEvent> encodHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Settings s = Settings.getInstance();
                RadioMenuItem radItem = (RadioMenuItem) event.getSource();
                String butName = radItem.getText();
                if (butName.equals("UTF-8")) {
                    if (radItem.isSelected()) {
                        s.encoding = s.UTF;
                        s.save();
                        System.out.println("utf done");
                    }
                } else if (butName.equals("windows-1251")) {
                    if (radItem.isSelected()) {
                        s.encoding = s.W1251;
                        s.save();
                        System.out.println("(\"windows-1251 done");
                    }
                }

            }
        };
        utfRadio.setOnAction(encodHandler);
        w1251Radio.setOnAction(encodHandler);

//        окно импорта файла
        FileChooser fileChoser = new FileChooser();
        fileChoser.setInitialDirectory(new File(settings.dictsFolderPath));
        fileChoser.setTitle("Импорт словаря");
        fileChoser.getExtensionFilters().add(new FileChooser.ExtensionFilter("zd", "*.zd"));

//        окно открытия файла
        FileChooser fileOpener = new FileChooser();
        fileOpener.setInitialDirectory(new File(settings.dictsFolderPath));
        fileOpener.setTitle("Открытие словаря");
        fileOpener.getExtensionFilters().add(new FileChooser.ExtensionFilter("d", "*.d"));

// строка и пункы меню
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle(settings.dStyle);
//        menuBar.setPrefSize(150, 40);
        menuBar.setPrefHeight(40);
        Menu fileMenu = new Menu("Словарь");

//   меню обработки импорта файла
        MenuItem importMI = new MenuItem("импорт");
        importMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File f = fileChoser.showOpenDialog(primaryStage);
                if (f != null) {
                    String openedFilePath = f.getAbsolutePath();
                    if (openedFilePath != null) {
                        Dictonary.progress.setValue(0);
                        popupProgress.show(primaryStage);
                        root.disableProperty().set(true);

                        Task importDic = new Task() {
                            @Override
                            protected Object call() throws Exception {
                                dictonary = Dictonary.getInstance(openedFilePath);
                                return null;
                            }

                        };
                        importDic.setOnSucceeded(new EventHandler() {
                            @Override
                            public void handle(Event event) {
                                popupProgress.hide();
                                dictName.setText(dictonary.name);
                                root.disableProperty().set(false);

                            }
                        });
                        ExecutorService eS = Executors.newSingleThreadExecutor();
                        eS.submit(importDic);
                        eS.shutdown();

                        progresBarOpening.progressProperty().bind(dictonary.progress);

                    }
                }

            }
        });
        MenuItem openMI = new MenuItem("открыть");
        openMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File f = fileOpener.showOpenDialog(primaryStage);
                 if (f != null) {
                    String openedFilePath = f.getAbsolutePath();
                    if (openedFilePath != null) {
                      
                        root.disableProperty().set(true);

                        Task openDic = new Task() {
                            @Override
                            protected Object call() throws Exception {
                                dictonary = Dictonary.getInstance(openedFilePath);
                                return null;
                            }

                        };
                        openDic.setOnSucceeded(new EventHandler() {
                            @Override
                            public void handle(Event event) {
                                dictName.setText(dictonary.name);
                                root.disableProperty().set(false);

                            }
                        });
                        ExecutorService eS = Executors.newSingleThreadExecutor();
                        eS.submit(openDic);
                        eS.shutdown();
                    }
                }


            }
        });
        MenuItem exitMI = new MenuItem("выход");
//   меню выхода
        exitMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
        
        
//       меню информации
        MenuItem infoMI = new MenuItem("инфо");
        infoMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (dictonary!=null){
  String s = "Словарь: "+dictonary.name+"<br>"
          + "кодировка: "+ dictonary.encoding+"<br>"
          + "статей: "+  dictonary.dSize;
  articleField.getEngine().loadContent(s);
                }
            }
        });
        
        
        fileMenu.getItems().addAll(openMI,importMI, infoMI,exitMI);
        menuBar.getMenus().add(fileMenu);
        
        
        articleField.setStyle(settings.dStyle);
// меню опций
        Menu optionsMenu = new Menu("Опции");
        Menu encodingMenu = new Menu("кодировка");

        encodingMenu.getItems().addAll(utfRadio, w1251Radio);
        optionsMenu.getItems().addAll(encodingMenu);
        menuBar.getMenus().addAll(optionsMenu);

// создание предлагаемого списка
        ListView<String> promptWordsBox = new ListView<>();
//        promptWordsBox.setMaxWidth(150);
        promptWordsBox.setMinWidth(150);
        promptWordsBox.setPrefHeight(1080);
        promptWordsBox.setStyle(settings.dStyle);
        promptWordsBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount()==2){
                String article = dictonary.getArticle(promptWordsBox.getSelectionModel().getSelectedItem());
                articleField.getEngine().loadContent(article);
                }
            }
        });

//       создание поля ввода текста для поиска
        TextField enterWord = new TextField();
//        enterWord.setMaxWidth(150);
        enterWord.setMinWidth(150);
        enterWord.setStyle(settings.dStyle + " -fx-text-fill:#000000; ");

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
                promptWordsBox.getSelectionModel().clearAndSelect(0);
                String article = dictonary.getArticle(promptWordsBox.getSelectionModel().getSelectedItem());
                articleField.getEngine().loadContent(article);

            }

        }
        );

// layouts
        root = new BorderPane();
        HBox hbox = new HBox();
        VBox vbox = new VBox();
        VBox rightVbox = new VBox();
        rightVbox.setStyle(settings.dStyle);
        rightVbox.setAlignment(Pos.CENTER);

        rightVbox.getChildren().addAll(dictName, articleField);
        
        vbox.getChildren().addAll(enterWord, promptWordsBox);
        hbox.getChildren().addAll(menuBar);
//        rightVbox.getChildren().addAll(dictName, articleField);
        root.setTop(menuBar);
        root.setLeft(vbox);
        root.setCenter(rightVbox);

        Scene scene = new Scene(root, 600, 500);

        primaryStage.setTitle("JaDict");
        primaryStage.setScene(scene);
        primaryStage.show();

//        всплывающее окно с прогрессбаром
        popupProgress.setWidth(400);
        popupProgress.setHeight(70);
        DropShadow effect = new DropShadow();
        effect.setOffsetX(5);
        effect.setOffsetY(5);
        
        
        Label popupName = new Label("Импорт словаря");
        VBox popupVbox = new VBox();
        
//        popupVbox.setStyle("-fx-background-color: " + settings.backColor);
        popupVbox.setStyle(settings.dStyle);
        popupVbox.setEffect(effect);
       

        progresBarOpening.setPrefSize(400, 30);

        popupVbox.getChildren().addAll(popupName, progresBarOpening);

        popupProgress.getContent().addAll(popupVbox);

    } // end of start method

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

} // class JaDict ends

