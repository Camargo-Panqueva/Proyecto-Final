package controller.dto;

/**
 * Represents an error response in the context of a service operation.
 * This class extends ServiceResponse<T> and is used to encapsulate error information with a message.
 *
 * @param <T> The type of data associated with the response (e.g., Void for no data).
 */
public final class ErrorResponse<T> extends ServiceResponse<T> {

    /**
     * Constructs an ErrorResponse with the specified error message.
     *
     * @param message The error message describing the issue.
     */
    public ErrorResponse(String message) {
        super(null, false, message);
    }
}