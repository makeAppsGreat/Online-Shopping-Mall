package kr.makeappsgreat.onlinemall.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Embeddable
@Getter @Setter
public class Address {

    @NotNull @Pattern(regexp = "\\d{5}", message = "{message.invalid-pattern}")
    private String zipcode;
    @NotBlank
    private String address;
    private String address2;
}
