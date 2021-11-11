package lev.filippov.demomvc.exceptions;

import lev.filippov.demomvc.models.User;
import lombok.Getter;

public class UserAlreadyExistException extends RuntimeException{

    private static final long serialVersionUID = 7093698835717003886L;

    @Getter
    private final User user;

    public UserAlreadyExistException(String message, User user) {
        super(message);
        this.user = user;
    }

}
