package jadict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author Сергей
 */
public class Settings implements Serializable {
// Interface

    public static Settings getInstance() {
        if (unicInstance == null) {
            unicInstance = getSavedInstance();
            if (unicInstance == null) {
                unicInstance = new Settings();
            }
        }
        return unicInstance;
    }

//    statics
    private static Settings unicInstance;

    static final String dictsFolderPath = "dicts\\";
    private static final String DICT_SETTINGS_FILE = dictsFolderPath+"settings.s";

    final String UTF = "UTF-8";
    final String W1251 = "windows-1251";

    public static int SUCCESS = 1;
    public static int FAIL = 2;

    
//    apearence
    String dFont = "TimesNewRoman;";
    String dStyle = "-fx-base:silver;"
//            + "-fx-text-fill:#433d8b; "
            + " -fx-border-width:1pt;"
            + "-fx-border-color:gray;"
            + "-fx-background-color:#f5f5f5;"          
            + "-fx-font: 16pt "

            + dFont;
    
    String dTitleStyle ="-fx-base:silver;"
//            + "-fx-text-fill:#433d8b; "
            + " -fx-font: 20pt "
            + dFont;
    String backColor = "#cccccc";

    

    
//   settings availabel to user 
    String encoding =  W1251;
    int MAX_FILE_SIZE_MB = 100;

    
    String dFileResolution = ".d";
    String dictLoggPath = "dicts\\log.txt";
    
    private String lastDictFilePath = null;
    public String getLastDictFilePath() {
        
        return lastDictFilePath;
        
    }
    public void setLastDictFilePath(String lastDictFilePath) {
        this.lastDictFilePath = lastDictFilePath;
        save();
    }

//   private ctor
    private Settings() { // ctor
        save();
    }

    void save() {
        File file = new File(DICT_SETTINGS_FILE);
        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file));) {
            oo.writeObject(this);
            oo.close();
        } catch (IOException ex) {
            System.out.println("class: Settings, method: save, IOException");
            Logger.getInstance().log("class: Settings, method: save, IOException");
        }
    }

    private static Settings getSavedInstance() {
        Settings instance = null;
        File file = new File(DICT_SETTINGS_FILE);
        if (file.exists() && file.length() > 0 && file.canRead()) {
            
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(file));) {
                instance = (Settings) oi.readObject();
                oi.close();
            } catch (IOException ex) {
// -d
                System.out.println("class: Settings, method: getSavedInstance(), IOException");
                Logger.getInstance().log("class: Settings, method: getSavedInstance(), IOException");
            } catch (ClassNotFoundException ex) {
// -d
                System.out.println("class: Settings, method: getSavedInstance(), ClassNotFoundException");
                Logger.getInstance().log("class: Settings, method: getSavedInstance(), ClassNotFoundException");
            }

        }
        return instance;
    }

     public  ArrayList<String> scanAvalabelDicts() {
        ArrayList<String> dFiles = new ArrayList<>();
        String dName;
        File file = new File(dictsFolderPath);
        File[] fileList = file.listFiles();
        for (File f: fileList) {
            dName = f.getAbsolutePath();
            if (dName.endsWith(".d")) {
                dFiles.add(dName);
            }
        }
        return dFiles;

    }


} // class ends
