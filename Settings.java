
package jadict;

/**
 *
 * @author Сергей
 */
public class Settings {
//    here all common system variables
     int MAX_FILE_SIZE_MB = 10;
     
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
