/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Сергей
 */
public class DictUtils {
    
    public static  void showTree(HashMap<ArrayList<Byte>, Long> dictMap, String encoding) throws UnsupportedEncodingException {
        Set<ArrayList<Byte>> keys = dictMap.keySet();
        System.out.println("set size" + keys.size());
        for (ArrayList<Byte> key : keys) {
            if (dictMap.get(key) != null) {
                System.out.print(collecToString(key, encoding) + " ");
                System.out.println(dictMap.get(key));
            }
        }
    } 
    static public String collecToString(ArrayList<Byte> b, String encoding) throws UnsupportedEncodingException {
        if (b != null) {
            byte byteString[] = toByteArray(b);
            return new String(byteString, encoding);
        }
        return "";
    }
    static public ArrayList<Byte> stringToCollect (String source, String encoding){
        ArrayList<Byte> arrayList = null;
        if (source!=null&&encoding!=null){
            try {
             
                byte byteString[] = source.getBytes(encoding);
                arrayList = new ArrayList<>(byteString.length);
                for (int i=0; i<byteString.length; i++){
                    arrayList.add(byteString[i]);
                }
            } catch (UnsupportedEncodingException ex) {
                System.out.println("Incorrect encoding in stringToCollect method");
            }
        }
        return arrayList;
    }

    private static byte[] toByteArray(ArrayList<Byte> sourse) {
        byte[] byteArray = new byte[sourse.size()];
        for (int i = 0; i < sourse.size(); i++) {
            byteArray[i] = sourse.get(i);
        }
        return byteArray;
    }


    
    
    
}
