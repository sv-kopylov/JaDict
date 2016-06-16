package jadict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
        String result = null;
        if ((key != null)&&(dictMap!=null)) {
            article=dictMap.get(key);
            if (article!=null){
                result=key + "  "+article;
            }
        
        } 
        return result;
    }
    public String[] getList(String key, int width) {
        String[] str = new String[width];
        return str;
    }
    public static Dictonary getInstance (String path){
        Dictonary instance = null;
        if (path!=null&&path.length()>0){
            if (path.endsWith(".d")){
                instance = getSavedInstance(path);
            } else 
            if (path.endsWith(".txt")||path.endsWith(".zd")){
                instance = new Dictonary(path);
            }
        }
        return instance;
    }
// the main variables
    private TreeMap<String, String> dictMap = null;
    private TreeSet<String> dictSet = null;
    private Settings settings = Settings.getInstance();
    private final long MAX = settings.MAX_FILE_SIZE_MB * 1024 * 1024;
    private String filePath = settings.lastDictFilePath;

  // ctor
    private Dictonary() {

    }
  // ctor
    private Dictonary(String path) {
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
    
    private static Dictonary getSavedInstance(String path) {
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
                try (BufferedReader fr = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(dicFile), settings.encoding))){
                    
                    StringBuilder sb = new StringBuilder();
                    String key = null;
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
                    System.out.println("class: Dictonary, method: parse, FileNotFoundException");
                    Logger.getInstance().log("class: Dictonary, method: parse, FileNotFoundException");
                } catch (IOException ex) {
                    System.out.println("class: Dictonary, method: parse, IOexception");
                    Logger.getInstance().log("class: Dictonary, method: parse, IOexception");

                }
        save();
        settings.addSavedDict(name, settings.dictsFolderPath);
        settings.addRunningDict(name, this);
            }  else {
                    System.out.println("class: Dictonary, method: parse, file too big, change settings");
                    Logger.getInstance().log("class: Dictonary, method: parse, file too big, change settings");
            }
        }
        
        return status;
    }

    private void save() {

        File file = new File(settings.dictsFolderPath + name + settings.dFileResolution);
        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file));) {
            oo.writeObject(this);
        } catch (IOException ex) {
            System.out.println("class: Dictonary, method: save, IOException");
            Logger.getInstance().log("class: Dictonary, method: save, IOException");
        }

    }

    private int formatAndParse(String path) {
        int status = Settings.FAIL;
        Settings s = Settings.getInstance();
        String makezdMessage = "nothing";
        String inerName = path.substring(path.lastIndexOf('\\')+1, path.indexOf('.'));

        if (path.endsWith(".zd")) {
            String txtName = inerName + ".txt";
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(s.dictsFolderPath + "makezd.exe", "-u", path, s.dictsFolderPath + txtName);
            try {
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                makezdMessage = builder.toString();
            } catch (IOException ex) {
                System.out.println("class: Dictonary, method: formatAndParse, IOException");
                Logger.getInstance().log("class: Dictonary, method: formatAndParse, IOException");
            }

            if (makezdMessage.contains("Unpack done")) {
                parse(s.dictsFolderPath + txtName);
                File file = new File(s.dictsFolderPath + txtName);
                file.delete();
                status = Settings.SUCCESS;
            } 
            
        }
        return status;
    }

//-d 
    void showSet() throws IOException {
         Settings s = Settings.getInstance();
    File f = new File (s.dictsFolderPath + "log.txt");
    FileWriter fwr = new FileWriter(f);
    
        for (String str : dictSet) {
            fwr.write(str + "\n");
            System.out.println(str);
        }
    }


} // class ends
