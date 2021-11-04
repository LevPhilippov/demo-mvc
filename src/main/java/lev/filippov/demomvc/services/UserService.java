package lev.filippov.demomvc.services;

import lev.filippov.demomvc.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
}
