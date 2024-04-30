package controller.serviceResponse;

public class ErrorResponse<T> extends ServiceResponse<T>{
    public ErrorResponse(String message){
        super(null, false, message);
    }
}
