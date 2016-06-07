/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

/**
 *
 * @author Сергей
 */
public class Logger {

    File file;
    FileWriter fw;
    static final int MAX = 10;
    int ctr = 0;
    Calendar cld;
    String filePath;
public static Logger getInstance (String filePath){
    return new Logger(filePath);
}
    private Logger(String filePath)  {
        this.filePath = filePath;
        file = new File(filePath);
        try {
            fw = new FileWriter(file);
        } catch (IOException ex) {
            System.out.println("Ошибка ввода - вывода в логере");
        }
        cld = new GregorianCalendar();
    }

    String curTime() {
         return GregorianCalendar.getInstance().getTime().toString();
    }
    
    void log (String...str){
        StringBuilder sb = new StringBuilder();
        sb.append(curTime());
        sb.append(": ");
        for (String s:str){
            sb.append(s);
            sb.append(": ");
        }
        try {
            fw.write(sb.toString());
            ctr++;
            if (ctr>=MAX){
             file.delete();
             file = new File(filePath);
            }
        } catch (IOException ex) {
            System.out.println(curTime()+" ошибка записи файла");
        }
        
        
    }

}
