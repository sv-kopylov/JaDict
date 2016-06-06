
package jadict;

import java.io.Serializable;

/**
 *
 * @author Сергей
 */
public class Settings implements Serializable{
//    here all common system variables
     int MAX_FILE_SIZE_MB = 10;
     String defaultFilePath = "C:\\Projects\\JaDict\\1.txt";
     
//   here singleton mechanism
    private Settings(){ // ctor
    }
    static Settings getInstance (){
        if (unicInstance==null){
            unicInstance = new Settings();
        }
        return unicInstance;
    }
       private static Settings unicInstance;
    
    
}
