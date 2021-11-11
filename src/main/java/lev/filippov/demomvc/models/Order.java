package lev.filippov.demomvc.models;

import lev.filippov.demomvc.utils.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "price")
    BigDecimal price;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "details_id")
    OrderDetails orderDetails;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "order")
    Set<OrderItem> orderItems;

    public Order(Cart cart, User user, OrderDetails orderDetails) {
        this.orderItems = new HashSet<>();
        this.orderItems.addAll(cart.getOrderItems());
        this.price = cart.getTotalPrice();
        this.user = user;
        this.orderDetails = orderDetails;

        for (OrderItem o : this.orderItems) {
            o.setOrder(this);
        }
    }
}
