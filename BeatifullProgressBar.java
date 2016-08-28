/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jadict;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

/**
 *
 * @author Сергей
 */
public class BeatifullProgressBar {
    
//    private final double widthPop = 400;
//    private final double heightPop = 100;
    private final double widthPB = 400;
    private final double heightPB = 20;
    private final DropShadow effect;
    private final Label label = new Label();
    private final Popup popup = new Popup();
    private final ProgressBar progressBar = new ProgressBar();
    private final VBox popupVbox = new VBox();
    
     public BeatifullProgressBar() {
        effect = new DropShadow();
        effect.setOffsetX(5);
        effect.setOffsetY(5);
        popupVbox.setAlignment(Pos.CENTER);
        popupVbox.setStyle(Settings.getInstance().dStyle);
        popupVbox.setEffect(effect);
        
        progressBar.setPrefSize(widthPB, heightPB);
//        popup.setWidth(widthPop);
//        popup.setHeight(heightPop);
        popupVbox.getChildren().addAll(label, progressBar);
        popup.getContent().addAll(popupVbox);
        
    }
     
     public void show (Window owner, String name, DoubleProperty progress){
         progressBar.progressProperty().bind(progress);
         label.setText(name);
         popup.show(owner);
     }

    void hide() {
popup.hide();
    }
    
}
