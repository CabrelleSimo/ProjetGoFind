package com.example.projetgofind;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SceneUtils {
    public static Stage getCurrentStage(ActionEvent event) {

        if (event.getSource() instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) event.getSource();
            Window ownerWindow = menuItem.getParentPopup().getOwnerWindow();
            if (ownerWindow instanceof Stage) {
                return (Stage) ownerWindow;
            }
        }

        else if (event.getSource() instanceof Node) {
            return (Stage) ((Node) event.getSource()).getScene().getWindow();
        }

        throw new IllegalArgumentException("Impossible de déterminer la fenêtre. Source de l'événement: " + event.getSource().getClass());
    }
}