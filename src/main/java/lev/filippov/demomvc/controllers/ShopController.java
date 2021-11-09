package lev.filippov.demomvc.controllers;

import lev.filippov.demomvc.models.Product;
import lev.filippov.demomvc.services.ProductService;
import lev.filippov.demomvc.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import static lev.filippov.demomvc.utils.ProductsUtils.*;

@Controller
@RequestMapping("/shop")
public class ShopController {

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
            return "redirect:/shop";
        }
        Map<String, String> params = parseFilters(request);
        String filters = parseFiltersMapToString(params);
        Page<Product> products = productService.findFiltered(pageNbr, params, (Integer) session.getAttribute("itemsOnPage"));
        model.addAttribute("products", products.getContent());
        model.addAttribute("filters", filters);
        model.addAttribute("pageQty", products.getTotalPages());
        model.addAttribute("pageNbr", products.getPageable().getPageNumber());
        addTitle(model, "Product list");
        return "shop";
    }


    private void addTitle(Model model, String title) {
        model.addAttribute("title", title);
    }



}
