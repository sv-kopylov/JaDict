
package jadict;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Сергей
 */
public class Settings implements Serializable{
//    here all common system variables
     
     public static int SUCCESS = 1;
     public static int FAIL = 2;
     
     String dFileResolution = ".d";
     int MAX_FILE_SIZE_MB = 10;
     String defaultFilePath = "C:\\Projects\\JaDict\\1.txt";
     String dictsFolderPath = "\\dicts\\";
     String dictLoggPath = "log\\log.txt";
     String dictSettingsPath = "settings\\settings.b";

     
     HashMap<String, String> savedDicts = new HashMap<>();
     
     
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
