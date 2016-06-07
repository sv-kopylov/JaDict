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

/**
 *
 * @author Сергей
 */
public class Dictonary implements Serializable {

  

    private TreeMap<String, String> dictMap = null;
    private TreeSet<String> dictSet = null;
    Settings settings = Settings.getInstance();
    private final long MAX = settings.MAX_FILE_SIZE_MB * 1024 * 1024;
    String filePath = settings.defaultFilePath;

    String load(String filePath) {
        String message = "fail";
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

                    message = "Success";

                } catch (FileNotFoundException ex) {
                    message = "FileNotFound";
                } catch (IOException ex) {
                    message = "FileCantBeRed";
                }
            }
        }
        return message;
    }

    public String getArticle(String key) {
        String article = null;
        if (key != null) {
            article = dictMap.get(key);
        }
        return article;
    }

//-d 
    void showSet() {
        for (String s : dictSet) {
            System.out.println(s);
        }
    }

    void saveDic() throws IOException {
        File file = new File("C:\\Projects\\JaDict\\dict.bin");
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file));
        oo.writeObject(this);

    }

    static Dictonary loadDic() throws IOException, ClassNotFoundException {
        File file = new File("C:\\Projects\\JaDict\\dict.bin");
        ObjectInputStream oi = new ObjectInputStream(new FileInputStream(file));
        Dictonary dic;// = new Dictonary();
        dic = (Dictonary) oi.readObject();
        return dic;

    }
}
