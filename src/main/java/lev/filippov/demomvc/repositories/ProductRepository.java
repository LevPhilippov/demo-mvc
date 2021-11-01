package lev.filippov.demomvc.repositories;

import lev.filippov.demomvc.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product>findAllByPriceBetween(BigDecimal minprice, BigDecimal maxprice);
    List<Product>findAllByPriceGreaterThan(BigDecimal minprice);
    List<Product>findAllByPriceLessThan(BigDecimal minprice);


}
