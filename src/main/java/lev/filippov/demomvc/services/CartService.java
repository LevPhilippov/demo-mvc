package lev.filippov.demomvc.services;

import lev.filippov.demomvc.models.Order;
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

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void saveOrder(Cart cart, User user, PrivateDetails privateDetails) {
        Order order = new Order(cart, user);
        if (!Objects.isNull(privateDetails.getEmail())) {
            user.getPrivateDetails().setEmail(privateDetails.getEmail());
        }
        if (!Objects.isNull(privateDetails.getPhone())) {
            user.getPrivateDetails().setPhone(privateDetails.getPhone());
        }
        orderRepository.save(order);
    }

}
