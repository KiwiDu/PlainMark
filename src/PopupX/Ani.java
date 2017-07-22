package PopupX;

import javafx.animation.FadeTransition;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/2/2.
 * All rights reserved.
 */
public class Ani {
    public static void hide(Window wid) {
        FadeTransition a = new FadeTransition(Duration.millis(400), wid.getScene().getRoot());
        a.setFromValue(1.0);
        a.setToValue(0.1);
        a.setOnFinished((e) -> wid.hide());
        a.play();
    }
}
