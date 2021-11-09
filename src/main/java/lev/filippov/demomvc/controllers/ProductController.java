package lev.filippov.demomvc.controllers;


import lev.filippov.demomvc.models.Product;
import lev.filippov.demomvc.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import static lev.filippov.demomvc.utils.ProductsUtils.*;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String findAll(Model model, HttpServletRequest request,
                          @RequestParam(name = "pageNbr", defaultValue = "0") Integer pageNbr,
                          @RequestParam (name = "itemsOnPage", required = false)Integer itemsOnPage,
                          HttpSession session){
        if(itemsOnPage != null) {
            session.setAttribute("itemsOnPage",itemsOnPage);
            return "redirect:admin/products";
        }
        Map<String, String> params = parseFilters(request);
        String filters = parseFiltersMapToString(params);
        Page<Product> products = productService.findFiltered(pageNbr, params, (Integer) session.getAttribute("itemsOnPage"));
        model.addAttribute("products", products.getContent());
        model.addAttribute("filters", filters);
        model.addAttribute("pageQty", products.getTotalPages());
        model.addAttribute("pageNbr", products.getPageable().getPageNumber()); //first page is number 0
        addTitle(model, "Product list");
        return "products";
    }


    //при удалении редиректом сбрасываются фильтры, а должны оставаться. Решено путем переадресации на Header - referer
    @RequestMapping(method = RequestMethod.GET, path = {"/del", })
    public void deleteProduct(@RequestParam(name = "id") Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}
