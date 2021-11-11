package lev.filippov.demomvc.exceptions;

public class PasswordOrUsernameException extends RuntimeException{

    private static final long serialVersionUID = 9167581338272001939L;

    public PasswordOrUsernameException() {
    }

    public PasswordOrUsernameException(String message) {
        super(message);
    }

    public PasswordOrUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordOrUsernameException(Throwable cause) {
        super(cause);
    }

    public PasswordOrUsernameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
