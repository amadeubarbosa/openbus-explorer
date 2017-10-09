package busexplorer.utils;

/**
 * Representação da conectividade da referência de uma oferta.
 */
public class Availability {
    public enum Status {
        ONLINE,
        UNREACHABLE,
        FAILURE,
        UNEXPECTED,
        UNKNOWN,
    }

    public Status status;
    public String detail;

    public Availability(Status status, String message) {
        this.status = status;
        this.detail = message;
    }

    public Availability(Status status) {
        this(status, "");
    }

}
