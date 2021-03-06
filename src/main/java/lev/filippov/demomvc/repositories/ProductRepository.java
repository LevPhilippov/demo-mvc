package lev.filippov.demomvc.repositories;

import lev.filippov.demomvc.models.Product;
import lev.filippov.demomvc.models.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Set<ProductDto> findAllByIdIn(Collection<Long> ids);
}
