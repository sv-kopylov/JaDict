package jadict;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Сергей
 */
public class Settings implements Serializable {
//    here all common system variables

    public static int SUCCESS = 1;
    public static int FAIL = 2;
    
    private static final String UTF = "UTF-8";
    private static final String W1251 = "windows-1251";
    String encoding = UTF;
   
    int MAX_FILE_SIZE_MB = 100;

   
    String dFileResolution = ".d";
    String sFileResolution = ".s";

    String lastDictFilePath = "C:\\Projects\\JaDict\\1.txt";
    String dictsFolderPath = "dicts\\";
    String dictLoggPath = "log\\log.txt";
    String dictSettingsPath = "settings\\";
    String makezdLocation = "makezd\\makezd.exe";

    // Saved Dicts list
    private HashMap<String, String> savedDicts = new HashMap<>();

    public void addSavedDict(String key, String path) {
        savedDicts.put(key, path);
    }

    public String getSavedDictPath(String key) {
        return savedDicts.get(key);
    }

    public void delFromSavedDictList(String key) {
        savedDicts.remove(key);
    }

//     Running Dicts List
    transient private HashMap<String, Dictonary> runningDicts = new HashMap<>();

    public void addRunningDict(String key, Dictonary dict) {
        runningDicts.put(key, dict);
    }

    public Dictonary getRunningDict(String key) {
        return runningDicts.get(key);
    }

    public void delRunningDictList(String key) {
        runningDicts.remove(key);
    }

//   here singleton mechanism
    private Settings() { // ctor
    }

    static Settings getInstance() {
        if (unicInstance == null) {
            unicInstance = new Settings();
        }
        return unicInstance;
    }
    private static Settings unicInstance;

}
