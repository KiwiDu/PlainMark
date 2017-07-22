package Utils;


import PopupX.ColorThemeChooser.ColorTheme;
import javafx.scene.paint.Color;
import main.Binary;
import main.Mime;
import main.MultiMime;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static Utils.Manager.getRes;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/26.
 * All rights reserved.
 */
public class MDocument {
    private String title;
    private String source;
    private String rendered;
    private MultiMime resources;

    public MDocument(String _title, String _source, String _rendered) {
        title = Objects.requireNonNull(_title);
        source = Objects.requireNonNull(_source);
        rendered = Objects.requireNonNull(_rendered);
        resources = new MultiMime(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String _title) {
        title=Objects.requireNonNull(_title);
    }

    public String getSource() {
        return source;
    }

    public String getRendered() {
        return rendered;
    }

    @Override
    public String toString() {
        return "For DEBUG ing only:\n" + rendered;
    }

    @SuppressWarnings("unchecked")
    public String toRenderedHTML(ColorTheme theme) {
        Objects.requireNonNull(theme);
        String basic_css = new Replacer<Color>(getBasicCss())
                .factory(Colors::toHex)
                .r("%bf%", theme.get(ColorTheme.TYPE.back_fill))
                .r("%tf%", theme.get(ColorTheme.TYPE.text_fill))
                .r("%pc%", theme.get(ColorTheme.TYPE.pri_color))
                .r("%sc%", theme.get(ColorTheme.TYPE.str_color))
                .toString();
        String result = new Replacer<String>(getHtmlTemplate())
                .r("%title%", title)
                .r("%theme%", theme.hashCode())
                .r("%rendered%", rendered)
                .r("%src%", source)
                .r("%reset-css%", getResetCss())
                .r("%basic-css%", basic_css)
                .toString();
        return result;
    }

    public String toRenderedMHT(ColorTheme theme) {
        byte[] html = toRenderedHTML(theme).getBytes(Charset.forName("utf-8"));
        resources.add(new Mime(new Binary(html, String.format("http://local/%s.html", getTitle()))));
        return resources.toString();
    }

    private String getHtmlTemplate() {
        return getRes("template.html", this.getClass());
    }

    private String getResetCss() {
        return getRes("reset.css", this.getClass());
    }

    private String getBasicCss() {
        return getRes("basic.css", this.getClass());
    }

    private class Replacer<A> {
        private String s;
        private Function<A, String> converter;
        private HashMap<String, A> pairs;

        Replacer(String _s) {
            s = _s;
            pairs = new HashMap<>();
            converter = String::valueOf;
        }

        Replacer factory(Function<A, String> fac) {
            converter = fac;
            return this;
        }

        Replacer r(String ori, A con) {
            pairs.put(ori, con);
            return this;
        }

        public String toString() {
            for (Map.Entry<String, A> pair : pairs.entrySet()) {
                s = s.replaceAll(pair.getKey(), converter.apply(pair.getValue()));
            }
            return s;
        }
    }

}
