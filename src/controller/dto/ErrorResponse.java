package controller.dto;

public final class ErrorResponse<T> extends ServiceResponse<T>{
    public ErrorResponse(String message){
        super(null, false, message);
    }
}
