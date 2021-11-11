package lev.filippov.demomvc.repositories;

import lev.filippov.demomvc.models.Order;
import lev.filippov.demomvc.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Set<Order> findAllByUser(User user);

}
