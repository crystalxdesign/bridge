import java.util.regex.Pattern;

public abstract class Call {
    public static boolean isCall(String s) {
        return Pattern.matches("^[Pp]|[Xx][Xx]?|[1-7](?:[CcDdHhSs]|[Nn][Tt]?)$", s);
    }

    public static boolean isBid(String s) {
        return Pattern.matches("^[1-7](?:[CcDdHhSs]|[Nn][Tt]?)$", s);
    }

    public static boolean isDouble(String s) {
        return Pattern.matches("^[Xx][Xx]?$", s);
    }
}
