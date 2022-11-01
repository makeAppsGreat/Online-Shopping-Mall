package kr.makeappsgreat.onlinemall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@NoArgsConstructor @AllArgsConstructor @Builder
public class Address {

    @NotBlank
    private String zipcode;
    @NotBlank
    private String address;
    private String address2;
}
