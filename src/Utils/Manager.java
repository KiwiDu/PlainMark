package Utils;

import java.io.BufferedInputStream;
import java.util.Scanner;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/26.
 * All rights reserved.
 */
public class Manager {
    public static String getRes(String name, Class<?> clz) {
        StringBuilder ret = new StringBuilder();
        Scanner s = new Scanner(new BufferedInputStream(clz.getResourceAsStream(name)));
        while (s.hasNextLine()) {
            ret.append(s.nextLine()).append('\n');
        }
        return ret.toString();
    }
}
