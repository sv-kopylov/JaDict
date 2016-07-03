/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author Сергей
 */
public class JaDict {

 
    public static void main(String[] args) throws IOException  {
Settings s = Settings.getInstance();
s.scanUnconvertedDicts(s.dictsFolderPath);
       
//String str = "-балльный  1. ";
//        char[] a = str.toCharArray();
//        for (int i=0; i<str.length();i++){
//            System.out.println(Character.codePointAt(a, i));  
//        }
//        Dictonary d = Dictonary.getSavedInstance(s.dictsFolderPath+"BigEnc2.d");
//        Dictonary d = Dictonary.getSavedInstance("C:\\programs\\JaDict\\dicts\\Eng2Fre.d");
//        Dictonary d = Dictonary.getInstance("C:\\programs\\JaDict\\dicts\\eng2Feng_rus_eng.txt");
//        System.out.println(d.name);
////        d.showSet();
//Logger l = Logger.getInstance();
//Scanner s = new Scanner(System.in);
//String st = "";
//st = s.next();
//while (!st.equals("q")){
//    System.out.println(st);
//    System.out.println(d.getArticle(st));
//    l.log(d.getArticle(st));
//    st = s.next();
//}
        
         
  
    }
    
}
