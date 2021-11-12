package lev.filippov.demomvc.controllers;

import lev.filippov.demomvc.exceptions.ServerException;
import lev.filippov.demomvc.exceptions.WrongAccessException;
import lev.filippov.demomvc.models.OrderDetails;
import lev.filippov.demomvc.models.User;
import lev.filippov.demomvc.services.CartService;
import lev.filippov.demomvc.services.ProductService;
import lev.filippov.demomvc.services.UserService;
import lev.filippov.demomvc.utils.Cart;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private Cart cart;
    private ProductService productService;
    private CartService cartService;

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

    @RequestMapping
    public String show(Model model) {
        model.addAttribute("cart", cart);
        model.addAttribute("title", "Cart");
        return "cart/cart";
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
    public String showOrderDetails(Model model) {
        cartEmptyCheck(cart);
        model.addAttribute("details", new OrderDetails());
        model.addAttribute(cart);
        return "cart/order_details";
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public void saveOrder(Principal principal, HttpServletResponse response, HttpServletRequest request,
                          @ModelAttribute("details") OrderDetails details) throws IOException {
        cartService.saveOrder(cart, principal, details);
        cart.clear();
        response.sendRedirect(request.getContextPath() + "/shop");
    }

    @RequestMapping(value = "/oneclick", method = RequestMethod.GET)
    public String showOneClickForm(Model model) {
        cartEmptyCheck(cart);
        model.addAttribute("details", new OrderDetails());
        model.addAttribute(cart);
        return "cart/one_click_form";
    }

    @RequestMapping(value = "/oneclick", method = RequestMethod.POST)
    public String showOneClickForm(@Valid @ModelAttribute("details") OrderDetails details,
                                   HttpServletRequest request, HttpServletResponse response,
                                   Model model, Principal principal) throws IOException {
        if(request.getParameter("roboCheck") == null && principal == null) {
            throw new WrongAccessException("You are fucking robot!");
        }

        if(Strings.isEmpty(details.getPhone()) || Strings.isEmpty(details.getFirstName()))
            {
                model.addAttribute("error", new ServerException("Name and phone number shouldn't be empty!"));
                model.addAttribute("details", new OrderDetails());
                model.addAttribute(cart);
                return  "cart/one_click_form";
            }
        if(principal!= null) {
           cartService.saveOrder(cart,principal,details);
        } else {
            cartService.saveAnonymousOrder(cart, details);
        }
        cart.clear();
        response.sendRedirect(request.getContextPath() + "/shop");
        return null;
    }

    private void cartEmptyCheck(Cart cart) {
        if(cart.getOrderItems().size()==0) {
            throw new ServerException("Cart is empty");
        }
    }


}

