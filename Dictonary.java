package jadict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

public class Dictonary implements Serializable {

    // interface  
    public String name;

    public String getArticle(String key) {
        String article = null;
        if (key != null) {
            article = key + "  " + dictMap.get(key)!=null?dictMap.get(key):"Article no found";
        }
        return article;
    }

    public String[] getList(String key, int width) {
        String[] str = new String[width];
        return str;
    }

// the main variables
    private TreeMap<String, String> dictMap = null;
    private TreeSet<String> dictSet = null;
    Settings settings = Settings.getInstance();
    private final long MAX = settings.MAX_FILE_SIZE_MB * 1024 * 1024;
    String filePath = settings.lastDictFilePath;

    // ctor
    public Dictonary() {

    }
    // ctor

//-d 
    void showSet() {
        for (String s : dictSet) {
            System.out.println(s);
        }
    }
    public Dictonary(String path) {
        if (path.endsWith(".txt")) {
            parse(path);
        } else if (path.endsWith(".zd")) {
            formatAndParse(path);
        } else {
            //-d                    
            System.out.println("ctor: incorrect file format");
            Logger.getInstance().log("ctor: incorrect file format");

        }
    }

    public static Dictonary getSavedInstance(String path) {
        Dictonary instance = null;
        if (path.endsWith(".d")) {
            File file = new File(path);
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(file));) {
                instance = (Dictonary) oi.readObject();
            } catch (IOException ex) {
// -d
                System.out.println("class: Dictonary, method: load(Dictonary,String), cannot find file");
                Logger.getInstance().log("class: SaverLoader, method: load(Dictonary,String), cannot find file");
            } catch (ClassNotFoundException ex) {
// -d
                System.out.println("class: Dictonary, method: load(Dictonary,String), cannot find class");
                Logger.getInstance().log("class: Dictonary, method: load(Dictonary,String), cannot find class");
            }

        } else {
            //-d                    
            System.out.println("getInstance: incorrect file format");
            Logger.getInstance().log("getInstance: incorrect file format");

        }

        return instance;
    }

   
    private int parse(String path) {
        int status = Settings.FAIL;
        char ch;
        int nextCh;
        File dicFile = new File(path);
        name = dicFile.getName();
        name = name.substring(0, name.lastIndexOf('.'));

        if (dicFile.exists() && dicFile.canRead() && (dicFile.length() > 0)) {
            if ((dicFile.length() < MAX)) {
                try (FileReader fr = new FileReader(dicFile);) {
                    StringBuilder sb = new StringBuilder();
                    String key=null;
                    String value;
                    dictSet = new TreeSet<>();
                    dictMap = new TreeMap<>();

                    nextCh = fr.read();
                    ch = (char) nextCh;
                    while (nextCh != -1) {
                        if ((ch != ' ') && (ch != '\r')) {
                            sb.append(ch);

                        } else {
                            nextCh = fr.read();
                            ch = (char) nextCh;
                            if (ch == ' ') {
                                key = sb.toString();
                                dictSet.add(key);

                                sb.delete(0, sb.length());
                            } else if (ch == '\n') {
                                value = sb.toString();
                                dictMap.put(key, value);
                                sb.delete(0, sb.length());
                            } else {
                                sb.append(' ');
                                sb.append(ch);

                            }
                        }
                        nextCh = fr.read();
                        ch = (char) nextCh;

                    }

                    status = Settings.SUCCESS;

                } catch (FileNotFoundException ex) {
//-d                    
                    System.out.println("FileNotFound");
                    Logger.getInstance().log("FileNotFound");
                } catch (IOException ex) {
//-d
                    System.out.println("FileCantBeRead");
                    Logger.getInstance().log("FileCantBeRead");

                }
            }
        }
        save();
        settings.addSavedDict(name, settings.dictsFolderPath);
        settings.addRunningDict(name, this);
        return status;
    }

    private void save() {

        File file = new File(settings.dictsFolderPath + name + settings.dFileResolution);
        System.out.println(file.getAbsolutePath());
        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file));) {
            oo.writeObject(this);
        } catch (IOException ex) {
            //-d 
            System.out.println("class: Dictonary, method: save, IOException");
            Logger.getInstance().log("class: Dictonary, method: save, IOException");
        }



    }

    private int formatAndParse(String path) {
        int status = Settings.FAIL;
//        ProcessBuilder pb = new ProcessBuilder(makezdLocation," -u ", path, );
        return status;

    }

    
}
