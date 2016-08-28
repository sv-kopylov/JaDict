/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Сергей
 */
public class StringReaderCounter {
    private long filePointer = 0;
    File file;
    private boolean isEOFReached = false;
    BufferedInputStream fileInputStream;
       
    public StringReaderCounter(File file) throws IOException{
        this.file =file;
        fileInputStream = new BufferedInputStream(new FileInputStream(file));
    
    }
  
    public void close() throws IOException{
        fileInputStream.close();
    }
 
    public long getFilePointer() {
        return filePointer;
    }
    
    public ArrayList<Byte> readLN () {
       
        if (!isEOFReached) {
        ArrayList<Byte> result = new ArrayList<>();
        
        int buff;
        try {
        buff = fileInputStream.read();
        filePointer++;
       
        while (buff!=10&&buff!=13){
            result.add((byte)buff);
             buff = fileInputStream.read();
             filePointer++;
             if (buff==-1){
//                 System.err.println("file closing tadam");
                 fileInputStream.close();
                 isEOFReached = true;
                 result=null;
                 break;
             }
        }
        if (!isEOFReached){
        buff = fileInputStream.read();
        if (buff==-1){
            isEOFReached = true;
             fileInputStream.close();
                 
             } 
         filePointer++;
        if (buff!=10&&buff!=13){
            System.err.println("A CHARACTER IS LOST!!!" +(char)buff);
        }
        }
       
       
        } catch (IOException e){
            System.err.println(e.toString());
        }
        return result;
    } else {
            System.err.println("EOFreached!!!!!!!!!!!!!!!!");
            return null;
        }
    } 
    
    
}
