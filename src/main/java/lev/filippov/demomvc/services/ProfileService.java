package lev.filippov.demomvc.services;

import lev.filippov.demomvc.models.Order;
import lev.filippov.demomvc.models.User;
import lev.filippov.demomvc.repositories.OrderRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProfileService {

    OrderRepository orderRepository;
    UserService userService;

    public ProfileService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    public Set<Order> findAllOrdersByUser(Long userId) throws UsernameNotFoundException {
        User user = userService.findById(userId);
        return orderRepository.findAllByUser(user);
    }
}
