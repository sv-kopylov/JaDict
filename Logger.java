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
    static final int MAX = 10;
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
        sb.append("\n");
        try {
            
            fw.write(sb.toString());
            ctr++;
            
            if (ctr>=MAX){
             file.delete();
             file = new File(Settings.getInstance().dictLoggPath);
             ctr=0;
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
           
            
            fw = new FileWriter(file);
            
        } catch (IOException ex) {
            System.out.println("Ошибка ввода - вывода в логере");
        }
     
    }

}
