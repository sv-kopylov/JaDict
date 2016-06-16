/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 *
 * @author Сергей
 */
public class JaDict {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException  {

     
//String str = "-балльный  1. ";
//        char[] a = str.toCharArray();
//        for (int i=0; i<str.length();i++){
//            System.out.println(Character.codePointAt(a, i));  
//        }
        

            
        
        
//        Dictonary d = Dictonary.getSavedInstance(s.dictsFolderPath+"BigEnc2.d");
        Dictonary d = new Dictonary("C:\\programs\\JaDict\\dicts\\eng2Fre.txt");
        System.out.println(d.name);
        d.showSet();
//        System.out.println(d.getArticle("АБСУРД"));
         
  
    }
    
}
