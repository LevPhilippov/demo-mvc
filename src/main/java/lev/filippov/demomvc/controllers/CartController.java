package lev.filippov.demomvc.controllers;

import lev.filippov.demomvc.models.Order;
import lev.filippov.demomvc.models.OrderDetails;
import lev.filippov.demomvc.models.PrivateDetails;
import lev.filippov.demomvc.models.User;
import lev.filippov.demomvc.services.CartService;
import lev.filippov.demomvc.services.ProductService;
import lev.filippov.demomvc.services.UserService;
import lev.filippov.demomvc.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private Cart cart;
    private ProductService productService;
    private CartService cartService;
    private UserService userService;

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping
    public String show(Model model) {
        model.addAttribute("cart", cart);
        model.addAttribute("title", "Cart");
        return "cart";
    }

    @RequestMapping("/add/{id}")
    public void add(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) throws Throwable {
        this.cart.add(productService.findById(id));
        response.sendRedirect(request.getHeader("referer"));
    }

    @RequestMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {
        this.cart.remove(productService.getById(id));
        return "redirect:/cart";
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String showOrderDetails(Model model, Principal principal) {
//        PrivateDetails privateDetails = userService.findByUsername(principal.getName()).getPrivateDetails();
        model.addAttribute("details", new OrderDetails());
        model.addAttribute(cart);
        return "order_details";
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public void saveOrder(Principal principal, HttpServletResponse response, HttpServletRequest request,
                          @ModelAttribute("details") OrderDetails details) throws IOException {
        User user = userService.findByUsername(principal.getName());
        cartService.saveOrder(cart, user, details);
        cart.clear();
        response.sendRedirect(request.getContextPath() + "/shop");
    }

}

