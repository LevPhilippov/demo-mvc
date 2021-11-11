package lev.filippov.demomvc.exceptions;

public class PasswordOrUsernameException extends ServerException{

    private static final long serialVersionUID = 9167581338272001939L;


    public PasswordOrUsernameException(String message) {
        super(message);
    }

    public PasswordOrUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
