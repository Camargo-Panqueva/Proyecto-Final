package controller.dto;

/**
 * Represents a service response containing payload, message, and status information.
 *
 * @param <T> The type of data associated with the response.
 */
public abstract class ServiceResponse<T> {

    /**
     * The payload data associated with the response.
     */
    public final T payload;

    /**
     * The message describing the response or any error message.
     */
    public final String message;

    /**
     * The status indicating whether the response is successful (true) or not (false).
     */
    public final boolean ok;

    /**
     * Constructs a ServiceResponse object with the specified payload, status, and message.
     *
     * @param payload The data payload associated with the response.
     * @param ok      The status indicating whether the response is successful (true) or not (false).
     * @param message The message describing the response or any error message.
     */
    public ServiceResponse(T payload, boolean ok, String message) {
        this.payload = payload;
        this.message = message;
        this.ok = ok;
    }
}
