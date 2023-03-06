package plantventure.utils;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class FileUtils {
    private static final Logger logger = LoggerUtils.getLogger(FileUtils.class);
    public static final String resourcePath = "src/main/resources/";

    private FileUtils() {
    }

    public static String loadAsString(URL file) {
        StringBuilder result = new StringBuilder();
        try {
            Scanner scanner = new Scanner(file.openStream());
            scanner.useDelimiter("\\A");
            while (scanner.hasNext()) {
                result.append(scanner.next());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}