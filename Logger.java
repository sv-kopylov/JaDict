/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;

/**
 *
 * @author Сергей
 */
public class Logger {
   
    private File file;
    private FileWriter fw;
    static final int MAX = 1000;
    int ctr = 0;
   

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
        sb.append("\t\n");
        try {
            
            fw.write(sb.toString());
            fw.flush();
            System.out.println("пиwу в лог "+sb.toString());
            ctr++;
            
            if (ctr>=MAX){
               fw.flush();
               fw.close();
             file.delete();
             file = new File(Settings.getInstance().dictLoggPath);
             ctr=0;
             fw = new FileWriter(file);
            }
        } catch (IOException ex) {
            System.out.println(curTime()+" ошибка записи файла в лог");
        }
        
        
    }

 //   here singleton mechanism  
private static Logger unicInstance;    
public static Logger getInstance (){
    if (unicInstance==null){
            unicInstance = new Logger();
        }
        return unicInstance;
}
private Logger()  {
        file = new File(Settings.getInstance().dictLoggPath);
        try {
           
            System.out.println(file.getAbsolutePath());
            fw = new FileWriter(file);
            
        } catch (IOException ex) {
            System.out.println("Ошибка ввода - вывода в логере");
        }
     
    }

}
