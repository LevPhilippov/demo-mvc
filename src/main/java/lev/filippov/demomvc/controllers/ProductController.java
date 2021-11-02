package lev.filippov.demomvc.controllers;


import lev.filippov.demomvc.models.Product;
import lev.filippov.demomvc.services.ProductService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static lev.filippov.demomvc.services.ProductService.filtersSet;

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
                          @RequestParam(name = "pageNbr", required = false, defaultValue = "0") Integer pageNbr,
                          @RequestParam (name = "itemsOnPage", required = false, defaultValue = "5")Integer itemsOnPage, Session session) {
        Map<String, String> params = parseFilters(request);
        String filters = parseFiltersMapToString(params);
        Page<Product> products = productService.findFiltered(pageNbr, params, itemsOnPage);
        model.addAttribute("products", products.getContent());
        model.addAttribute("filters", filters);
        model.addAttribute("pageQty", products.getTotalPages());
        model.addAttribute("pageNbr", products.getPageable().getPageNumber()); //first page is number 0
        addTitle(model, "Product list");
        return "products";
    }


    //при удалении редиректом сбрасываются фильтры, а должны оставаться. Решено путем переадресации на Header - referer
    @RequestMapping(method = RequestMethod.GET, path = {"/del/{id}", "filters/del/{id}"})
    public void deleteProduct(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        productService.deleteProduct(id);
        response.sendRedirect(request.getHeader("referer"));
    }

    private void addTitle(Model model, String title) {
        model.addAttribute("title", title);
    }

    @RequestMapping(method = RequestMethod.GET, path = {"/edit","/edit/{id}/{filters}","/edit/{id}/*"})
    public String editProduct(@PathVariable(name = "id", required = false) Long id,
                              @PathVariable(required = false) String filters,
                              Model model, HttpServletRequest request) throws Throwable {
        Product product = productService.findById(id);
//        System.out.println(request);
//        System.out.println(product);
        model.addAttribute("product", product);
        if (filters != null)
            model.addAttribute(filters);
        addTitle(model, "Edit product");
        return "edit_product_form";
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/edit", "/edit/{filters}"})
    public void editProduct(@ModelAttribute(name = "product") Product product,
                            @PathVariable(name = "filters", required = false) String filters,
                            HttpServletResponse response,
                            HttpServletRequest request) throws IOException {
        productService.saveOrUpdate(product);
        response.sendRedirect(request.getContextPath() + "/products");
    }

    private Map<String, String> parseFilters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (String s : filtersSet) {
            if (request.getParameter(s) != null && !request.getParameter(s).isEmpty())
                params.put(s, request.getParameter(s));
        }
        return params;
    }

    private String parseFiltersMapToString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((key, value) -> {
            for (String s : filtersSet) {
                if (key.equals(s))
                    sb.append(key + "=" + value + "&");
            }
        });
        return sb.toString();
    }
}
