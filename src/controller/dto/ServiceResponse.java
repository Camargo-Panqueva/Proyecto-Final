package controller.dto;

public abstract class ServiceResponse<T> {

    public final T payload;
    public final String message;
    public final boolean ok;
    public ServiceResponse(T payload, boolean ok ,String message){

        this.payload = payload;
        this.message = message;
        this.ok = ok;
    }
}
