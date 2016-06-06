package jadict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Сергей
 */
public class Dictonary {

    private HashMap<String, String> dictMap = null;
    private TreeSet<String> dictSet = null;
    Settings settings = Settings.getInstance();
    private long MAX = settings.MAX_FILE_SIZE_MB * 1024 * 1024;
    String filePath = settings.defaultFilePath;

    String load(String filePath) {
        String message = "fail";
        char ch;
        File dicFile = new File(filePath);

        if (dicFile.exists() && dicFile.canRead() && (dicFile.length() > 0)) {
            if ((dicFile.length() < MAX)) {
                try (FileReader fr = new FileReader(dicFile);) {
                    StringBuilder sb = new StringBuilder();
                    String key = null;
                    String value = null;
                    dictSet = new TreeSet<>();

                    ch = (char) fr.read();
                    while (ch != -1) {
                        if ((ch != ' ') && (ch != '\r')) {
                            sb.append(ch);
                        } else {
                            ch = (char) fr.read();
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
                        ch = (char) fr.read();
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
    void showSet(){
        for (String s:dictSet){
            System.out.println(s);
        }
    }
}
