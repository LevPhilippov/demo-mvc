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
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static lev.filippov.demomvc.repositories.ProductSpecs.*;

@Service
public class ProductService {

    private ProductRepository productRepository;
    public static final String[] filtersSet = {"minPrice","maxPrice", "word"};
    private static final Integer DEF_ITEMS_ON_PAGE = 5;

    @Autowired
    @Qualifier("productRepository")
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Page<Product> findFiltered(Integer pageNbr, Map<String, String> params,Integer itemsOnPage) {
        return productRepository.findAll(buildProductSpecification(params),
        PageRequest.of(Optional.ofNullable(pageNbr).orElse(0),Optional.ofNullable(itemsOnPage).orElse(DEF_ITEMS_ON_PAGE), Sort.by(Sort.Direction.ASC, "id")));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product findById(Long id) throws Throwable {
        if(id!=null)
           return productRepository.findById(id).orElseThrow((Supplier<Throwable>) () -> new RuntimeException("Продукт с запрошенным ID отсутсвуетс в базе!"));
        else
            return new Product();
    }
    @Transactional
    public void saveOrUpdate(Product product) {
        productRepository.save(product);
    }

    private Specification<Product> buildProductSpecification(Map<String, String> params) {
        Specification<Product> ps = Specification.where(null);
        if(params.containsKey(filtersSet[0]))
            ps = ps.and(priceGreaterThanOrEq(new BigDecimal(params.get(filtersSet[0]))));
        if(params.containsKey(filtersSet[1]))
            ps = ps.and(priceLessThanOrEq(new BigDecimal(params.get(filtersSet[1]))));
        if(params.containsKey(filtersSet[2]))
            ps = ps.and(wordLike(params.get(filtersSet[2])));
        return ps;
    }
}
