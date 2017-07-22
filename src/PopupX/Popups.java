package PopupX;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.stage.Popup;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/28.
 * All rights reserved.
 */
public class Popups extends Popup {
    /**
     * It is material design by default
     * [p,float,block]
     */
    private FlowPane p;

    {
        p = new FlowPane();
        p.getStyleClass().addAll("block", "float");
        p.setAlignment(Pos.CENTER_RIGHT);
        this.getScene().setUserAgentStylesheet(getClass().getResource("md.css").toString());
        this.getContent().add(p);
        this.centerOnScreen();
    }

    protected ObservableList<Node> getPane() {
        return p.getChildren();
    }

    protected void minimize() {
        p.setAlignment(Pos.CENTER);
        p.setPadding(Insets.EMPTY);
        p.setPrefWidth(0);
        p.setMinWidth(0);
    }

}
