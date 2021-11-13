package lev.filippov.demomvc.controllers;

import lev.filippov.demomvc.models.Product;
import lev.filippov.demomvc.models.ProductDto;
import lev.filippov.demomvc.services.ProductService;
import lev.filippov.demomvc.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.comparator.Comparators;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Predicate;

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
        putProductsFromCookiesToAModel(request, model);
        model.addAttribute("products", products.getContent());
        model.addAttribute("filters", filters);
        model.addAttribute("pageQty", products.getTotalPages());
        model.addAttribute("pageNbr", products.getPageable().getPageNumber());
        return "shop";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{productId}")
    public String showProduct(@PathVariable Long productId, Model model, HttpServletRequest request,
                              HttpServletResponse response) {
        Product product = productService.getById(productId);
        model.addAttribute(product);
        setShownProductInCookie(productId,request, response);
        return "product_page";
    }

    private void setShownProductInCookie(Long productId, HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Deque<Cookie> shownProductCookies = new LinkedList<>();
        Arrays.stream(cookies).filter(cookie -> cookie.getName().matches("^prod_[0-9]*$"))
                .sorted((o1, o2) -> Long.parseLong(o1.getName().split("_")[1])>Long.parseLong(o2.getName().split("_")[1]) ? 1 : -1)
                .forEach(shownProductCookies :: add);
        if(shownProductCookies.size()>=5) {
            Cookie cookieToRemove = shownProductCookies.pollFirst();
            cookieToRemove.setValue(null);
            cookieToRemove.setMaxAge(0);
            response.addCookie(cookieToRemove);
        }
        long cookieIndex = 0L;
        if(shownProductCookies.size()>0){
            cookieIndex = Long.parseLong(shownProductCookies.peekLast().getName().split("_")[1]);

        }
        Cookie cookie = new Cookie("prod_" + (cookieIndex+1) , productId.toString());
        cookie.setMaxAge(60);
        response.addCookie(cookie);
    }

    // for show 5 last shown products
    private void putProductsFromCookiesToAModel(HttpServletRequest request,Model model) {
        Cookie[] cookies = request.getCookies();
        Set<Long> shownProductIds = new HashSet<>();
        assert (cookies != null);
        Arrays.stream(cookies).filter(cookie -> cookie.getName().matches("^prod_[0-9]*$"))
                .sorted((o1, o2) -> Long.parseLong(o1.getName().split("_")[1])>Long.parseLong(o2.getName().split("_")[1]) ? 1 : -1)
                .forEach(c -> shownProductIds.add(Long.parseLong(c.getValue())));

        if(shownProductIds.size() >0) {
            Set<ProductDto> allByIds = productService.findAllByIds(shownProductIds);
            model.addAttribute("shownProds", allByIds);
        }
    }


}
