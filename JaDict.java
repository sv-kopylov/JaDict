/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 *
 * @author Сергей
 */
public class JaDict {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException  {
                
        Settings s = Settings.getInstance();
//        Dictonary d = new Dictonary(s.dictsFolderPath+"BigEnc2.d");
        Dictonary d = new Dictonary("C:\\Projects\\JaDict\\BigEnc2.zd");
        System.out.println(d.name);
        System.out.println(d.getArticle("яхонт"));
        
//Settings s = Settings.getInstance();
//ProcessBuilder pb = new ProcessBuilder();
//pb.directory(new File(s.dictsFolderPath));
//pb.command("makezd.exe","-u","eng2FRASS.zd","eng2FRASS.txt");
//Process p = pb.start();
//
//BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//StringBuilder builder = new StringBuilder();
//String line = null;
//while ( (line = reader.readLine()) != null) {
//   builder.append(line);
////   builder.append(System.getProperty("line.separator"));
//}
//String result = builder.toString();
//     
//        System.out.println(result);
//           System.out.println("red");
//

        






        
       

        
        
        
    

        

        
   
  
    }
    
}
