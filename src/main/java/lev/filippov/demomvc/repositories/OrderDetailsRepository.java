package lev.filippov.demomvc.repositories;

import lev.filippov.demomvc.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
}
