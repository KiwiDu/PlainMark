package PopupX;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/30.
 * All rights reserved.
 */
public class ColorThemeChooser {
    public final static HashMap<String, ColorTheme> themes;
    public final static ColorTheme DEFAULT_THEME;

    static {
        themes = new LinkedHashMap<>();
        DEFAULT_THEME = new ColorTheme("#000", "#fff", "#ddd", "#333");
        themes.put("Black n White(Default)", DEFAULT_THEME);
        themes.put("MoonLight(Night Mode)", new ColorTheme("#eee", "#424242", "#333", "#e0e0e0"));
        themes.put("Material Blue", new ColorTheme("#424242", "#f0f0f0", "#BBDEFB", "#2196F3"));
        themes.put("Material Green", new ColorTheme("#424242", "#f0f0f0", "#b9f6ca", "#00e676"));
        themes.put("Material Pink", new ColorTheme("#424242", "#f0f0f0", "#f48fb1", "#ad1457"));
        themes.put("Sharp Contrast", new ColorTheme("#000", "#fff", "#fff", "#000"));
    }

    public double fill(ObservableList<Pane> src) {
        for (Map.Entry<String, ColorTheme> t : themes.entrySet()) {
            HBox box = t.getValue().getBox();
            box.setId(t.getKey());
            Label l = new Label(t.getKey());
            VBox vbox = new VBox(l, box);
            vbox.setStyle(":hover{-fx-background-color:#BBDEFB;}");
            src.add(vbox);
        }
        return themes.size() * (30 + 25);
    }

    public static class ColorTheme {
        Color[] colors = new Color[4];

        ColorTheme(Color _tf, Color _bf, Color _pc, Color _sc) {
            colors[0] = _tf;
            colors[1] = _bf;
            colors[2] = _pc;
            colors[3] = _sc;
        }

        ColorTheme(String _tf, String _bf, String _pc, String _sc) {
            this(Color.web(_tf), Color.web(_bf), Color.web(_pc), Color.web(_sc));
        }

        HBox getBox() {
            Node[] children = Arrays.stream(colors)
                    .map(c -> new Rectangle(30, 30, c))
                    .toArray(Node[]::new);
            return new HBox(children);
        }

        public Color get(TYPE t) {
            return colors[t.ordinal()];
        }

        public String toString() {
            return String.format("ColorTheme[%s.%s,%s.%s]", colors);
        }

        @Override
        public int hashCode() {
            return toString().hashCode() ^ "BitMask".hashCode();
        }

        public enum TYPE {
            text_fill,
            back_fill,
            pri_color,
            str_color;
        }
    }
}
