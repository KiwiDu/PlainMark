package FileIO;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * This is PlainMark,
 * which was created by kiwid on 2017/1/30.
 * All rights reserved.
 */
public class MDFileReader {
    private boolean scanned;
    private File file;
    private String title;
    private StringBuilder content;

    public MDFileReader(File _f) {
        if (_f.exists() && _f.canRead()) {
            file = _f;
        }
        scanned = false;
    }

    private void scanHTML() {
        try (Scanner s = new Scanner(new BufferedInputStream(new FileInputStream(file)))) {
            s.findWithinHorizon("<-- *type=\"markdown\"", 0);
            s.findInLine("title=");
            title = s.next().replaceAll("\"(.*)\"", "$1");
            s.findInLine("theme=");
            int themeid = Integer.parseInt(s.next().replaceAll("\"(.*)\"", "$1"));
            s.findWithinHorizon("content=", 0);
            s.nextLine();

            content = new StringBuilder();
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.trim().equals("-->")) {
                    break;
                }
                content.append(line).append('\n');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        scanned = true;
    }

    public String readTitle() {
        synchronized (this) {
            if (!scanned) {
                scanHTML();
            }
        }
        return title;
    }

    public String readContent() {
        synchronized (this) {
            if (!scanned) {
                scanHTML();
            }
        }
        return content.toString();
    }
}
