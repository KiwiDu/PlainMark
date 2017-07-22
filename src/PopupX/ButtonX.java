package PopupX;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Window;
import main.IconLoader;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/29.
 * All rights reserved.
 */
public class ButtonX {
    public static Node getBtn(EventHandler<? super MouseEvent> value) {
        Circle c = new Circle(30);
        c.setStyle("-fx-fill:#009866;");
        Node icon = IconLoader.load("done");
        icon.setStyle("-fx-fill:#fff");
        StackPane s = new StackPane(c, icon);
        s.getStyleClass().addAll("float");
        s.setOnMouseClicked(value);
        return s;
    }

    public static Node getSubmit(Window owner) {
        return getBtn((e) -> Ani.hide(owner));
    }

    public static Node getDefaultActionButton(Window owner, EventHandler<? super MouseEvent> handler) {
        return getBtn((e) -> {
            handler.handle(e);
            Ani.hide(owner);
        });
    }

}
