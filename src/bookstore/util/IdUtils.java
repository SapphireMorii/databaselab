package bookstore.util;

import java.util.UUID;

public class IdUtils {

    public static String getUUID() {

        return UUID.randomUUID().toString();

    }
}
