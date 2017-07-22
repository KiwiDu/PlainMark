package Utils;

import javafx.scene.paint.Color;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/2/1.
 * All rights reserved.
 */
public class Colors {
    public static String toHex(Color c) {
        String hex = c.toString();
        hex = "#" + hex.substring(2, hex.length() - 2);
        return hex;
    }
}
