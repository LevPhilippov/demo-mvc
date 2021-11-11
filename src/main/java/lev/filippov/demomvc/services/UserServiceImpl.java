package lev.filippov.demomvc.services;

import lev.filippov.demomvc.exceptions.UserAlreadyExistException;
import lev.filippov.demomvc.models.Order;
import lev.filippov.demomvc.models.PrivateDetails;
import lev.filippov.demomvc.models.Role;
import lev.filippov.demomvc.models.User;
import lev.filippov.demomvc.repositories.OrderRepository;
import lev.filippov.demomvc.repositories.RoleRepository;
import lev.filippov.demomvc.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private OrderRepository orderRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @SneakyThrows
    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByUsername(username.toLowerCase()));
        return userOptional.orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s is not found!", username)));
    }

    @Override
    public User findById(Long userId) throws UsernameNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(String.format("User with user Id %d is not found!", userId)));
    }

    @Transactional
    @Override
    public User saveNewUser(String username, String password) throws UserAlreadyExistException, UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username.toLowerCase());
        if (user != null) {
            throw new UserAlreadyExistException(String.format("User with username '%s' already exists!", username),user);
        }
        user = new User();
        user.setUsername(username.toLowerCase());
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepository.findByName("ROLE_USER");
        System.out.println("Role is " + role);
        user.setRoles(new HashSet<>(List.of(role)));
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public void saveDetails(Long userId, PrivateDetails details) throws  UsernameNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(String.format("User with Id = %d is not found!", userId)));
        user.setPrivateDetails(details);
        userRepository.save(user);

        //реализация простейшего механизма автоподвязвания заказов по номеру телефона
        Set<Order> orders = orderRepository.findAllByOrderDetailsPhone(details.getPhone());
        for (Order order : orders) {
            order.setUser(user);
        }
        orderRepository.saveAll(orders);
        //
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        System.out.println(user);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}
