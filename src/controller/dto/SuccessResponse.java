package controller.dto;

/**
 * Represents a success response in the context of a service operation.
 * This class extends ServiceResponse<T> and is used to encapsulate successful responses with a payload and message.
 *
 * @param <T> The type of data associated with the response (e.g., Void for no data).
 */
public final class SuccessResponse<T> extends ServiceResponse<T> {

    /**
     * Constructs a SuccessResponse with the specified payload and message.
     *
     * @param payload The data payload associated with the response.
     * @param message The success message describing the operation.
     */
    public SuccessResponse(T payload, String message) {
        super(payload, true, message);
    }

}