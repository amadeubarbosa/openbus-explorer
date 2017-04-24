package busexplorer.utils;

/**
 * Representação da conectividade da referência de uma oferta.
 */
public class Status {
    public static final int ONLINE = 1;
    public static final int UNREACHABLE = 0;
    public static final int FAILURE = -1;
    public static final int UNEXPECTED = -2;
    public static final int UNKNOWN = -3;
}
