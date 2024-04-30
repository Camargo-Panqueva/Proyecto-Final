package controller.serviceResponse;

public final class ErrorResponse<T> extends ServiceResponse<T>{
    public ErrorResponse(String message){
        super(null, false, message);
    }
}
