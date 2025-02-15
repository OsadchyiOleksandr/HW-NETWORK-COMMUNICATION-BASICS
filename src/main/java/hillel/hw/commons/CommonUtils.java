package hillel.hw.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class CommonUtils {

    private static final AtomicInteger CLIENT_NUMBER = new AtomicInteger(1);
    private static final List<String> CLIENT_NAMES = new ArrayList<>();

    private CommonUtils() {
    }

    public static String generateClientName() {
        return "Client-" + CLIENT_NUMBER.getAndIncrement();
    }

    public static String generateConnectionDetails(final String name) {
        return """
                Name %s
                """.formatted(name);
    }

    public static List<String> getClientNames() {
        return CLIENT_NAMES;
    }
}