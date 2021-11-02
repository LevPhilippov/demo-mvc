package lev.filippov.demomvc.controllers;

import lev.filippov.demomvc.models.Product;
import lev.filippov.demomvc.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping
    public Product save(@RequestBody Product product) {

        if(product.getId()!=null)
            throw new RuntimeException("Id must be null!");
        productService.saveOrUpdate(product);
        return product;
    }

    @PutMapping
    public Product update(@RequestBody Product product) {
        if(product.getId()==null)
            throw new RuntimeException("Id must not be null!");
        productService.saveOrUpdate(product);
        return product;
    }

}
