package MainFrame;

import BasicNode.Node;
import FileIO.MDFileReader;
import PopupX.ColorThemeChooser;
import PopupX.PopupConfirm;
import PopupX.PopupList;
import PopupX.PopupText;
import Rendering.DefaultRenderEngine;
import Rendering.RenderEngine;
import Utils.MDocument;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import main.Binary;
import main.Mime;
import main.Parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

import static PopupX.ColorThemeChooser.DEFAULT_THEME;
import static PopupX.ColorThemeChooser.themes;
import static Utils.Manager.getRes;

public class Controller implements Initializable {

    private static String rendered;
    private static boolean debugging = false;
    private static StringProperty title = new SimpleStringProperty("Untitled");
    private static ColorThemeChooser.ColorTheme theme = DEFAULT_THEME;
    @FXML
    private WebView WebViewDisplay;
    @FXML
    private TextArea MainEditor;
    @FXML
    private BorderPane Frame;
    @FXML
    private TabPane TopBar;
    @FXML
    private HBox BodyBox;

    private Window getRoot() {
        return Frame.getScene().getWindow();
    }


    @FXML
    public void saveBtn() {
        FileChooser popup = new FileChooser();
        popup.setTitle("Save document:");
        popup.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Document", "*.html", "*.htm"),
                new FileChooser.ExtensionFilter("Mime Document", "*.mhtml", "*.mht"));
        popup.setInitialFileName(title.get() + ".html");
        File file = popup.showSaveDialog(getRoot());
        if (file == null) {
            return;
        }
        try {
            switch (popup.getSelectedExtensionFilter().getDescription()) {
                case "Document": {
                    Files.write(file.toPath(), new MDocument(title.get(), MainEditor.getText(), rendered).toRenderedHTML(theme).getBytes());
                }
                break;
                case "Mime Document": {
                    Files.write(file.toPath(), new MDocument(title.get(), MainEditor.getText(), rendered).toRenderedMHT(theme).getBytes());
                }
                break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openBtn() {
        FileChooser popup = new FileChooser();
        popup.setTitle("Open document:");
        popup.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Document", "*.html", "*.htm"),
                new FileChooser.ExtensionFilter("Mime Document", "*.mhtml", "*.mht"));
        popup.setInitialFileName(title.get() + ".html");
        File file = popup.showOpenDialog(getRoot());
        if (file == null) {//cancelled by user or file corruption
            return;
        }
        MDFileReader reader = new MDFileReader(file);
        title.set(reader.readTitle());
        MainEditor.setText(reader.readContent());
    }

    @FXML
    public void newBtn() {
        PopupConfirm.ask(this.getRoot(), "New:", title.get(), title::set);
        MainEditor.setText("");
    }

    @FXML
    public void newName() {
        PopupConfirm.ask(this.getRoot(), "New:", title.get(), title::set);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        *    To force the text area to change size.
        *    It may seem to be stupid but texts are seen as "Rigid"
        *    in JavaFx,which means its size won't be changed by the layout pane.
        *    To solve this,I added this listener to respond to resizing.
        */
        Font customFont = Font.loadFont(
                Controller.class.getResourceAsStream("OpenSans.ttf"), 32);
        Frame.setStyle(String.format("-fx-font-family: '%s';", customFont.getName()));

        MainEditor.minWidthProperty().bind(BodyBox.widthProperty().divide(2));
        /*
        *   Listen the change in  the text area,then change according to it
        */
        MainEditor.textProperty().addListener((Obs, prev, cur) -> rerender(cur));
        initTutor();
    }

    public void rerender(String s) {
        rerender(s, debugging ? "text/plain" : "text/html");
    }

    public void rerender(String s, String mime) {
        Node parsed = Parser.instance.parse(s);
        RenderEngine re = new DefaultRenderEngine();
        rendered = re.accept(parsed).render();
        MDocument dom = new MDocument(title.get(), "", rendered);
        WebViewDisplay.getEngine().loadContent(dom.toRenderedHTML(theme), mime);
    }

    private void showTxt(String con) {
        PopupText popup = new PopupText();
        popup.inner().setValue(con);
        popup.show(getRoot());
    }

    private void showTxtFromSrc(String src) {
        showTxt(getRes(src, getClass()));
    }

    public void showInfo() {
        showTxtFromSrc("info.txt");
    }

    public void showThx() {
        showTxtFromSrc("thx.txt");
    }

    public void initTutor() {
        MainEditor.setText(getRes("tutor.md", getClass()));
    }

    public void colorSelect() {
        PopupList pl = new PopupList();
        ColorThemeChooser chooser = new ColorThemeChooser();
        pl.listHeightProperty().set(chooser.fill(pl.getContentList()));
        pl.getSelectedId().addListener((o, p, c) -> {
            theme = themes.get(c);
            rerender(MainEditor.getText());
        });
        pl.show(getRoot());
    }

    private void insertImg(String url) {
        int pos = MainEditor.getAnchor();
        String s = MainEditor.getText();
        String str = "![" + url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.')) + "](" + url + ")";
        MainEditor.setText(s.substring(0, pos) + str + s.substring(pos));
    }

    public void addLocImg() {
        FileChooser popup = new FileChooser();
        popup.setTitle("Open a picture:");
        popup.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Picture", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif"));
        List<File> files = popup.showOpenMultipleDialog(this.getRoot());
        for (File f : files) {
            Mime m = new Mime(new Binary(f));
            insertImg(m.getVirtualPath());

        }
    }

    public void addWebImg() {
        PopupConfirm.ask(this.getRoot(), "Enter URL:", "", this::insertImg);
    }

    public void renderDebug(ActionEvent actionEvent) {
        debugging = !debugging;
        rerender(MainEditor.getText());
    }
}
