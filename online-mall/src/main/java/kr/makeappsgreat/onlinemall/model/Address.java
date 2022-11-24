package kr.makeappsgreat.onlinemall.model;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Embeddable
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
public class Address {

    @NotNull @Pattern(regexp = "\\d{5}", message = "{message.invalid-pattern}")
    private String zipcode;
    @NotBlank
    private String address;
    private String address2;
}
