package PopupX;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/30.
 * All rights reserved.
 */
public class PopupList extends Popups {
    private ListView<Pane> contents;
    private StringProperty selectedIdProperty = new SimpleStringProperty("");

    {
        contents = new ListView<>();
        contents.setPrefWidth(200);
        contents.setStyle("-fx-background-color:#fff;");
        super.getPane().add(contents);
        super.minimize();

        contents.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener<Pane>) (c) -> {
                    ObservableList<? extends Pane> h = c.getList();
                    if (h.size() == 0) {
                        selectedIdProperty.set("");
                    } else {
                        selectedIdProperty.set(contents.getSelectionModel().getSelectedItems().get(0).getChildren().get(1).getId());
                    }
                    Ani.hide(this);
                }
        );

    }

    public DoubleProperty listHeightProperty() {
        return contents.prefHeightProperty();
    }

    public ObservableList<Pane> getContentList() {
        return contents.getItems();
    }

    public StringProperty getSelectedId() {
        return selectedIdProperty;
    }

}
