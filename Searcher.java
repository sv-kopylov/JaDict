/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Сергей
 */
class Searcher {
    String encoding = "UTF-8";
    private Dictonary dictan;
    TreeMap<String, ArrayList<Byte>> treeMap;

    public Searcher(Dictonary dictan) {
        this.dictan = dictan;
        Set<ArrayList<Byte>> set = dictan.getKeySet();
        treeMap = new TreeMap<>();
        try {
        for (ArrayList<Byte> s: set){
            
                treeMap.put(DictUtils.collecToString(s, Settings.getInstance().encoding), s);
    }
          } catch (UnsupportedEncodingException ex) {
                System.out.println("Searcher constructor Encoding usuported");
            }
    }
    
    public ArrayList<String> getList (String key, int width){
         ArrayList<String> arrayList = null;
        if (key != null && width > 0) {
            arrayList = new ArrayList<>();
            if (treeMap != null) {
                TreeSet<String> dictSet = new TreeSet<>(treeMap.keySet());
                String str = dictSet.ceiling(key);
                if (str==null){
                    key = key.toUpperCase();
                    str = dictSet.ceiling(key);
                }
                if (str==null){
                    key = key.toLowerCase();
                    str = dictSet.ceiling(key);
                }
                
                for (int i =0; i<width;i++){
                    if (str!=null){
                        arrayList.add(str);
                        
                    } else {
                        arrayList.add("");
                    }
                    if (str!=null){
                    str = dictSet.higher(str);
                    }
                }
//                        
            }
        }

        return arrayList;
        
    }
    
    
    
}
