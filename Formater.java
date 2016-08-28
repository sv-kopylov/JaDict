/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Сергей
 */
public class Formater {

    private String htmlStyle = "<html>\n"
            + "<head>\n"
            + "<style>\n"
            + "body {\n"
            + "background:"+Settings.getInstance().backColor+ ";\n"
            + "}\n"
            + "</style>\n"
            + "</head>\n"
           ;
     private String setHtml(String enter) {
        return htmlStyle+"<body>"+enter
                + "</body></html>";
    }
    private String fontColor = "#424242";
    private String fontFace = "Arial";

    private String setFont(String enter, String sz) {
        return "<font size = \"" + sz + "\" color = \"" + fontColor + "\" face = \"" + fontFace + "\">" + enter + "</font>";
    }

    private String bold(String enter) {
        return "<b>" + enter + "</b>";
    }


    public String format(String initialStr, String key) {
        String result = "";
        if (initialStr != null && key != null) {
            result = initialStr.replaceFirst(key + ".." + key, key);
            result = result.replaceFirst(key, setFont(bold(key), "5"));
            result = setHtml(setFont(result, "4"));
        }
        return result;
    }

}
