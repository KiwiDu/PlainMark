package PopupX;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.util.function.Consumer;

import static PopupX.ButtonX.getDefaultActionButton;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/29.
 * All rights reserved.
 */
public class PopupConfirm extends Popups {
    private Label label;
    private TextField input;

    {
        label = new Label();
        input = new TextField();
        input.getStyleClass().add("text");
    }

    private PopupConfirm() {
    }

    public static void ask(Window owner, String ques, String defaultV, Consumer<String> action) {
        PopupConfirm dialog = new PopupConfirm();
        dialog.label.setText(ques + "\t");
        dialog.input.setText(defaultV);
        dialog.getPane().addAll(dialog.label, dialog.input, getDefaultActionButton(
                dialog,
                (e) -> {
                    action.accept(dialog.input.getText());
                }
        ));
        dialog.show(owner);
    }
}
