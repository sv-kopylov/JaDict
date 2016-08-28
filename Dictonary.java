package jadict;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Dictonary implements Serializable {
    
    private HashMap<ArrayList<Byte>, Long> dictMap;
    public String name;
    public String encoding = "deprecated feature, see programm settings";
    public int dSize;
    public static DoubleProperty progress = new SimpleDoubleProperty(0);
    
    transient private RandomAccessFile randomAccessFile;
    transient private String dictFilePath;
    transient private Searcher searcher;


    private Dictonary() {
    }
    private Dictonary(String path) {
        if (path.endsWith(".txt")) {
            parse(path);
            
        } else if (path.endsWith(".zd")) {
            formatAndParse(path);
        } else {
//-d                    
            System.out.println("ctor: incorrect file format");

        }
    }
  
    Set<ArrayList<Byte>> getKeySet (){
        if (dictMap!=null){
            return dictMap.keySet();
        }
        return null;
    }

    private String getDictFilePath() {
        return dictFilePath;
    }
    private void setDictFilePath(String dictFilePath) {
        this.dictFilePath = dictFilePath;
    }
    private int parse(String path) {
         
       
        int graduation = 10;
        double progressVal = 0.1d;
        long treshold = 0l;
        progress.setValue(progressVal);

        File dicFile = new File(path);
        treshold = dicFile.length()/graduation;
        
        name = dicFile.getName();
        name = name.substring(0, name.lastIndexOf('.'));

        if (dicFile.exists() && dicFile.canRead() && (dicFile.length() > 0)) {

            try {
                StringReaderCounter brc = new StringReaderCounter(dicFile);

                ArrayList<Byte> key;
                long filePointerPosition;
                dictMap = new HashMap<>();

                filePointerPosition = brc.getFilePointer();
                key = brc.readLN();
                key = headUntil(key, "  ");
                while (key != null) {

                    if (!key.isEmpty()) {
                        dictMap.put(key, filePointerPosition);
                    }
                    filePointerPosition = brc.getFilePointer();
                    key = brc.readLN();
                    key = headUntil(key, "  ");
                    
                    if (filePointerPosition>treshold){
                        treshold+= dicFile.length()/graduation;
                        progressVal= 0.1 + 0.5*((double)((double)treshold/(double)dicFile.length()));
                        progress.setValue(progressVal);
                    }
                   
                    

                }
                
                brc.close();
//                System.err.println("parse donne");
                searcher = new Searcher(this);
                dSize = dictMap.size();
                save(path);
            } catch (FileNotFoundException ex) {
                System.out.println("class: Dictonary, method: parse, FileNotFoundException");

            } catch (IOException ex) {
                System.out.println("class: Dictonary, method: parse, IOexception");

            } 

        }

        return 0;
    }

    private ArrayList<Byte> headUntil(ArrayList<Byte> source, String substring) throws UnsupportedEncodingException {

        ArrayList<Byte> list = null;
        boolean substringFound = false;
        int firstIndex = 0;
        byte[] substringBytes = substring.getBytes(Settings.getInstance().encoding);
        if (source != null) {
            list = new ArrayList<>();
            for (int i = 0; i < source.size(); i++) {
                for (int j = 0; j < substringBytes.length; j++) {
                    if (source.get(i + j) != substringBytes[j]) {
                        break;
                    } else if (j+1 == substringBytes.length){
                    substringFound = true;
                    }
                }
                if (substringFound) {
                    break;
                }
                list.add(source.get(i));

            }

        }

        return list;
    }
    private void save(String textSourceFilePath) {
        String destFileName = textSourceFilePath.substring(0, textSourceFilePath.lastIndexOf(".")) + ".d";
        setDictFilePath(destFileName);
        File textFile = new File(textSourceFilePath);
        File innerDictFile = new File(destFileName);
        
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(textFile));) {
            DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(innerDictFile));

            long textFileLen = textFile.length();
            dataOutput.writeLong(textFileLen);
            progress.setValue(0.7);
//            System.out.println("saved long: " + textFileLen);

            dataOutput.close();
//            System.out.println("number saved");

            BufferedOutputStream bufOutput = new BufferedOutputStream(new FileOutputStream(innerDictFile, true));
//            System.out.println("size saved");
            int buff = input.read();
            while (buff != -1) {
                bufOutput.write(buff);
                buff = input.read();
            }
            bufOutput.close();
            progress.setValue(0.9);
