package lev.filippov.demomvc.services;

import lev.filippov.demomvc.models.PrivateDetails;
import lev.filippov.demomvc.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    User saveNewUser(String username, String password);
    void saveDetails(Long userId, PrivateDetails details) throws UsernameNotFoundException;
    User findById(Long userId) throws UsernameNotFoundException;
}
