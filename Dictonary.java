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
            article = key+"  "+dictMap.get(key);
        }
        return article;
    }
    public String[] getList(String key, int width){
    String[] str = new String[width];
    return str;
    }

  
    public final String dictName=null;
    private TreeMap<String, String> dictMap = null;
    private TreeSet<String> dictSet = null;
    Settings settings = Settings.getInstance();
    private final long MAX = settings.MAX_FILE_SIZE_MB * 1024 * 1024;
    String filePath = settings.defaultFilePath;
    
    static int load(String path) {
        int status = Settings.FAIL;
        if (path.endsWith(".txt")) {
            status = parse(path);
            save();
            status = Settings.SUCCESS;
        } else if (path.endsWith(".zd")) {
            formatAndParse(d, path);
            status = Settings.SUCCESS;;
        } else if (path.endsWith(".d")) {
            File file = new File(path);
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(file));) {
                d = (Dictonary) oi.readObject();
                status = Settings.SUCCESS;;
            } catch (IOException ex) {
                Logger.getInstance().log("class: SaverLoader, method: load(Dictonary,String), cannot find file");
            } catch (ClassNotFoundException ex) {
                Logger.getInstance().log("class: SaverLoader, method: load(Dictonary,String), cannot find class");
            }
            //-d
            System.out.println("loaded");

        }
        return status;
    }
    
    

    int load(String filePath) {
        int status = Settings.FAIL;
        char ch;
        int nextCh;
        File dicFile = new File(filePath);
        if (dicFile.exists() && dicFile.canRead() && (dicFile.length() > 0)) {
            if ((dicFile.length() < MAX)) {
                try (FileReader fr = new FileReader(dicFile);) {

                    StringBuilder sb = new StringBuilder();
                    String key = null;
                    String value = null;
                    dictSet = new TreeSet<>();
                    dictMap = new TreeMap<>();
//-d
                    int i = 0;

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

                    status=Settings.SUCCESS;

                } catch (FileNotFoundException ex) {
//-d                    
                    System.out.println("FileNotFound");
                    Logger.getInstance().log("FileNotFound");
                } catch (IOException ex) {
                    System.out.println("FileCantBeRead");
                    Logger.getInstance().log("FileCantBeRead");
                          
                }
            }
        }
        return status;
    }

 

//-d 
    void showSet() {
        for (String s : dictSet) {
            System.out.println(s);
        }
    }

   private void save() {
        File file = new File(settings.dictsFolderPath + name + settings.RESOLUTION);
        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file));){
           oo.writeObject(this);  
        } catch (IOException ex) {
 //-d 
            System.out.println("class: Dictonary, method: save, IOException");
            Logger.getInstance().log("class: Dictonary, method: save, IOException");
        }
       
//-d
        System.out.println("saved"+file.getAbsolutePath());

    }

  
}
