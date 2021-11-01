package lev.filippov.demomvc.controllers;


import lev.filippov.demomvc.models.Product;
import lev.filippov.demomvc.services.ProductService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static lev.filippov.demomvc.repositories.ProductSpecs.priceGreaterThanOrEq;
import static lev.filippov.demomvc.repositories.ProductSpecs.priceLessThanOrEq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Iterator;

@Controller
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String findAll(Model model, HttpServletRequest request,
                          @RequestParam(name = "pageNbr", required = false) Integer pageNbr){
        Page<Product> products = productService.findAll(pageNbr);
        model.addAttribute("products", products.getContent());
        request.setAttribute("pageQty", products.getTotalPages());
        addTitle(model, "Product list");
        return "products";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/filters")
    public String findFiltered(Model model, HttpServletRequest request,
                            @RequestParam(name = "pageNbr", required = false) Integer pageNbr,
                            @RequestParam(name = "minPrice", required = false) Double minPrice,
                            @RequestParam(name = "maxPrice", required = false) Double maxPrice) {
        Specification<Product> spec = Specification.where(null);
        StringBuilder sb = new StringBuilder();
        if (minPrice != null) {
            spec = spec.and(priceGreaterThanOrEq(new BigDecimal(minPrice)));
            sb.append("&minPrice=" + minPrice);
        }
        if (maxPrice != null) {
            spec = spec.and(priceLessThanOrEq(new BigDecimal(maxPrice)));
            sb.append("&maxPrice=" + maxPrice);
        }
        Page<Product> products = productService.findFiltered(pageNbr, spec);
        model.addAttribute("products", products.getContent());
        model.addAttribute("filters", sb.toString());
        request.setAttribute("pageQty", products.getTotalPages());
        addTitle(model, "Filtered product list");
        return "products";
    }
    //при удалении редиректом сбрасываются фильтры, а должны оставаться. Решено путем переадресации на Header - referer
    @RequestMapping(method = RequestMethod.GET, path = {"/del/{id}","filters/del/{id}" })
    public void deleteProduct(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
           productService.deleteProduct(id);
           response.sendRedirect(request.getHeader("referer"));
    }

    private void addTitle(Model model, String title) {
        model.addAttribute("title", title);
    }

    @RequestMapping(method = RequestMethod.GET, path = {"/edit/{id}", "filters/edit/{id}/{filters}", "filter/edit/{id}"})
    public String editProduct(@PathVariable(name = "id") Long id,
                              @PathVariable(required = false) String filters,
                              Model model, HttpServletRequest request) {
//        if(request.getHeader("referer").contains("filter")){
//            System.out.println(request.getHeader("referer"));
//        }
        Product product = productService.getById(id);
//        System.out.println(request);
        System.out.println(product);
        model.addAttribute("product", product);
        if(filters != null)
            model.addAttribute(filters);
        addTitle(model, "Edit product");
        return "edit_product_form";
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/edit","/edit/{filters}"})
    public void editProduct(@ModelAttribute(name = "product")Product product,
                            @PathVariable(name = "filters", required = false) String filters,
                            HttpServletResponse response,
                            HttpServletRequest request) throws IOException
    {
        productService.saveOrUpdate(product);
        if(filters != null) {
            System.out.println(filters);
            response.sendRedirect(request.getContextPath() + "/products/filters?" + filters);
        } else
            response.sendRedirect(request.getContextPath() + "/products");
    }
}
