package lev.filippov.demomvc.services;


import lev.filippov.demomvc.models.Product;
import lev.filippov.demomvc.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private final int DEF_ITEMS_ON_PAGE = 5;

    @Autowired
    @Qualifier("productRepository")
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> findAll(Integer pageNbr){
        return productRepository.findAll(PageRequest.of(Optional.ofNullable(pageNbr).orElse(0), DEF_ITEMS_ON_PAGE,Sort.by(Sort.Direction.ASC,"id")));
    }

    public Page<Product> findFiltered(Integer pageNbr, Specification<Product> spec) {
        return productRepository.findAll(spec,PageRequest.of(Optional.ofNullable(pageNbr).orElse(0),DEF_ITEMS_ON_PAGE, Sort.by(Sort.Direction.ASC, "id")));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(new Product());
    }
    @Transactional
    public void saveOrUpdate(Product product) {
        productRepository.save(product);
    }
}
