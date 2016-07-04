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

    private static final String DICT_SETTINGS_FILE = "settings\\settings.s";

    private static final String UTF = "UTF-8";
    private static final String W1251 = "windows-1251";

    public static int SUCCESS = 1;
    public static int FAIL = 2;

    public String getLastDictFilePath() {
        
        return lastDictFilePath;
        
    }

    public void setLastDictFilePath(String lastDictFilePath) {
        this.lastDictFilePath = lastDictFilePath;
        save();
    }

    
//   settings availabel to user 
    String encoding =  W1251;
    int MAX_FILE_SIZE_MB = 100;

    String dictsFolderPath = "dicts\\";
    String dFileResolution = ".d";
    String dictLoggPath = "log\\log.txt";
    private String lastDictFilePath = null;

// Saved Dicts list
    private HashMap<String, String> savedDicts = new HashMap<>();

    public void addSavedDict(String name, String path) {
        savedDicts.put(name, path);
        save();
    }

    public String getSavedDictPath(String key) {
        return savedDicts.get(key);
    }

    public void delFromSavedDictList(String key) {
        savedDicts.remove(key);
        save();
    }

//  Running Dicts List
    transient private HashMap<String, Dictonary> runningDicts = new HashMap<>();

    public void addRunningDict(String key, Dictonary dict) {
        if (key != null && dict != null) {
            runningDicts.put(key, dict);
        }
    }

    public Dictonary getRunningDict(String key) {
        return runningDicts.get(key);
    }

    public void delRunningDictList(String key) {
        runningDicts.remove(key);
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
                instance.runningDicts = new HashMap<>();
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

    public void scanUnconvertedDicts(String path) {
        TreeSet<String> zdFiles = new TreeSet<>();
        TreeSet<String> dFiles = new TreeSet<>();
        String dName;

        File file = new File(path);
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            dName = f.getName();
            if (dName.endsWith(".zd")) {
                zdFiles.add(dName.substring(0, dName.indexOf(".")));
            } else if (dName.endsWith(".d")) {
                dFiles.add(dName.substring(0, dName.indexOf(".")));
            }
        }

        for (String s : dFiles) {
            zdFiles.remove(s);
        }
        for (String s : zdFiles) {
            Dictonary.getInstance(path + s + ".zd");
        }

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
