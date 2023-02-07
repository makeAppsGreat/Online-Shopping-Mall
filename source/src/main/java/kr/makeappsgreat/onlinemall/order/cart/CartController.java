package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.common.SimpleResult;
import kr.makeappsgreat.onlinemall.user.AccountUserDetails;
import kr.makeappsgreat.onlinemall.user.member.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller @RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    @Value("${common.delivery-fee}")
    private int DELIVERY_FEE;

    private final CartService cartService;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;

    @ModelAttribute
    public void addAttributes(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("user", ((AccountUserDetails) authentication.getPrincipal()).getAccount());
        }
    }

    @GetMapping
    public String index(@ModelAttribute("user") Member user, Model model) {
        if (user == null || user.getId() == null) return "redirect:/login";

        CartRequestWrapper wrapper = new CartRequestWrapper();
        for (Cart item : cartService.listCart(user)) wrapper.add(modelMapper.map(item, CartRequest.class));

        model.addAttribute("wrapper", wrapper);
        model.addAttribute("deliveryFee", DELIVERY_FEE);


        return "/order/cart";
    }

    @PostMapping
    public ResponseEntity index(@ModelAttribute("wrapper") CartRequestWrapper wrapper) {
        return ResponseEntity.ok(wrapper);
    }

    @PostMapping("/add")
    public ResponseEntity<SimpleResult> addToCart(@RequestBody @Validated CartRequest cartRequest, BindingResult bindingResult,
                                                  @ModelAttribute("user") Member user, Locale locale) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);

            return ResponseEntity.ok(
                    SimpleResult.builder()
                            .result(false)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .name(error.getField())
                            .message(error.getDefaultMessage())
                            .build());
        }

        Cart cart = modelMapper.map(cartRequest, Cart.class);
        cart.setMember(user);
        Object[] args = { cartService.addToCart(cart) };


        return ResponseEntity.ok(
                SimpleResult.builder()
                        .result(true)
                        .code(HttpStatus.OK.value())
                        .name(null)
                        .message(String.format("%s\n%s",
                                messageSource.getMessage("message.cart-added", args, locale),
                                messageSource.getMessage("message.move-to-cart", null, locale)))
                        .build());
    }
}
