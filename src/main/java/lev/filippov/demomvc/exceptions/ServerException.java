package lev.filippov.demomvc.exceptions;

public class ServerException extends RuntimeException{
    private static final long serialVersionUID = -5985116848258428739L;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
