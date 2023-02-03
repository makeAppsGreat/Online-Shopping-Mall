package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.common.SimpleResult;
import kr.makeappsgreat.onlinemall.user.AccountUserDetails;
import kr.makeappsgreat.onlinemall.user.member.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller @RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ModelMapper modelMapper;

    @ModelAttribute
    public void addAttributes(Authentication authentication, Model model) {
        model.addAttribute("user", ((AccountUserDetails) authentication.getPrincipal()).getAccount());
    }

    @GetMapping
    public String index() {
        return "/order/cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<SimpleResult> addToCart(@RequestBody @Validated CartRequest cartRequest, BindingResult bindingResult,
                                                  @ModelAttribute("user") Member user) {
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
        cartService.addToCart(cart);


        return ResponseEntity.ok(
                SimpleResult.builder()
                        .result(true)
                        .code(HttpStatus.OK.value())
                        .name(null)
                        .message(null)
                        .build());
    }
}
