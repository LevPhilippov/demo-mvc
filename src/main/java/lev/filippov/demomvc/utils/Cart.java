package lev.filippov.demomvc.utils;

import lev.filippov.demomvc.models.OrderItem;
import lev.filippov.demomvc.models.Product;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Data
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SessionScope
@Component
public class Cart {

    private Set<OrderItem> orderItems;
    private BigDecimal totalPrice;
    private Integer counter;

    public Cart(){
        this.orderItems = new HashSet<>();
        this.counter = 0;
    }

    public void add(Product product) {
        OrderItem orderItem = new OrderItem(product);
        orderItem.setNumber(countIncr());
        if(!orderItems.add(orderItem)){
            countDecr();
            orderItems.stream().filter(i->i.equals(orderItem)).forEach(i -> i.setQty(i.getQty()+1));
            WeakReference reference = new WeakReference(orderItem);
        }
        countPrice();
    }

    public void remove(Product product) {
        OrderItem orderItem = new OrderItem(product);
        if(orderItems.contains(orderItem)){
            if(orderItems.stream().filter(i->i.equals(orderItem)).anyMatch(i -> {
                i.setQty(i.getQty()-1);
                return i.getQty() == 0;
            })) {
                orderItems.remove(orderItem);
                countDecr();
            }
        }
        WeakReference reference = new WeakReference(orderItem);
        countPrice();
    }

    public void countPrice() {
        totalPrice = new BigDecimal(0);
        for (OrderItem i : orderItems) {
            totalPrice = totalPrice.add(i.getProduct().getPrice().multiply(new BigDecimal(i.getQty())));
        }
    }

    private Integer countIncr(){
       return ++counter;
    }
    private void countDecr() {
        --counter;
    }

    public void clear() {
        orderItems.clear();
        totalPrice=new BigDecimal(0);
        counter=0;
    }
}
