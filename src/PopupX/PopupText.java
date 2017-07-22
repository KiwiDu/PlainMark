package PopupX;

import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import static PopupX.ButtonX.getSubmit;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/28.
 * All rights reserved.
 */
public class PopupText extends Popups {

    TextFlow content;
    Text text;

    public PopupText() {
        text = new Text();
        content = new TextFlow(text);
        content.getStyleClass().add("p");
        getPane().addAll(content, getSubmit(this));
    }

    public StringProperty inner() {
        return text.textProperty();
    }

}
