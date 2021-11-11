package lev.filippov.demomvc.services;

import lev.filippov.demomvc.models.Order;
import lev.filippov.demomvc.models.OrderDetails;
import lev.filippov.demomvc.models.PrivateDetails;
import lev.filippov.demomvc.models.User;
import lev.filippov.demomvc.repositories.OrderRepository;
import lev.filippov.demomvc.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class CartService {

    OrderRepository orderRepository;
    UserService userService;

    @PersistenceContext
    EntityManager entityManager;

    public CartService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Transactional
    public void saveOrder(Cart cart, User user, OrderDetails details) {
        Order order = new Order(cart, user, details);
        orderRepository.save(order);
    }

}
