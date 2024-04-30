package controller.serviceResponse;

public class Response<T> extends ServiceResponse<T>{
    public Response(T payload, String message){
        super(payload, true, message);
    }
}