//            System.out.println("text saved");

            ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(innerDictFile, true));
            objectOutput.writeObject(this);
            progress.setValue(1);
            objectOutput.close();
            Settings.getInstance().setLastDictFilePath(destFileName);
            
//            System.out.println("object saved");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Dictonary.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private static Dictonary load(String filePath) {
        File file = new File(filePath);
        Dictonary dict = null;
        try {
            DataInputStream dataInput = new DataInputStream(new FileInputStream(file));
            long mark = dataInput.readLong();

//            System.out.println("mark is: " + mark);
//            System.out.println("file lenght is: " + file.length());

            dataInput.close();
            RandomAccessFile random = new RandomAccessFile(file, "r");

            random.seek(mark + 8);

            int dictObjLenght = (int) (file.length() - mark - 8);
            byte[] dictAsByteArray = new byte[dictObjLenght];

            random.read(dictAsByteArray);

            ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(dictAsByteArray));
            dict = (Dictonary) objIn.readObject();

            dict.setDictFilePath(filePath);
            dict.searcher = new Searcher(dict);

        } catch (FileNotFoundException ex) {
            ex.toString();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return dict;
    }
    private RandomAccessFile getFileStream() {
        if (randomAccessFile != null) {
            return randomAccessFile;
        } else if (dictFilePath != null) {
            try {
                randomAccessFile = new RandomAccessFile(dictFilePath, "r");
                return randomAccessFile;
            } catch (FileNotFoundException ex) {
                System.out.println("FileNotFoundException in getFileStream method");
            }
        } else {
            return null;
        }
return null;
    }
    private void show (byte[]b){
        for (int i = 0; i<b.length; i++){
            System.out.print(b[i]+" ");
        }
        System.out.println("");
    }
    private void formatAndParse(String path) {
   
        Settings s = Settings.getInstance();
        
        
        String makezdMessage = "nothing";
        while (path.contains("\\")){
            path = path.substring(path.indexOf('\\')+1);
        }
        String tempoTXTFileName = path.substring(0, path.indexOf('.'));

            String txtName = tempoTXTFileName + ".txt";
            ProcessBuilder pb = new ProcessBuilder();
            
            pb.command(s.dictsFolderPath + "makezd.exe", "-u", path, s.dictsFolderPath + txtName);
            try {
                   
                
                Process p = pb.start();
                
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                makezdMessage = builder.toString();
            } catch (IOException ex) {
                System.out.println("class: Dictonary, method: formatAndParse, IOException"
                        + " no makezd find");
                Logger.getInstance().log("class: Dictonary, method: formatAndParse, IOException"
                        + " no makezd find");     
            }

            if (makezdMessage.contains("Unpack done")) {
                parse(s.dictsFolderPath + txtName);
                File file = new File(s.dictsFolderPath + txtName);
                file.delete();
            }

        
    }
    
    public String getArticle(String keyWord) throws UnsupportedEncodingException {
        String article = null;
        Long filePoint;
        ArrayList<Byte> arrayBuff = new ArrayList<>();
        int intBuff;
        randomAccessFile = getFileStream();
        
    
        if (dictMap != null && randomAccessFile != null) {
          
            
            try {
                ArrayList<Byte> kwBytes = DictUtils.stringToCollect(keyWord, Settings.getInstance().encoding);
                filePoint = dictMap.get(kwBytes);
                
                
              if (filePoint != null) {
                  
                 
  
                    randomAccessFile.seek(filePoint+8);
                    intBuff = randomAccessFile.read();
                    while (intBuff != 10 && intBuff != 13) {
                        arrayBuff.add((byte) intBuff);
                        intBuff = randomAccessFile.read();
                    }

                    article = DictUtils.collecToString(arrayBuff, Settings.getInstance().encoding);
                }
            } catch (IOException ex) {
                System.out.println("getArticle - IOException");
            }

        }
        return article;
    }
    public static Dictonary getInstance(String path){
         Dictonary instance = null;
        if (path != null && path.length() > 0) {
            if (path.endsWith(".d")) {
                instance = load(path);
                Settings.getInstance().setLastDictFilePath(path);
            } else if (path.endsWith(".txt") || path.endsWith(".zd")) {
                instance = new Dictonary(path);

            }
        }
        return instance;
    }
    public ArrayList<String> getList(String key, int width) {
            return searcher.getList(key, width);
       
    }
    public void updateSearcher (){
        searcher = new Searcher(this);
    }
    


} // class ends
